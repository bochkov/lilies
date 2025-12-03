package sb.lilies.repo;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sb.lilies.model.Instrument;
import sb.lilies.model.InstrumentView;

import java.util.List;

@Transactional(readOnly = true)
public interface InstrumentRepo extends JpaRepository<Instrument, Integer> {

    @Query("from Instrument")
    List<InstrumentView> fetchAll(Sort sort);

}
