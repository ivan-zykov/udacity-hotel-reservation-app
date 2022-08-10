package com.udacity.hotel.service;

import com.udacity.hotel.model.Customer;

import java.util.*;

/**
 * A singleton service to keep track of {@link Customer}s, record new and retrieve existing ones.
 *
 * @author Ivan V. Zykov
 */
public final class CustomerService {

    private static CustomerService INSTANCE;

    private final Map<String, Customer> customers;

    private CustomerService() {
        this.customers = new HashMap<>();
    }

    /**
     * Returns the instance of this singleton service.
     *
     * @return      customerService representing this service
     */
    public static CustomerService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CustomerService();
        }

        return INSTANCE;
    }

    /**
     * Creates a new {@link Customer} and records it if no customer already recorded with the provided email.
     *
     * @param email                     string, email of the customer
     * @param firstName                 string, first name of the customer
     * @param lastName                  string, last name of the customer
     * @throws IllegalArgumentException if a customer with the supplied email was already recorded
     */
    public void addCustomer(String email, String firstName, String lastName) {
        Customer newCustomer = new Customer(firstName, lastName, email);
        if (customers.containsKey(email)) {
            throw new IllegalArgumentException("Customer with this email is " +
                    "already registered.");
        } else {
            customers.put(email, newCustomer);
        }
    }

    /**
     * Returns a customer object if a customer with provided email has already been registered.
     *
     * @param customerEmail string, customer's email
     * @return              customer identified by provided email
     */
    public Customer getCustomer(String customerEmail) {
        if (this.customers.containsKey(customerEmail)) {
            return this.customers.get(customerEmail);
        }

        return null;
    }

    /**
     * Returns all customers registered in the app.
     *
     * @return      collection of customers
     */
    public Collection<Customer> getAllCustomers() {
        List<Customer> allCustomers = new ArrayList<>();
        for (String email: this.customers.keySet()) {
            allCustomers.add(this.customers.get(email));
        }
        return allCustomers;
    }
}
