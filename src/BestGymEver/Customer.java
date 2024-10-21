package BestGymEver;

import java.time.LocalDate;

public class Customer {
    private String personalNumber;
    private String name;
    private LocalDate lastPaymentDate;

    public Customer(String personalNumber, String name, LocalDate lastPaymentDate) {
        this.personalNumber = personalNumber;
        this.name = name;
        this.lastPaymentDate = lastPaymentDate;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public String getName() {
        return name;
    }

    public LocalDate getLastPaymentDate() {
        return lastPaymentDate;
    }
}
