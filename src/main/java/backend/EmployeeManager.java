package backend;

import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.XMLDBException;

import java.util.List;

/**
 * Created by Tomas on 27.06.2016.
 */
public interface EmployeeManager {

    /**
     * Method to create an employee
     * @param employee employee to be created
     * @throws XMLDBException
     */
    void createEmployee(Employee employee) throws XMLDBException;

    /**
     * Method to delete an employee
     * @param employee employee to be deleted
     * @throws XMLDBException
     */
    void deleteEmployee(Employee employee) throws XMLDBException;

    /**
     * Method to retrieve all employees
     * @return all employees
     */
    List<Employee> listAllEmployees();

    /**
     * Method to retrieve a given employee
     * @param id employee id
     * @return desired employee
     * @throws XMLDBException
     */
    Employee getEmployee(Long id) throws XMLDBException;

    /**
     * Method to assign data to a given employee
     * @param iterator iterator
     * @return employee
     * @throws XMLDBException
     */
    Employee setUpEmployee(ResourceIterator iterator) throws XMLDBException;
}
