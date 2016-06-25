package backend;

import org.xmldb.api.base.Collection;

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

    public void exportToPDF(Invoice invoice) {


    }
    
    public List<Invoice> listAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        
        return invoices;
    }

    public Invoice getInvoice(Long id) {
        Invoice invoice = new Invoice();

        return invoice;
    }
    
}
