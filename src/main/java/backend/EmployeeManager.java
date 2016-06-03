package backend;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;
import org.xmldb.api.*;
import javax.xml.transform.OutputKeys;
import org.exist.xmldb.EXistResource;

/**
 * Created by Tomas on 12.05.2016.
 */
public class EmployeeManager {

    private Collection collection;

    public EmployeeManager(Collection collection) {
        this.collection = collection;
    }

    public void CreateEmployee(String firstName, String surname, Double hourlyWage) throws XMLDBException {

        //nedokoncene.. query pre pridanie noveho node
        String Query = "insert node <>";

        XPathQueryService xpqs = (XPathQueryService)collection.getService("XPathQueryService", "1.0");
        xpqs.setProperty("indent", "yes");
        xpqs.query(Query);
    }

    public void UpdateEmpoyee() {

    }

    public void DeleteEmployee() {

    }


}
