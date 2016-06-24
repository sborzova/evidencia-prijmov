package Gui;

import backend.Employee;
import backend.EmployeeManager;
import backend.Revenue;

import javax.swing.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Marek Scholtz
 * @version 2016.06.23
 */
public class MainFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JLabel hourlyWageTextField;
    private JTextField forenameTextField;
    private JTextField surnameTextField;
    private JTextField textField3;
    private JTable employeeTable;
    private JButton deleteEmployeeButton;
    private JButton createStatementOfRevenueButton;
    private JButton addEmployeeButton;
    private JTable revenueTable;
    private JComboBox fromMonthComboBox;
    private JComboBox fromYearComboBox;
    private JComboBox toMonthComboBox;
    private JComboBox toYearComboBox;
    private JTextField textField1;
    private JTable invoiceTable;
    private JButton exportToPDFButton;
    private JButton viewRevenuesButton;
    private JButton deleteRevenueButton;
    private JButton generateInvoiceButton;
    private EmployeeManager employeeManager = new EmployeeManager();
    private RevenueManager revenueManager = new RevenueManager();
    private InvoiceManager invoiceManager = new InvoiceManager();

    public static void main(String[] args) {

        JFrame frame = new JFrame("Movie database");
        frame.setContentPane(new MainFrame().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private MainFrame() {

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
                Employee employee = employeeManager.getEmployee((Long) employeeTableModel.getValueAt(employeeTable.getSelectedRow(), 0));
                RevenueTableModel revenueTableModel = (RevenueTableModel) revenueTable.getModel();
                new RevenueDialog().main(employee, employeeTableModel, revenueTableModel, employeeTable.getSelectedRow());
                addEmployeeButton.setEnabled(true);
                deleteEmployeeButton.setEnabled(false);
                createStatementOfRevenueButton.setEnabled(false);
            }
        });

        employeeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                addEmployeeButton.setEnabled(false);
                deleteEmployeeButton.setEnabled(true);
                createStatementOfRevenueButton.setEnabled(true);
                RevenueTableModel revenueTableModel = (RevenueTableModel) revenueTable.getModel();
                revenueTableModel.findRows(employeeTable.getSelectedRow());
            }
        });

        viewRevenuesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int fromMonth = fromMonthComboBox.getSelectedIndex() + 1;
                int fromYear = Integer.parseInt((String) fromYearComboBox.getSelectedItem());
                int toMonth = toMonthComboBox.getSelectedIndex() + 1;
                int toYear = Integer.parseInt((String) toYearComboBox.getSelectedItem());
                RevenueTableModel revenueTableModel = (RevenueTableModel) revenueTable.getModel();
                revenueTableModel.listRows(LocalDate.of(fromYear, fromMonth, 1), LocalDate.of(toYear, toMonth, 1));
            }
        });

        deleteRevenueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RevenueTableModel revenueTableModel = (RevenueTableModel) revenueTable.getModel();
                revenueTableModel.deleteRow(revenueTable.getSelectedRow());
            }
        });

        generateInvoiceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int fromMonth = fromMonthComboBox.getSelectedIndex() + 1;
                int fromYear = Integer.parseInt((String) fromYearComboBox.getSelectedItem());
                int toMonth = toMonthComboBox.getSelectedIndex() + 1;
                int toYear = Integer.parseInt((String) toYearComboBox.getSelectedItem());
                RevenueTableModel revenueTableModel = (RevenueTableModel) revenueTable.getModel();
                InvoiceTableModel invoiceTableModel = (InvoiceTableModel) invoiceTable.getModel();
                revenueTableModel.generateRows(LocalDate.of(fromYear, fromMonth, 1), LocalDate.of(toYear, toMonth, 1), invoiceTableModel);
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
                InvoiceTableModel invoiceTableModel = (InvoiceTableModel) invoiceTable.getModel();
                invoiceTableModel.exportToPDF(invoiceTable.getSelectedRow());
                exportToPDFButton.setEnabled(false);
            }
        });
    }
}
