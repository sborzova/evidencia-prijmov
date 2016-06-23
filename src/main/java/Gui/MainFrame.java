package Gui;

import backend.Employee;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

/**
 * @author Marek Scholtz
 * @version 2016.06.23
 */
public class MainFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JLabel hourlyWageTextField;
    private JTextField fornameTextField;
    private JTextField surnameTextField;
    private JTextField textField3;
    private JTable employeeTable;
    private JButton deleteButton;
    private JButton createStatementOfRevenueButton;
    private JButton addButton;
    private JTable revenueTable;
    private JComboBox comboBox3;
    private JComboBox comboBox1;
    private JComboBox comboBox4;
    private JComboBox comboBox2;
    private JTextField textField1;
    private JTable table1;
    private JButton exportToPDFButton;

    public static void main(String[] args) {

        JFrame frame = new JFrame("Movie database");
        frame.setContentPane(new MainFrame().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public MainForm() {
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Employee employee = new Employee();
                employee.setForename(fornameTextField.getText());
                employee.setSurname(surnameTextField.getText());
                employee.setHourlyWage(new BigDecimal(hourlyWageTextField.getText()));
                EmployeeTableModel employeeTableModel = (EmployeeTableModel) employeeTable.getModel();
                employeeTableModel.addRow(employee);
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EmployeeTableModel employeeTableModel = (EmployeeTableModel) employeeTable.getModel();
                employeeTableModel.deleteRow(employeeTable.getSelectedRow());
                defaultSettings();
            }
        });
        createStatementOfRevenueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
