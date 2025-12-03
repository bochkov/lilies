package sb.lilies.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sb.lilies.model.Music;
import sb.lilies.model.MusicView;

import java.util.List;

@Transactional(readOnly = true)
public interface MusicRepo extends JpaRepository<Music, Long>, JpaSpecificationExecutor<Music> {

    @Query("from Music m where m.id = :id")
    MusicView fetch(Long id);

    @Query("""
            select m from Music m
              join m.difficulty d
              join m.instruments i
            where
              d.rating in :difficulties
              and
              i.slug in :instruments
            order by m.name
            """)
    List<MusicView> fetchDiffsAndInstruments(List<Integer> difficulties, List<String> instruments);

    @Query("""
            select m from Music m
                left join m.composers c
                left join m.writers w
            where
                lower(m.name) like lower(concat('%',:q,'%'))
             or lower(m.subname) like lower(concat('%',:q,'%'))
             or lower(c.lastName) like lower(concat('%',:q,'%'))
             or lower(w.lastName) like lower(concat('%',:q,'%'))
            """)
    List<MusicView> matched(String q);

}
