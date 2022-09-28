package com.udacity.hotel.service;

import com.udacity.hotel.model.Customer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    private static CustomerService customerService;

    private String firstName;
    private String lastName;
    private String email;

    @BeforeAll
    static void initAll() {
        customerService = CustomerService.getInstance();
    }

    @BeforeEach
    void init() throws NoSuchFieldException, IllegalAccessException {
        firstName = "I";
        lastName = "Z";
        email = "i@z.com";

        // Reset the SUT which is a singleton
        Field customers = CustomerService.class.getDeclaredField("customers");
        customers.setAccessible(true);
        customers.set(customerService, new HashMap<>());
    }

    @Test
    void getInstance() {
        CustomerService customerServiceOther = CustomerService.getInstance();
        assertSame(customerServiceOther, customerService);
    }

    @Test
    void addCustomer_getCustomer_OK() {
        customerService.addCustomer(email, firstName, lastName);

        Customer customer = new Customer(firstName, lastName, email);
        assertEquals(customer, customerService.getCustomer(email));
    }

    @Test
    void addCustomer_exists_exception() {
        customerService.addCustomer(email, firstName, lastName);

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
        customerService.addCustomer(email, firstName, lastName);
        String emailJr = "j@r.com";
        customerService.addCustomer(emailJr, "J", "R");

        Collection<Customer> allCustomers = customerService.getAllCustomers();

        Customer iZ = customerService.getCustomer(email);
        Customer jR = customerService.getCustomer(emailJr);
        assertAll(
                () -> assertEquals(2, allCustomers.size()),
                () -> assertTrue(allCustomers.contains(iZ)),
                () -> assertTrue(allCustomers.contains(jR))
        );
    }
}
