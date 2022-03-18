package service;

import model.Customer;

import java.util.ArrayList;
import java.util.Collection;

public class CustomerServiceTest {

    public static void main(String[] args) {

        System.out.println("--- Start CustomerService test");
        System.out.println();

        // Add one customer and get it
        CustomerService serv1 = CustomerService.getInstance();
        String emailVanya = "vanya@bk.ru";
        serv1.addCustomer(emailVanya, "Vanya", "Zykov");
        Customer vanya = serv1.getCustomer(emailVanya);
        System.out.println(vanya);
        System.out.println();

        // Check that singleton works: i.e. only one only one instance exists
        CustomerService serv2 = CustomerService.getInstance();
        serv2.addCustomer("jenny@mail.com", "Jenny", "Red");
        Collection<Customer> allCustomers = new ArrayList<>();
        allCustomers = serv2.getAllCustomers();
        for (Customer aCustomer: allCustomers) {
            System.out.println(aCustomer);
        }
        System.out.println();

        // Adding existing email
        try {
            serv1.addCustomer(emailVanya, "Jack", "Zet");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        System.out.println();

        // Handling adding customer with invalid email
        try {
            serv1.addCustomer("invalidEmail", "Jack", "Zet");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        System.out.println();

        // Handling getting unregistered customer
        try {
            serv1.getCustomer("notReg");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        System.out.println();

        System.out.println("--- End CustomerService test");
    }
}
