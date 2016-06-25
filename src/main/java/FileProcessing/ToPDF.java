package FileProcessing;

import org.apache.fop.apps.FOPException;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Anonym on 24. 6. 2016.
 */
public interface ToPDF {

    /**
     * Method to convert the given XML to PDF
     * @throws IOException
     * @throws FOPException
     * @throws TransformerException
     * @param file
     */
    void convertToPDF(File file)  throws IOException, FOPException, TransformerException;
}
