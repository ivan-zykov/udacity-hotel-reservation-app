package com.udacity.hotel.ui;

import com.udacity.hotel.model.Customer;
import com.udacity.hotel.model.Room;
import com.udacity.hotel.model.RoomType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ConsolePrinterImplTest {

    private ConsolePrinter consolePrinter;

    private static ByteArrayOutputStream outContent;

    @BeforeAll
    static void initAll() {
        // Overtake printing to the console
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @BeforeEach
    void init() {
        consolePrinter = new ConsolePrinterImpl();
    }

    @ParameterizedTest(name = "[{index}] Object to print: {0}")
    @MethodSource("provideObjectsToPrint")
    <T> void print(T object) {
        // Run this test
        consolePrinter.print(object);

        assertTrue(outContent.toString().endsWith(object.toString() + System.lineSeparator()));
    }

    private static Stream<Arguments> provideObjectsToPrint() {
        var customer = new Customer("I", "Z", "i@z.com");
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        return Stream.of(
                Arguments.of("Test string"),
                Arguments.of(customer),
                Arguments.of(room)
        );
    }
}