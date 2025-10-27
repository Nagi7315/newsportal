package com.gofortrainings.newsportal.core.servlets;

import com.google.gson.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;

import javax.servlet.Servlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component(service = { Servlet.class })
@SlingServletPaths("/bin/exportcsv")
@ServiceDescription("CSV Export Servlet Without External Libraries")
public class CsvExportServlet extends SlingAllMethodsServlet {

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

        // Set response headers to trigger file download
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"table_data.csv\"");
        BufferedReader reader = new BufferedReader((request.getReader()));
        JsonArray tableData = JsonParser.parseReader(reader).getAsJsonObject().getAsJsonArray("data");
//        StringBuilder stringBuilder = new StringBuilder();
//        String line;
//        try (BufferedReader reader = request.getReader()) {
//            while ((line = reader.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//        }
//
//        String jsonData = stringBuilder.toString();
//        Gson gson = new Gson();
//        JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
//        JsonArray tableData = jsonObject.getAsJsonArray("data");
//        JsonArray tableData = gson.fromJson(jsonData, JsonArray.class);
        try (OutputStream outputStream = response.getOutputStream()) {
            StringBuilder csvBuilder = new StringBuilder();

            for (JsonElement row : tableData) {
                List<String> rowValues = new Gson().fromJson(row, List.class);
                String csvRow = rowValues.stream()
                        .map(this::escapeCsvValue) // Escape special characters
                        .collect(Collectors.joining(",")); // Join values with commas

                csvBuilder.append(csvRow).append("\r\n"); // Append row with newline
            }

            byte[] csvBytes = csvBuilder.toString().getBytes(StandardCharsets.UTF_8);
            outputStream.write(csvBytes);
            outputStream.flush();
        } catch (Exception e) {
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error generating CSV: " + e.getMessage());
        }
    }

    // Escape CSV values (handle commas, quotes, and new lines)
    private String escapeCsvValue(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\""; // Escape double quotes
        }
        return value;
    }
}
