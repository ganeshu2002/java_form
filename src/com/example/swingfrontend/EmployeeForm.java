// package com.example.swingfrontend;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

public class EmployeeForm extends JFrame {

    private JTextField txtUserId, txtFirstName, txtLastName, txtDob, txtDoj, txtAge, txtAddress, txtMobile;
    private JTextField txtCity, txtState, txtCountry, txtTenth, txtTwelfth, txtGraduation;
    private JButton btnAdd, btnUpdate, btnDelete, btnGetAll;
    private JTable table;
    private DefaultTableModel tableModel;

    private EmployeeService service;

    public EmployeeForm() {
        service = new EmployeeService();

        setTitle("Employee Form");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(16, 2, 5, 5));

        panel.add(new JLabel("User ID (for Update/Delete)"));
        txtUserId = new JTextField(); panel.add(txtUserId);

        panel.add(new JLabel("First Name")); txtFirstName = new JTextField(); panel.add(txtFirstName);
        panel.add(new JLabel("Last Name")); txtLastName = new JTextField(); panel.add(txtLastName);
        panel.add(new JLabel("Date of Birth (yyyy-mm-dd)")); txtDob = new JTextField(); panel.add(txtDob);
        panel.add(new JLabel("Date of Joining (yyyy-mm-dd)")); txtDoj = new JTextField(); panel.add(txtDoj);
        panel.add(new JLabel("Age")); txtAge = new JTextField(); panel.add(txtAge);
        panel.add(new JLabel("Address")); txtAddress = new JTextField(); panel.add(txtAddress);
        panel.add(new JLabel("Mobile")); txtMobile = new JTextField(); panel.add(txtMobile);
        panel.add(new JLabel("City")); txtCity = new JTextField(); panel.add(txtCity);
        panel.add(new JLabel("State")); txtState = new JTextField(); panel.add(txtState);
        panel.add(new JLabel("Country")); txtCountry = new JTextField(); panel.add(txtCountry);
        panel.add(new JLabel("10th (%)")); txtTenth = new JTextField(); panel.add(txtTenth);
        panel.add(new JLabel("12th (%)")); txtTwelfth = new JTextField(); panel.add(txtTwelfth);
        panel.add(new JLabel("Graduation (%)")); txtGraduation = new JTextField(); panel.add(txtGraduation);

        btnAdd = new JButton("Add Employee");
        btnUpdate = new JButton("Update Employee");
        btnDelete = new JButton("Delete Employee");
        btnGetAll = new JButton("Get All Employees");

        panel.add(btnAdd); panel.add(btnUpdate);
        panel.add(btnDelete); panel.add(btnGetAll);

        add(panel, BorderLayout.NORTH);

        // Table for showing employees
        String[] columns = {"ID","First Name","Last Name","DOB","DOJ","Age","City","Mobile"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Button actions
        btnAdd.addActionListener(e -> handleAdd());
        btnUpdate.addActionListener(e -> handleUpdate());
        btnDelete.addActionListener(e -> handleDelete());
        btnGetAll.addActionListener(e -> handleGetAll());
    }

    // Build JSON from form fields
    private String getJsonFromForm() {
        return "{"
                + "\"firstName\":\"" + txtFirstName.getText() + "\","
                + "\"lastName\":\"" + txtLastName.getText() + "\","
                + "\"dob\":\"" + txtDob.getText() + "\","
                + "\"doj\":\"" + txtDoj.getText() + "\","
                + "\"age\":" + txtAge.getText() + ","
                + "\"address\":\"" + txtAddress.getText() + "\","
                + "\"mobile\":\"" + txtMobile.getText() + "\","
                + "\"city\":\"" + txtCity.getText() + "\","
                + "\"state\":\"" + txtState.getText() + "\","
                + "\"country\":\"" + txtCountry.getText() + "\","
                + "\"tenth\":\"" + txtTenth.getText() + "\","
                + "\"twelfth\":\"" + txtTwelfth.getText() + "\","
                + "\"graduation\":\"" + txtGraduation.getText() + "\""
                + "}";
    }

    private void handleAdd() {
        try {
            String json = getJsonFromForm();
            String response = service.createEmployee(json);
            JOptionPane.showMessageDialog(this, "Add: " + response);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void handleUpdate() {
        try {
            Long id = Long.parseLong(txtUserId.getText());
            String json = getJsonFromForm();
            String response = service.updateEmployee(id, json);
            JOptionPane.showMessageDialog(this, "Update: " + response);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void handleDelete() {
        try {
            Long id = Long.parseLong(txtUserId.getText());
            String response = service.deleteEmployee(id);
            JOptionPane.showMessageDialog(this, "Delete: " + response);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void handleGetAll() {
        try {
            String response = service.getAllEmployees();
            tableModel.setRowCount(0); // clear table

            JSONArray arr = new JSONArray(response);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject emp = arr.getJSONObject(i);
                Object[] row = new Object[]{
                        emp.getLong("userId"),
                        emp.getString("firstName"),
                        emp.getString("lastName"),
                        emp.getString("dob"),
                        emp.optString("doj",""),
                        emp.getInt("age"),
                        emp.getString("city"),
                        emp.getString("mobile")
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeForm().setVisible(true));
    }
}
