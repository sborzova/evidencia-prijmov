package backend;

import FileProcessing.CreateXMLImpl;
import FileProcessing.ToPDFImpl;
import org.apache.fop.apps.FOPException;
import org.exist.xmldb.EXistResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marek Scholtz
 * @version 2016.06.25
 */
public class InvoiceManager {

    private Collection collection;

    public InvoiceManager(Collection collection) {
        this.collection = collection;
    }


    public void exportToPDF(Invoice invoice) throws XMLDBException, TransformerException, IOException, FOPException {

        /*
        XPathQueryService xpqs = (XPathQueryService)collection.getService("XPathQueryService", "1.0");
        xpqs.setProperty("indent", "yes");
        ResourceSet result = xpqs.query("string(/revenues/revenue[last()]/rid)");
        */

        File file = new File(".\\invoices\\"+invoice.getId()+".dbk");

        new ToPDFImpl().convertToPDF(file);
    }

    public List<Invoice> listAllInvoices() throws XMLDBException, ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = null;

        List<Invoice> invoices = new ArrayList<>();

        File directory = new File(".\\invoices");

        for(File file : directory.listFiles()) {
            Invoice invoice = new Invoice();

            doc = builder.parse(file);

            NodeList nodeList;
            nodeList = doc.getElementsByTagName("iid");
            Element divElement;
            divElement = (Element) nodeList.item(0);
            String iid = divElement.getTextContent();
            invoice.setId(Long.parseLong(iid));

            nodeList = doc.getElementsByTagName("eid");
            divElement = (Element) nodeList.item(0);
            String eid = divElement.getTextContent();
            invoice.setEmployeeID(Long.parseLong(eid));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            nodeList = doc.getElementsByTagName("from");
            divElement = (Element) nodeList.item(0);
            String from = divElement.getTextContent();
            invoice.setFrom(LocalDate.parse(from, formatter));

            nodeList = doc.getElementsByTagName("to");
            divElement = (Element) nodeList.item(0);
            String to = divElement.getTextContent();
            invoice.setTo(LocalDate.parse(to, formatter));

            invoices.add(invoice);
        }
        return invoices;
    }

    public Invoice getInvoice(Long id) throws XMLDBException, ParserConfigurationException, IOException, SAXException {
        Invoice invoice = new Invoice();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = null;

        File file = new File(".\\invoices\\"+id+".dbk");

        doc = builder.parse(file);

        NodeList nodeList;
        nodeList = doc.getElementsByTagName("iid");
        Element divElement;
        divElement = (Element) nodeList.item(0);
        String iid = divElement.getTextContent();
        invoice.setId(Long.parseLong(iid));

        nodeList = doc.getElementsByTagName("eid");
        divElement = (Element) nodeList.item(0);
        String eid = divElement.getTextContent();
        invoice.setEmployeeID(Long.parseLong(eid));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        nodeList = doc.getElementsByTagName("from");
        divElement = (Element) nodeList.item(0);
        String from = divElement.getTextContent();
        invoice.setFrom(LocalDate.parse(from, formatter));

        nodeList = doc.getElementsByTagName("to");
        divElement = (Element) nodeList.item(0);
        String to = divElement.getTextContent();
        invoice.setTo(LocalDate.parse(to, formatter));

        return invoice;
    }

    public void generateDocBook(Invoice invoice) throws XMLDBException {

        Employee employee = new EmployeeManager(collection).getEmployee(invoice.getEmployeeID());

        File directory = new File(".\\invoices");

        Integer size = directory.listFiles().length;
        size++;
        Long sizeLong = Long.parseLong(size.toString());
        invoice.setId(sizeLong);

        File f = new CreateXMLImpl().createXML(sizeLong,employee,invoice.getFrom(),invoice.getTo(),
                new RevenueManager(collection).listRevenuesByDate(employee,invoice.getFrom(),invoice.getTo()));
        File newFile = new File(".\\invoices\\"+invoice.getId()+".dbk");
        f.renameTo(newFile);
    }
}
