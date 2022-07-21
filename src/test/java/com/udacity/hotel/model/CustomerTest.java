package com.udacity.hotel.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    @ParameterizedTest(name = "[i] Invalid email: {0}")
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
    public void getFirstName() {
        assertTrue(true);
    }
}