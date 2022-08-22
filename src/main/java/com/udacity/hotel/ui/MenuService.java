package com.udacity.hotel.ui;

import java.util.Scanner;

/**
 * A parent class for menu services.
 *
 * @author Ivan V. Zykov
 */
abstract class MenuService {

    final Scanner scanner;

    /**
     * Constructor of this class.
     *
     * @param scanner   scanner object that reads user's input
     */
    MenuService(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Prints menu to the console.
     */
    public abstract void printMenu();

    // TODO: move it to a dedicated ConsolePrinter class
    <T> void print(T text) {
        System.out.println(text);
    }

    boolean isNumber(String strInt) {
        if (strInt == null) {
            return false;
        }
        try {
            Double.parseDouble(strInt);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
