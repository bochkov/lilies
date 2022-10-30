package sb.lilies2.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sb.lilies2.model.Author;
import sb.lilies2.model.Difficulty;
import sb.lilies2.model.Instrument;
import sb.lilies2.model.Music;
import sb.lilies2.repo.AuthorRepo;
import sb.lilies2.repo.DiffRepo;
import sb.lilies2.repo.InstrumentRepo;
import sb.lilies2.repo.MusicRepo;

@RestController
@RequestMapping("/api")
public final class Api {

    @Autowired
    private AuthorRepo authorRepo;
    @Autowired
    private DiffRepo diffRepo;
    @Autowired
    private InstrumentRepo instRepo;
    @Autowired
    private MusicRepo musicRepo;

    @RequestMapping("/authors")
    public List<Author> authors() {
        return authorRepo.findAll();
    }

    @RequestMapping("/difficulties")
    public List<Difficulty> difficulties() {
        return diffRepo.findAll(Sort.by("rating"));
    }

    @RequestMapping("/instruments")
    public List<Instrument> instruments() {
        return instRepo.findAll(Sort.by("name"));
    }

    @RequestMapping("/sheet/{id}")
    public Music sheet(@PathVariable Long id) {
        return musicRepo.findById(id).orElseThrow();
    }
}
