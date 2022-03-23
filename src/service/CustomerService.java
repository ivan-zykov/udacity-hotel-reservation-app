package service;

import model.Customer;

import java.util.*;

public final class CustomerService {

    private static CustomerService INSTANCE;

    private final Map<String, Customer> customers;

    private CustomerService() {
        this.customers = new HashMap<>();
    }

    public static CustomerService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CustomerService();
        }

        return INSTANCE;
    }

    public void addCustomer(String email, String firstName, String lastName) {
        Customer newCustomer = new Customer(firstName, lastName, email);
        if (customers.containsKey(email)) {
            throw new IllegalArgumentException("Customer with this email is " +
                    "already registered.");
        } else {
            customers.put(email, newCustomer);
        }
    }

    public Customer getCustomer(String customerEmail) {
        if (this.customers.containsKey(customerEmail)) {
            return this.customers.get(customerEmail);
        }

        return null;
    }

    public Collection<Customer> getAllCustomers() {
        List<Customer> allCustomers = new ArrayList<>();
        for (String email: this.customers.keySet()) {
            allCustomers.add(this.customers.get(email));
        }
        return allCustomers;
    }
}
