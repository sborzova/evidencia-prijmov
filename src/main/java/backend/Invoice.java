package backend;

import java.time.LocalDate;

/**
 * @author Tomas Gordian
 * @version 2016.06.25
 */
public class Invoice {

    private Long id;
    private Long employeeID;
    private LocalDate from;
    private LocalDate to;

    public Invoice(Long id, Long employeeID, LocalDate from, LocalDate to) {
        this.id = id;
        this.employeeID = employeeID;
        this.from = from;
        this.to = to;
    }

    public Invoice() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Long employeeID) {
        this.employeeID = employeeID;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }
}
