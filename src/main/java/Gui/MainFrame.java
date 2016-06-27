package Gui;

import backend.*;
import org.exist.xmldb.EXistResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.event.*;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Marek Scholtz
 * @version 2016.06.23
 */
public class MainFrame {
    private JPanel mainPanel;
    private JTextField forenameTextField;
    private JTextField surnameTextField;
    private JTable employeeTable;
    private JButton deleteEmployeeButton;
    private JButton createStatementOfRevenueButton;
    private JButton addEmployeeButton;
    private JTable revenueTable;
    private JComboBox fromMonthComboBox;
    private JComboBox fromYearComboBox;
    private JComboBox toMonthComboBox;
    private JComboBox toYearComboBox;
    private JTable invoiceTable;
    private JButton exportToPDFButton;
    private JButton viewRevenuesButton;
    private JButton generateInvoiceButton;
    private JTabbedPane tabbedPane1;
    private JPanel employeePanel;
    private JTextField hourlyWageTextField;
    private JPanel revenuePanel;
    private JTextField revenuesInTotalTextField;
    private JLabel hourlyWageLabel;

    private static EmployeeManagerImpl employeeManagerImpl;
    private static RevenueManagerImpl revenueManagerImpl;
    private static InvoiceManagerImpl invoiceManagerImpl;
    private static Collection collection;

    public static void initializeDatabase() throws Exception {

        collection = new ExistDbImpl().setUpDatabase();
        employeeManagerImpl = new EmployeeManagerImpl(collection);
        revenueManagerImpl = new RevenueManagerImpl(collection);
        invoiceManagerImpl = new InvoiceManagerImpl(collection);
    }

    public static void main(String[] args) throws Exception {

        initializeDatabase();
        JFrame frame = new JFrame("Salary evidence");
        frame.setContentPane(new MainFrame().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private MainFrame() {

        employeeTable.setModel(new EmployeeTableModel(employeeManagerImpl));
        revenueTable.setModel(new RevenueTableModel(revenueManagerImpl, employeeManagerImpl));
        invoiceTable.setModel(new InvoiceTableModel(invoiceManagerImpl));

        addEmployeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Employee employee = new Employee();
                employee.setForename(forenameTextField.getText());
                employee.setSurname(surnameTextField.getText());
                employee.setHourlyWage(new BigDecimal(hourlyWageTextField.getText()));
                EmployeeTableModel employeeTableModel = (EmployeeTableModel) employeeTable.getModel();
                employeeTableModel.addRow(employee);
            }
        });

        deleteEmployeeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EmployeeTableModel employeeTableModel = (EmployeeTableModel) employeeTable.getModel();
                employeeTableModel.deleteRow(employeeTable.getSelectedRow());
                addEmployeeButton.setEnabled(true);
                deleteEmployeeButton.setEnabled(false);
                createStatementOfRevenueButton.setEnabled(false);
            }
        });

        createStatementOfRevenueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EmployeeTableModel employeeTableModel = (EmployeeTableModel) employeeTable.getModel();
                RevenueTableModel revenueTableModel = (RevenueTableModel) revenueTable.getModel();
                String hourlyWage = employeeTableModel.getValueAt(employeeTable.getSelectedRow(), 3).toString();
                RevenueDialog dialog = new RevenueDialog(revenueTableModel, (Long) employeeTableModel.getValueAt(employeeTable.getSelectedRow(), 0), new BigDecimal(hourlyWage));
                dialog.main();
                addEmployeeButton.setEnabled(true);
                deleteEmployeeButton.setEnabled(false);
                createStatementOfRevenueButton.setEnabled(false);
            }
        });
        employeeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                EmployeeTableModel employeeTableModel = (EmployeeTableModel) employeeTable.getModel();
                RevenueTableModel revenueTableModel = (RevenueTableModel) revenueTable.getModel();
                revenueTableModel.listRows((Long) employeeTableModel.getValueAt(employeeTable.getSelectedRow(), 0));
                deleteEmployeeButton.setEnabled(true);
                createStatementOfRevenueButton.setEnabled(true);
                revenueTableModel.revenuesInTotal(revenueTable, revenuesInTotalTextField);
            }
        });

        viewRevenuesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int fromMonth = fromMonthComboBox.getSelectedIndex() + 1;
                int fromYear = Integer.parseInt((String) fromYearComboBox.getSelectedItem());
                int toMonth = toMonthComboBox.getSelectedIndex() + 1;
                int toYear = Integer.parseInt((String) toYearComboBox.getSelectedItem());
                EmployeeTableModel employeeTableModel = (EmployeeTableModel) employeeTable.getModel();
                RevenueTableModel revenueTableModel = (RevenueTableModel) revenueTable.getModel();
                revenueTableModel.findRows(LocalDate.of(fromYear, fromMonth, 1), LocalDate.of(toYear, toMonth, 1), (Long) employeeTableModel.getValueAt(employeeTable.getSelectedRow(), 0));
                revenueTableModel.revenuesInTotal(revenueTable, revenuesInTotalTextField);
            }
        });

        generateInvoiceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Invoice invoice = new Invoice();
                int fromMonth = fromMonthComboBox.getSelectedIndex() + 1;
                int fromYear = Integer.parseInt((String) fromYearComboBox.getSelectedItem());
                int toMonth = toMonthComboBox.getSelectedIndex() + 1;
                int toYear = Integer.parseInt((String) toYearComboBox.getSelectedItem());
                EmployeeTableModel employeeTableModel = (EmployeeTableModel) employeeTable.getModel();
                InvoiceTableModel invoiceTableModel = (InvoiceTableModel) invoiceTable.getModel();
                invoice.setEmployeeID((Long) employeeTableModel.getValueAt(employeeTable.getSelectedRow(), 0));
                invoice.setFrom(LocalDate.of(fromYear, fromMonth, 1));
                invoice.setTo(LocalDate.of(toYear, toMonth, 1));
                invoiceTableModel.addRow(invoice);
            }
        });

        invoiceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                exportToPDFButton.setEnabled(true);
            }
        });

        exportToPDFButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InvoiceTableModel invoiceTableModel = (InvoiceTableModel) invoiceTable.getModel();
                invoiceTableModel.exportToPDF((Long) invoiceTableModel.getValueAt(invoiceTable.getSelectedRow(), 0));
                exportToPDFButton.setEnabled(false);
                invoiceTable.clearSelection();
            }
        });
    }
}