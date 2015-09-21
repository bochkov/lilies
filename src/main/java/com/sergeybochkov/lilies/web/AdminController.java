package com.sergeybochkov.lilies.web;

import com.sergeybochkov.lilies.config.StaticResourceConfig;
import com.sergeybochkov.lilies.model.*;
import com.sergeybochkov.lilies.service.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController extends WebMvcConfigurerAdapter {

    @Autowired private MusicService musicService;
    @Autowired private DifficultyService difficultyService;
    @Autowired private InstrumentService instrumentService;
    @Autowired private AuthorService authorService;
    @Autowired private UserService userService;

    @InitBinder("user")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new UserValidationForm());
    }

    @RequestMapping("/")
    public String adminIndex() {
        return "redirect:/admin/music/";
    }

    @RequestMapping("/login/")
    public String login() {
        return "admin/login";
    }

    // =========== CHANGE PASSWORD ============

    @RequestMapping("/password/")
    public String updateP(Model model) {
        model.addAttribute("user", new User());
        return "admin/updateP";
    }

    @RequestMapping("/password/save/")
    public String updatePSave(Model model, @ModelAttribute @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("fields", bindingResult);
            return "admin/updateP";
        }
        else {
            userService.save(user);
            return "redirect:/admin/";
        }
    }

    // =============== MUSIC ==================

    @RequestMapping("/music/")
    public String allMusic(Model model) {
        model.addAttribute("musicList", musicService.findAll());
        return "admin/musicList";
    }

    @RequestMapping(value = "/a/music/get/", method = RequestMethod.POST)
    public @ResponseBody Music getMusic(@RequestParam Long id) {
        return musicService.findOne(id);
    }

    @RequestMapping("/music/add/")
    public String addMusic(Model model) {
        model.addAttribute("music", new Music());
        model.addAttribute("difficulties", difficultyService.findAll());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("instruments", instrumentService.findAll());
        return "admin/musicAdd";
    }

    @RequestMapping("/music/edit/{{id}}/")
    public String editMusic(Model model, @PathVariable Long id) {
        Music m = musicService.findOne(id);
        return "redirect:/music/add/";
    }

    @RequestMapping(value = "/music/save/", method = RequestMethod.POST)
    public String saveMusic(Model model,
                            @RequestParam("name") String name,
                            @RequestParam(value = "subname", required = false) String subName,
                            @RequestParam(value = "composer", required = false) String composer,
                            @RequestParam(value = "writer", required = false) String writer,
                            @RequestParam("difficulty") Integer difficulty,
                            @RequestParam("instrument") String instrument,
                            @RequestParam("src_file") MultipartFile file) {

        Music music = new Music();
        music.setName(name);
        music.setSubName(subName);

        if (composer != null) {
            List<Author> composerList = new ArrayList<>();
            for (String c : composer.split(","))
                composerList.add(authorService.findOne(Long.parseLong(c)));
            music.setComposer(composerList);
        }

        if (writer != null) {
            List<Author> writerList = new ArrayList<>();
            for (String c : writer.split(","))
                writerList.add(authorService.findOne(Long.parseLong(c)));
            music.setWriter(writerList);
        }

        music.setDifficulty(difficultyService.findOne(difficulty));

        List<Instrument> instrumentList = new ArrayList<>();
        for (String i : instrument.split(","))
            instrumentList.add(instrumentService.findBySlug(i));
        music.setInstrument(instrumentList);

        music.setSrcFilename(file.getOriginalFilename());

        musicService.save(music);

        Storage storage = musicService.getStorage(music);
        File savedFile = new File(StaticResourceConfig.MEDIA_DIR, file.getOriginalFilename());
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(savedFile));
            stream.write(file.getBytes());
            stream.close();
            storage.setSrcFile(IOUtils.toByteArray(new FileInputStream(savedFile)));
        }
        catch (IOException ex) {
            //
        }

        musicService.save(storage);

        music.setStorage(storage);
        musicService.save(music);

        musicService.generateFiles(music);
        return "redirect:/admin/music/";
    }

    @RequestMapping(value = "/a/music/delete/", method = RequestMethod.POST)
    public @ResponseBody Serializable deleteMusic(@RequestParam Long id) {
        try {
            musicService.delete(id);
            return new AjaxResponse();
        }
        catch (Exception ex) {
            return new AjaxResponse("Ошибка удаления. Присутствуют связанные записи.");
        }
    }

    // ================= DIFFICULTY ===================

    @RequestMapping("/difficulty/")
    public String allDifficulty(Model model) {
        model.addAttribute("difficultyList", difficultyService.findAll());
        return "admin/difficultyList";
    }

    @RequestMapping(value = "/a/difficulty/get/", method = RequestMethod.POST)
    public @ResponseBody Difficulty getDifficulty(@RequestParam Integer id) {
        return difficultyService.get(id);
    }

    @RequestMapping("/difficulty/{id}/")
    public String editDiff(Model model, @PathVariable Integer id) {
        model.addAttribute("difficulty", difficultyService.findOne(id));
        return "admin/difficultyAdd";
    }

    @RequestMapping("/difficulty/add/")
    public String addDifficulty(Model model) {
        model.addAttribute("diff", new Difficulty());
        return "admin/difficultyAdd";
    }

    @RequestMapping(value = "/difficulty/save/", method = RequestMethod.POST)
    public String saveDifficulty(@ModelAttribute("difficulty") Difficulty difficulty) {
        difficultyService.save(difficulty);
        return "redirect:/admin/difficulty/";
    }

    @RequestMapping(value = "/a/difficulty/delete", method = RequestMethod.POST)
    public @ResponseBody Serializable deleteDifficulty(@RequestParam Integer id) {
        try {
            difficultyService.delete(id);
            return new AjaxResponse();
        }
        catch (Exception ex) {
            return new AjaxResponse("Ошибка удаления. Присутствуют связанные записи.");
        }
    }

    // ================ INSTRUMENT =======================

    @RequestMapping("/instrument/")
    public String allInstruments(Model model) {
        model.addAttribute("instrumentList", instrumentService.findAll());
        return "admin/instrumentList";
    }

    @RequestMapping(value = "/a/instrument/get/", method = RequestMethod.POST)
    public @ResponseBody Instrument getInstrument(@RequestParam Long id) {
        return instrumentService.findOne(id);
    }

    @RequestMapping("/instrument/{id}/")
    public String editInstrument(Model model, @PathVariable Long id) {
        model.addAttribute("instrument", instrumentService.findOne(id));
        return "admin/instrumentAdd";
    }

    @RequestMapping("/instrument/add/")
    public String addInstrument(Model model) {
        model.addAttribute("inst", new Instrument());
        return "admin/instrumentAdd";
    }

    @RequestMapping(value = "/instrument/save/" , method = RequestMethod.POST)
    public String saveInstrument(@ModelAttribute("instrument") Instrument instrument) {
        instrumentService.save(instrument);
        return "redirect:/admin/instrument/";
    }

    @RequestMapping(value = "/a/instrument/delete/", method = RequestMethod.POST)
    public @ResponseBody Serializable deleteInstrument(@RequestParam Long id) {
        try {
            instrumentService.delete(id);
            return new AjaxResponse();
        }
        catch (Exception ex) {
            return new AjaxResponse("Ошибка удаления. Присутствуют связанные записи.");
        }
    }

    // =============== AUTHOR ================

    @RequestMapping("/author/")
    public String allAuthors(Model model) {
        model.addAttribute("authorList", authorService.findAll());
        return "admin/authorList";
    }

    @RequestMapping("/author/add/")
    public String addAuthor(Model model) {
        model.addAttribute("author", new Author());
        return "admin/authorAdd";
    }

    @RequestMapping(value = "/author/save/", method = RequestMethod.POST)
    public String saveAuthor(@ModelAttribute("author") Author author) {
        authorService.save(author);
        return "redirect:/admin/author/";
    }

    @RequestMapping(value = "/author/{id}/")
    public String editAuthor(Model model, @PathVariable Long id) {
        model.addAttribute("author", authorService.findOne(id));
        return "admin/authorAdd";
    }

    @RequestMapping(value = "/a/author/get", method = RequestMethod.POST)
    public @ResponseBody Author getAuthor(@RequestParam Long id) {
        return authorService.findOne(id);
    }

    @RequestMapping(value = "/a/author/delete/", method = RequestMethod.POST)
    public @ResponseBody Serializable deleteAuthor(@RequestParam Long id) {
        try {
            authorService.delete(id);
            return new AjaxResponse();
        }
        catch (Exception ex) {
            return new AjaxResponse("Ошибка удаления. Присутствуют связанные записи.");
        }
    }
}
