package model;

import java.util.regex.Pattern;

public class Customer implements Comparable<Customer> {

    final private String firstName, lastName, email;

    public Customer(String firstName, String lastName, String email) {
        if (isValidEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email is of wrong format. " +
                    "Please correct your email.");
        }
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Customer " + firstName + " " + lastName + ", " + email + ".";
    }

    @Override
    public int compareTo(Customer customer) {
        return this.email.compareTo(customer.getEmail());
    }

    private boolean isValidEmail(String email) {
        final String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        return emailPattern.matcher(email).matches();
    }
}
