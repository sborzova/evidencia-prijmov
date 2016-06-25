package FileProcessing;

import org.apache.fop.apps.FOPException;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Anonym on 24. 6. 2016.
 */
public interface ToPDF {

    void convertToPDF()  throws IOException, FOPException, TransformerException;
}
