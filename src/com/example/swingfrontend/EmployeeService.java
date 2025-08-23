import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class EmployeeService {

    private static final String BASE_URL = "http://localhost:8080/api/employees";

    // Send POST Request
    public static String addEmployee(JSONObject emp) throws Exception {
        return sendRequest(BASE_URL, "POST", emp.toString());
    }

    // Send PUT Request
    public static String updateEmployee(long id, JSONObject emp) throws Exception {
        return sendRequest(BASE_URL + "/" + id, "PUT", emp.toString());
    }

    // Delete Employee
    public static String deleteEmployee(long id) throws Exception {
        return sendRequest(BASE_URL + "/" + id, "DELETE", null);
    }

    // Get All Employees
    public static JSONArray getAllEmployees() throws Exception {
        String response = sendRequest(BASE_URL, "GET", null);
        return new JSONArray(response); // Backend returns a JSON array
    }

    // Get Employee by ID
    public static JSONObject getEmployeeById(long id) throws Exception {
        String response = sendRequest(BASE_URL + "/" + id, "GET", null);
        return new JSONObject(response);
    }

    private static String sendRequest(String urlString, String method, String jsonInput) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        if (jsonInput != null) {
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes());
            }
        }

        InputStream is = (conn.getResponseCode() < 400) ? conn.getInputStream() : conn.getErrorStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);

        return sb.toString();
    }
}
