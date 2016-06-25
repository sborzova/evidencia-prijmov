package FileProcessing;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

/**
 * Created by Anonym on 24. 6. 2016.
 */

public class ToDkbImpl implements ToDkb{
    /**
     *
     * @return
     * @throws TransformerException
     */
    @Override
    public File toDbk() throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer xsltProc = tf.newTransformer(
                new StreamSource(new File(".\\src\\main\\resources\\xmlTemplate.xsl")));

        xsltProc.transform(
                new StreamSource(new File(".\\src\\main\\resources\\revenueXml.xml")),
                new StreamResult(new File(".\\src\\main\\resources\\Invoice.xml")));

        //return new File(url.getPath());
        return new File(".\\src\\main\\resources\\Invoice.xml");
    }
}
