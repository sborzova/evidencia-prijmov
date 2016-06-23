package backend;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Tomas on 29.04.2016.
 */
public class Employee {

    private Long id;
    private String forename;
    private String surname;
    private BigDecimal hourlyWage;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public BigDecimal getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(BigDecimal hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String name) {
        this.forename = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        return id.equals(employee.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
