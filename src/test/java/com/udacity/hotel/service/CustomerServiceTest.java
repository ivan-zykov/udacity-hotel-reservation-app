package com.udacity.hotel.service;

import com.udacity.hotel.model.Customer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    private static CustomerService customerService;

    private static String firstName;
    private static String lastName;
    private static String email;


    @BeforeAll
    static void initAll() {
        firstName = "I";
        lastName = "Z";
        email = "i@z.com";
        customerService = CustomerService.getInstance();
        customerService.addCustomer(email, firstName, lastName);
    }

    @BeforeEach
    void init() {
    }

    @Test
    void getInstance() {
        CustomerService customerServiceOther = CustomerService.getInstance();
        assertSame(customerServiceOther, customerService);
    }

    /**
     * Customer is added in {@link CustomerServiceTest#initAll()} method.
     * Also tests getting a customer.
     */
    @Test
    void addCustomer_getCustomer_OK() {
        Customer customer = new Customer(firstName, lastName, email);
        assertEquals(customer, customerService.getCustomer(email));
    }

    @Test
    void addCustomer_exists_exception() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> customerService.addCustomer(email, firstName, lastName)
        );
        assertEquals("Customer with this email is already registered.", exception.getMessage());
    }

    @Test
    void getCustomer_notRegistered() {
        assertNull(customerService.getCustomer("a@b.com"));
    }

    @Test
    void getAllCustomers() {
        String emailJr = "j@r.com";
        customerService.addCustomer(emailJr, "J", "R");

        Collection<Customer> allCustomers = customerService.getAllCustomers();

        Customer iZ = customerService.getCustomer(email);
        Customer jR = customerService.getCustomer(emailJr);
        assertAll(
                () -> assertTrue(allCustomers.contains(iZ)),
                () -> assertEquals(2, allCustomers.size()),
                () -> assertTrue(allCustomers.contains(jR))
        );
    }
}
