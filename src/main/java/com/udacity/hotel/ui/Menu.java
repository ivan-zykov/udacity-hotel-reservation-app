package com.udacity.hotel.ui;

import java.util.Scanner;

/**
 * Console UI which serves to provide common methods and dependencies.
 *
 * @author Ivan V. Zykov
 */
abstract class Menu {

    final Scanner scanner;

    /**
     * Constructor of this class.
     *
     * @param scanner   scanner object that reads user's input
     */
    Menu(Scanner scanner) {
        this.scanner = scanner;
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
