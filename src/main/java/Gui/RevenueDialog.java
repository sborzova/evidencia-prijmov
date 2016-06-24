package Gui;

import backend.Employee;
import backend.EmployeeManager;
import backend.Revenue;

import javax.swing.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class RevenueDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox monthComboBox;
    private JComboBox yearComboBox;
    private JTextField hoursTextField;
    private Employee employee;
    private EmployeeTableModel employeeTableModel;
    private EmployeeManager employeeManager = new EmployeeManager();
    private RevenueTableModel revenueTableModel;
    private int selectedRow;

    public RevenueDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        Revenue revenue = new Revenue();
        revenue.setEmployeeId(employee.getId());
        int hours = Integer.parseInt(hoursTextField.getText());
        revenue.setHours(hours);
        revenue.setTotalSalary(employee.getHourlyWage().multiply(new BigDecimal(hours)).doubleValue());
        int month = monthComboBox.getSelectedIndex() + 1;
        int year = Integer.parseInt((String) yearComboBox.getSelectedItem());
        revenue.setDrawInvoiceDate(LocalDate.of(year, month, 1));
        employeeTableModel.createStatementOfRevenueOfRow(employeeManager, revenueTableModel, revenue, selectedRow);
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    void main(Employee employee, EmployeeTableModel employeeTableModel, RevenueTableModel revenueTableModel, int selectedRow) {
        this.employee = employee;
        this.employeeTableModel = employeeTableModel;
        this.revenueTableModel = revenueTableModel;
        this.selectedRow = selectedRow;
        RevenueDialog dialog = new RevenueDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
