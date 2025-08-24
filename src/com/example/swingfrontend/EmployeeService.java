// File: EmployeeService.java
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class EmployeeService {

    // Existing generic sendRequest method
    public String sendRequest(String method, String urlString, String jsonBody) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        if (jsonBody != null && (method.equals("POST") || method.equals("PUT"))) {
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        }

        int responseCode = conn.getResponseCode();
        InputStream is = (responseCode >= 200 && responseCode < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line.trim());
        }

        return response.toString();
    }

    /**
     * ✅ New method: Fetch all employees and return as structured data
     * This is useful for JTable integration in UI
     */
    public List<Map<String, String>> getAllEmployees(String urlString) {
        List<Map<String, String>> employeeList = new ArrayList<>();
        try {
            String response = sendRequest("GET", urlString, null);

            if (response.trim().startsWith("[")) {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Map<String, String> map = new LinkedHashMap<>();
                    for (String key : obj.keySet()) {
                        map.put(key, obj.optString(key, ""));
                    }
                    employeeList.add(map);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing employee list: " + e.getMessage());
        }
        return employeeList;
    }
}
