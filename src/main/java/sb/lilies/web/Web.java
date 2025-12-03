package sb.lilies.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sb.lilies.model.MusicView;
import sb.lilies.service.Musics;

import java.util.List;

@Controller
@RequiredArgsConstructor
public final class Web {

    private final Musics musics;

    @GetMapping("/about/")
    public String about(Model model) {
        model.addAttribute("sheets", musics.totalSheets());
        model.addAttribute("instruments", musics.totalInstruments());
        return "about";
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("instruments", musics.allInstruments());
        model.addAttribute("difficulties", musics.allRatings());
        return "index";
    }

    @PostMapping("/a/music/")
    public String allSheets(@RequestParam(required = false, name = "difficulties[]", defaultValue = "") List<Integer> difficulties,
                            @RequestParam(required = false, name = "instruments[]", defaultValue = "") List<String> instruments,
                            Model model) {
        List<MusicView> list = musics.filter(difficulties, instruments);
        model.addAttribute("object_list", list);
        return "ajax_list";
    }

    @GetMapping("/sheet/{id}/")
    public String oneSheet(@PathVariable Long id, Model model) {
        model.addAttribute("object", musics.get(id));
        return "detail";
    }

    @GetMapping("/search/")
    public String search(@RequestParam String query, Model model) {
        model.addAttribute("query", query);
        model.addAttribute("object_list", musics.search(query));
        return "search";
    }

}
