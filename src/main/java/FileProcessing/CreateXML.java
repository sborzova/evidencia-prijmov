package FileProcessing;

import backend.Employee;
import backend.Revenue;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Anonym on 25. 6. 2016.
 */
public interface CreateXML {

    void createXML(Employee employee, LocalDate fromDate, LocalDate toDate, List<Revenue> revenuesList);
}
