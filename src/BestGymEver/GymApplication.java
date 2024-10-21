package BestGymEver;

import javax.swing.*;

public class GymApplication {
    private FileManager fileManager;
    private CustomerService customerService;

    public GymApplication(String customerFilePath, String trainingLogFilePath) {
        this.fileManager = new FileManager(customerFilePath, trainingLogFilePath);
        this.customerService = new CustomerService(fileManager);
    }

    public void run() {
        boolean running = true;

        while (running) {

            int choice = showMenu();

            if (choice == 1) {
                JOptionPane.showMessageDialog(null, "Programmet avslutas.");
                running = false;
            }

            if (choice == 0) {
                String input = JOptionPane.showInputDialog(null, "Ange kundens personnummer eller namn:");
                checkInput(input);
            }
        }
    }

    public int showMenu(){
        String[] options = {"Sök kund", "Avsluta"};
        return JOptionPane.showOptionDialog(
                null,
                "Välj ett alternativ:",
                "BestGymEver",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
    }

    public void checkInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Inget personnummer eller namn angavs.");
        } else {
            Customer customer = customerService.findCustomer(input);
            membership(customer);
        }
    }

    public void membership(Customer customer) {
        if (customer != null) {
            MembershipStatus status = customerService.checkMembership(customer);

            switch (status) {
                case CURRENT_MEMBER:
                    String logEntry = customerService.registerTraining(customer, fileManager);
                    JOptionPane.showMessageDialog(null, customer.getName() + " är en nuvarande medlem.\nTräningslogg: " + logEntry);
                    break;
                case FORMER_MEMBER:
                    JOptionPane.showMessageDialog(null, customer.getName() + " är en före detta medlem. Betala årsavgiften för att återuppta medlemskapet.");
                    break;
                case NON_MEMBER:
                    JOptionPane.showMessageDialog(null, customer.getName() + " är obehörig och har aldrig varit medlem.");
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Personen finns inte i filen och är obehörig.");
        }
    }


    public static void main(String[] args) {
        String customerFilePath = "src/BestGymEver/Data/customerData.txt";
        String trainingLogFilePath = "src/BestGymEver/Data/traningslogg.txt";

        new GymApplication(customerFilePath, trainingLogFilePath).run();
    }
}
