package com.udacity.hotel.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    static class MenuImpl extends Menu {
        MenuImpl() {
            super(new Scanner(System.in));
        }
    }

    private Menu menu;

    @BeforeEach
    void init() {
        menu = new MenuImpl();
    }

    @ParameterizedTest(name = "[{index}] Input: \"{0}\"")
    @ValueSource(strings = {"2", "02", "2 "})
    void isNumber_true(String num) {
        assertTrue(menu.isNumber(num));
    }

    @ParameterizedTest(name = "[{index}] Input: \"{0}\"")
    @ValueSource(strings = {"a", "A", "!", "2a", "2!", "", "\n"})
    void isNumber_false(String num) {
        assertFalse(menu.isNumber(num));
    }
}
