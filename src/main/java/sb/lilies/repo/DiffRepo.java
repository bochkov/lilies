package sb.lilies.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import sb.lilies.model.Difficulty;
import sb.lilies.model.DifficultyView;

public interface DiffRepo extends JpaRepository<Difficulty, Integer> {

    List<DifficultyView> findAllProjectedByOrderByRating();

}
