package FileProcessing;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

/**
 * Created by Anonym on 24. 6. 2016.
 */
public class ToPDFImpl {

    /**
     * final variable represents path to invoice transformation template xsl path
     */
    private static final String TEMPLATE_XSL_PATH = ".\\src\\main\\resources\\invoiceTemplate.xsl";

    public void convertToPDF(File file)  throws IOException, FOPException, TransformerException {

        File xsltFile = new File(TEMPLATE_XSL_PATH);

        StreamSource xmlSource = new StreamSource(file);

        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

        OutputStream out;
        String fileName = file.getName();
        fileName = fileName.replace(".dbk","");
        out = new java.io.FileOutputStream(".\\pdf\\Invoice "+fileName+".pdf");

        try {

            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            Result res = new SAXResult(fop.getDefaultHandler());

            transformer.transform(xmlSource, res);
        } finally {
            out.close();
        }
    }
}
