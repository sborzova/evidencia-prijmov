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
     * final variable represents path to revenue xml file
     */
    private static final String REVENUE_XML_PATH = ".\\src\\main\\resources\\revenueXml.xml";

    /**
     * final variable represents path to transformation template xsl file
     */
    private static final String TEMPLATE_XSL_PATH = ".\\src\\main\\resources\\xmlTemplate.xsl";

    /**
     * final variable represents path to invoice docbook file
     */
    private static final String INVOICE_XML_PATH = ".\\src\\main\\resources\\Invoice.dbk";


    @Override
    public File toDbk() throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer xsltProc = tf.newTransformer(
                new StreamSource(new File(TEMPLATE_XSL_PATH)));

        xsltProc.transform(
                new StreamSource(new File(REVENUE_XML_PATH)),
                new StreamResult(new File(INVOICE_XML_PATH)));

        return new File(INVOICE_XML_PATH);
    }
}
