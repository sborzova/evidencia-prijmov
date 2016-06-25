package backend;

import FileProcessing.CreateXMLImpl;
import org.exist.xmldb.EXistResource;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas on 24.06.2016.
 */
public class RevenueManager {

    private Collection collection;

    public RevenueManager(Collection collection) {
        this.collection = collection;
    }

    public void createRevenue(Revenue revenue) throws XMLDBException {

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
                + "<revenue>"
                + "     <rid>"+ revenue.getId() +"</rid>"
                + "     <eid>"+ revenue.getEmployeeId() +"</eid>"
                + "     <hours>"+ revenue.getHours() +"</hours>"
                + "     <totalSalary>"+ revenue.getTotalSalary() +"</totalSalary>"
                + "     <drawInvoiceDate>"+ revenue.getDrawInvoiceDate() +"</drawInvoiceDate>"
                + "</revenue>"
                + "into /revenues";

        xpqs.query(Query);
    }

    public List<Revenue> listAllRevenues() {

        List<Revenue> revenues = new ArrayList<Revenue>();

        try {
            XPathQueryService xpqs = (XPathQueryService)collection.getService("XPathQueryService", "1.0");
            xpqs.setProperty("indent", "yes");

            String xpath = "/revenues/revenue/child::node()/text()";

            ResourceSet result = xpqs.query(xpath);
            ResourceIterator i = result.getIterator();

            while(i.hasMoreResources()) {
                revenues.add(setUpRevenue(i));
            }

        } catch (XMLDBException e) {
            e.printStackTrace();
        } finally {
            if(collection != null) {
                try { collection.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
            }
        }

        return revenues;
    }

    public List<Revenue> findRevenuesByEmployee(Employee employee) throws XMLDBException {

        List<Revenue> revenues = new ArrayList<Revenue>();

        XPathQueryService xpqs = (XPathQueryService)collection.getService("XPathQueryService", "1.0");
        xpqs.setProperty("indent", "yes");

        String xpath = "/revenues/revenue[eid="+employee.getId()+"]/child::node()/text()";
        ResourceSet result = xpqs.query(xpath);
        ResourceIterator iterator = result.getIterator();

        while(iterator.hasMoreResources()) {
            revenues.add(setUpRevenue(iterator));
        }

        return revenues;
    }

    public List<Revenue> listRevenuesByDate(Employee employee, LocalDate from, LocalDate to) throws XMLDBException {

        List<Revenue> revenues = new ArrayList<Revenue>();

        XPathQueryService xpqs = (XPathQueryService)collection.getService("XPathQueryService", "1.0");
        xpqs.setProperty("indent", "yes");

        String f = from.toString();
        f = f.replace("-","");
        Integer.parseInt(f);

        String t = to.toString();
        t = t.replace("-","");
        Integer.parseInt(t);

        String xpath = "/revenues/revenue[eid="+employee.getId()+" and number(translate(drawInvoiceDate/text(),'-','')) >="+f+" " +
                "and number(translate(drawInvoiceDate/text(),'-','')) <="+t+"]/child::node()/text()";
        ResourceSet result = xpqs.query(xpath);
        ResourceIterator iterator = result.getIterator();

        while(iterator.hasMoreResources()) {
            revenues.add(setUpRevenue(iterator));
        }

        return revenues;
    }

    public Revenue setUpRevenue(ResourceIterator i) throws XMLDBException {

        Revenue revenue = new Revenue();
        Resource res = null;

        try {
            res = i.nextResource();
            revenue.setId(Long.parseLong(res.getContent().toString()));
        } finally {
            try { ((EXistResource)res).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
        }
        try {
            res = i.nextResource();
            revenue.setEmployeeId(Long.parseLong(res.getContent().toString()));
        } finally {
            try { ((EXistResource)res).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
        }
        try {
            res = i.nextResource();
            revenue.setHours(Integer.parseInt(res.getContent().toString()));
        } finally {
            try { ((EXistResource)res).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
        }
        try {
            res = i.nextResource();
            revenue.setTotalSalary(new BigDecimal(res.getContent().toString()));
        } finally {
            try { ((EXistResource)res).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date;

            res = i.nextResource();
            revenue.setDrawInvoiceDate(LocalDate.parse(res.getContent().toString(), formatter));
        } finally {
            try { ((EXistResource)res).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
        }

        return revenue;
    }
}