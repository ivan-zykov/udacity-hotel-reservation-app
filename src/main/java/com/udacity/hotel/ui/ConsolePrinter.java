package com.udacity.hotel.ui;

/**
 * Prints to the console.
 */
public interface ConsolePrinter {

    /**
     * Prints supplied object to the console.
     *
     * @param text  object of generic type to print
     * @param <T>   type of the object to print
     */
    <T> void print(T text);
}
