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
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
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
    private static EmployeeManager employeeManager;
    private static RevenueManager revenueManager;
    private static InvoiceManager invoiceManager;

    private static String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db/";
    private static Collection collection = null;

    public static void initializeDatabase() throws Exception {

        final String driver = "org.exist.xmldb.DatabaseImpl";
        Class cl = Class.forName(driver);
        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");
        DatabaseManager.registerDatabase(database);

        XMLResource res = null;
        XMLResource res2 = null;

        try {
            collection = getOrCreateCollection("revenueEvidence");

            if(!new File(".\\employees.xml").isFile()) {
                File f = createXmlFile("employees");
                if(!f.canRead()) {
                    return;
                }
                res = (XMLResource)collection.createResource(null, "XMLResource");
                res.setContent(f);
                collection.storeResource(res);
            }

            if(!new File(".\\revenues.xml").isFile()) {
                File f2 = createXmlFile("revenues");
                if(!f2.canRead()) {
                    return;
                }
                res2 = (XMLResource)collection.createResource(null, "XMLResource");
                res2.setContent(f2);
                collection.storeResource(res2);
            }

        } finally {
            if(res != null) {
                try {
                    ((EXistResource)res).freeResources();
                } catch(XMLDBException xe) {
                    xe.printStackTrace();
                }
            }
            if(collection != null) {
                try {
                    collection.close();
                } catch(XMLDBException xe) {
                    xe.printStackTrace();
                }
            }
        }

        employeeManager = new EmployeeManager(collection);
        revenueManager = new RevenueManager(collection);
        invoiceManager = new InvoiceManager(collection);
    }

    public static File createXmlFile(String name) {

        File file = null;

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement(name);
            doc.appendChild(rootElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            file = new File(".\\files\\" + name + ".xml");
            StreamResult result = new StreamResult(file);

            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException ex) {
            ex.printStackTrace();
        }

        return file;
    }


    private static Collection getOrCreateCollection(String collectionUri) throws
            XMLDBException {
        return getOrCreateCollection(collectionUri, 0);
    }

    private static Collection getOrCreateCollection(String collectionUri, int pathSegmentOffset) throws XMLDBException {

        Collection col = DatabaseManager.getCollection(URI + collectionUri);
        if(col == null) {
            if(collectionUri.startsWith("/")) {
                collectionUri = collectionUri.substring(1);
            }
            String pathSegments[] = collectionUri.split("/");
            if(pathSegments.length > 0) {
                StringBuilder path = new StringBuilder();
                for(int i = 0; i <= pathSegmentOffset; i++) {
                    path.append("/" + pathSegments[i]);
                }
                Collection start = DatabaseManager.getCollection(URI + path);
                if(start == null) {

                    String parentPath = path.substring(0, path.lastIndexOf("/"));
                    Collection parent = DatabaseManager.getCollection(URI + parentPath);
                    CollectionManagementService mgt = (CollectionManagementService) parent.getService("CollectionManagementService", "1.0");
                    col = mgt.createCollection(pathSegments[pathSegmentOffset]);
                    col.close();
                    parent.close();
                } else {
                    start.close();
                }
            }
            return getOrCreateCollection(collectionUri, ++pathSegmentOffset);
        } else {
            return col;
        }
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
        invoiceTable.setModel(new InvoiceTableModel(invoiceManager));

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