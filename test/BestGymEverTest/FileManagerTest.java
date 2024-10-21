package BestGymEverTest;

import BestGymEver.Customer;
import BestGymEver.FileManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileManagerTest {
    private FileManager fileManager;
    private File tempCustomerFile;
    private File tempTrainingLogFile;

    @BeforeEach
    public void setUp() throws IOException {
        tempCustomerFile = File.createTempFile("kunddata", ".txt");
        tempTrainingLogFile = File.createTempFile("traningslogg", ".txt");

        try (FileWriter writer = new FileWriter(tempCustomerFile)) {
            writer.write("7703021234, Alhambra Aromes\n2024-07-01\n");
            writer.write("8204021234, Bear Belle\n2019-12-02\n");
        }

        fileManager = new FileManager(tempCustomerFile.getAbsolutePath(), tempTrainingLogFile.getAbsolutePath());
    }

    @Test
    public void testReadCustomerData() {
        List<Customer> customers = fileManager.readCustomerData();
        assertNotNull(customers);
        assertEquals(2, customers.size());

        Customer customer1 = customers.get(0);
        assertEquals("7703021234", customer1.getPersonalNumber());
        assertEquals("Alhambra Aromes", customer1.getName());
        assertEquals(LocalDate.parse("2024-07-01"), customer1.getLastPaymentDate());

        Customer customer2 = customers.get(1);
        assertEquals("8204021234", customer2.getPersonalNumber());
        assertEquals("Bear Belle", customer2.getName());
        assertEquals(LocalDate.parse("2019-12-02"), customer2.getLastPaymentDate());
    }

    @Test
    public void testLogTrainingSession() throws IOException {
        Customer customer = new Customer("7703021234", "Alhambra Aromes", LocalDate.now());

        String logEntry = fileManager.logTrainingSession(customer);
        assertNotNull(logEntry);

        assertTrue(logEntry.contains("7703021234"));
        assertTrue(logEntry.contains("Alhambra Aromes"));

        List<String> lines = Files.readAllLines(tempTrainingLogFile.toPath());
        assertEquals(1, lines.size());
        assertEquals(logEntry, lines.get(0));
    }

    @AfterEach
    public void tearDown() throws IOException  {
        Files.deleteIfExists(tempCustomerFile.toPath());
        Files.deleteIfExists(tempTrainingLogFile.toPath());
    }
}
