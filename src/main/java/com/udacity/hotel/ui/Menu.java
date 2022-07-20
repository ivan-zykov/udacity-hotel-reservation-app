package com.udacity.hotel.ui;

import java.util.Scanner;

/**
 * Console UI which serves to provide common methods and dependencies.
 */
abstract class Menu {

    protected final Scanner scanner;

    /**
     * Constructor of this class.
     *
     * @param scanner   scanner object that reads user's input
     */
    public Menu(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * @see <span>Inspired by an article on <a href="https://www.baeldung.com/">baeldung.com</a></span>
     */
    protected boolean isNumber(String strInt) {
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
