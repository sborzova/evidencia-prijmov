package Gui;

import backend.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Marek Scholtz
 * @version 2016.06.23
 */
public class RevenueTableModel extends AbstractTableModel {

    private final RevenueManagerImpl revenueManagerImpl;
    private final EmployeeManagerImpl employeeManagerImpl;
    private List<Revenue> revenues = new ArrayList<>();
    private Integer total;
    private Invoice invoice;
    private ReadAllSwingWorker readAllSwingWorker;

    public RevenueTableModel(RevenueManagerImpl revenueManagerImpl, EmployeeManagerImpl employeeManagerImpl) {

        this.revenueManagerImpl = revenueManagerImpl;
        this.employeeManagerImpl = employeeManagerImpl;
        readAllSwingWorker = new ReadAllSwingWorker(revenueManagerImpl);
        readAllSwingWorker.execute();

    }

    public int getRowCount() {
        return revenues.size();
    }

    public int getColumnCount() {
        return 5;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Revenue revenue = revenues.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return revenue.getId();
            case 1:
                return revenue.getEmployeeId();
            case 2:
                return revenue.getHours();
            case 3:
                return revenue.getTotalSalary();
            case 4:
                return revenue.getDrawInvoiceDate();
            default:
                throw new IllegalArgumentException("Column index out of range");
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "ID";
            case 1:
                return "Employee ID";
            case 2:
                return "Hours";
            case 3:
                return "Total salary";
            case 4:
                return "Date";
            default:
                throw new IllegalArgumentException("Column index out of range");
        }
    }

    private class ReadAllSwingWorker extends SwingWorker<List<Revenue>, Void> {

        private final RevenueManagerImpl revenueManagerImpl;

        ReadAllSwingWorker(RevenueManagerImpl revenueManagerImpl) {
            this.revenueManagerImpl = revenueManagerImpl;
        }

        @Override
        protected List<Revenue> doInBackground() throws Exception {
            return revenueManagerImpl.listAllRevenues();
        }

        @Override
        protected void done() {
            try {
                revenues = get();
                fireTableDataChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class AddSwingWorker extends SwingWorker <Void, Void> {

        private final RevenueManagerImpl revenueManagerImpl;
        private final Revenue revenue;

        AddSwingWorker(RevenueManagerImpl revenueManagerImpl, Revenue revenue) {
            this.revenueManagerImpl = revenueManagerImpl;
            this.revenue = revenue;
        }

        @Override
        protected Void doInBackground() throws Exception {
            revenueManagerImpl.createRevenue(revenue);
            return null;
        }

        @Override
        protected void done() {
            try {
                get();
                revenues.add(revenue);
                fireTableRowsInserted(revenues.size() - 1, revenues.size() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ListSwingWorker extends SwingWorker<List<Revenue>, Void> {

        private final RevenueManagerImpl revenueManagerImpl;
        private final EmployeeManagerImpl employeeManagerImpl;
        private final Long id;

        public ListSwingWorker(RevenueManagerImpl revenueManagerImpl, EmployeeManagerImpl employeeManagerImpl, Long id) {
            this.revenueManagerImpl = revenueManagerImpl;
            this.employeeManagerImpl = employeeManagerImpl;
            this.id = id;
        }

        @Override
        protected List<Revenue> doInBackground() throws Exception {
            return revenueManagerImpl.findRevenuesByEmployee(employeeManagerImpl.getEmployee(id));
        }

        @Override
        protected void done() {
            try {
                revenues = get();
                fireTableDataChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

        private class FindSwingWorker extends SwingWorker<List<Revenue>, Void> {

            private final RevenueManagerImpl revenueManagerImpl;
            private final EmployeeManagerImpl employeeManagerImpl;
            private final LocalDate from;
            private final LocalDate to;
            private final Long id;

            public FindSwingWorker(RevenueManagerImpl revenueManagerImpl, EmployeeManagerImpl employeeManagerImpl, LocalDate from, LocalDate to, Long id) {
                this.revenueManagerImpl = revenueManagerImpl;
                this.employeeManagerImpl = employeeManagerImpl;
                this.from = from;
                this.to = to;
                this.id = id;
            }

            @Override
            protected List<Revenue> doInBackground() throws Exception {
                return revenueManagerImpl.listRevenuesByDate(employeeManagerImpl.getEmployee(id), from, to);
            }

            @Override
            protected void done() {
                try {
                    revenues = get();
                    fireTableDataChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    private class RevenuesInTotalSwingWorker extends SwingWorker<Integer, Void> {

        private final JTable revenueTable;
        private final JTextField revenuesInTotalTextField;
        private Integer sum = 0;

        public RevenuesInTotalSwingWorker(JTable revenueTable, JTextField revenuesInTotalTextField) {
            this.revenueTable = revenueTable;
            this.revenuesInTotalTextField = revenuesInTotalTextField;
        }

        @Override
        protected Integer doInBackground() throws Exception {
            for (int i = 0; i < revenueTable.getRowCount(); i++) {
                sum += Integer.parseInt(revenueTable.getValueAt(i,3).toString());
            }
            return sum;
        }

        @Override
        protected void done() {
            try {
                total = get();
                revenuesInTotalTextField.setText(total.toString());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    void addRow(Revenue revenue) {
        AddSwingWorker addSwingWorker = new AddSwingWorker(revenueManagerImpl, revenue);
        addSwingWorker.execute();
    }

    void listRows(Long id) {
        ListSwingWorker listSwingWorker = new ListSwingWorker(revenueManagerImpl, employeeManagerImpl, id);
        listSwingWorker.execute();
    }

    void findRows(LocalDate from, LocalDate to, Long id) {
        FindSwingWorker findSwingWorker = new FindSwingWorker(revenueManagerImpl, employeeManagerImpl, from, to, id);
        findSwingWorker.execute();
    }
    
    void revenuesInTotal(JTable revenueTable, JTextField revenuesInTotalTextField) {
        RevenuesInTotalSwingWorker revenuesSwingWorker = new RevenuesInTotalSwingWorker(revenueTable, revenuesInTotalTextField);
        revenuesSwingWorker.execute();
    }
}
