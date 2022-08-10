package com.udacity.hotel.ui;

/**
 * Helper class to exit from UI menus prematurely for testing purposes.
 *
 * @author Ivan V. Zykov
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

    /**
     * Enables exiting the app by stubbing this method to return true in tests.
     * Used for sub-methods to prevent interfering with {@link ExitHelper#exit()}.
     *
     * @return  boolean, always false for prod env
     */
    public boolean exitNested() {
        return false;
    }
}