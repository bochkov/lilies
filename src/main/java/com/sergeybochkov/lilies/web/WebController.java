package com.sergeybochkov.lilies.web;

import com.sergeybochkov.lilies.model.Author;
import com.sergeybochkov.lilies.model.Difficulty;
import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.model.Music;
import com.sergeybochkov.lilies.repository.DifficultyRepository;
import com.sergeybochkov.lilies.service.AuthorService;
import com.sergeybochkov.lilies.service.InstrumentService;
import com.sergeybochkov.lilies.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WebController {

    @Autowired
    private MusicService service;

    @Autowired private DifficultyRepository diffRepo;
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

        Difficulty diff = diffRepo.save(new Difficulty(3, "средне"));
        music.setDifficulty(diff);

        List<Instrument> instruments = new ArrayList<>();
        instruments.add(instrumentService.getOrSave(new Instrument("баян")));
        instruments.add(instrumentService.getOrSave(new Instrument("аккордеон")));
        music.setInstrument(instruments);

        return service.save(music);
    }

    @RequestMapping("/findAll/")
    public @ResponseBody List<Music> findAll() {
        return service.findAll();
    }
}
