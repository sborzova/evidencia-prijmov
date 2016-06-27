package backend;

import org.apache.fop.apps.FOPException;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

/**
 * Created by Tomas on 27.06.2016.
 */
public interface InvoiceManager {

    /**
     * Method to invoke a conversion of an invoice into a pdf file
     * @param invoice invoice to be converted
     * @throws XMLDBException
     * @throws TransformerException
     * @throws IOException
     * @throws FOPException
     */
    void exportToPDF(Invoice invoice) throws XMLDBException, TransformerException, IOException, FOPException;

    /**
     * Method to list all invoices
     * @return all invoices
     * @throws XMLDBException
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    List<Invoice> listAllInvoices() throws XMLDBException, ParserConfigurationException, IOException, SAXException;

    /**
     * Method to retrieve a given invoice
     * @param id invoice id
     * @return desired invoice
     * @throws XMLDBException
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    Invoice getInvoice(Long id) throws XMLDBException, ParserConfigurationException, IOException, SAXException;

    /**
     * Method to invoke a conversion of an invoice into a dbk file
     * @param invoice invoice to be converted
     * @throws XMLDBException
     */
    void generateDocBook(Invoice invoice) throws XMLDBException;
}
