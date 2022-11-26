package sb.lilies.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sb.lilies.model.MusicView;
import sb.lilies.service.Musics;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public final class Api {

    private final Musics musics;

    @RequestMapping("/sheet/{id}/")
    public MusicView sheet(@PathVariable Long id) {
        return musics.get(id);
    }
}
