package backend;

import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;
import org.xmldb.api.*;
import javax.xml.transform.OutputKeys;
import org.exist.xmldb.EXistResource;

import java.math.BigDecimal;

// TENTO MAIN BUDE V MAINFOrM!!!! VYTVORI sa TAM KOLEKCIA AK ESTE NEEXISTUJE A PRIDAJU SA DO NEJ XML SUBORY SO ZAKLADNOU KOSTROU
//XML SO ZAKLADNOU KOSTROU BUDE ULOZENY V RESOURCES, ABY PRI PRVOM SPSTENI APLIKACIE MOOHOL PRIRADIT DO NOVOVYTORENEJ KOLEKCIE
//

public class Main {

    private static String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db/RevenueEvidence";
    private static Collection col = null;

    public static void initializeDatabase() throws Exception {

        final String driver = "org.exist.xmldb.DatabaseImpl";
        Class cl = Class.forName(driver);
        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");
        DatabaseManager.registerDatabase(database);

        XMLResource res = null;
        try {
            col = DatabaseManager.getCollection(URI);
            col.setProperty(OutputKeys.INDENT, "no");

        } finally {

            if(col != null) {
                try { col.close();
                } catch(XMLDBException xe) {
                    xe.printStackTrace();
                }
            }
        }
    }

    public static void main(String args[]) throws Exception {

        initializeDatabase();
        EmployeeManager manager = new EmployeeManager(col);
        //manager.listAllEmployees();


        Employee emp = new Employee();
        emp.setId(1L);
        emp.setForename("Debil");
        emp.setSurname("retardovany");
        emp.setHourlyWage(BigDecimal.valueOf(50));

        manager.createEmployee(emp);
        //manager.deleteEmployee(emp);
        //manager.getEmployee(2L);
    }
}