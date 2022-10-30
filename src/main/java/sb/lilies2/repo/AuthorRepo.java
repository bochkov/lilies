package sb.lilies2.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import sb.lilies2.model.Author;

public interface AuthorRepo extends JpaRepository<Author, Long> {
}
