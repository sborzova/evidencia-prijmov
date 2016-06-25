package backend;

import java.time.LocalDate;

/**
 * Created by Tomas on 12.05.2016.
 */
public class Revenue {

    private long employeeId;
    private long id;
    private int hours;
    private Double totalSalary;
    private LocalDate drawInvoiceDate;

    public Revenue(long employeeId, long id, int hours, Double totalSalary, LocalDate drawInvoiceDate) {
        this.employeeId = employeeId;
        this.id = id;
        this.hours = hours;
        this.totalSalary = totalSalary;
        this.drawInvoiceDate = drawInvoiceDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public Double getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(Double totalSalary) {
        this.totalSalary = totalSalary;
    }

    public LocalDate getDrawInvoiceDate() {
        return drawInvoiceDate;
    }

    public void setDrawInvoiceDate(LocalDate drawInvoiceDate) {
        this.drawInvoiceDate = drawInvoiceDate;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }
}
