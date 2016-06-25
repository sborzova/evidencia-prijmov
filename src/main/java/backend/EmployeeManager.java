package backend;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;
import org.exist.xmldb.EXistResource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas on 12.05.2016.
 */
public class EmployeeManager {

    private Collection collection;

    public EmployeeManager(Collection collection) {
        this.collection = collection;
    }

    public void createEmployee(Employee employee) throws XMLDBException {

        XPathQueryService xpqs = (XPathQueryService)collection.getService("XPathQueryService", "1.0");
        xpqs.setProperty("indent", "yes");
        ResourceSet result = xpqs.query("string(/employees/employee[last()]/eid)");

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

        employee.setId(id);

        String insertQuery = "update insert"
        + "<employee>"
        + "     <eid>"+ id +"</eid>"
        + "     <name>"+ employee.getForename() +"</name>"
        + "     <surname>"+ employee.getSurname() +"</surname>"
        + "     <hourlyWage>"+ employee.getHourlyWage() +"</hourlyWage>"
        + "</employee>"
        + "into /employees";

        xpqs.query(insertQuery);
    }

    public void deleteEmployee(Employee employee) throws XMLDBException {

        Long id = employee.getId();

        String Query = "update delete /employees/employee[eid="+id+"]";

        XPathQueryService xpqs = (XPathQueryService)collection.getService("XPathQueryService", "1.0");
        xpqs.setProperty("indent", "yes");
        xpqs.query(Query);
    }

    public List<Employee> listAllEmployees() {

        List<Employee> employees = new ArrayList<Employee>();

        try {
            XPathQueryService xpqs = (XPathQueryService)collection.getService("XPathQueryService", "1.0");
            xpqs.setProperty("indent", "yes");

            String xpath = "/employees/employee/child::node()/text()";

            ResourceSet result = xpqs.query(xpath);
            ResourceIterator i = result.getIterator();

            while(i.hasMoreResources()) {
                employees.add(setUpEmployee(i));
            }
            
        } catch (XMLDBException e) {
            e.printStackTrace();
        } finally {
            if(collection != null) {
                try { collection.close(); } catch(XMLDBException xe) {xe.printStackTrace();}
            }
        }

        return employees;
    }

    public Employee getEmployee(Long id) throws XMLDBException {

        XPathQueryService xpqs = (XPathQueryService)collection.getService("XPathQueryService", "1.0");
        xpqs.setProperty("indent", "yes");

        String xpath = "/employees/employee[eid="+id+"]/child::node()/text()";

        ResourceSet result = xpqs.query(xpath);
        ResourceIterator iterator = result.getIterator();

        return setUpEmployee(iterator);
    }

    public Employee setUpEmployee(ResourceIterator i) throws XMLDBException {

        Employee employee = new Employee();
        Resource res = null;

        try {
            res = i.nextResource();
            employee.setId(Long.parseLong(res.getContent().toString()));
        } finally {
            try { ((EXistResource)res).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
        }
        try {
            res = i.nextResource();
            employee.setForename(res.getContent().toString());
        } finally {
            try { ((EXistResource)res).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
        }
        try {
            res = i.nextResource();
            employee.setSurname(res.getContent().toString());
        } finally {

            try { ((EXistResource)res).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
        }
        try {
            res = i.nextResource();
            employee.setHourlyWage(new BigDecimal(res.getContent().toString()));
        } finally {
            try { ((EXistResource)res).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
        }

        return employee;
    }
}
