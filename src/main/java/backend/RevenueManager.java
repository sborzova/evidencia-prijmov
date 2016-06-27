package backend;

import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.XMLDBException;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Tomas on 27.06.2016.
 */
public interface RevenueManager {

    /**
     * Method to create a revenue
     * @param revenue revenue to be created
     * @throws XMLDBException
     */
    void createRevenue(Revenue revenue) throws XMLDBException;

    /**
     * Method to retrieve all revenues
     * @return all revenues
     */
    List<Revenue> listAllRevenues();

    /**
     * Method to retrieve an employee´s revenues
     * @param employee employee
     * @return desired revenues
     * @throws XMLDBException
     */
    List<Revenue> findRevenuesByEmployee(Employee employee);

    /**
     * Method to retrieve revenues by employee and date
     * @param employee employee
     * @param from from date
     * @param to to date
     * @return desired revenues
     * @throws XMLDBException
     */
    List<Revenue> listRevenuesByDate(Employee employee, LocalDate from, LocalDate to);

    /**
     * Method to assign data to a given revenue
     * @param iterator iterator
     * @return revenue
     * @throws XMLDBException
     */
    Revenue setUpRevenue(ResourceIterator iterator) throws XMLDBException;

}
