package sb.lilies2.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import sb.lilies2.model.Difficulty;

public interface DiffRepo extends JpaRepository<Difficulty, Integer> {
}
