package Gui;

import backend.Employee;
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
    private RevenueTableModel revenueTableModel;
    private Long employeeID;
    private BigDecimal hourlyWage;

    public RevenueDialog(RevenueTableModel revenueTableModel, Long employeeID, BigDecimal hourlyWage) {
        this.revenueTableModel = revenueTableModel;
        this.employeeID = employeeID;
        this.hourlyWage = hourlyWage;
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
        revenue.setEmployeeId(employeeID);
        int hours = Integer.parseInt(hoursTextField.getText());
        revenue.setHours(hours);
        revenue.setTotalSalary(hourlyWage.multiply(new BigDecimal(hours)));
        int month = monthComboBox.getSelectedIndex() + 1;
        int year = Integer.parseInt((String) yearComboBox.getSelectedItem());
        revenue.setDrawInvoiceDate(LocalDate.of(year, month, 1));
        revenueTableModel.addRow(revenue);
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    void main() {

        this.pack();
        this.setVisible(true);
    }
}
