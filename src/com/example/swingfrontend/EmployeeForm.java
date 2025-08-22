import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
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
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final Map<String, String[]> cityMap = new HashMap<>();
    static {
        cityMap.put("mumbai", new String[]{"Maharashtra", "India"});
        cityMap.put("pune", new String[]{"Maharashtra", "India"});
        cityMap.put("new york", new String[]{"New York", "USA"});
        cityMap.put("los angeles", new String[]{"California", "USA"});
        cityMap.put("london", new String[]{"England", "UK"});
    }

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
        panel.add(new JLabel("Date of Birth (yyyy-MM-dd)")); txtDob = new JTextField(); panel.add(txtDob);
        panel.add(new JLabel("Date of Joining (yyyy-MM-dd)")); txtDoj = new JTextField(); panel.add(txtDoj);
        panel.add(new JLabel("Age")); txtAge = new JTextField(); txtAge.setEditable(false); panel.add(txtAge);
        panel.add(new JLabel("Address")); txtAddress = new JTextField(); panel.add(txtAddress);
        panel.add(new JLabel("Mobile")); txtMobile = new JTextField(); panel.add(txtMobile);
        panel.add(new JLabel("City")); txtCity = new JTextField(); panel.add(txtCity);
        panel.add(new JLabel("State")); txtState = new JTextField(); txtState.setEditable(false); panel.add(txtState);
        panel.add(new JLabel("Country")); txtCountry = new JTextField(); txtCountry.setEditable(false); panel.add(txtCountry);
        panel.add(new JLabel("10th Marks")); txtTenth = new JTextField(); panel.add(txtTenth);
        panel.add(new JLabel("12th Marks")); txtTwelfth = new JTextField(); panel.add(txtTwelfth);
        panel.add(new JLabel("Graduation Marks")); txtGraduation = new JTextField(); panel.add(txtGraduation);

        btnAdd = new JButton("Add Employee");
        panel.add(btnAdd);
        btnUpdate = new JButton("Update Employee"); panel.add(btnUpdate);
        btnDelete = new JButton("Delete Employee"); panel.add(btnDelete);
        btnGetAll = new JButton("Get All Employees"); panel.add(btnGetAll);

        add(panel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID","First Name","Last Name","Age","City","State","Country"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Listeners
        txtDob.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) { calculateAge(); }
        });

        txtCity.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) { fillStateCountry(); }
        });

        btnAdd.addActionListener(e -> addEmployee());
        btnGetAll.addActionListener(e -> fetchEmployees());
    }

    private void calculateAge() {
        String dobStr = txtDob.getText().trim();
        if (!dobStr.isEmpty()) {
            try {
                LocalDate dob = LocalDate.parse(dobStr, formatter);
                int age = Period.between(dob, LocalDate.now()).getYears();
                txtAge.setText(String.valueOf(age));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid DOB format! Use yyyy-MM-dd");
            }
        }
    }

    private void fillStateCountry() {
        String city = txtCity.getText().trim().toLowerCase();
        if (cityMap.containsKey(city)) {
            txtState.setText(cityMap.get(city)[0]);
            txtCountry.setText(cityMap.get(city)[1]);
        } else {
            txtState.setText("");
            txtCountry.setText("");
        }
    }

    private void addEmployee() {
        try {
            if (txtFirstName.getText().trim().isEmpty() || txtDob.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "First Name and DOB are required!");
                return;
            }

            JSONObject json = new JSONObject();
            putIfNotEmpty(json, "firstName", txtFirstName.getText());
            putIfNotEmpty(json, "lastName", txtLastName.getText());
            putIfNotEmpty(json, "dob", txtDob.getText());
            putIfNotEmpty(json, "doj", txtDoj.getText());
            if (!txtAge.getText().trim().isEmpty()) json.put("age", Integer.parseInt(txtAge.getText()));
            putIfNotEmpty(json, "address", txtAddress.getText());
            putIfNotEmpty(json, "mobile", txtMobile.getText());
            putIfNotEmpty(json, "city", txtCity.getText());
            putIfNotEmpty(json, "state", txtState.getText());
            putIfNotEmpty(json, "country", txtCountry.getText());
            if (!txtTenth.getText().trim().isEmpty()) json.put("education10", Integer.parseInt(txtTenth.getText()));
            if (!txtTwelfth.getText().trim().isEmpty()) json.put("education12", Integer.parseInt(txtTwelfth.getText()));
            if (!txtGraduation.getText().trim().isEmpty()) json.put("graduation", Integer.parseInt(txtGraduation.getText()));

            System.out.println("Sending JSON: " + json);
            String response = service.createEmployee(json.toString());
            JOptionPane.showMessageDialog(this, response);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding employee: " + ex.getMessage());
        }
    }

    private void fetchEmployees() {
        try {
            String response = service.getAllEmployees();
            System.out.println("Response from API: " + response);
            tableModel.setRowCount(0);
            JSONArray arr = new JSONArray(response);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                tableModel.addRow(new Object[]{
                        obj.optLong("userId"),
                        obj.optString("firstName"),
                        obj.optString("lastName"),
                        obj.optInt("age"),
                        obj.optString("city"),
                        obj.optString("state"),
                        obj.optString("country")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error fetching employees: " + ex.getMessage());
        }
    }

    private void putIfNotEmpty(JSONObject json, String key, String value) {
        if (value != null && !value.trim().isEmpty()) {
            json.put(key, value.trim());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeForm().setVisible(true));
    }
}
