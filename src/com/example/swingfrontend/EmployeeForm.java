import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class EmployeeForm extends JFrame {
    private JTextField firstNameField, lastNameField, dobField, dojField, ageField, addressField, mobileField, cityField, stateField, countryField;
    private JTextField tenthSchool, tenthObt, tenthTotal, tenthPerc;
    private JTextField twelfthSchool, twelfthObt, twelfthTotal, twelfthPerc;
    private JTextField gradSchool, gradObt, gradTotal, gradPerc;
    private JTextArea resultArea;
    private JButton addBtn, updateBtn, deleteBtn, findBtn, getAllBtn;
    private JTextField idField;

    // Hardcoded City → State, Country
    private Map<String, String[]> cityMap = new HashMap<>();

    public EmployeeForm() {
        setTitle("Employee Management");
        setSize(900, 750);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // City mapping
        cityMap.put("Kanpur", new String[]{"Uttar Pradesh", "India"});
        cityMap.put("Mumbai", new String[]{"Maharashtra", "India"});
        cityMap.put("Delhi", new String[]{"Delhi", "India"});
        cityMap.put("Bangalore", new String[]{"Karnataka", "India"});
        cityMap.put("Kolkata", new String[]{"West Bengal", "India"});

        JLabel lblId = new JLabel("Employee ID:");
        lblId.setBounds(30, 20, 100, 25);
        add(lblId);
        idField = new JTextField();
        idField.setBounds(150, 20, 150, 25);
        add(idField);

        JLabel lblFirst = new JLabel("First Name:");
        lblFirst.setBounds(30, 60, 100, 25);
        add(lblFirst);
        firstNameField = new JTextField();
        firstNameField.setBounds(150, 60, 150, 25);
        add(firstNameField);

        JLabel lblLast = new JLabel("Last Name:");
        lblLast.setBounds(30, 100, 100, 25);
        add(lblLast);
        lastNameField = new JTextField();
        lastNameField.setBounds(150, 100, 150, 25);
        add(lastNameField);

        JLabel lblDob = new JLabel("DOB (yyyy-MM-dd):");
        lblDob.setBounds(30, 140, 150, 25);
        add(lblDob);
        dobField = new JTextField();
        dobField.setBounds(180, 140, 120, 25);
        add(dobField);

        JLabel lblAge = new JLabel("Age:");
        lblAge.setBounds(320, 140, 50, 25);
        add(lblAge);
        ageField = new JTextField();
        ageField.setBounds(370, 140, 50, 25);
        ageField.setEditable(false);
        add(ageField);

        JLabel lblDoj = new JLabel("DOJ (yyyy-MM-dd):");
        lblDoj.setBounds(30, 180, 150, 25);
        add(lblDoj);
        dojField = new JTextField();
        dojField.setBounds(180, 180, 120, 25);
        add(dojField);

        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setBounds(30, 220, 100, 25);
        add(lblAddress);
        addressField = new JTextField();
        addressField.setBounds(150, 220, 250, 25);
        add(addressField);

        JLabel lblMobile = new JLabel("Mobile:");
        lblMobile.setBounds(30, 260, 100, 25);
        add(lblMobile);
        mobileField = new JTextField();
        mobileField.setBounds(150, 260, 150, 25);
        add(mobileField);

        JLabel lblCity = new JLabel("City:");
        lblCity.setBounds(30, 300, 100, 25);
        add(lblCity);
        cityField = new JTextField();
        cityField.setBounds(150, 300, 150, 25);
        add(cityField);

        JLabel lblState = new JLabel("State:");
        lblState.setBounds(320, 300, 50, 25);
        add(lblState);
        stateField = new JTextField();
        stateField.setBounds(370, 300, 150, 25);
        stateField.setEditable(false);
        add(stateField);

        JLabel lblCountry = new JLabel("Country:");
        lblCountry.setBounds(540, 300, 60, 25);
        add(lblCountry);
        countryField = new JTextField();
        countryField.setBounds(600, 300, 150, 25);
        countryField.setEditable(false);
        add(countryField);

        JLabel lblEdu = new JLabel("Education Details:");
        lblEdu.setBounds(30, 340, 150, 25);
        add(lblEdu);

        // Tenth
        JLabel lblTenth = new JLabel("10th:");
        lblTenth.setBounds(30, 370, 50, 25);
        add(lblTenth);
        tenthSchool = new JTextField("School Name");
        tenthSchool.setBounds(80, 370, 150, 25);
        add(tenthSchool);
        tenthObt = new JTextField("Obt");
        tenthObt.setBounds(240, 370, 50, 25);
        add(tenthObt);
        tenthTotal = new JTextField("Total");
        tenthTotal.setBounds(300, 370, 50, 25);
        add(tenthTotal);
        tenthPerc = new JTextField("%");
        tenthPerc.setBounds(360, 370, 50, 25);
        tenthPerc.setEditable(false);
        add(tenthPerc);

        // Twelfth
        JLabel lblTwelfth = new JLabel("12th:");
        lblTwelfth.setBounds(30, 410, 50, 25);
        add(lblTwelfth);
        twelfthSchool = new JTextField("School Name");
        twelfthSchool.setBounds(80, 410, 150, 25);
        add(twelfthSchool);
        twelfthObt = new JTextField("Obt");
        twelfthObt.setBounds(240, 410, 50, 25);
        add(twelfthObt);
        twelfthTotal = new JTextField("Total");
        twelfthTotal.setBounds(300, 410, 50, 25);
        add(twelfthTotal);
        twelfthPerc = new JTextField("%");
        twelfthPerc.setBounds(360, 410, 50, 25);
        twelfthPerc.setEditable(false);
        add(twelfthPerc);

        // Graduation
        JLabel lblGrad = new JLabel("Grad:");
        lblGrad.setBounds(30, 450, 50, 25);
        add(lblGrad);
        gradSchool = new JTextField("College Name");
        gradSchool.setBounds(80, 450, 150, 25);
        add(gradSchool);
        gradObt = new JTextField("Obt");
        gradObt.setBounds(240, 450, 50, 25);
        add(gradObt);
        gradTotal = new JTextField("Total");
        gradTotal.setBounds(300, 450, 50, 25);
        add(gradTotal);
        gradPerc = new JTextField("%");
        gradPerc.setBounds(360, 450, 50, 25);
        gradPerc.setEditable(false);
        add(gradPerc);

        // Buttons
        addBtn = new JButton("Add");
        addBtn.setBounds(30, 500, 80, 30);
        add(addBtn);
        updateBtn = new JButton("Update");
        updateBtn.setBounds(120, 500, 80, 30);
        add(updateBtn);
        deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(210, 500, 80, 30);
        add(deleteBtn);
        findBtn = new JButton("Find");
        findBtn.setBounds(300, 500, 80, 30);
        add(findBtn);
        getAllBtn = new JButton("Get All");
        getAllBtn.setBounds(390, 500, 100, 30);
        add(getAllBtn);

        // Result area
        resultArea = new JTextArea();
        resultArea.setBounds(30, 550, 800, 130);
        resultArea.setEditable(false);
        add(resultArea);

        // Listeners
        dobField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                calculateAge();
            }
        });

        cityField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                autoFillStateCountry();
            }
        });

        FocusAdapter percListener = new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                calculatePercentage();
            }
        };
        tenthObt.addFocusListener(percListener);
        tenthTotal.addFocusListener(percListener);
        twelfthObt.addFocusListener(percListener);
        twelfthTotal.addFocusListener(percListener);
        gradObt.addFocusListener(percListener);
        gradTotal.addFocusListener(percListener);

        // Button Actions
        addBtn.addActionListener(e -> addEmployee());
        updateBtn.addActionListener(e -> updateEmployee());
        deleteBtn.addActionListener(e -> deleteEmployee());
        findBtn.addActionListener(e -> findEmployee());
        getAllBtn.addActionListener(e -> getAllEmployees());
    }

    private void calculateAge() {
        try {
            LocalDate dob = LocalDate.parse(dobField.getText());
            int age = Period.between(dob, LocalDate.now()).getYears();
            ageField.setText(String.valueOf(age));
        } catch (Exception ex) {
            ageField.setText("");
        }
    }

    private void autoFillStateCountry() {
        String city = cityField.getText().trim();
        if (cityMap.containsKey(city)) {
            stateField.setText(cityMap.get(city)[0]);
            countryField.setText(cityMap.get(city)[1]);
        }
    }

    private void calculatePercentage() {
        calculateSinglePerc(tenthObt, tenthTotal, tenthPerc);
        calculateSinglePerc(twelfthObt, twelfthTotal, twelfthPerc);
        calculateSinglePerc(gradObt, gradTotal, gradPerc);
    }

    private void calculateSinglePerc(JTextField obt, JTextField total, JTextField perc) {
        try {
            double o = Double.parseDouble(obt.getText());
            double t = Double.parseDouble(total.getText());
            double p = (o / t) * 100;
            perc.setText(String.format("%.2f%%", p));
        } catch (Exception ex) {
            perc.setText("");
        }
    }

    private JSONObject buildJson() {
        JSONObject json = new JSONObject();
        json.put("firstName", firstNameField.getText());
        json.put("lastName", lastNameField.getText());
        json.put("dob", dobField.getText());
        json.put("doj", dojField.getText());
        json.put("age", Integer.parseInt(ageField.getText()));
        json.put("address", addressField.getText());
        json.put("mobileNo", mobileField.getText());
        json.put("city", cityField.getText());
        json.put("state", stateField.getText());
        json.put("country", countryField.getText());

        JSONArray eduArray = new JSONArray();
        eduArray.put(createEducationJson("Tenth", tenthSchool.getText(), tenthObt.getText(), tenthTotal.getText(), tenthPerc.getText()));
        eduArray.put(createEducationJson("Twelfth", twelfthSchool.getText(), twelfthObt.getText(), twelfthTotal.getText(), twelfthPerc.getText()));
        eduArray.put(createEducationJson("Graduation", gradSchool.getText(), gradObt.getText(), gradTotal.getText(), gradPerc.getText()));

        json.put("education", eduArray);
        return json;
    }

    private JSONObject createEducationJson(String level, String name, String obt, String total, String perc) {
        JSONObject obj = new JSONObject();
        obj.put("level", level);
        obj.put("schoolOrCollegeName", name);
        obj.put("obtainedScore", obt);
        obj.put("totalScore", total);
        obj.put("percentage", perc);
        return obj;
    }

    private void addEmployee() {
        sendRequest("POST", "http://localhost:8080/api/employees", buildJson().toString());
    }

    private void updateEmployee() {
        String id = idField.getText();
        sendRequest("PUT", "http://localhost:8080/api/employees/" + id, buildJson().toString());
    }

    private void deleteEmployee() {
        String id = idField.getText();
        sendRequest("DELETE", "http://localhost:8080/api/employees/" + id, null);
    }

    private void findEmployee() {
        String id = idField.getText();
        sendRequest("GET", "http://localhost:8080/api/employees/" + id, null);
    }

    private void getAllEmployees() {
        sendRequest("GET", "http://localhost:8080/api/employees", null);
    }

    private void sendRequest(String method, String urlString, String jsonInput) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            if (jsonInput != null) {
                try (OutputStream os = con.getOutputStream()) {
                    os.write(jsonInput.getBytes());
                }
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            resultArea.setText(response.toString());
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeForm().setVisible(true));
    }
}
