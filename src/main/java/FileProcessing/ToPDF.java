package FileProcessing;

import org.apache.fop.apps.FOPException;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Anonym on 24. 6. 2016.
 */
public interface ToPDF {

    /** Converts the given XML to PDF
     *
     * @param file xml file
     * @throws IOException
     * @throws FOPException
     * @throws TransformerException
     */
    void convertToPDF(File file)  throws IOException, FOPException, TransformerException;
}
