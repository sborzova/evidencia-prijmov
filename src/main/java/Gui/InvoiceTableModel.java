package Gui;

import backend.EmployeeManager;
import backend.Invoice;
import backend.InvoiceManager;
import backend.Revenue;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marek Scholtz
 * @version 2016.06.23
 */
public class InvoiceTableModel extends AbstractTableModel {

    private final InvoiceManager invoiceManager;
    private List<Invoice> invoices = new ArrayList<Invoice>();
    private ReadAllSwingWorker readAllSwingWorker;

    public InvoiceTableModel(InvoiceManager invoiceManager) {

        this.invoiceManager = invoiceManager;
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
                return invoice.getForename();
            case 2:
                return invoice.getSurname();
            case 3:
                return invoice.getFromDate();
            case 4:
                return invoice.getToDate();
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

    private class AddSwingWorker extends SwingWorker<Void, Void> {

        private final InvoiceManager invoiceManager;
        private final Invoice invoice;

        public AddSwingWorker(InvoiceManager invoiceManager, Invoice invoice) {
            this.invoiceManager = invoiceManager;
            this.invoice = invoice;
        }

        @Override
        protected Void doInBackground() throws Exception {
            invoiceManager.createInvoice(invoice);
            return null;
        }

        @Override
        protected void done() {
            try {
                get();
                invoices.add(invoice);
                fireTableRowsInserted(invoices.size() - 1, invoices.size() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class PDFSwingWorker extends SwingWorker <Void, Void> {

        private final InvoiceManager invoiceManager;
        private final int row;

        public PDFSwingWorker(InvoiceManager invoiceManager, int row) {
            this.invoiceManager = invoiceManager;
            this.row = row;
        }

        @Override
        protected Void doInBackground() throws Exception {
            invoiceManager.exportToPDF(invoiceManager.getInvoice((Long) getValueAt(row, 0)));
            return null;
        }
    }

    void addRow(Invoice invoice) {
        AddSwingWorker addSwingWorker = new AddSwingWorker(invoiceManager, invoice);
        addSwingWorker.execute();
    }

    void exportToPDF(int row) {
        PDFSwingWorker pdfSwingWorker = new PDFSwingWorker(invoiceManager, row);
        pdfSwingWorker.execute();
    }
}
