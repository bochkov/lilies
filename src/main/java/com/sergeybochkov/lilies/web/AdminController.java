package com.sergeybochkov.lilies.web;

import com.sergeybochkov.lilies.config.StaticResourceConfig;
import com.sergeybochkov.lilies.model.*;
import com.sergeybochkov.lilies.service.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public final class AdminController extends WebMvcConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

    private final MusicService musicService;
    private final DifficultyService difficultyService;
    private final InstrumentService instrumentService;
    private final AuthorService authorService;
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService,
                           MusicService musicService,
                           DifficultyService difficultyService,
                           InstrumentService instrumentService,
                           AuthorService authorService) {
        this.musicService = musicService;
        this.difficultyService = difficultyService;
        this.userService = userService;
        this.instrumentService = instrumentService;
        this.authorService = authorService;
    }

    @InitBinder("user")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new UserForm());
    }

    @RequestMapping("/")
    public String adminIndex() {
        return "redirect:/admin/music/";
    }

    @RequestMapping("/login/")
    public String login() {
        return "admin/login";
    }

    // ============ CHANGE PASSWORD ============

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
    public String allMusic() {
        return "redirect:/admin/music/1/";
    }

    @RequestMapping("/music/{page}/")
    public String allMusic(Model model, @PathVariable("page") Integer page) {
        Page<Music> p = musicService.findAll(page);
        int current = p.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, p.getTotalPages());

        int pageList[] = new int[end - begin + 1];
        for (int i = begin - 1; i < end; ++i)
            pageList[i] = i + 1;

        model.addAttribute("musicList", p);
        model.addAttribute("pageList", pageList);
        model.addAttribute("current", current);
        model.addAttribute("begin", begin);
        model.addAttribute("end", end);
        return "admin/musicList";
    }

    @RequestMapping(value = "/a/music/get/", method = RequestMethod.POST)
    public @ResponseBody Serializable getMusic(@RequestParam Long id) {
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

    @RequestMapping("/music/edit/{id}/")
    public String editMusic(Model model, @PathVariable Long id) {
        model.addAttribute("music", musicService.findOne(id));
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("difficulties", difficultyService.findAll());
        model.addAttribute("instruments", instrumentService.findAll());
        return "admin/musicAdd";
    }

    @RequestMapping(value = "/music/save/", method = RequestMethod.POST)
    public String saveMusic(@RequestParam(value = "id", required = false) Long id,
                            @RequestParam(value = "name") String name,
                            @RequestParam(value = "subname", required = false) String subName,
                            @RequestParam(value = "composer", required = false) String composer,
                            @RequestParam(value = "writer", required = false) String writer,
                            @RequestParam(value = "difficulty") Integer difficulty,
                            @RequestParam(value = "instrument") String instrument,
                            @RequestParam(value = "src_file", required = false) MultipartFile file) {

        List<Author> composerList = new ArrayList<>();
        if (composer != null)
            for (String c : composer.split(","))
                composerList.add(authorService.findOne(Long.parseLong(c)));

        List<Author> writerList = new ArrayList<>();
        if (writer != null)
            for (String c : writer.split(","))
                writerList.add(authorService.findOne(Long.parseLong(c)));

        List<Instrument> instrumentList = new ArrayList<>();
        for (String i : instrument.split(","))
            instrumentList.add(instrumentService.findBySlug(i));

        File savedFile;
        Music music;
        if (file != null) {
            savedFile = new File(StaticResourceConfig.MEDIA_DIR, file.getOriginalFilename());
            if (!file.isEmpty()) {
                try {
                    IOUtils.write(file.getBytes(), new FileOutputStream(savedFile));
                } catch (IOException ex) {
                    LOG.error(ex.getMessage(), ex);
                }
            }
            music = musicService.save(new Music(id, name, subName, composerList,
                    writerList, difficultyService.findOne(difficulty), instrumentList, savedFile));
            if (savedFile.exists() && !savedFile.delete())
                LOG.warn(String.format("Не удален загруженный файл %s", savedFile.getAbsolutePath()));
            musicService.generateFiles(music);
        }
        else
            music = musicService.update(new Music(id, name, subName, composerList,
                    writerList, difficultyService.findOne(difficulty), instrumentList, musicService.findOne(id)));

        return String.format("redirect:/admin/music/%s/", musicService.pageNum(music));
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

    @RequestMapping(value = "/a/music/regenerate/", method = RequestMethod.POST)
    public @ResponseBody Serializable regenerate(@RequestParam Long id) {
        musicService.generateFiles(musicService.findOne(id));
        return new AjaxResponse();
    }

    // ================== DIFFICULTY =====================

    @RequestMapping("/difficulty/")
    public String allDifficulty(Model model) {
        model.addAttribute("difficultyList", difficultyService.findAll());
        return "admin/difficultyList";
    }

    @RequestMapping(value = "/a/difficulty/get/", method = RequestMethod.POST)
    public @ResponseBody Serializable getDifficulty(@RequestParam Integer id) {
        return difficultyService.get(id);
    }

    @RequestMapping("/difficulty/edit/{id}/")
    public String editDiff(Model model, @PathVariable Integer id) {
        Difficulty diff = difficultyService.findOne(id);
        model.addAttribute("rating", diff.getRating());
        model.addAttribute("name", diff.getName());
        return "admin/difficultyAdd";
    }

    @RequestMapping("/difficulty/add/")
    public String addDifficulty() {
        return "admin/difficultyAdd";
    }

    @RequestMapping(value = "/difficulty/save/", method = RequestMethod.POST)
    public String saveDifficulty(@RequestParam Integer rating,
                                 @RequestParam String name) {
        difficultyService.save(new Difficulty(rating, name));
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

    // ================== INSTRUMENT ========================

    @RequestMapping("/instrument/")
    public String allInstruments(Model model) {
        model.addAttribute("instrumentList", instrumentService.findAll(new Sort("id")));
        return "admin/instrumentList";
    }

    @RequestMapping(value = "/a/instrument/get/", method = RequestMethod.POST)
    public @ResponseBody Serializable getInstrument(@RequestParam Long id) {
        return instrumentService.findOne(id);
    }

    @RequestMapping("/instrument/edit/{id}/")
    public String editInstrument(Model model, @PathVariable Long id) {
        Instrument instrument = instrumentService.findOne(id);
        model.addAttribute("id", instrument.getId());
        model.addAttribute("slug", instrument.getSlug());
        model.addAttribute("name", instrument.getName());
        return "admin/instrumentAdd";
    }

    @RequestMapping("/instrument/add/")
    public String addInstrument() {
        return "admin/instrumentAdd";
    }

    @RequestMapping(value = "/instrument/save/" , method = RequestMethod.POST)
    public String saveInstrument(@RequestParam(required = false) Long id,
                                 @RequestParam String slug,
                                 @RequestParam String name) {
        instrumentService.save(new Instrument(id, name, slug));
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

    // =============== AUTHOR ==================

    @RequestMapping("/author/")
    public String allAuthors(Model model) {
        model.addAttribute("authorList", authorService.findAll());
        return "admin/authorList";
    }

    @RequestMapping("/author/add/")
    public String addAuthor() {
        return "admin/authorAdd";
    }

    @RequestMapping(value = "/author/save/", method = RequestMethod.POST)
    public String saveAuthor(@RequestParam(required = false) Long id,
                             @RequestParam String lastName,
                             @RequestParam(required = false) String firstName,
                             @RequestParam(required = false) String middleName) {
        authorService.save(new Author(id,lastName, firstName, middleName));
        return "redirect:/admin/author/";
    }

    @RequestMapping(value = "/author/edit/{id}/")
    public String editAuthor(Model model, @PathVariable Long id) {
        Author author = authorService.findOne(id);
        model.addAttribute("id", author.getId());
        model.addAttribute("lastName", author.getLastName());
        model.addAttribute("firstName", author.getFirstName());
        model.addAttribute("middleName", author.getMiddleName());
        return "admin/authorAdd";
    }

    @RequestMapping(value = "/a/author/get", method = RequestMethod.POST)
    public @ResponseBody Serializable getAuthor(@RequestParam Long id) {
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
