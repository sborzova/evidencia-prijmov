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
                return invoice.getEmployeeID();
            case 2:
                return invoice.getFrom();
            case 3:
                return invoice.getTo();
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
                return "From date";
            case 3:
                return "To date";
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
            invoiceManager.generateDocBook(invoice);
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

    void addRow(Invoice invoice) {
        AddSwingWorker addSwingWorker = new AddSwingWorker(invoiceManager, invoice);
        addSwingWorker.execute();
    }

    void exportToPDF(Long id) {
        PDFSwingWorker pdfSwingWorker = new PDFSwingWorker(invoiceManager, id);
        pdfSwingWorker.execute();
    }
}

