package Gui;

import backend.Employee;
import backend.EmployeeManagerImpl;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Marek Scholtz
 * @version 2016.06.23
 */
public class EmployeeTableModel extends AbstractTableModel {

    private final EmployeeManagerImpl employeeManagerImpl;
    private List<Employee> employees = new ArrayList<Employee>();
    private JOptionPane dialog;

    EmployeeTableModel(EmployeeManagerImpl employeeManagerImpl) {

        this.employeeManagerImpl = employeeManagerImpl;
        ReadAllSwingWorker readAllSwingWorker = new ReadAllSwingWorker(employeeManagerImpl);
        readAllSwingWorker.execute();

    }

    public int getRowCount() {
        return employees.size();
    }

    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "ID";
            case 1:
                return "Forename";
            case 2:
                return "Surname";
            case 3:
                return "Hourly wage";
            default:
                throw new IllegalArgumentException("Column index out of range");
        }
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

        private final EmployeeManagerImpl employeeManagerImpl;

        public ReadAllSwingWorker(EmployeeManagerImpl employeeManagerImpl) {
            this.employeeManagerImpl = employeeManagerImpl;
        }

        @Override
        protected List<Employee> doInBackground() throws Exception {
            return employeeManagerImpl.listAllEmployees();
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

        private final EmployeeManagerImpl employeeManagerImpl;
        private final Employee employee;

        public AddSwingWorker(EmployeeManagerImpl employeeManagerImpl, Employee employee) {
            this.employeeManagerImpl = employeeManagerImpl;
            this.employee = employee;
        }

        @Override
        protected Void doInBackground() throws Exception {
            employeeManagerImpl.createEmployee(employee);
            return null;
        }

        @Override
        protected void done() {
            try {
                get();
                employees.add(employee);
                fireTableRowsInserted(employees.size() - 1, employees.size() - 1);
            } catch (InterruptedException | ExecutionException e) {
                JOptionPane.showMessageDialog(dialog, e.getCause().getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class DeleteSwingWorker extends SwingWorker <Void, Void> {

        private final EmployeeManagerImpl employeeManagerImpl;
        private final int row;

        public DeleteSwingWorker(EmployeeManagerImpl employeeManagerImpl, int rowIndex) {
            this.employeeManagerImpl = employeeManagerImpl;
            this.row = rowIndex;
        }

        @Override
        protected Void doInBackground() throws Exception {
            employeeManagerImpl.deleteEmployee(employeeManagerImpl.getEmployee((Long) getValueAt(row, 0)));
            return null;
        }

        @Override
        protected void done() {
            employees.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }

    void addRow(Employee employee) {
        AddSwingWorker addSwingWorker = new AddSwingWorker(employeeManagerImpl, employee);
        addSwingWorker.execute();
    }

    void deleteRow(int row) {
        DeleteSwingWorker deleteSwingWorker = new DeleteSwingWorker(employeeManagerImpl, row);
        deleteSwingWorker.execute();
    }
}
