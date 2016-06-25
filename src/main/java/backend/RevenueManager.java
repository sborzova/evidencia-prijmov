package backend;

import org.xmldb.api.base.*;
import org.xmldb.api.modules.XPathQueryService;

import java.util.List;

/**
 * Created by Tomas on 24.06.2016.
 */
public class RevenueManager {

    private Collection collection;

    public RevenueManager(Collection collection) {
        this.collection = collection;
    }

    public void createStatementOfRevenue(Employee employee, Revenue revenue) throws XMLDBException {

        XPathQueryService xpqs = (XPathQueryService)collection.getService("XPathQueryService", "1.0");
        xpqs.setProperty("indent", "yes");
        ResourceSet result = xpqs.query("string(/revenues/revenue[last()]/rid)");

        ResourceIterator i = result.getIterator();
        Resource res;
        res = i.nextResource();

        Long id;
        if(res.getContent().toString().equals("")) {
            id = 1L;
        } else {
            id = Long.parseLong(res.getContent().toString());
            id++;
        }

        revenue.setId(id);

        String Query = "update insert"
                + "<employee>"
                + "     <eid>"+ employee.getId() +"</eid>"
                + "     <rid>"+ revenue.getEmployeeId() +"</rid>"
                + "     <hours>"+ revenue.getHours() +"</hours>"
                + "     <totalSalary>"+ revenue.getTotalSalary() +"</totalSalary>"
                + "     <drawInvoiceDate>"+ revenue.getDrawInvoiceDate() +"</drawInvoiceDate>"
                + "</employee>"
                + "into /revenues";

        xpqs.query(Query);
    }


    /*
    public List<Revenue> listAllRevenues() {

    }

    public void createRevenue() {

    }

    public List<Revenue> findRevenuesByEmployee(Employee employee) {

    }

    public List<Revenue> listRevenuesByDate() {

    }

*/

}
