package sb.lilies.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import sb.lilies.model.Author;

@Transactional(readOnly = true)
public interface AuthorRepo extends JpaRepository<Author, Long> {
}
