package sb.lilies.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sb.lilies.model.Storage;
import sb.lilies.model.StorageView;

@Transactional(readOnly = true)
public interface StorageRepo extends JpaRepository<Storage, Long> {

    @Query("from Storage s where s.id = :id")
    StorageView fetch(Long id);

}
