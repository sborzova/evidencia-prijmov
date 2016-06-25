package backend;

import FileProcessing.CreateXMLImpl;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

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


    public void exportToPDF(Invoice invoice) throws XMLDBException {

        XPathQueryService xpqs = (XPathQueryService)collection.getService("XPathQueryService", "1.0");
        xpqs.setProperty("indent", "yes");
        ResourceSet result = xpqs.query("string(/revenues/revenue[last()]/rid)");

    }
    
    public List<Invoice> listAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        
        return invoices;
    }

    public Invoice getInvoice(Long id) {
        Invoice invoice = new Invoice();

        return invoice;
    }

    public void generateDocBook(Employee employee, LocalDate from, LocalDate to) throws XMLDBException {

        XMLResource res = null;
        res = (XMLResource)collection.createResource(null, "XMLResource");

        res.setContent(new CreateXMLImpl().createXML(employee,from,to, new RevenueManager(collection).findRevenuesByEmployee(employee)));
        collection.storeResource(res);
    }
}
