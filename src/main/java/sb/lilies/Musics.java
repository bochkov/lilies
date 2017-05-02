package sb.lilies;

import java.sql.SQLException;

public interface Musics {

    Iterable<Music> iterate() throws SQLException;

    long count() throws SQLException;

    Iterable<Music> search(String token) throws SQLException;

}
