package FileProcessing;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;

/**
 * Created by Anonym on 24. 6. 2016.
 */
public interface ToDkb {

    /** Converts the given XML to PDF
     *
     * @return docbook file
     * @throws TransformerException
     */
    File toDbk() throws TransformerException;
}
