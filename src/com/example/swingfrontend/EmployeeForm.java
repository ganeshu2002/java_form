import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeForm extends JFrame {

    private JTextField txtId, txtFirstName, txtLastName, txtDob, txtDoj, txtAge, txtAddress,
            txtMobile, txtCity, txtState, txtCountry,
            txtTenthSchool, txtTenthObtained, txtTenthTotal,
            txtTwelfthSchool, txtTwelfthObtained, txtTwelfthTotal,
            txtGradCollege, txtGradObtained, txtGradTotal;

    private JTextArea outputArea;

    public EmployeeForm() {
        setTitle("Employee Management");
        setSize(900, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for form fields
        JPanel formPanel = new JPanel(new GridLayout(18, 2, 5, 5));

        // Fields
        txtId = new JTextField();
        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        txtDob = new JTextField("yyyy-mm-dd");
        txtDoj = new JTextField("yyyy-mm-dd");
        txtAge = new JTextField();
        txtAddress = new JTextField();
        txtMobile = new JTextField();
        txtCity = new JTextField("Kanpur");
        txtState = new JTextField("UttarPradesh");
        txtCountry = new JTextField("India");

        txtTenthSchool = new JTextField();
        txtTenthObtained = new JTextField();
        txtTenthTotal = new JTextField();

        txtTwelfthSchool = new JTextField();
        txtTwelfthObtained = new JTextField();
        txtTwelfthTotal = new JTextField();

        txtGradCollege = new JTextField();
        txtGradObtained = new JTextField();
        txtGradTotal = new JTextField();

        // Add to panel
        formPanel.add(new JLabel("ID (for Update/Delete/Find):")); formPanel.add(txtId);
        formPanel.add(new JLabel("First Name:")); formPanel.add(txtFirstName);
        formPanel.add(new JLabel("Last Name:")); formPanel.add(txtLastName);
        formPanel.add(new JLabel("Date of Birth:")); formPanel.add(txtDob);
        formPanel.add(new JLabel("Date of Joining:")); formPanel.add(txtDoj);
        formPanel.add(new JLabel("Age:")); formPanel.add(txtAge);
        formPanel.add(new JLabel("Address:")); formPanel.add(txtAddress);
        formPanel.add(new JLabel("Mobile:")); formPanel.add(txtMobile);
        formPanel.add(new JLabel("City:")); formPanel.add(txtCity);
        formPanel.add(new JLabel("State:")); formPanel.add(txtState);
        formPanel.add(new JLabel("Country:")); formPanel.add(txtCountry);

        formPanel.add(new JLabel("10th School:")); formPanel.add(txtTenthSchool);
        formPanel.add(new JLabel("10th Obtained Marks:")); formPanel.add(txtTenthObtained);
        formPanel.add(new JLabel("10th Total Marks:")); formPanel.add(txtTenthTotal);

        formPanel.add(new JLabel("12th School:")); formPanel.add(txtTwelfthSchool);
        formPanel.add(new JLabel("12th Obtained Marks:")); formPanel.add(txtTwelfthObtained);
        formPanel.add(new JLabel("12th Total Marks:")); formPanel.add(txtTwelfthTotal);

        formPanel.add(new JLabel("Graduation College:")); formPanel.add(txtGradCollege);
        formPanel.add(new JLabel("Graduation Obtained Marks:")); formPanel.add(txtGradObtained);
        formPanel.add(new JLabel("Graduation Total Marks:")); formPanel.add(txtGradTotal);

        add(formPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnFind = new JButton("Find By ID");
        JButton btnGetAll = new JButton("Get All Employees");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnFind);
        buttonPanel.add(btnGetAll);
        add(buttonPanel, BorderLayout.SOUTH);

        // Output area
        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.NORTH);

        // Button Actions
        btnAdd.addActionListener(e -> addEmployee());
        btnUpdate.addActionListener(e -> updateEmployee());
        btnDelete.addActionListener(e -> deleteEmployee());
        btnFind.addActionListener(e -> findEmployee());
        btnGetAll.addActionListener(e -> getAllEmployees());
    }

    // Create JSON object from form
    private JSONObject createEmployeeJSON() {
        JSONObject emp = new JSONObject();
        emp.put("firstName", txtFirstName.getText());
        emp.put("lastName", txtLastName.getText());
        emp.put("dob", txtDob.getText());
        emp.put("doj", txtDoj.getText());
        emp.put("age", Integer.parseInt(txtAge.getText()));
        emp.put("address", txtAddress.getText());
        emp.put("mobile", txtMobile.getText());
        emp.put("city", txtCity.getText());
        emp.put("state", txtState.getText());
        emp.put("country", txtCountry.getText());

        emp.put("tenthSchool", txtTenthSchool.getText());
        emp.put("tenthObtainedMarks", Integer.parseInt(txtTenthObtained.getText()));
        emp.put("tenthTotalMarks", Integer.parseInt(txtTenthTotal.getText()));

        emp.put("twelfthSchool", txtTwelfthSchool.getText());
        emp.put("twelfthObtainedMarks", Integer.parseInt(txtTwelfthObtained.getText()));
        emp.put("twelfthTotalMarks", Integer.parseInt(txtTwelfthTotal.getText()));

        emp.put("graduationCollege", txtGradCollege.getText());
        emp.put("graduationObtainedMarks", Integer.parseInt(txtGradObtained.getText()));
        emp.put("graduationTotalMarks", Integer.parseInt(txtGradTotal.getText()));

        return emp;
    }

    private void addEmployee() {
        try {
            JSONObject emp = createEmployeeJSON();
            String response = EmployeeService.addEmployee(emp);
            outputArea.setText("Employee Added: " + response);
        } catch (Exception ex) {
            ex.printStackTrace();
            outputArea.setText("Error adding employee: " + ex.getMessage());
        }
    }

    private void updateEmployee() {
        try {
            long id = Long.parseLong(txtId.getText());
            JSONObject emp = createEmployeeJSON();
            String response = EmployeeService.updateEmployee(id, emp);
            outputArea.setText("Employee Updated: " + response);
        } catch (Exception ex) {
            ex.printStackTrace();
            outputArea.setText("Error updating employee: " + ex.getMessage());
        }
    }

    private void deleteEmployee() {
        try {
            long id = Long.parseLong(txtId.getText());
            String response = EmployeeService.deleteEmployee(id);
            outputArea.setText("Employee Deleted: " + response);
        } catch (Exception ex) {
            ex.printStackTrace();
            outputArea.setText("Error deleting employee: " + ex.getMessage());
        }
    }

    private void findEmployee() {
        try {
            long id = Long.parseLong(txtId.getText());
            JSONObject response = EmployeeService.getEmployeeById(id);
            outputArea.setText("Employee Found: " + response.toString(4));
        } catch (Exception ex) {
            ex.printStackTrace();
            outputArea.setText("Error finding employee: " + ex.getMessage());
        }
    }

    private void getAllEmployees() {
        try {
            JSONArray response = EmployeeService.getAllEmployees();
            outputArea.setText("All Employees: " + response.toString(4));
        } catch (Exception ex) {
            ex.printStackTrace();
            outputArea.setText("Error getting employees: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmployeeForm form = new EmployeeForm();
            form.setVisible(true);
        });
    }
}
