package Gui;

import backend.Employee;
import backend.EmployeeManager;
import backend.RevenueManager;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import javax.swing.*;
import javax.xml.transform.OutputKeys;
import java.awt.event.*;
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
    private JTextField textField1;
    private JLabel hourlyWageLabel;
    private static EmployeeManager employeeManager;
    private static RevenueManager revenueManager;
    //private static InvoiceManager invoiceManager;
    private static String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db/RevenueEvidence";
    private static Collection collection = null;

    public static void initializeDatabase() throws Exception {

        final String driver = "org.exist.xmldb.DatabaseImpl";
        Class cl = Class.forName(driver);
        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");
        DatabaseManager.registerDatabase(database);

        XMLResource res = null;
        try {
            collection = DatabaseManager.getCollection(URI);
            collection.setProperty(OutputKeys.INDENT, "no");
        } finally {

            if (collection != null) {
                try {
                    collection.close();
                } catch (XMLDBException xe) {
                    xe.printStackTrace();
                }
            }
        }

        employeeManager = new EmployeeManager(collection);
        revenueManager = new RevenueManager(collection);
        //invoiceManager = new InvoiceManager(collection);
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

        employeeTable.setModel(new EmployeeTableModel(employeeManager));
        revenueTable.setModel(new RevenueTableModel(revenueManager, employeeManager));


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
                RevenueTableModel revenueTableModel = (RevenueTableModel) revenueTable.getModel();
                revenueTableModel.listRows(employeeTable.getSelectedRow());
                addEmployeeButton.setEnabled(false);
                deleteEmployeeButton.setEnabled(true);
                createStatementOfRevenueButton.setEnabled(true);
            }
        });

        /*
        viewRevenuesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int fromMonth = fromMonthComboBox.getSelectedIndex() + 1;
                int fromYear = Integer.parseInt((String) fromYearComboBox.getSelectedItem());
                int toMonth = toMonthComboBox.getSelectedIndex() + 1;
                int toYear = Integer.parseInt((String) toYearComboBox.getSelectedItem());
                RevenueTableModel revenueTableModel = (RevenueTableModel) revenueTable.getModel();
                revenueTableModel.findRows(LocalDate.of(fromYear, fromMonth, 1), LocalDate.of(toYear, toMonth, 1), employeeTable.getSelectedRow());
            }
        });

        generateInvoiceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int fromMonth = fromMonthComboBox.getSelectedIndex() + 1;
                int fromYear = Integer.parseInt((String) fromYearComboBox.getSelectedItem());
                int toMonth = toMonthComboBox.getSelectedIndex() + 1;
                int toYear = Integer.parseInt((String) toYearComboBox.getSelectedItem());
                RevenueTableModel revenueTableModel = (RevenueTableModel) revenueTable.getModel();
                //InvoiceTableModel invoiceTableModel = (InvoiceTableModel) invoiceTable.getModel();
                //revenueTableModel.generateRows(LocalDate.of(fromYear, fromMonth, 1), LocalDate.of(toYear, toMonth, 1), invoiceTableModel);
            }
        });

        revenueTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                deleteRevenueButton.setEnabled(true);
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
                invoiceTable.clearSelection();
                //InvoiceTableModel invoiceTableModel = (InvoiceTableModel) invoiceTable.getModel();
                //invoiceTableModel.exportToPDF(invoiceTable.getSelectedRow());
                exportToPDFButton.setEnabled(false);
            }
        });
        */

    }
}