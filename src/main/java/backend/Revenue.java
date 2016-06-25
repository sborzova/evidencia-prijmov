package backend;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by Tomas on 12.05.2016.
 */
public class Revenue {

    private Long employeeId;
    private Long id;
    private int hours;
    private BigDecimal totalSalary;
    private LocalDate drawInvoiceDate;

    public Revenue(long employeeId, long id, int hours, BigDecimal totalSalary, LocalDate drawInvoiceDate) {
        this.employeeId = employeeId;
        this.id = id;
        this.hours = hours;
        this.totalSalary = totalSalary;
        this.drawInvoiceDate = drawInvoiceDate;
    }

    public Revenue() {

    }

    public Long getId() {
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

    public BigDecimal getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(BigDecimal totalSalary) {
        this.totalSalary = totalSalary;
    }

    public LocalDate getDrawInvoiceDate() {
        return drawInvoiceDate;
    }

    public void setDrawInvoiceDate(LocalDate drawInvoiceDate) {
        this.drawInvoiceDate = drawInvoiceDate;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }
}
