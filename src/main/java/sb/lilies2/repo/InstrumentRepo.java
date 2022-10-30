package sb.lilies2.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import sb.lilies2.model.Instrument;

public interface InstrumentRepo extends JpaRepository<Instrument, Integer> {
}
