package FileProcessing;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;

/**
 * Created by Anonym on 24. 6. 2016.
 */
public interface ToDkb {

    File toDbk() throws TransformerException;
}
