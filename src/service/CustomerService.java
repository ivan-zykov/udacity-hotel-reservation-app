package service;

import model.Customer;

import java.util.*;

public class CustomerService {

    private static CustomerService INSTANCE;

    private Map<String, Customer> customers;

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
        try {
            Customer newCustomer = new Customer(firstName, lastName, email);
            if (customers.containsKey(email)) {
                // TODO: display error and retry
                System.out.println("Customer with this email is already registered.");
            } else {
                customers.put(email, newCustomer);
            }
        } catch (IllegalArgumentException ex) {
            // TODO: display error and let user reenter email
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public Customer getCustomer(String customerEmail) {
        if (this.customers.containsKey(customerEmail)) {
            return this.customers.get(customerEmail);
        } else {
            // TODO: finish it
            System.out.println("Not found customer with this email. Please register.");
            return null;
        }
    }

    public Collection<Customer> getAllCustomers() {
        List<Customer> allCustomers = new ArrayList<>();
        for (String email: this.customers.keySet()) {
            allCustomers.add(this.customers.get(email));
        }
        return allCustomers;
    }
}
