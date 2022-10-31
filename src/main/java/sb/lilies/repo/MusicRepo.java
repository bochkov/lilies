package sb.lilies.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import sb.lilies.model.Music;
import sb.lilies.model.MusicView;

public interface MusicRepo extends JpaRepository<Music, Long> {

    List<MusicView> findAllProjectedBy();

    MusicView findProjectedById(Long id);


}
