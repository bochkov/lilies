package com.sergeybochkov.lilies.web;

import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.model.Music;
import com.sergeybochkov.lilies.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.List;

@Controller
public class WebController {

    @Autowired
    private MusicService service;

    @RequestMapping("/save/")
    public @ResponseBody Serializable saveOne() {
        Music music = new Music();
        music.setName("Ах, Самара-городок!");
        music.setSubName("Русская народная песня");

        music.setInstrument(new Instrument("Аккордеон"));
        return service.save(music);
    }

    @RequestMapping("/findAll/")
    public @ResponseBody List<Music> findAll() {
        return service.findAll();
    }
}
