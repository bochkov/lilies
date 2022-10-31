package sb.lilies.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import sb.lilies.model.Author;

public interface AuthorRepo extends JpaRepository<Author, Long> {
}
