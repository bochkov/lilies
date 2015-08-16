package com.sergeybochkov.lilies.web;

import com.sergeybochkov.lilies.config.StaticResourceConfig;
import com.sergeybochkov.lilies.model.Author;
import com.sergeybochkov.lilies.model.Difficulty;
import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.model.Music;
import com.sergeybochkov.lilies.service.AuthorService;
import com.sergeybochkov.lilies.service.DifficultyService;
import com.sergeybochkov.lilies.service.InstrumentService;
import com.sergeybochkov.lilies.service.MusicService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Paths;

@Controller
public class AdminController {

    @Autowired private MusicService musicService;
    @Autowired private DifficultyService difficultyService;
    @Autowired private InstrumentService instrumentService;
    @Autowired private AuthorService authorService;

    @RequestMapping("/admin/")
    public String adminIndex() {
        return "admin/musicList";
    }

    // =============== MUSIC ==================

    @RequestMapping("/admin/music/")
    public String allMusic(Model model) {
        model.addAttribute("musicList", musicService.findAll());
        return "admin/musicList";
    }

    @RequestMapping("/admin/music/add/")
    public String addMusic(Model model) {
        model.addAttribute("music", new Music());
        model.addAttribute("difficulties", difficultyService.findAll());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("instruments", instrumentService.findAll());
        return "admin/musicAdd";
    }

    @RequestMapping(value = "/admin/music/save/", method = RequestMethod.POST)
    public String saveMusic(@ModelAttribute("music") Music music, @RequestParam("src_file") MultipartFile file, BindingResult result) {
        if (result.hasErrors())
            return "admin/musicAdd";

        File savedFile = new File(StaticResourceConfig.MEDIA_DIR, file.getOriginalFilename());
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(savedFile));
            stream.write(file.getBytes());
            stream.close();
            music.setSrcFile(IOUtils.toByteArray(new FileInputStream(savedFile)));
            music.setSrcFilename(file.getOriginalFilename());
        }
        catch (IOException ex) {
            //
        }
        musicService.save(music);
        return "redirect:/admin/music/";
    }

    @RequestMapping(value = "/admin/music/delete/", method = RequestMethod.DELETE)
    public String deleteMusic(@PathVariable Long id) {
        musicService.delete(id);
        return "redirect:/admin/music";
    }

    // ================= DIFFICULTY ===================

    @RequestMapping("/admin/difficulty/")
    public String allDifficulty(Model model) {
        model.addAttribute("difficultyList", difficultyService.findAll());
        return "admin/difficultyList";
    }

    @RequestMapping("/admin/difficulty/add/")
    public String addDifficulty(Model model) {
        model.addAttribute("diff", new Difficulty());
        return "admin/difficultyAdd";
    }

    @RequestMapping(value = "/admin/difficulty/save/", method = RequestMethod.POST)
    public String saveDifficulty(@ModelAttribute("difficulty") Difficulty difficulty) {
        difficultyService.save(difficulty);
        return "redirect:/admin/difficulty/";
    }

    @RequestMapping("/admin/difficulty/delete")
    public String deleteDifficulty(@PathVariable Integer id) {
        difficultyService.delete(id);
        return "redirect:/admin/difficulty/";
    }

    // ================ INSTRUMENT =======================

    @RequestMapping("/admin/instrument/")
    public String allInstruments(Model model) {
        model.addAttribute("instrumentList", instrumentService.findAll());
        return "admin/instrumentList";
    }

    @RequestMapping("/admin/instrument/add/")
    public String addInstrument(Model model) {
        model.addAttribute("inst", new Instrument());
        return "admin/instrumentAdd";
    }

    @RequestMapping(value = "/admin/instrument/save/" , method = RequestMethod.POST)
    public String saveInstrument(@ModelAttribute("instrument") Instrument instrument) {
        instrumentService.getOrSave(instrument);
        return "redirect:/admin/instrument/";
    }

    @RequestMapping(value = "/admin/instrument/delete/", method = RequestMethod.POST)
    public String deleteInstrument(@RequestParam Long id) {
        instrumentService.delete(id);
        return "redirect:/admin/instrument/";
    }

    // =============== AUTHOR ================

    @RequestMapping("/admin/author/")
    public String allAuthors(Model model) {
        model.addAttribute("authorList", authorService.findAll());
        return "admin/authorList";
    }

    @RequestMapping("/admin/author/add/")
    public String addAuthor(Model model) {
        model.addAttribute("author", new Author());
        return "admin/authorAdd";
    }

    @RequestMapping(value = "/admin/author/save/", method = RequestMethod.POST)
    public String saveAuthor(@ModelAttribute("author") Author author) {
        authorService.getOrSave(author);
        return "redirect:/admin/author/";
    }

    @RequestMapping("/admin/author/delete/")
    public String deleteAuthor(@PathVariable Long id) {
        authorService.delete(id);
        return "redirect:/admin/author/";
    }
}
