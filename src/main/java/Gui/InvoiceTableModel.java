package Gui;

import backend.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marek Scholtz
 * @version 2016.06.23
 */
public class InvoiceTableModel extends AbstractTableModel {


    private final InvoiceManager invoiceManager;
    private final RevenueManager revenueManager;
    private final EmployeeManager employeeManager;
    private List<Invoice> invoices = new ArrayList<Invoice>();
    private Invoice invoice;
    private ReadAllSwingWorker readAllSwingWorker;

    public InvoiceTableModel(InvoiceManager invoiceManager, RevenueManager revenueManager, EmployeeManager employeeManager) {

        this.invoiceManager = invoiceManager;
        this.revenueManager = revenueManager;
        this.employeeManager = employeeManager;
        readAllSwingWorker = new ReadAllSwingWorker(invoiceManager);
        readAllSwingWorker.execute();

    }

    public int getRowCount() {
        return invoices.size();
    }

    public int getColumnCount() {
        return 4;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Invoice invoice = invoices.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return invoice.getId();
            case 1:
                return invoice.getEmployeeID();
            case 2:
                return invoice.getFrom();
            case 3:
                return invoice.getTo();
            default:
                throw new IllegalArgumentException("Column index out of range");
        }
    }

    private class ReadAllSwingWorker extends SwingWorker<List<Invoice>, Void> {

        private final InvoiceManager invoiceManager;

        public ReadAllSwingWorker(InvoiceManager invoiceManager) {
            this.invoiceManager = invoiceManager;
        }

        @Override
        protected List<Invoice> doInBackground() throws Exception {
            return invoiceManager.listAllInvoices();
        }

        @Override
        protected void done() {
            try {
                invoices = get();
                fireTableDataChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class AddSwingWorker extends SwingWorker<Invoice, Void> {

        private final RevenueManager revenueManager;
        private final EmployeeManager employeeManager;
        private final Long id;
        private final LocalDate from;
        private final LocalDate to;

        public AddSwingWorker(RevenueManager revenueManager, EmployeeManager employeeManager, Long id, LocalDate from, LocalDate to) {
            this.revenueManager = revenueManager;
            this.employeeManager = employeeManager;
            this.id = id;
            this.from = from;
            this.to = to;
        }

        @Override
        protected Invoice doInBackground() throws Exception {
            return revenueManager.generateDocBook(employeeManager.getEmployee(id), from, to);
        }

        @Override
        protected void done() {
            try {
                invoice = get();
                invoices.add(invoice);
                fireTableRowsInserted(invoices.size() - 1, invoices.size() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class PDFSwingWorker extends SwingWorker <Void, Void> {

        private final InvoiceManager invoiceManager;
        private final Long id;

        public PDFSwingWorker(InvoiceManager invoiceManager, Long id) {
            this.invoiceManager = invoiceManager;
            this.id = id;
        }

        @Override
        protected Void doInBackground() throws Exception {
            invoiceManager.exportToPDF(invoiceManager.getInvoice(id));
            return null;
        }
    }

    void generateRows(Long id, LocalDate from, LocalDate to, InvoiceTableModel invoiceTableModel) {
        AddSwingWorker addSwingWorker = new AddSwingWorker(revenueManager, employeeManager, id, from, to);
        addSwingWorker.execute();
    }

    void exportToPDF(Long id) {
        PDFSwingWorker pdfSwingWorker = new PDFSwingWorker(invoiceManager, id);
        pdfSwingWorker.execute();
    }
}

