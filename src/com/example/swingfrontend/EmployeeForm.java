import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import org.json.JSONArray;
import org.json.JSONObject;

public class EmployeeForm extends JFrame {
    private JTextField idField, firstNameField, lastNameField, dobField, dojField, ageField, addressField, cityField, stateField, countryField, mobileField;
    private JTable educationTable;
    private JTextArea resultArea;
    private EmployeeService employeeService;

    public EmployeeForm() {
        employeeService = new EmployeeService();
        setTitle("Employee Management");
        setSize(900, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(12, 2, 10, 10));

        inputPanel.add(new JLabel("Employee ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        inputPanel.add(firstNameField);

        inputPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        inputPanel.add(lastNameField);

        inputPanel.add(new JLabel("Date of Birth (yyyy-mm-dd):"));
        dobField = new JTextField();
        inputPanel.add(dobField);

        inputPanel.add(new JLabel("Date of Joining (yyyy-mm-dd):"));
        dojField = new JTextField();
        inputPanel.add(dojField);

        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        ageField.setEditable(false);
        inputPanel.add(ageField);

        inputPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        inputPanel.add(addressField);

        inputPanel.add(new JLabel("City:"));
        cityField = new JTextField();
        inputPanel.add(cityField);

        inputPanel.add(new JLabel("State:"));
        stateField = new JTextField();
        stateField.setEditable(false);
        inputPanel.add(stateField);

        inputPanel.add(new JLabel("Country:"));
        countryField = new JTextField();
        countryField.setEditable(false);
        inputPanel.add(countryField);

        inputPanel.add(new JLabel("Mobile No:"));
        mobileField = new JTextField();
        inputPanel.add(mobileField);

        add(inputPanel, BorderLayout.NORTH);

        String[] columns = {"Level", "School/College", "Obtained", "Total", "Percentage"};
        String[][] data = {
                {"Tenth", "", "", "", ""},
                {"Twelfth", "", "", "", ""},
                {"Graduation", "", "", "", ""}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        educationTable = new JTable(model);

        // ✅ Auto calculate percentage when Obtained or Total changes
        model.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();
            if (col == 2 || col == 3) {
                try {
                    String obtainedStr = (String) model.getValueAt(row, 2);
                    String totalStr = (String) model.getValueAt(row, 3);
                    if (obtainedStr != null && totalStr != null && !obtainedStr.isEmpty() && !totalStr.isEmpty()) {
                        double obtained = Double.parseDouble(obtainedStr);
                        double total = Double.parseDouble(totalStr);
                        if (total > 0) {
                            double percentage = (obtained / total) * 100;
                            model.setValueAt(String.format("%.2f", percentage), row, 4);
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(educationTable);
        tableScrollPane.setPreferredSize(new Dimension(800, 80));
        add(tableScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Employee");
        JButton updateButton = new JButton("Update Employee");
        JButton deleteButton = new JButton("Delete Employee");
        JButton getButton = new JButton("Get All");
        JButton findButton = new JButton("Find");
        JButton clearButton = new JButton("Clear");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(getButton);
        buttonPanel.add(findButton);
        buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.SOUTH);

        resultArea = new JTextArea(10, 80);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.EAST);

        // ✅ City -> Auto State & Country
        cityField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                String city = cityField.getText().trim();
                if (city.equalsIgnoreCase("Kanpur")) {
                    stateField.setText("Uttar Pradesh");
                    countryField.setText("India");
                } else {
                    stateField.setText("");
                    countryField.setText("");
                }
            }
        });

        // ✅ DOB -> Auto Age
        dobField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                calculateAge();
            }
        });

        dobField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { calculateAge(); }
            public void removeUpdate(DocumentEvent e) { calculateAge(); }
            public void changedUpdate(DocumentEvent e) { calculateAge(); }
        });

        addButton.addActionListener(e -> addEmployee());
        updateButton.addActionListener(e -> updateEmployee());
        deleteButton.addActionListener(e -> deleteEmployee());
        getButton.addActionListener(e -> getAllEmployees());
        findButton.addActionListener(e -> findEmployee());
        clearButton.addActionListener(e -> clearFields());
    }

    private void calculateAge() {
        try {
            String dob = dobField.getText().trim();
            if (!dob.isEmpty()) {
                LocalDate birthDate = LocalDate.parse(dob);
                int age = Period.between(birthDate, LocalDate.now()).getYears();
                ageField.setText(String.valueOf(age));
            } else {
                ageField.setText("");
            }
        } catch (Exception ignored) {
            ageField.setText("");
        }
    }

    private JSONObject buildJson() {
        JSONObject json = new JSONObject();
        if (!idField.getText().trim().isEmpty()) {
            json.put("userId", Integer.parseInt(idField.getText().trim()));
        }
        json.put("firstName", firstNameField.getText());
        json.put("lastName", lastNameField.getText());
        json.put("dob", dobField.getText());
        json.put("doj", dojField.getText());
        json.put("age", ageField.getText().isEmpty() ? 0 : Integer.parseInt(ageField.getText()));
        json.put("address", addressField.getText());
        json.put("city", cityField.getText());
        json.put("state", stateField.getText());
        json.put("country", countryField.getText());
        json.put("mobileNo", mobileField.getText());

        JSONArray educationArray = new JSONArray();
        DefaultTableModel model = (DefaultTableModel) educationTable.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            JSONObject edu = new JSONObject();
            edu.put("level", model.getValueAt(i, 0));
            edu.put("schoolOrCollegeName", model.getValueAt(i, 1));
            edu.put("obtainedScore", model.getValueAt(i, 2));
            edu.put("totalScore", model.getValueAt(i, 3));
            edu.put("percentage", model.getValueAt(i, 4));
            educationArray.put(edu);
        }
        json.put("education", educationArray);

        return json;
    }

    private void addEmployee() {
        try {
            JSONObject json = buildJson();
            String response = employeeService.sendRequest("POST", "http://localhost:8080/api/employees", json.toString());
            resultArea.setText("Employee Added Successfully:\n" + formatJson(response));
            clearFields();
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void updateEmployee() {
        try {
            if (idField.getText().trim().isEmpty()) {
                resultArea.setText("Please enter Employee ID to update.");
                return;
            }
            JSONObject json = buildJson();
            String url = "http://localhost:8080/api/employees/" + idField.getText().trim();
            String response = employeeService.sendRequest("PUT", url, json.toString());
            resultArea.setText("Employee Updated Successfully:\n" + formatJson(response));
            clearFields();
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void deleteEmployee() {
        try {
            if (idField.getText().trim().isEmpty()) {
                resultArea.setText("Please enter Employee ID to delete.");
                return;
            }
            String url = "http://localhost:8080/api/employees/" + idField.getText().trim();
            String response = employeeService.sendRequest("DELETE", url, null);
            resultArea.setText("Deleted Successfully:\n" + response);
            clearFields();
        } catch (Exception e) {
            resultArea.setText("Delete failed: " + e.getMessage());
        }
    }

    private void getAllEmployees() {
        try {
            String response = employeeService.sendRequest("GET", "http://localhost:8080/api/employees", null);
            resultArea.setText("All Employees:\n" + formatEmployeeList(response));
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void findEmployee() {
        String firstName = firstNameField.getText().trim();
        String dob = dobField.getText().trim();
        if (firstName.isEmpty()) {
            resultArea.setText("Please enter First Name to search.");
            return;
        }

        try {
            StringBuilder urlBuilder = new StringBuilder("http://localhost:8080/api/employees/search?firstName=" + firstName);
            if (!dob.isEmpty()) {
                urlBuilder.append("&dob=").append(dob);
            }
            String response = employeeService.sendRequest("GET", urlBuilder.toString(), null);
            resultArea.setText("Search Results:\n" + formatEmployeeList(response));
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void clearFields() {
        idField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        dobField.setText("");
        dojField.setText("");
        ageField.setText("");
        addressField.setText("");
        cityField.setText("");
        stateField.setText("");
        countryField.setText("");
        mobileField.setText("");

        DefaultTableModel model = (DefaultTableModel) educationTable.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 1; j < model.getColumnCount(); j++) {
                model.setValueAt("", i, j);
            }
        }
    }

    private String formatEmployeeList(String response) {
        StringBuilder formatted = new StringBuilder();
        try {
            JSONArray arr = new JSONArray(response);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject emp = arr.getJSONObject(i);
                formatted.append(formatSingleEmployee(emp)).append("\n\n");
            }
        } catch (Exception e) {
            try {
                JSONObject emp = new JSONObject(response);
                formatted.append(formatSingleEmployee(emp));
            } catch (Exception ignored) {
                formatted.append(response);
            }
        }
        return formatted.toString();
    }

    private String formatSingleEmployee(JSONObject emp) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(emp.optInt("userId")).append("\n");
        sb.append("Name: ").append(emp.optString("firstName")).append(" ").append(emp.optString("lastName")).append("\n");
        sb.append("DOB: ").append(emp.optString("dob")).append("\n");
        sb.append("DOJ: ").append(emp.optString("doj")).append("\n");
        sb.append("Age: ").append(emp.optInt("age")).append("\n");
        sb.append("Address: ").append(emp.optString("address")).append(", ").append(emp.optString("city")).append(", ")
                .append(emp.optString("state")).append(", ").append(emp.optString("country")).append("\n");
        sb.append("Mobile: ").append(emp.optString("mobileNo")).append("\n");
        sb.append("Education:\n");
        JSONArray eduArr = emp.optJSONArray("education");
        if (eduArr != null) {
            for (int j = 0; j < eduArr.length(); j++) {
                JSONObject edu = eduArr.getJSONObject(j);
                sb.append(" - ").append(edu.optString("level")).append(": ")
                        .append(edu.optString("schoolOrCollegeName")).append(", ")
                        .append("Obtained: ").append(edu.optString("obtainedScore")).append("/")
                        .append(edu.optString("totalScore")).append(", ")
                        .append("Percent: ").append(edu.optString("percentage")).append("\n");
            }
        }
        return sb.toString();
    }

    private String formatJson(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            return obj.toString(4);
        } catch (Exception e) {
            return json;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeForm().setVisible(true));
    }
}
