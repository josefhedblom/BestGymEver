package BestGymEver;

import java.time.LocalDate;
import java.util.List;

public class CustomerService {
    private List<Customer> customers;

    public CustomerService(FileManager fileManager) {
        this.customers = fileManager.readCustomerData();
    }

    public Customer findCustomer(String input) {
        for (Customer customer : customers) {
            if (customer.getPersonalNumber().equals(input) || customer.getName().equalsIgnoreCase(input)) {
                return customer;
            }
        }
        return null;
    }

    public MembershipStatus checkMembership(Customer customer) {
        LocalDate currentDate = LocalDate.now();
        if (customer.getLastPaymentDate().isAfter(currentDate.minusYears(1))) {
            return MembershipStatus.CURRENT_MEMBER;
        } else if (customer.getLastPaymentDate().isBefore(currentDate.minusYears(1))) {
            return MembershipStatus.FORMER_MEMBER;
        } else {
            return MembershipStatus.NON_MEMBER;
        }
    }

    public String registerTraining(Customer customer, FileManager fileManager) {
        return fileManager.logTrainingSession(customer);
    }
}
