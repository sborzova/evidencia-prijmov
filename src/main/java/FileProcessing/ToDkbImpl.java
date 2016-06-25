package FileProcessing;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

/**
 * Created by Anonym on 24. 6. 2016.
 */
public class ToDkbImpl {
    public static void main(String[] args)
            throws TransformerConfigurationException, TransformerException {

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer xsltProc = tf.newTransformer(
                new StreamSource(new File("C:\\Users\\Anonym\\Documents\\GitHub\\EvidenciaPrijmov\\evidencia-prijmov\\src\\main\\resources\\xmlTemplate.xsl")));

        xsltProc.transform(
                new StreamSource(new File("C:\\Users\\Anonym\\Documents\\GitHub\\EvidenciaPrijmov\\evidencia-prijmov\\src\\main\\resources\\file.xml")),
                new StreamResult(new File("C:\\Users\\Anonym\\Documents\\GitHub\\EvidenciaPrijmov\\evidencia-prijmov\\src\\main\\resources\\Invoice.xml")));
    }

}
