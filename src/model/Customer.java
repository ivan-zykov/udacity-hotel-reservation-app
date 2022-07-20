package model;

import java.util.regex.Pattern;

/**
 * A customer who uses the app to search for a free {@link model.IRoom} and book one.
 * Holds basic information about a customer.
 */
public class Customer implements Comparable<Customer> {

    final private String firstName, lastName, email;

    /**
     * Constructor of this class.
     *
     * @param firstName                 string, first name of this customer
     * @param lastName                  string, last name of this customer
     * @param email                     string, email of this customer
     * @throws IllegalArgumentException if the supplied email is of wrong format
     */
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

    /**
     * Returns basic data of this customer and formats its string representation.
     *
     * @return      string representation of the customer's data
     */
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
