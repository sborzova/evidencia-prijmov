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

    /**
     *
     * @param employeeData Employee
     * @param fromDate
     * @param toDate
     * @param revenuesList
     */
    File createXML(Employee employeeData, LocalDate fromDate, LocalDate toDate, List<Revenue> revenuesList);
}
