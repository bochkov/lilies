package sb.lilies;

import com.google.common.collect.Iterables;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class FilterDifficulties implements Musics {

    private final Musics origin;
    private final List<String> difficulties;

    public FilterDifficulties(List<String> difficulties, Musics origin) {
        this.origin = origin;
        this.difficulties = difficulties;
    }

    @Override
    public Iterable<Music> iterate() throws SQLException {
        List<Music> musics = new ArrayList<>();
        for (Music music : this.origin.iterate()) {
            if (music.withDifficulties(difficulties))
                musics.add(music);
        }
        return musics;
    }

    @Override
    public long count() throws SQLException {
        return Iterables.size(iterate());
    }

    @Override
    public Music find(int id) throws SQLException {
        return this.origin.find(id);
    }

    @Override
    public Iterable<Music> search(String token) throws SQLException {
        return origin.search(token);
    }
}
