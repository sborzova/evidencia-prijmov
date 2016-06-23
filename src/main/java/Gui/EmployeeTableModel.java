package Gui;

import backend.Employee;
import backend.EmployeeManager;
import backend.Revenue;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marek Scholtz
 * @version 2016.06.23
 */
public class EmployeeTableModel extends AbstractTableModel {

    private final EmployeeManager employeeManager;
    private List<Employee> employees = new ArrayList<Employee>();
    private ReadAllSwingWorker readAllSwingWorker;

    public EmployeeTableModel(EmployeeManager employeeManager) {

        this.employeeManager = employeeManager;
        readAllSwingWorker = new ReadAllSwingWorker(employeeManager);
        readAllSwingWorker.execute();

    }

    public int getRowCount() {
        return employees.size();
    }

    public int getColumnCount() {
        return 4;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee employee = employees.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return employee.getId();
            case 1:
                return employee.getForename();
            case 2:
                return employee.getSurname();
            case 3:
                return employee.getHourlyWage();
            default:
                throw new IllegalArgumentException("Column index out of range");
        }
    }

    private class ReadAllSwingWorker extends SwingWorker<List<Employee>, Void> {

        private final EmployeeManager employeeManager;

        public ReadAllSwingWorker(EmployeeManager employeeManager) {
            this.employeeManager = employeeManager;
        }

        @Override
        protected List<Employee> doInBackground() throws Exception {
            return employeeManager.listAllEmployees();
        }

        @Override
        protected void done() {
            try {
                employees = get();
                fireTableDataChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class AddSwingWorker extends SwingWorker<Void, Void> {

        private final EmployeeManager employeeManager;
        private final Employee employee;

        public AddSwingWorker(EmployeeManager employeeManager, Employee employee) {
            this.employeeManager = employeeManager;
            this.employee = employee;
        }

        @Override
        protected Void doInBackground() throws Exception {
            employeeManager.createEmployee(employee);
            return null;
        }

        @Override
        protected void done() {
            try {
                get();
                employees.add(employee);
                fireTableRowsInserted(employees.size() - 1, employees.size() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class DeleteSwingWorker extends SwingWorker <Void, Void> {

        private final EmployeeManager employeeManager;
        private final int row;

        public DeleteSwingWorker(EmployeeManager employeeManager, int rowIndex) {
            this.employeeManager = employeeManager;
            this.row = rowIndex;
        }

        @Override
        protected Void doInBackground() throws Exception {
            employeeManager.deleteEmployee(employeeManager.getEmployee((Long) getValueAt(row, 0)));
            return null;
        }

        @Override
        protected void done() {
            employees.delete(row);
            fireTableRowsDeleted(row, row);
        }
    }

    private class createStatementOfRevenueSwingWorker extends SwingWorker <Void, Void> {

        private final EmployeeManager employeeManager;
        private final Revenue revenue;
        private final int row;

        public createStatementOfRevenueSwingWorker(EmployeeManager employeeManager, Revenue revenue, int rowIndex) {
            this.employeeManager = employeeManager;
            this.revenue = revenue;
            this.row = rowIndex;
        }

        @Override
        protected Void doInBackground() throws Exception {
            employeeManager.createStatementOfRevenue(employeeManager.getEmployee((Long) getValueAt(row, 0)), revenue);
            return null;
        }
    }

    void addRow(Employee employee) {
        AddSwingWorker addSwingWorker = new AddSwingWorker(employeeManager, employee);
        addSwingWorker.execute();
    }

    public void deleteRow(int row) {
        DeleteSwingWorker deleteSwingWorker = new DeleteSwingWorker(employeeManager, row);
        deleteSwingWorker.execute();
    }

    public void createStatementOfRevenueOfRow(EmployeeManager employeeManager, Revenue revenue, int row) {
        EmployeeTableModel.createStatementOfRevenueSwingWorker createStatementOfRevenueSwingWorker = new createStatementOfRevenueSwingWorker(employeeManager, revenue, row);
        createStatementOfRevenueSwingWorker.execute();
    }


}
