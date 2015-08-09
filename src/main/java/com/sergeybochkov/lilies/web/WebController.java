package com.sergeybochkov.lilies.web;

import com.sergeybochkov.lilies.model.Author;
import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.model.Music;
import com.sergeybochkov.lilies.service.AuthorService;
import com.sergeybochkov.lilies.service.DifficultyService;
import com.sergeybochkov.lilies.service.InstrumentService;
import com.sergeybochkov.lilies.service.MusicService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WebController {

    @Autowired private MusicService service;
    @Autowired private DifficultyService diffService;
    @Autowired private AuthorService authorService;
    @Autowired private InstrumentService instrumentService;

    @RequestMapping("/save/")
    public @ResponseBody Serializable saveOne() {
        Music music = new Music();
        music.setName("Ах, Самара-городок!");
        music.setSubName("Русская народная песня");

        List<Author> composers = new ArrayList<>();
        composers.add(authorService.getOrSave(new Author("Иванов", "Иван", "Иванович")));
        music.setComposer(composers);

        List<Author> writers = new ArrayList<>();
        writers.add(authorService.getOrSave(new Author("Петров", "Петр", "Петрович")));
        writers.add(authorService.getOrSave(new Author("Сидоров", "Сидор", "Сидорович")));
        music.setWriter(writers);

        music.setDifficulty(diffService.get(3));

        List<Instrument> instruments = new ArrayList<>();
        instruments.add(instrumentService.getOrSave(new Instrument("баян")));
        instruments.add(instrumentService.getOrSave(new Instrument("аккордеон")));
        music.setInstrument(instruments);

        try {
            File file = new File(".gitignore");
            music.setSrcFile(IOUtils.toByteArray(new FileInputStream(file)));
        }
        catch (IOException ex) { ex.printStackTrace(); }

        return service.save(music);
    }

    @RequestMapping("/findAll/")
    public @ResponseBody List<Music> findAll() {
        return service.findAll();
    }

    @RequestMapping("/showFile/")
    public @ResponseBody String showFile() {
        List<Music> musicList = findAll();
        Music m = musicList.get(0);
        byte[] f = m.getSrcFile();

        String content = "";
        try {
            content = IOUtils.toString(f, "UTF-8");
        }
        catch (IOException ex) { ex.printStackTrace(); }
        return content;
    }
}
