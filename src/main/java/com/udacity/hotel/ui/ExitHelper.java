package com.udacity.hotel.ui;

/**
 * Helper class to exit from UI menus prematurely for testing purposes.
 */
public class ExitHelper {
    /**
     * Enables exiting the app by stubbing this method to return true in tests.
     *
     * @return  boolean, always false for prod env
     */
    public boolean exit() {
        return false;
    }
}
