package model;

import java.util.regex.Pattern;

public class Customer {

    private String firstName, lastName, email;

    public Customer(String firstName, String lastName, String email) {
        // Validate email
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        if (emailPattern.matcher(email).matches()) {
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
}
