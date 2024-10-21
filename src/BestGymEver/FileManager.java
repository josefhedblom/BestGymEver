package BestGymEver;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private String customerFilePath;
    private String trainingLogFilePath;

    public FileManager(String customerFilePath, String trainingLogFilePath) {
        this.customerFilePath = customerFilePath;
        this.trainingLogFilePath = trainingLogFilePath;
    }

    public List<Customer> readCustomerData() {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(customerFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length != 2) {
                    System.err.println("Felaktig radformat: " + line);
                    continue;
                }
                String personalNumber = parts[0];
                String name = parts[1];

                String paymentDateLine = br.readLine();
                if (paymentDateLine != null) {
                    try {
                        LocalDate lastPaymentDate = LocalDate.parse(paymentDateLine);
                        customers.add(new Customer(personalNumber, name, lastPaymentDate));
                    } catch (DateTimeParseException e) {
                        System.err.println("Felaktigt datumformat: " + paymentDateLine);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Fel vid läsning av kunddata: " + e.getMessage());
        }
        return customers;
    }


    public String logTrainingSession(Customer customer) {
        LocalDateTime currentTime = LocalDateTime.now();
        String logEntry = customer.getPersonalNumber() + ", " + customer.getName() + ", " +
                LocalDate.now() + ", " + currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(trainingLogFilePath, true))) {
            bw.write(logEntry);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Fel vid skrivning av träningslogg: " + e.getMessage());
        }
        return logEntry;
    }
}
