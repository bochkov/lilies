package sb.lilies.repo;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sb.lilies.model.Difficulty;
import sb.lilies.model.DifficultyView;

import java.util.List;

@Transactional(readOnly = true)
public interface DiffRepo extends JpaRepository<Difficulty, Integer> {

    @Query("from Difficulty")
    List<DifficultyView> fetchAll(Sort sort);

}
