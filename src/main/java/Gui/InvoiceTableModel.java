package Gui;

import backend.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marek Scholtz
 * @version 2016.06.23
 */
public class InvoiceTableModel extends AbstractTableModel {


    private final InvoiceManagerImpl invoiceManagerImpl;
    private List<Invoice> invoices = new ArrayList<>();
    private ReadAllSwingWorker readAllSwingWorker;

    public InvoiceTableModel(InvoiceManagerImpl invoiceManagerImpl) {

        this.invoiceManagerImpl = invoiceManagerImpl;
        readAllSwingWorker = new ReadAllSwingWorker(invoiceManagerImpl);
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

        private final InvoiceManagerImpl invoiceManagerImpl;

        public ReadAllSwingWorker(InvoiceManagerImpl invoiceManagerImpl) {
            this.invoiceManagerImpl = invoiceManagerImpl;
        }

        @Override
        protected List<Invoice> doInBackground() throws Exception {
            return invoiceManagerImpl.listAllInvoices();
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

        private final InvoiceManagerImpl invoiceManagerImpl;
        private final Invoice invoice;

        public AddSwingWorker(InvoiceManagerImpl invoiceManagerImpl, Invoice invoice) {
            this.invoiceManagerImpl = invoiceManagerImpl;
            this.invoice = invoice;
        }

        @Override
        protected Void doInBackground() throws Exception {
            invoiceManagerImpl.generateDocBook(invoice);
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

        private final InvoiceManagerImpl invoiceManagerImpl;
        private final Long id;

        public PDFSwingWorker(InvoiceManagerImpl invoiceManagerImpl, Long id) {
            this.invoiceManagerImpl = invoiceManagerImpl;
            this.id = id;
        }

        @Override
        protected Void doInBackground() throws Exception {
            invoiceManagerImpl.exportToPDF(invoiceManagerImpl.getInvoice(id));
            return null;
        }
    }

    void addRow(Invoice invoice) {
        AddSwingWorker addSwingWorker = new AddSwingWorker(invoiceManagerImpl, invoice);
        addSwingWorker.execute();
    }

    void exportToPDF(Long id) {
        PDFSwingWorker pdfSwingWorker = new PDFSwingWorker(invoiceManagerImpl, id);
        pdfSwingWorker.execute();
    }
}

