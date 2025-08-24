import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class EmployeeForm extends JFrame {
    private JTextField idField, firstNameField, lastNameField, dobField, dojField, ageField, addressField, cityField, stateField, countryField, mobileField;
    private JTable educationTable;
    private JTable employeeTable; // ✅ New table for displaying employees
    private JTextArea resultArea;
    private EmployeeService employeeService;

    public EmployeeForm() {
        employeeService = new EmployeeService();
        setTitle("Employee Management System");
        setSize(1200, 800); // ✅ Increased window size for better layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ✅ Create main panel with better organization
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Input panel at top
        JPanel inputPanel = createInputPanel();
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        
        // Center panel with tables
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Button panel at bottom
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Result area on the right (smaller now)
        resultArea = new JTextArea(8, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setPreferredSize(new Dimension(350, 600));
        add(resultScrollPane, BorderLayout.EAST);

        // ✅ City -> Auto State & Country
        setupCityAutoComplete();
        
        // ✅ Setup age calculation
        setupAgeCalculation();
        
        // ✅ Setup button actions
        setupButtonActions();
    }
    
    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(6, 4, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Employee Information"));
        
        // Row 1
        inputPanel.add(new JLabel("Employee ID:"));
        idField = new JTextField();
        inputPanel.add(idField);
        inputPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        inputPanel.add(firstNameField);
        
        // Row 2
        inputPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        inputPanel.add(lastNameField);
        inputPanel.add(new JLabel("Date of Birth:"));
        dobField = new JTextField();
        inputPanel.add(dobField);
        
        // Row 3
        inputPanel.add(new JLabel("Date of Joining:"));
        dojField = new JTextField();
        inputPanel.add(dojField);
        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        ageField.setEditable(false);
        inputPanel.add(ageField);
        
        // Row 4
        inputPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        inputPanel.add(addressField);
        inputPanel.add(new JLabel("City:"));
        cityField = new JTextField();
        inputPanel.add(cityField);
        
        // Row 5
        inputPanel.add(new JLabel("State:"));
        stateField = new JTextField();
        stateField.setEditable(false);
        inputPanel.add(stateField);
        inputPanel.add(new JLabel("Country:"));
        countryField = new JTextField();
        countryField.setEditable(false);
        inputPanel.add(countryField);
        
        // Row 6
        inputPanel.add(new JLabel("Mobile No:"));
        mobileField = new JTextField();
        inputPanel.add(mobileField);
        inputPanel.add(new JLabel("")); // Empty label for spacing
        inputPanel.add(new JLabel("")); // Empty label for spacing
        
        return inputPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        // Education table at top
        JPanel educationPanel = new JPanel(new BorderLayout());
        educationPanel.setBorder(BorderFactory.createTitledBorder("Education Details"));
        
        String[] eduColumns = {"Level", "School/College", "Obtained", "Total", "Percentage"};
        String[][] eduData = {
                {"Tenth", "", "", "", ""},
                {"Twelfth", "", "", "", ""},
                {"Graduation", "", "", "", ""}
        };
        
        DefaultTableModel eduModel = new DefaultTableModel(eduData, eduColumns);
        educationTable = new JTable(eduModel);
        educationTable.setRowHeight(25);
        educationTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // ✅ Auto calculate percentage when Obtained or Total changes
        eduModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();
            if (col == 2 || col == 3) {
                try {
                    String obtainedStr = (String) eduModel.getValueAt(row, 2);
                    String totalStr = (String) eduModel.getValueAt(row, 3);
                    if (obtainedStr != null && totalStr != null && !obtainedStr.isEmpty() && !totalStr.isEmpty()) {
                        double obtained = Double.parseDouble(obtainedStr);
                        double total = Double.parseDouble(totalStr);
                        if (total > 0) {
                            double percentage = (obtained / total) * 100;
                            eduModel.setValueAt(String.format("%.2f", percentage), row, 4);
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        });
        
        JScrollPane eduScrollPane = new JScrollPane(educationTable);
        eduScrollPane.setPreferredSize(new Dimension(800, 120));
        educationPanel.add(eduScrollPane, BorderLayout.CENTER);
        
        // Employee table at bottom
        JPanel employeePanel = new JPanel(new BorderLayout());
        employeePanel.setBorder(BorderFactory.createTitledBorder("Employee List"));
        
        // ✅ Updated columns to show separate education columns
        String[] empColumns = {"ID", "Name", "DOB", "DOJ", "Age", "Address", "City", "State", "Country", "Mobile", "Tenth", "Twelfth", "Graduation"};
        DefaultTableModel empModel = new DefaultTableModel(empColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable for better UX
            }
        };
        
        employeeTable = new JTable(empModel);
        employeeTable.setRowHeight(35); // ✅ Increased row height for better readability
        employeeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // ✅ Set column widths for better display
        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(120);  // Name
        employeeTable.getColumnModel().getColumn(2).setPreferredWidth(80);   // DOB
        employeeTable.getColumnModel().getColumn(3).setPreferredWidth(80);   // DOJ
        employeeTable.getColumnModel().getColumn(4).setPreferredWidth(40);   // Age
        employeeTable.getColumnModel().getColumn(5).setPreferredWidth(120);  // Address
        employeeTable.getColumnModel().getColumn(6).setPreferredWidth(80);   // City
        employeeTable.getColumnModel().getColumn(7).setPreferredWidth(100);  // State
        employeeTable.getColumnModel().getColumn(8).setPreferredWidth(80);   // Country
        employeeTable.getColumnModel().getColumn(9).setPreferredWidth(100);  // Mobile
        employeeTable.getColumnModel().getColumn(10).setPreferredWidth(120); // Tenth
        employeeTable.getColumnModel().getColumn(11).setPreferredWidth(120); // Twelfth
        employeeTable.getColumnModel().getColumn(12).setPreferredWidth(120); // Graduation
        
        // ✅ Add selection listener to populate form fields
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = employeeTable.getSelectedRow();
                if (selectedRow >= 0) {
                    populateFormFromTable(selectedRow);
                }
            }
        });
        
        JScrollPane empScrollPane = new JScrollPane(employeeTable);
        empScrollPane.setPreferredSize(new Dimension(800, 250)); // ✅ Increased height
        employeePanel.add(empScrollPane, BorderLayout.CENTER);
        
        // Add both panels to center
        centerPanel.add(educationPanel, BorderLayout.NORTH);
        centerPanel.add(employeePanel, BorderLayout.CENTER);
        
        return centerPanel;
    }
    
    private JPanel createButtonPanel() {
        // ✅ Use GridLayout to ensure all buttons are visible in the same line
        JPanel buttonPanel = new JPanel(new GridLayout(1, 6, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton addButton = new JButton("➕ Add");
        JButton updateButton = new JButton("✏ Update");
        JButton deleteButton = new JButton("🗑 Delete");
        JButton getButton = new JButton("📋 Get All");
        JButton findButton = new JButton("🔍 Find");
        JButton clearButton = new JButton("🧹 Clear");
        
        // ✅ Style buttons
        Color buttonColor = new Color(70, 130, 180);
        Color buttonTextColor = Color.WHITE;
        Font buttonFont = new Font("Arial", Font.BOLD, 11); // ✅ Smaller font
        
        JButton[] buttons = {addButton, updateButton, deleteButton, getButton, findButton, clearButton};
        for (JButton button : buttons) {
            button.setBackground(buttonColor);
            button.setForeground(buttonTextColor);
            button.setFont(buttonFont);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setPreferredSize(new Dimension(120, 30)); // ✅ Smaller size
        }
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(getButton);
        buttonPanel.add(findButton);
        buttonPanel.add(clearButton);
        
        return buttonPanel;
    }
    
    private void setupCityAutoComplete() {
        cityField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                String city = cityField.getText().trim();
                if (city.equalsIgnoreCase("Kanpur")) {
                    stateField.setText("Uttar Pradesh");
                    countryField.setText("India");
                } 
                else if (city.equalsIgnoreCase("Delhi")) {
                    stateField.setText("Delhi");
                    countryField.setText("India");
                }
                else if (city.equalsIgnoreCase("Mumbai")) {
                    stateField.setText("Maharashtra");
                    countryField.setText("India");
                }
                else if (city.equalsIgnoreCase("Bangalore") || city.equalsIgnoreCase("Bengaluru")) {
                    stateField.setText("Karnataka");
                    countryField.setText("India");
                }
                else if (city.equalsIgnoreCase("Chennai")) {
                    stateField.setText("Tamil Nadu");
                    countryField.setText("India");
                }
                else if (city.equalsIgnoreCase("Kolkata")) {
                    stateField.setText("West Bengal");
                    countryField.setText("India");
                }
                else if (city.equalsIgnoreCase("Hyderabad")) {
                    stateField.setText("Telangana");
                    countryField.setText("India");
                }
                else if (city.equalsIgnoreCase("Pune")) {
                    stateField.setText("Maharashtra");
                    countryField.setText("India");
                }
                else if (city.equalsIgnoreCase("Ahmedabad")) {
                    stateField.setText("Gujarat");
                    countryField.setText("India");
                }
                else if (city.equalsIgnoreCase("Jaipur")) {
                    stateField.setText("Rajasthan");
                    countryField.setText("India");
                }
                else if (city.equalsIgnoreCase("Lucknow")) {
                    stateField.setText("Uttar Pradesh");
                    countryField.setText("India");
                }
                else if (city.equalsIgnoreCase("Patna")) {
                    stateField.setText("Bihar");
                    countryField.setText("India");
                }
                else if (city.equalsIgnoreCase("Chandigarh")) {
                    stateField.setText("Chandigarh");
                    countryField.setText("India");
                }
                else if (city.equalsIgnoreCase("Bhopal")) {
                    stateField.setText("Madhya Pradesh");
                    countryField.setText("India");
                }
                else if (city.equalsIgnoreCase("Indore")) {
                    stateField.setText("Madhya Pradesh");
                    countryField.setText("India");
                }
                else if (city.equalsIgnoreCase("Vadodara")) {
                    stateField.setText("Gujarat");
                    countryField.setText("India");
                }
                else if (city.equalsIgnoreCase("Surat")) {
                    stateField.setText("Gujarat");
                    countryField.setText("India");
                }
                else {
                    stateField.setText("");
                    countryField.setText("");
                }
            }
        });
    }
    
    private void setupAgeCalculation() {
        dobField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { calculateAge(); }
            public void removeUpdate(DocumentEvent e) { calculateAge(); }
            public void changedUpdate(DocumentEvent e) { calculateAge(); }
        });
    }
    
    private void setupButtonActions() {
        // Get button references from the button panel
        JPanel buttonPanel = (JPanel) ((JPanel) getContentPane().getComponent(0)).getComponent(2);
        JButton addButton = (JButton) buttonPanel.getComponent(0);
        JButton updateButton = (JButton) buttonPanel.getComponent(1);
        JButton deleteButton = (JButton) buttonPanel.getComponent(2);
        JButton getButton = (JButton) buttonPanel.getComponent(3);
        JButton findButton = (JButton) buttonPanel.getComponent(4);
        JButton clearButton = (JButton) buttonPanel.getComponent(5);
        
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
            resultArea.setText("✅ Employee Added Successfully:\n" + formatJson(response));
            clearFields();
            getAllEmployees(); // ✅ Refresh the employee table
        } catch (Exception e) {
            resultArea.setText("❌ Error: " + e.getMessage());
        }
    }

    private void updateEmployee() {
        try {
            if (idField.getText().trim().isEmpty()) {
                resultArea.setText("⚠ Please enter Employee ID to update.");
                return;
            }
            JSONObject json = buildJson();
            String url = "http://localhost:8080/api/employees/" + idField.getText().trim();
            String response = employeeService.sendRequest("PUT", url, json.toString());
            resultArea.setText("✅ Employee Updated Successfully:\n" + formatJson(response));
            clearFields();
            getAllEmployees(); // ✅ Refresh the employee table
        } catch (Exception e) {
            resultArea.setText("❌ Error: " + e.getMessage());
        }
    }

    private void deleteEmployee() {
        try {
            if (idField.getText().trim().isEmpty()) {
                resultArea.setText("⚠ Please enter Employee ID to delete.");
                return;
            }
            String url = "http://localhost:8080/api/employees/" + idField.getText().trim();
            String response = employeeService.sendRequest("DELETE", url, null);
            resultArea.setText("✅ Deleted Successfully:\n" + response);
            clearFields();
            getAllEmployees(); // ✅ Refresh the employee table
        } catch (Exception e) {
            resultArea.setText("❌ Delete failed: " + e.getMessage());
        }
    }

    // ✅ Updated method to populate employee table instead of text area
    private void getAllEmployees() {
        try {
            List<Map<String, String>> employees = employeeService.getAllEmployees("http://localhost:8080/api/employees");
            populateEmployeeTable(employees);
            resultArea.setText("✅ Loaded " + employees.size() + " employees successfully!");
        } catch (Exception e) {
            resultArea.setText("❌ Error loading employees: " + e.getMessage());
        }
    }
    
    // ✅ New method to populate the employee table
    private void populateEmployeeTable(List<Map<String, String>> employees) {
        DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
        model.setRowCount(0); // Clear existing rows
        
        for (Map<String, String> employee : employees) {
            String id = employee.getOrDefault("userId", "");
            String firstName = employee.getOrDefault("firstName", "");
            String lastName = employee.getOrDefault("lastName", "");
            String name = firstName + " " + lastName;
            String dob = employee.getOrDefault("dob", "");
            String doj = employee.getOrDefault("doj", "");
            String age = employee.getOrDefault("age", "");
            String address = employee.getOrDefault("address", "");
            String city = employee.getOrDefault("city", "");
            String state = employee.getOrDefault("state", "");
            String country = employee.getOrDefault("country", "");
            String mobile = employee.getOrDefault("mobileNo", "");
            
            // ✅ Format education information for separate columns
            String tenth = formatEducationForColumn(employee.getOrDefault("education", ""), "Tenth");
            String twelfth = formatEducationForColumn(employee.getOrDefault("education", ""), "Twelfth");
            String graduation = formatEducationForColumn(employee.getOrDefault("education", ""), "Graduation");
            
            model.addRow(new Object[]{id, name, dob, doj, age, address, city, state, country, mobile, tenth, twelfth, graduation});
        }
    }
    
    // ✅ New method to format education for specific column
    private String formatEducationForColumn(String educationJson, String level) {
        try {
            if (educationJson == null || educationJson.isEmpty()) {
                return "No data";
            }
            
            JSONArray eduArray = new JSONArray(educationJson);
            if (eduArray.length() == 0) {
                return "No data";
            }
            
            for (int i = 0; i < eduArray.length(); i++) {
                JSONObject edu = eduArray.getJSONObject(i);
                String eduLevel = edu.optString("level", "");
                
                if (eduLevel.equals(level)) {
                    String school = edu.optString("schoolOrCollegeName", "");
                    String obtained = edu.optString("obtainedScore", "");
                    String total = edu.optString("totalScore", "");
                    String percentage = edu.optString("percentage", "");
                    
                    StringBuilder eduText = new StringBuilder();
                    if (!school.isEmpty()) {
                        eduText.append(school);
                    }
                    if (!obtained.isEmpty() && !total.isEmpty()) {
                        if (eduText.length() > 0) eduText.append(" | ");
                        eduText.append(obtained).append("/").append(total);
                        if (!percentage.isEmpty()) {
                            eduText.append(" (").append(percentage).append("%)");
                        }
                    }
                    
                    return eduText.length() > 0 ? eduText.toString() : "No data";
                }
            }
            
            return "No data";
        } catch (Exception e) {
            return "Error";
        }
    }
    
    // ✅ New method to populate form fields when a row is selected
    private void populateFormFromTable(int selectedRow) {
        DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
        
        String id = (String) model.getValueAt(selectedRow, 0);
        String name = (String) model.getValueAt(selectedRow, 1);
        String dob = (String) model.getValueAt(selectedRow, 2);
        String doj = (String) model.getValueAt(selectedRow, 3);
        String age = (String) model.getValueAt(selectedRow, 4);
        String address = (String) model.getValueAt(selectedRow, 5);
        String city = (String) model.getValueAt(selectedRow, 6);
        String state = (String) model.getValueAt(selectedRow, 7);
        String country = (String) model.getValueAt(selectedRow, 8);
        String mobile = (String) model.getValueAt(selectedRow, 9);
        
        // Split name into first and last name
        String[] nameParts = name.split(" ", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        
        // Populate form fields
        idField.setText(id);
        firstNameField.setText(firstName);
        lastNameField.setText(lastName);
        dobField.setText(dob);
        dojField.setText(doj);
        ageField.setText(age);
        addressField.setText(address);
        cityField.setText(city);
        stateField.setText(state);
        countryField.setText(country);
        mobileField.setText(mobile);
        
        // ✅ Populate education table with employee's education data
        populateEducationTable(id);
    }
    
    // ✅ New method to populate education table with employee's education data
    private void populateEducationTable(String employeeId) {
        try {
            // Get the employee's full data to access education
            String response = employeeService.sendRequest("GET", "http://localhost:8080/api/employees", null);
            JSONArray allEmployees = new JSONArray(response);
            
            for (int i = 0; i < allEmployees.length(); i++) {
                JSONObject emp = allEmployees.getJSONObject(i);
                if (emp.optString("userId", "").equals(employeeId)) {
                    JSONArray educationArray = emp.optJSONArray("education");
                    if (educationArray != null) {
                        DefaultTableModel eduModel = (DefaultTableModel) educationTable.getModel();
                        
                        // Clear existing education data
                        for (int row = 0; row < eduModel.getRowCount(); row++) {
                            for (int col = 1; col < eduModel.getColumnCount(); col++) {
                                eduModel.setValueAt("", row, col);
                            }
                        }
                        
                        // Populate with employee's education data
                        for (int j = 0; j < educationArray.length() && j < eduModel.getRowCount(); j++) {
                            JSONObject edu = educationArray.getJSONObject(j);
                            String level = edu.optString("level", "");
                            
                            // Find matching row in education table
                            for (int row = 0; row < eduModel.getRowCount(); row++) {
                                if (eduModel.getValueAt(row, 0).equals(level)) {
                                    eduModel.setValueAt(edu.optString("schoolOrCollegeName", ""), row, 1);
                                    eduModel.setValueAt(edu.optString("obtainedScore", ""), row, 2);
                                    eduModel.setValueAt(edu.optString("totalScore", ""), row, 3);
                                    eduModel.setValueAt(edu.optString("percentage", ""), row, 4);
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error populating education table: " + e.getMessage());
        }
    }

    private void findEmployee() {
        String firstName = firstNameField.getText().trim();
        String dob = dobField.getText().trim();
        
        if (firstName.isEmpty() && dob.isEmpty()) {
            resultArea.setText("⚠ Please enter either First Name or Date of Birth to search.");
            return;
        }

        try {
            // Get all employees first using the existing API
            String response = employeeService.sendRequest("GET", "http://localhost:8080/api/employees", null);
            
            // Parse the response and filter results
            JSONArray allEmployees = new JSONArray(response);
            JSONArray filteredEmployees = new JSONArray();
            
            for (int i = 0; i < allEmployees.length(); i++) {
                JSONObject emp = allEmployees.getJSONObject(i);
                String empFirstName = emp.optString("firstName", "");
                String empDob = emp.optString("dob", "");
                
                // Check if employee matches search criteria
                boolean matches = false;
                
                // Check first name (case-insensitive partial match)
                if (!firstName.isEmpty() && empFirstName.toLowerCase().contains(firstName.toLowerCase())) {
                    matches = true;
                }
                
                // Check DOB (exact match)
                if (!dob.isEmpty() && empDob.contains(dob)) {
                    matches = true;
                }
                
                // If either criteria matches, add to filtered results
                if (matches) {
                    filteredEmployees.put(emp);
                }
            }
            
            if (filteredEmployees.length() == 0) {
                resultArea.setText("🔍 No employees found matching the search criteria.");
            } else {
                // ✅ Convert filtered results to table format
                List<Map<String, String>> filteredList = new ArrayList<>();
                for (int i = 0; i < filteredEmployees.length(); i++) {
                    JSONObject emp = filteredEmployees.getJSONObject(i);
                    Map<String, String> map = new LinkedHashMap<>();
                    for (String key : emp.keySet()) {
                        map.put(key, emp.optString(key, ""));
                    }
                    filteredList.add(map);
                }
                populateEmployeeTable(filteredList);
                resultArea.setText("🔍 Found " + filteredEmployees.length() + " employee(s) matching the search criteria.");
            }
            
        } catch (Exception e) {
            resultArea.setText("❌ Error: " + e.getMessage());
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
        
        // ✅ Clear employee table selection
        employeeTable.clearSelection();
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
