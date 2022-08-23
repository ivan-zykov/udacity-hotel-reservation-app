package com.udacity.hotel.ui;

/**
 * Prints to the console using System.out.println().
 */
public final class ConsolePrinterImpl implements ConsolePrinter {

    /**
     * Prints supplied object to the console using System.out.println()
     *
     * @param text  object of generic type to print
     * @param <T>   type of the object to print
     */
    @Override
    public <T> void print(T text) {
        System.out.println(text);
    }
}
