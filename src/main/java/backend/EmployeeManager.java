package backend;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;
import org.xmldb.api.*;
import javax.xml.transform.OutputKeys;
import org.exist.xmldb.EXistResource;

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

        //nedokoncene.. query pre pridanie noveho node
        String Query = "insert node <>";

        XPathQueryService xpqs = (XPathQueryService)collection.getService("XPathQueryService", "1.0");
        xpqs.setProperty("indent", "yes");
        xpqs.query(Query);
    }

    public void deleteEmployee(Employee employee) {

    }

    public void createStatementOfRevenue(Employee employee, Revenue revenue) {

    }

    public List<Employee> listAllEmployees() {
        XMLResource res = null;

        try {
            res = (XMLResource)collection.getResource("Employees");

            if(res == null) {
                System.out.println("document not found!");
            } else {
                System.out.println(res.getContent());
            }
        } catch (XMLDBException e) {
            e.printStackTrace();
        } finally {

            if(res != null) {
                try {
                    ((EXistResource)res).freeResources();
                } catch(XMLDBException xe) {
                    xe.printStackTrace();
                }
            }

            if(collection != null) {
                try {
                    collection.close();
                } catch(XMLDBException xe) {
                    xe.printStackTrace();
                }
            }
        }
    }

    public Employee getEmployee(Long id) {
        return null;
    }
}
