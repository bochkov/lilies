package sb.lilies;

import java.sql.SQLException;

public interface Musics {

    long count() throws SQLException;

    Music find(int id) throws SQLException;

    Iterable<Music> iterate() throws SQLException;

    Iterable<Music> search(String token) throws SQLException;

}
