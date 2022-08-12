package com.udacity.hotel.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MenuManagerTest {

    static class MenuManagerImpl extends MenuManager {
        MenuManagerImpl() {
            super(new Scanner(System.in));
        }
    }

    private MenuManager menuManager;

    @BeforeEach
    void init() {
        menuManager = new MenuManagerImpl();
    }

    @ParameterizedTest(name = "[{index}] Input: \"{0}\"")
    @ValueSource(strings = {"2", "02", "2 "})
    void isNumber_true(String num) {
        assertTrue(menuManager.isNumber(num));
    }

    @ParameterizedTest(name = "[{index}] Input: \"{0}\"")
    @ValueSource(strings = {"a", "A", "!", "2a", "2!", "", "\n"})
    void isNumber_false(String num) {
        assertFalse(menuManager.isNumber(num));
    }
}
