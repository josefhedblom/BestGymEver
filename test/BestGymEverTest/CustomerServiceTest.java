package BestGymEverTest;

import BestGymEver.Customer;
import BestGymEver.CustomerService;
import BestGymEver.FileManager;
import BestGymEver.MembershipStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerServiceTest {
    private CustomerService customerService;
    private Customer customer;
    private FileManager fileManager;
    private File tempCustomerFile;
    private File tempTrainingLogFile;

    @BeforeEach
    public void setUp() throws IOException {
        tempCustomerFile = File.createTempFile("./kunddata_test", ".txt");
        tempTrainingLogFile = File.createTempFile("./traningslogg_test", ".txt");

        try (FileWriter writer = new FileWriter(tempCustomerFile)) {
            writer.write("7703021234, Alhambra Aromes\n2024-07-01\n");
            writer.write("8204021234, Bear Belle\n2019-12-02\n");
        }

        fileManager = new FileManager(tempCustomerFile.getAbsolutePath(), tempTrainingLogFile.getAbsolutePath());
        customerService = new CustomerService(fileManager);
    }

    @Test
    public void testFindCustomerByName() {
        customer = customerService.findCustomer("Alhambra Aromes");
        assertNotNull(customer);
        assertEquals("7703021234", customer.getPersonalNumber());
    }

    @Test
    public void testFindCustomerByPersonalNumber() {
        customer = customerService.findCustomer("7703021234");
        assertNotNull(customer);
        assertEquals("Alhambra Aromes", customer.getName());
    }

    @Test
    public void testCheckMembershipCurrentMember() {
        customer = customerService.findCustomer("Alhambra Aromes");
        MembershipStatus status = customerService.checkMembership(customer);
        assertEquals(MembershipStatus.CURRENT_MEMBER, status);
    }

    @Test
    public void testCheckMembershipFormerMember() {
        customer = customerService.findCustomer("Bear Belle");
        MembershipStatus status = customerService.checkMembership(customer);
        assertEquals(MembershipStatus.FORMER_MEMBER, status);
    }

    @Test
    public void testRegisterTraining() throws IOException {
        customer = customerService.findCustomer("Alhambra Aromes");
        assertNotNull(customer);

        String logEntry = customerService.registerTraining(customer, fileManager);
        assertNotNull(logEntry);
        assertTrue(logEntry.contains("Alhambra Aromes"));

        List<String> lines = Files.readAllLines(tempTrainingLogFile.toPath());
        assertEquals(1, lines.size());
        assertEquals(logEntry, lines.get(0));
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempCustomerFile.toPath());
        Files.deleteIfExists(tempTrainingLogFile.toPath());
    }
}
