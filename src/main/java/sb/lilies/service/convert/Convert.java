package sb.lilies.service.convert;

import java.io.IOException;

import sb.lilies.model.Sheet;

public interface Convert {

    void convert(Sheet sheet) throws IOException;

}
