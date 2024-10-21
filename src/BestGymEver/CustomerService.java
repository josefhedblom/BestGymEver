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
        if (customer == null) {
            return MembershipStatus.NON_MEMBER;
        }

        if (!customers.contains(customer)) {
            return MembershipStatus.NON_MEMBER;
        }

        LocalDate currentDate = LocalDate.now();
        if (customer.getLastPaymentDate().isAfter(currentDate.minusYears(1))) {
            return MembershipStatus.CURRENT_MEMBER;
        } else {
            return MembershipStatus.FORMER_MEMBER;
        }
    }

    public String registerTraining(Customer customer, FileManager fileManager) {
        return fileManager.logTrainingSession(customer);
    }
}
