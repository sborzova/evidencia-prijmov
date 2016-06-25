package backend;

import FileProcessing.CreateXMLImpl;
import FileProcessing.ToPDFImpl;
import org.apache.fop.apps.FOPException;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
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

        XPathQueryService xpqs = (XPathQueryService)collection.getService("XPathQueryService", "1.0");
        xpqs.setProperty("indent", "yes");
        ResourceSet result = xpqs.query("string(/revenues/revenue[last()]/rid)");

        File file = new File("ahoj.dbk");

        new ToPDFImpl().convertToPDF(file);
    }
/*
    public List<Invoice> listAllInvoices() throws XMLDBException {
        List<Invoice> invoices = new ArrayList<>();

        XPathQueryService xpqs = (XPathQueryService)collection.getService("XPathQueryService", "1.0");
        xpqs.setProperty("indent", "yes");


        File directory = new File("\\");

        for(File file : directory.listFiles()) {
            ResourceSet result = xpqs.query("/" + file.getName() + "/book/child::node()/text()");




            Invoice invoice = new Invoice();
            invoice.setId();
            invoice.setEmployeeID();
        }

        return invoices;
    }
*/
    public Invoice getInvoice(Long id) {
        Invoice invoice = new Invoice();




        return invoice;
    }

    public void generateDocBook(Invoice invoice) throws XMLDBException {

        XMLResource res = null;
        res = (XMLResource)collection.createResource(null, "XMLResource");


        Employee employee = new EmployeeManager(collection).getEmployee(invoice.getEmployeeID());

        File f = new CreateXMLImpl().createXML(employee,invoice.getFrom(),invoice.getTo(),
                new RevenueManager(collection).listRevenuesByDate(employee,invoice.getFrom(),invoice.getTo()));
        File newFile = new File("\\invoices\\ahoj.dbk");
        f.renameTo(newFile);

        res.setContent(newFile);
        collection.storeResource(res);
    }
}
