import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Student;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Client {
    static String contentType = "";
    static String url = "";
    static boolean loop = true;
    static Scanner sc = new Scanner(System.in);
    static String fileExtension = "";


    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

       String url ="";
       System.out.println("Choose a choice");
        System.out.println("1. Fetch students");
        System.out.println("2. Fetch student by Id");
        System.out.println("3. insert a new student");
        int choice = sc.nextInt();
        switch(choice){
            case 1:
                printStudents(client);
            default:
                System.out.println("enter");
        }


       url = sc.nextLine();


    }
    public static void printStudents(HttpClient client) throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .GET()
                .header("accept", "text/json")
                .uri(URI.create("http://localhost:5050/students"))
                .build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();

        List<Student> students = mapper.readValue(response.body(), new TypeReference<>() {
        });
       students.forEach(student->System.out.println(student));
    }
    public static CompletableFuture<Void> POSTRequest(Map<String, String> bodyText) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(bodyText);

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create("http://localhost/" + url))
                .build();

        return HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode)
                .thenAccept(System.out::println);
    }
}
