package backend;

import common.ValidationException;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;
import org.exist.xmldb.EXistResource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas on 12.05.2016.
 */
public class EmployeeManagerImpl implements EmployeeManager {

    private Collection collection;

    public EmployeeManagerImpl(Collection collection) {
        this.collection = collection;
    }

    @Override
    public void createEmployee(Employee employee) throws XMLDBException {

        validateEmployee(employee);

        XPathQueryService xpqs = (XPathQueryService)collection.getService("XPathQueryService", "1.0");
        xpqs.setProperty("indent", "yes");
        ResourceSet result = xpqs.query("string(/employees/employee[last()]/eid)");

        ResourceIterator i = result.getIterator();
        Resource res = null;
        Long id;

        try {
            res = i.nextResource();
            if (res.getContent().toString().equals("")) {
                id = 1L;
            } else {
                id = Long.parseLong(res.getContent().toString());
                id++;
            }
        } finally {
            try {
                ((EXistResource)res).freeResources();
            } catch(XMLDBException xe) {
                xe.printStackTrace();
            }
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

    @Override
    public void deleteEmployee(Employee employee) throws XMLDBException {

        Long id = employee.getId();

        XPathQueryService xpqs = (XPathQueryService)collection.getService("XPathQueryService", "1.0");
        xpqs.setProperty("indent", "yes");

        String Query = "update delete /employees/employee[eid="+id+"]";
        String Query2 = "update delete /revenues/revenue[eid="+id+"]";
        xpqs.query(Query);
        xpqs.query(Query2);
    }

    @Override
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

    @Override
    public Employee getEmployee(Long id) throws XMLDBException {

        XPathQueryService xpqs = (XPathQueryService)collection.getService("XPathQueryService", "1.0");
        xpqs.setProperty("indent", "yes");

        String xpath = "/employees/employee[eid="+id+"]/child::node()/text()";

        ResourceSet result = xpqs.query(xpath);
        ResourceIterator iterator = result.getIterator();

        return setUpEmployee(iterator);
    }

    @Override
    public Employee setUpEmployee(ResourceIterator iterator) throws XMLDBException {

        Employee employee = new Employee();
        Resource res = null;

        try {
            res = iterator.nextResource();
            employee.setId(Long.parseLong(res.getContent().toString()));
        } finally {
            try { ((EXistResource)res).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
        }
        try {
            res = iterator.nextResource();
            employee.setForename(res.getContent().toString());
        } finally {
            try { ((EXistResource)res).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
        }
        try {
            res = iterator.nextResource();
            employee.setSurname(res.getContent().toString());
        } finally {

            try { ((EXistResource)res).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
        }
        try {
            res = iterator.nextResource();
            employee.setHourlyWage(new BigDecimal(res.getContent().toString()));
        } finally {
            try { ((EXistResource)res).freeResources(); } catch(XMLDBException xe) {xe.printStackTrace();}
        }

        return employee;
    }

    private void validateEmployee(Employee employee) throws ValidationException {
        if (employee == null) {
            throw new ValidationException("Employee is null.");
        }
        if (employee.getForename() == null) {
            throw new ValidationException("Employee forename is null.");
        }
        if (employee.getForename().isEmpty()) {
            throw new ValidationException("Employee forename is empty.");
        }
        if (employee.getSurname() == null) {
            throw new ValidationException("Employee surname is null.");
        }
        if (employee.getSurname().isEmpty()) {
            throw new ValidationException("Employee surname is empty.");
        }
    }
}
