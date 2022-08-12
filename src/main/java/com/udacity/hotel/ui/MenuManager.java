package com.udacity.hotel.ui;

import java.util.Scanner;

/**
 * A parent class for menu managers.
 *
 * @author Ivan V. Zykov
 */
// TODO: rethink the purpose if this. Replace with interface?
abstract class MenuManager {

    final Scanner scanner;

    /**
     * Constructor of this class.
     *
     * @param scanner   scanner object that reads user's input
     */
    MenuManager(Scanner scanner) {
        this.scanner = scanner;
    }

    // TODO: del after refactoring MainMenuManager
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
