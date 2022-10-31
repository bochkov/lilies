package sb.lilies.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import sb.lilies.model.Instrument;
import sb.lilies.model.InstrumentView;

public interface InstrumentRepo extends JpaRepository<Instrument, Integer> {

    List<InstrumentView> findAllProjectedByOrderByName();

}
