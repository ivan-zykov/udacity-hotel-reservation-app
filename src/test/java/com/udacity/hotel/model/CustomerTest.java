package com.udacity.hotel.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;

    @BeforeEach
    public void init() {
        customer = new Customer("I", "Z", "i@z.com");
    }

    @ParameterizedTest(name = "[{index}] Invalid email: {0}")
    @MethodSource("provide_invalidEmails")
    public void constructor_invalidEmail_exception(String email) {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Customer("I", "Z", email)
        );
        assertEquals("Email is of wrong format. Please correct your email.", exception.getMessage());
    }

    private static Stream<Arguments> provide_invalidEmails() {
        return Stream.of(
                Arguments.of("@z.com"),
                Arguments.of("iz.com"),
                Arguments.of("i@.com"),
                Arguments.of("i@zcom"),
                Arguments.of("i@z.")
        );
    }

    @Test
    public void toStringTest() {
        assertEquals("Customer I Z, i@z.com.", customer.toString());
    }

    /**
     * Check the difference between first characters of compared emails which is returned by compareTo method.
     */
    @ParameterizedTest(name = "[{index}] email 1: i@z.com, email 2: {0}")
    @MethodSource("provide_compareToTest")
    public void compareToTest(String emailOther, int expectedComparison) {
        var customerOther = new Customer("A", "B", emailOther);
        assertEquals(expectedComparison, customer.compareTo(customerOther));
    }

    private static Stream<Arguments> provide_compareToTest() {
        return Stream.of(
                Arguments.of("a@z.com", 8),
                Arguments.of("i@z.com", 0),
                Arguments.of("z@z.com", -17)
        );
    }
}
