package FileProcessing;

import backend.Employee;
import backend.Revenue;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anonym on 25. 6. 2016.
 */
public class CreateXMLImpl {

    public static void main(String argv[]) {

        try {

            Employee emp = new Employee(1L, "Rki", "ttt", new BigDecimal(10));
            List<Revenue> revenuesList = new ArrayList<>();
            revenuesList.add(new Revenue(1L, 1L, 50, new BigDecimal(4576.12), LocalDate.of(2000,10,1)));
            revenuesList.add(new Revenue(1L, 2L, 90, new BigDecimal(458.12), LocalDate.of(2000,10,1)));

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("invoice");
            doc.appendChild(rootElement);

            // staff elements
            Element employee = doc.createElement("employee");
            rootElement.appendChild(employee);

            // set attribute to staff element
            Attr employee_atr = doc.createAttribute("id");
            employee_atr.setValue(Long.toString(emp.getId()));
            employee.setAttributeNode(employee_atr);

            // shorten way
            // staff.setAttribute("id", "1");

            // firstname elements
            Element forname = doc.createElement("forname");
            forname.appendChild(doc.createTextNode(emp.getForename()));
            employee.appendChild(forname);

            // lastname elements
            Element surname = doc.createElement("surname");
            surname.appendChild(doc.createTextNode(emp.getSurname()));
            employee.appendChild(surname);

            Element hourlyWage = doc.createElement("hourlyWage");
            hourlyWage.appendChild(doc.createTextNode(emp.getHourlyWage().toString()));
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
            StreamResult result = new StreamResult(new File("C:\\Users\\Anonym\\Documents\\GitHub\\EvidenciaPrijmov\\evidencia-prijmov\\src\\main\\resources\\file.xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}
