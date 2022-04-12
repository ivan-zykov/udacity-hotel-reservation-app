package ui;

import java.util.Scanner;

/**
 * Serves to provide common methods and dependencies
 */
abstract class Menu {

    protected final Scanner scanner;

    public Menu(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Implementation inspired by example on www.baeldung.com
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
