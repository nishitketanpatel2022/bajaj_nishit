package com.example.demo;

import com.example.demo.dto.SubmissionRequest;
import com.example.demo.dto.WebhookRequest;
import com.example.demo.dto.WebhookResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ChallengeRunner implements CommandLineRunner {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Application started, beginning challenge process...");
        generateWebhook();
    }

    private void generateWebhook() {
        String apiUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        // Your details are correctly filled in here
        WebhookRequest requestBody = new WebhookRequest(
                "Nishit Ketan Patel",
                "22BIT0529",
                "nishitketan.patel2022@vitstudent.ac.in"
        );

        try {
            ResponseEntity<WebhookResponse> responseEntity = restTemplate.postForEntity(
                    apiUrl,
                    requestBody,
                    WebhookResponse.class
            );

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                WebhookResponse response = responseEntity.getBody();
                if (response != null) {
                    String webhookUrl = response.getWebhookURL();
                    String accessToken = response.getAccessToken();
                    String regNo = requestBody.getRegNo();

                    System.out.println("Successfully generated webhook!");
                    System.out.println("Webhook URL: " + webhookUrl);
                    // System.out.println("Access Token: " + accessToken); // It's better not to print tokens

                    // --- This is the new part that continues the process ---
                    solveAndSubmit(webhookUrl, accessToken, regNo);
                }
            } else {
                System.err.println("Failed to generate webhook. Status code: " + responseEntity.getStatusCode());
                System.err.println("Response Body: " + responseEntity.getBody());
            }

        } catch (Exception e) {
            System.err.println("An error occurred while calling the webhook API: " + e.getMessage());
        }
    }

    /**
     * Determines the SQL query and calls the submission method.
     */
    private void solveAndSubmit(String webhookUrl, String accessToken, String regNo) {
        // Your regNo '22BIT0529' ends in 29, which is ODD. So we use the query for Question 1.
        String finalSqlQuery = "SELECT\n" +
                "    p.AMOUNT AS SALARY,\n" +
                "    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,\n" +
                "    TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE,\n" +
                "    d.DEPARTMENT_NAME\n" +
                "FROM\n" +
                "    PAYMENTS p\n" +
                "JOIN\n" +
                "    EMPLOYEE e ON p.EMP_ID = e.EMP_ID\n" +
                "JOIN\n" +
                "    DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID\n" +
                "WHERE\n" +
                "    EXTRACT(DAY FROM p.PAYMENT_TIME) != 1\n" +
                "ORDER BY\n" +
                "    p.AMOUNT DESC\n" +
                "LIMIT 1;";

        // Call the method to submit the solution
        submitSolution(webhookUrl, accessToken, finalSqlQuery);
    }

    /**
     * Submits the final SQL query to the provided webhook URL.
     */
    private void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        System.out.println("Submitting final SQL query...");

        // Create the request body for submission
        SubmissionRequest submissionBody = new SubmissionRequest(sqlQuery);

        // Set the necessary headers, including the Authorization token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", accessToken);

        HttpEntity<SubmissionRequest> entity = new HttpEntity<>(submissionBody, headers);

        try {
            // Send the final POST request
            ResponseEntity<String> submissionResponse = restTemplate.exchange(
                    webhookUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (submissionResponse.getStatusCode().is2xxSuccessful()) {
                System.out.println("Solution submitted successfully!");
                System.out.println("Response: " + submissionResponse.getBody());
            } else {
                System.err.println("Failed to submit solution. Status: " + submissionResponse.getStatusCode());
                System.err.println("Response Body: " + submissionResponse.getBody());
            }
        } catch (Exception e) {
            System.err.println("An error occurred during submission: " + e.getMessage());
        }
    }
}
