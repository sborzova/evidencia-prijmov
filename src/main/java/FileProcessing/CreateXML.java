package FileProcessing;

import backend.Employee;
import backend.Revenue;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Anonym on 25. 6. 2016.
 */
public interface CreateXML {

    /** Creates invoice XML and then converts to DOCBOOK from the given data
     *  using method toDbk() from class ToDbkImpl
     * @param idInvoice id
     * @param employeeData Employee
     * @param fromDate LocalDate
     * @param toDate LocalDate
     * @param revenuesList list of revenues for Employee in period between fromDate to toDate
     * @return docbook file with processed data
     */
    File createXML(Long idInvoice, Employee employeeData, LocalDate fromDate, LocalDate toDate, List<Revenue> revenuesList);
}
