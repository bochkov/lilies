package sb.lilies.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import sb.lilies.model.Storage;
import sb.lilies.model.StorageView;

@Transactional
public interface StorageRepo extends JpaRepository<Storage, Long> {

    StorageView findProjectedById(Long id);

}
