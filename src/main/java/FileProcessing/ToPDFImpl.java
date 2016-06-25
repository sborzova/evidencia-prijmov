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
/*
    public static void main(String[] args) {
        ToPDFImpl fOPPdfDemo = new ToPDFImpl();
        try {
            fOPPdfDemo.convertToPDF(toDbkManager.toDbk());
        } catch (FOPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * final variable represents path to invoice transformation template xsl path
     */
    private static final String TEMPLATE_XSL_PATH = ".\\src\\main\\resources\\invoiceTemplate.xsl";

    /**
     * final variable represents path to invoice pdf file
     */
    private static final String INVOICE_PDF_PATH = ".\\src\\main\\resources\\Invoice.pdf";


    public void convertToPDF(File file)  throws IOException, FOPException, TransformerException {

        File xsltFile = new File(TEMPLATE_XSL_PATH);

        StreamSource xmlSource = new StreamSource(file);

        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

        OutputStream out;
        out = new java.io.FileOutputStream(INVOICE_PDF_PATH);

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

    /**
     * Methon to convert the given XML to XSL-FO
     * @throws IOException
     * @throws FOPException
     * @throws TransformerException
     */
    /*
    public void convertToFO()  throws IOException, FOPException, TransformerException {

        File xsltFile = new File(".\\src\\main\\resources\\invoiceTemplate.xsl");

        StreamSource xmlSource = new StreamSource(new File(".\\src\\main\\resources\\Invoice.xml"));

        OutputStream out;

        out = new java.io.FileOutputStream(".\\src\\main\\resources\\temp.fo");

        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            Result res = new StreamResult(out);

            transformer.transform(xmlSource, res);

            transformer.transform(xmlSource, res);
        } finally {
            out.close();
        }
    }*/
}
