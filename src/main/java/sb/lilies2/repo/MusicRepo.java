package sb.lilies2.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import sb.lilies2.model.Music;

public interface MusicRepo extends JpaRepository<Music, Long> {
}
