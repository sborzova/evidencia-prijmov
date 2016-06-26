package FileProcessing;

import backend.Employee;
import backend.Revenue;
import org.apache.fop.apps.FOPException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anonym on 25. 6. 2016.
 */
public class CreateXMLImpl implements CreateXML {

    /**
     * final variable represents path to revenue xml file
     */
    private static final String REVENUE_XML_PATH = ".\\src\\main\\resources\\revenueXml.xml";

    private ToDkbImpl toDbkManager = new ToDkbImpl();

    @Override
    public File createXML(Long idInvoice, Employee employeeData, LocalDate fromDate, LocalDate toDate, List<Revenue> revenuesList) {

        File file = null;

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("invoice");
            doc.appendChild(rootElement);

            Element iid = doc.createElement("iid");
            iid.appendChild(doc.createTextNode(idInvoice.toString()));
            rootElement.appendChild(iid);

            Element date = doc.createElement("date");
            rootElement.appendChild(date);

            Element from = doc.createElement("from");
            from.appendChild(doc.createTextNode(fromDate.toString()));
            date.appendChild(from);

            Element to = doc.createElement("to");
            to.appendChild(doc.createTextNode(toDate.toString()));
            date.appendChild(to);

            Element employee = doc.createElement("employee");
            rootElement.appendChild(employee);

            Element eid = doc.createElement("eid");
            eid.appendChild(doc.createTextNode(Long.toString(employeeData.getId())));
            employee.appendChild(eid);

            // shorten way staff.setAttribute("id", "1");

            Element forname = doc.createElement("forname");
            forname.appendChild(doc.createTextNode(employeeData.getForename()));
            employee.appendChild(forname);

            Element surname = doc.createElement("surname");
            surname.appendChild(doc.createTextNode(employeeData.getSurname()));
            employee.appendChild(surname);

            Element hourlyWage = doc.createElement("hourlyWage");
            hourlyWage.appendChild(doc.createTextNode(employeeData.getHourlyWage().toString()));
            employee.appendChild(hourlyWage);

            Element revenues = doc.createElement("revenues");
            rootElement.appendChild(revenues);

            for(Revenue rev : revenuesList){

                Element revenue = doc.createElement("revenue");
                revenues.appendChild(revenue);

                Attr revenues_atr = doc.createAttribute("id");
                revenues_atr.setValue(Long.toString(rev.getId()));
                revenue.setAttributeNode(revenues_atr);

                Element hours = doc.createElement("hours");
                hours.appendChild(doc.createTextNode(Integer.toString(rev.getHours())));
                revenue.appendChild(hours);

                Element totalSalary = doc.createElement("totalSalary");
                totalSalary.appendChild(doc.createTextNode(rev.getTotalSalary().toString()));
                revenue.appendChild(totalSalary);
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(REVENUE_XML_PATH));
            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            file = toDbkManager.toDbk();

        } catch (ParserConfigurationException | TransformerException ex) {
            ex.printStackTrace();
        }

        return file;
    }


/*    public static void main(String[] args) {
        ToDkbImpl toDbkManager = new ToDkbImpl();
        ToPDFImpl toPDFManager = new ToPDFImpl();
        try {

            Employee employeeData = new Employee(1L, "Rita", "Mal", new BigDecimal(10));
            List<Revenue> revenuesList = new ArrayList<>();
            revenuesList.add(new Revenue(1L, 1L, 50, new BigDecimal(4576.12), LocalDate.of(2000,10,1)));
            revenuesList.add(new Revenue(1L, 2L, 90, new BigDecimal(458.12), LocalDate.of(2000,10,1)));

            LocalDate fromDate = LocalDate.of(2222,2,2);
            LocalDate toDate = LocalDate.of(1998,10,1);

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("invoice");
            doc.appendChild(rootElement);

            Element date = doc.createElement("date");
            rootElement.appendChild(date);

            Element from = doc.createElement("from");
            from.appendChild(doc.createTextNode(fromDate.toString()));
            date.appendChild(from);

            Element to = doc.createElement("to");
            to.appendChild(doc.createTextNode(toDate.toString()));
            date.appendChild(to);

            Element employee = doc.createElement("employee");
            rootElement.appendChild(employee);

            /*
            Element eid = doc.createElement("eid");
            eid.appendChild(doc.createTextNode(Long.toString(emp.getId())));
            employee.appendChild(eid);
            */

 /*           Attr employee_atr = doc.createAttribute("id");
            employee_atr.setValue(Long.toString(employeeData.getId()));
            employee.setAttributeNode(employee_atr);

            // shorten way
            // staff.setAttribute("id", "1");

            Element forname = doc.createElement("forname");
            forname.appendChild(doc.createTextNode(employeeData.getForename()));
            employee.appendChild(forname);

            Element surname = doc.createElement("surname");
            surname.appendChild(doc.createTextNode(employeeData.getSurname()));
            employee.appendChild(surname);

            Element hourlyWage = doc.createElement("hourlyWage");
            hourlyWage.appendChild(doc.createTextNode(employeeData.getHourlyWage().toString()));
            employee.appendChild(hourlyWage);

            Element revenues = doc.createElement("revenues");
            rootElement.appendChild(revenues);

            for(Revenue rev : revenuesList){

                Element revenue = doc.createElement("revenue");
                revenues.appendChild(revenue);

                Attr revenues_atr = doc.createAttribute("id");
                revenues_atr.setValue(Long.toString(rev.getId()));
                revenue.setAttributeNode(revenues_atr);

                Element hours = doc.createElement("hours");
                hours.appendChild(doc.createTextNode(Integer.toString(rev.getHours())));
                revenue.appendChild(hours);

                Element totalSalary = doc.createElement("totalSalary");
                totalSalary.appendChild(doc.createTextNode(rev.getTotalSalary().toString()));
                revenue.appendChild(totalSalary);
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(REVENUE_XML_PATH));

            transformer.transform(source, result);

            File file = toDbkManager.toDbk();
            toPDFManager.convertToPDF(file);

        } catch (ParserConfigurationException | TransformerException | FOPException | IOException pce) {
            pce.printStackTrace();
        }
    }
*/

}
