// package com.example.swingfrontend;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EmployeeService {

    private static final String BASE_URL = "http://localhost:8080/api/employees";
    private HttpClient client;

    public EmployeeService() {
        client = HttpClient.newHttpClient();
    }

    public String createEmployee(String json) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        System.out.println("POST Request URL: " + BASE_URL);
        System.out.println("POST Body: " + json);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response: " + response.statusCode() + " | " + response.body());

        if (response.statusCode() == 201 || response.statusCode() == 200) {
            return "Employee added successfully!";
        } else {
            return "Error: " + response.statusCode() + " | " + response.body();
        }
    }

    public String updateEmployee(Long id, String json) throws IOException, InterruptedException {
        String url = BASE_URL + "/" + id;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        System.out.println("PUT Request URL: " + url);
        System.out.println("PUT Body: " + json);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response: " + response.statusCode() + " | " + response.body());

        if (response.statusCode() == 200) {
            return "Employee updated successfully!";
        } else {
            return "Error: " + response.statusCode() + " | " + response.body();
        }
    }

    public String deleteEmployee(Long id) throws IOException, InterruptedException {
        String url = BASE_URL + "/" + id;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

        System.out.println("DELETE Request URL: " + url);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response: " + response.statusCode() + " | " + response.body());

        if (response.statusCode() == 200) {
            return "Employee deleted successfully!";
        } else {
            return "Error: " + response.statusCode() + " | " + response.body();
        }
    }

    public String getAllEmployees() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        System.out.println("GET Request URL: " + BASE_URL);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response: " + response.statusCode() + " | " + response.body());

        if (response.statusCode() == 200) {
            return response.body();  // Return raw JSON array
        } else {
            return "[]";  // Return empty array on error
        }
    }
}
