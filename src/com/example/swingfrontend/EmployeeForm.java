

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class EmployeeForm extends JFrame {

    private JTextField txtFirstName, txtLastName, txtDob, txtAge, txtAddress, txtMobile, txtCity, txtState, txtCountry;
    private JTextField txtTenth, txtTwelfth, txtGraduation;
    private JTextField txtUserId; // for update/delete
    private JTextArea txtOutput;
    private EmployeeService service = new EmployeeService();

    public EmployeeForm() {
        setTitle("Employee Form");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(14, 2));

        panel.add(new JLabel("User ID (for update/delete)"));
        txtUserId = new JTextField();
        panel.add(txtUserId);

        panel.add(new JLabel("First Name"));
        txtFirstName = new JTextField();
        panel.add(txtFirstName);

        panel.add(new JLabel("Last Name"));
        txtLastName = new JTextField();
        panel.add(txtLastName);

        panel.add(new JLabel("DOB (yyyy-mm-dd)"));
        txtDob = new JTextField();
        panel.add(txtDob);

        panel.add(new JLabel("Age"));
        txtAge = new JTextField();
        panel.add(txtAge);

        panel.add(new JLabel("Address"));
        txtAddress = new JTextField();
        panel.add(txtAddress);

        panel.add(new JLabel("Mobile"));
        txtMobile = new JTextField();
        panel.add(txtMobile);

        panel.add(new JLabel("City"));
        txtCity = new JTextField();
        panel.add(txtCity);

        panel.add(new JLabel("State"));
        txtState = new JTextField();
        panel.add(txtState);

        panel.add(new JLabel("Country"));
        txtCountry = new JTextField();
        panel.add(txtCountry);

        panel.add(new JLabel("10th"));
        txtTenth = new JTextField();
        panel.add(txtTenth);

        panel.add(new JLabel("12th"));
        txtTwelfth = new JTextField();
        panel.add(txtTwelfth);

        panel.add(new JLabel("Graduation"));
        txtGraduation = new JTextField();
        panel.add(txtGraduation);

        add(panel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();

        JButton btnCreate = new JButton("Create");
        JButton btnGetAll = new JButton("Get All");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");

        btnPanel.add(btnCreate);
        btnPanel.add(btnGetAll);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);

        add(btnPanel, BorderLayout.NORTH);

        txtOutput = new JTextArea();
        add(new JScrollPane(txtOutput), BorderLayout.SOUTH);

        // Button actions
        btnCreate.addActionListener(e -> {
            try {
                String json = getJsonFromForm();
                String response = service.createEmployee(json);
                txtOutput.setText(response);
            } catch (Exception ex) {
                txtOutput.setText(ex.getMessage());
            }
        });

        btnGetAll.addActionListener(e -> {
            try {
                String response = service.getAllEmployees();
                txtOutput.setText(response);
            } catch (Exception ex) {
                txtOutput.setText(ex.getMessage());
            }
        });

        btnUpdate.addActionListener(e -> {
            try {
                Long id = Long.parseLong(txtUserId.getText());
                String json = getJsonFromForm();
                String response = service.updateEmployee(id, json);
                txtOutput.setText(response);
            } catch (Exception ex) {
                txtOutput.setText(ex.getMessage());
            }
        });

        btnDelete.addActionListener(e -> {
            try {
                Long id = Long.parseLong(txtUserId.getText());
                String response = service.deleteEmployee(id);
                txtOutput.setText(response);
            } catch (Exception ex) {
                txtOutput.setText(ex.getMessage());
            }
        });
    }

    private String getJsonFromForm() {
        return "{"
                + "\"firstName\":\"" + txtFirstName.getText() + "\","
                + "\"lastName\":\"" + txtLastName.getText() + "\","
                + "\"dob\":\"" + txtDob.getText() + "\","
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
}
