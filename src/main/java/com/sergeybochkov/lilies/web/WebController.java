package com.sergeybochkov.lilies.web;

import com.sergeybochkov.lilies.model.Difficulty;
import com.sergeybochkov.lilies.model.Instrument;
import com.sergeybochkov.lilies.model.Music;
import com.sergeybochkov.lilies.service.DifficultyService;
import com.sergeybochkov.lilies.service.InstrumentService;
import com.sergeybochkov.lilies.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public final class WebController {

    private final MusicService musicService;
    private final DifficultyService diffService;
    //private final AuthorService authorService;
    private final InstrumentService instrumentService;

    @Autowired
    public WebController(DifficultyService diffService,
                         InstrumentService instrumentService,
                         //AuthorService authorService,
                         MusicService musicService) {
        this.diffService = diffService;
        this.musicService = musicService;
        this.instrumentService = instrumentService;
        //this.authorService = authorService;
    }

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("instruments", instrumentService.findAll());
        model.addAttribute("difficulties", diffService.findAll());
        return "lilies/index";
    }

    @RequestMapping("/about/")
    public String about(Model model) {
        model.addAttribute("sheets", musicService.findAll().size());
        model.addAttribute("instruments", instrumentService.findAll().size());
        return "lilies/about";
    }

    @RequestMapping("/search/")
    public String search(Model model, @RequestParam String query) {
        model.addAttribute("object_list", musicService.findBySomething(query));
        model.addAttribute("query", query);
        return "lilies/search";
    }

    @RequestMapping("/sheet-{id}/")
    public String detail(Model model, @PathVariable Long id) {
        model.addAttribute("object", musicService.findOne(id));
        return "lilies/detail";
    }

    @RequestMapping(value = "/a/music/", method = RequestMethod.POST)
    public String getMusic(Model model,
                           @RequestParam(value = "difficulties[]", required = false) String[] difficulties,
                           @RequestParam(value = "instruments[]", required = false) String[] instruments) {
        List<Instrument> instrumentList = new ArrayList<>();
        if (instruments != null)
            for (String inst : instruments)
                instrumentList.add(instrumentService.findBySlug(inst));

        List<Difficulty> difficultyList = new ArrayList<>();
        if (difficulties != null)
            for (String diff : difficulties)
                difficultyList.add(diffService.get(Integer.valueOf(diff)));

        model.addAttribute("object_list",
                musicService.findByDifficultyAndInstrumentIn(difficultyList, instrumentList));
        return "lilies/ajax_list";
    }

    @RequestMapping("/findAll/")
    public @ResponseBody List<Music> findAll() {
        return musicService.findAll();
    }
}
