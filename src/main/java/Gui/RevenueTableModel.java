package Gui;

import backend.EmployeeManager;
import backend.Revenue;
import backend.RevenueManager;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marek Scholtz
 * @version 2016.06.23
 */
public class RevenueTableModel extends AbstractTableModel {

    private final RevenueManager revenueManager;
    private final EmployeeManager employeeManager;
    private List<Revenue> revenues = new ArrayList<Revenue>();
    private ReadAllSwingWorker readAllSwingWorker;

    public RevenueTableModel(RevenueManager revenueManager, EmployeeManager employeeManager) {

        this.revenueManager = revenueManager;
        this.employeeManager = employeeManager;
        readAllSwingWorker = new ReadAllSwingWorker(revenueManager);
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

        private final RevenueManager revenueManager;

        ReadAllSwingWorker(RevenueManager revenueManager) {
            this.revenueManager = revenueManager;
        }

        @Override
        protected List<Revenue> doInBackground() throws Exception {
            return revenueManager.listAllRevenues();
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

        private final RevenueManager revenueManager;
        private final Revenue revenue;

        AddSwingWorker(RevenueManager revenueManager, Revenue revenue) {
            this.revenueManager = revenueManager;
            this.revenue = revenue;
        }

        @Override
        protected Void doInBackground() throws Exception {
            revenueManager.createRevenue(revenue);
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

        private final RevenueManager revenueManager;
        private final EmployeeManager employeeManager;
        private final Long id;

        public ListSwingWorker(RevenueManager revenueManager, EmployeeManager employeeManager, Long id) {
            this.revenueManager = revenueManager;
            this.employeeManager = employeeManager;
            this.id = id;
        }

        @Override
        protected List<Revenue> doInBackground() throws Exception {
            return revenueManager.findRevenuesByEmployee(employeeManager.getEmployee(id));
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

            private final RevenueManager revenueManager;
            private final EmployeeManager employeeManager;
            private final LocalDate from;
            private final LocalDate to;
            private final Long id;

            public FindSwingWorker(RevenueManager revenueManager, EmployeeManager employeeManager, LocalDate from, LocalDate to, Long id) {
                this.revenueManager = revenueManager;
                this.employeeManager = employeeManager;
                this.from = from;
                this.to = to;
                this.id = id;
            }

            @Override
            protected List<Revenue> doInBackground() throws Exception {
                return revenueManager.listRevenuesByDate(employeeManager.getEmployee(id), from, to);
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
/*
        private class GenerateSwingWorker extends SwingWorker<List<Revenue>, Void> {

            private final RevenueManager revenueManager;
            private final LocalDate from;
            private final LocalDate to;
            private final InvoiceTableModel invoiceTableModel;

            public GenerateSwingWorker(RevenueManager revenueManager, LocalDate from, LocalDate to, InvoiceTableModel invoiceTableModel) {
                this.revenueManager = revenueManager;
                this.from = from;
                this.to = to;
                this.invoiceTableModel = invoiceTableModel;
            }

            @Override
            protected Invoice doInBackground() throws Exception {
                Invoice invoice = revenueManager.createInvoice(from, to);
                invoiceTableModel.addRow(invoice);
                return invoice;
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
    */
    void addRow(Revenue revenue) {
        AddSwingWorker addSwingWorker = new AddSwingWorker(revenueManager, revenue);
        addSwingWorker.execute();
    }

    void listRows(Long id) {
        ListSwingWorker listSwingWorker = new ListSwingWorker(revenueManager, employeeManager, id);
        listSwingWorker.execute();
    }


    void findRows(LocalDate from, LocalDate to, Long id) {
        FindSwingWorker findSwingWorker = new FindSwingWorker(revenueManager, employeeManager, from, to, id);
        findSwingWorker.execute();
    }
/*
    void generateRows(LocalDate from, LocalDate to, InvoiceTableModel invoiceTableModel) {
        GenerateSwingWorker generateSwingWorker = new GenerateSwingWorker(revenueManager, from, to, invoiceTableModel);
        generateSwingWorker.execute();
    }
*/

}
