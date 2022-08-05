package com.udacity.hotel.ui;

import com.udacity.hotel.api.HotelResource;
import com.udacity.hotel.model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * For findAndReserveARoom() method.
 */
@ExtendWith(MockitoExtension.class)
class MainMenuFindAndReserveARoomTest {

    private MainMenu mainMenu;

    private final static int YEAR_NOW = 2022;
    private final static int MONTH_NOW = Calendar.AUGUST;
    private final static int DAY_NOW = 8;
    private static ByteArrayOutputStream outContent;
    private Date nowStubbed;
    private DateFormat dateFormat;

    @Mock
    private AdminMenu adminMenu;
    @Mock
    private HotelResource hotelResource;
    @Mock
    private Scanner scanner;
    @Mock
    private ExitHelper exitHelper;

    @BeforeAll
    static void initAll() {
        // Overtake printing to the console
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @BeforeEach
    void init() {
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        // Stub current date
        Calendar cal = Calendar.getInstance();
        cal.set(YEAR_NOW, MONTH_NOW, DAY_NOW);
        nowStubbed = cal.getTime();
        mainMenu = new MainMenu(adminMenu, hotelResource, scanner, dateFormat, exitHelper, nowStubbed);
    }

    @AfterAll
    static void cleanAll() {
        // Restore the standard out
        System.setOut(System.out);
    }

    @Test
    void findAndReserveARoom_invalidCheckIn() {
        // Stub user's input:
        when(scanner.nextLine()).thenReturn("1", "a");

        // Force exiting the app after incorrect input
        when(exitHelper.exitNested()).thenReturn(true);

        // Run this test
        mainMenu.open();

        assertAll(
            () -> assertTrue(outContent.toString().contains("Enter check-in date in format mm/dd/yyyy " +
                    "Example: 05/30/2022")),
            () -> assertTrue(outContent.toString().endsWith("Renter the date in format mm/dd/yyyy\r\n"))
        );
    }

    @Test
    void findAndReserveARoom_parseExceptionCheckIn() {
        // Stub user's input:
        when(scanner.nextLine()).thenReturn("1", "05/30/2023");

        // Stub failing to parse input date
        DateFormat dateFormat = mock(SimpleDateFormat.class);
        try {
            when(dateFormat.parse(any()))
                .thenReturn(null)
                .thenThrow(new ParseException("test error", 0));
        }
        catch (ParseException e) {/*obsolete*/}

        // Force exiting the app after incorrect input
        when(exitHelper.exitNested()).thenReturn(true);

        // Instantiate SUT with stubbed dateFormat
        var mainMenu = new MainMenu(adminMenu, hotelResource, scanner, dateFormat, exitHelper, nowStubbed);

        // Run this test
        mainMenu.open();

        assertAll(
            () -> assertTrue(outContent.toString().contains("Enter check-in date in format mm/dd/yyyy " +
                    "Example: 05/30/2022")),
            () -> assertTrue(outContent.toString().endsWith("Try entering the date again\r\n"))
        );
    }

    @Test
    void findAndReserveARoom_invalidCheckOut() {
        // Stub user's input:
        when(scanner.nextLine()).thenReturn("1", "05/30/2023", "a");

        // Force exiting the app after incorrect input
        when(exitHelper.exitNested()).thenReturn(true);

        // Run this test
        mainMenu.open();

        assertAll(
            () -> assertTrue(outContent.toString().contains("Enter check-out date in format mm/dd/yyyy " +
                    "Example: 05/30/2022")),
            () -> assertTrue(outContent.toString().endsWith("Renter the date in format mm/dd/yyyy\r\n"))
        );
    }

    @Test
    void findAndReserveARoom_parseExceptionCheckOut() {
        // Stub user's input:
        when(scanner.nextLine()).thenReturn("1", "05/30/2023", "06/10/2023");

        // Stub failing to parse input date
        DateFormat dateFormat = mock(SimpleDateFormat.class);
        Calendar cal = Calendar.getInstance();
        cal.set(2099, Calendar.JUNE, 30);
        Date checkIn = cal.getTime();
        try {
            when(dateFormat.parse(any()))
                    .thenReturn(null)
                    .thenReturn(checkIn)
                    .thenReturn(null)
                    .thenThrow(new ParseException("test error", 0));
        } catch (ParseException e) {/*obsolete*/}

        // Force exiting the app after incorrect input
        when(exitHelper.exitNested()).thenReturn(true);

        // Instantiate SUT with stubbed dateFormat
        var mainMenu = new MainMenu(adminMenu, hotelResource, scanner, dateFormat, exitHelper, nowStubbed);

        // Run this test
        mainMenu.open();

        assertAll(
            () -> assertTrue(outContent.toString().contains("Enter check-out date in format mm/dd/yyyy " +
                    "Example: 05/30/2022")),
            () -> assertTrue(outContent.toString().endsWith("Try entering the date again\r\n"))
        );
    }

    @Test
    void findAndReserveARoom_checkInAfterCheckout() {
        // Stub user's input:
        when(scanner.nextLine()).thenReturn("1", "06/10/2023", "05/30/2023");

        // Force exiting the app after incorrect input
        when(exitHelper.exit()).thenReturn(true);

        // Run this test
        mainMenu.open();

        assertTrue(outContent.toString().endsWith("Your check-in date is later than checkout " +
                "date. Please reenter dates\r\n"));
    }

    @ParameterizedTest(name = "[{index}] Check-in: {0}, check-out: {1}, Now: {2}")
    @MethodSource("provide_datesInThePast")
    // String nowTestName is used in test's name
    void findAndReserveARoom_datesInThePast(String checkIn, String checkOut, String nowTestName) {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("1", checkIn, checkOut);

        // Force exiting the app after incorrect input
        when(exitHelper.exit()).thenReturn(true);

        // Run this test
        mainMenu.open();

        assertTrue(outContent.toString().endsWith("At least one of the dates is in the past. " +
                "Please reenter dates\r\n"));
    }

    private static Stream<Arguments> provide_datesInThePast() {
        String nowStr = MONTH_NOW + "/" + DAY_NOW + "/" + YEAR_NOW;
        return Stream.of(
                Arguments.of("01/01/2019", "05/30/2023", nowStr),
                Arguments.of("01/01/2019", "01/20/2019", nowStr)
        );
    }

    @ParameterizedTest(name = "[{index}] Message: {1}")
    @MethodSource("provide_roomsAndInfoMessage")
    void findAndReserveARoom_noRoomsOnTheDates_backToMainMenu(
            Collection<IRoom> roomsNextDays, String message) {
        // Stub user's input
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        when(scanner.nextLine()).thenReturn("1", checkInString, checkOutString, "5");

        // Prepare dates
        Date checkInDate = null;
        Date checkOutDate = null;
        dateFormat.setLenient(false);
        try {
            checkInDate = dateFormat.parse(checkInString);
            checkOutDate = dateFormat.parse(checkOutString);
        } catch (ParseException e) {/*obsolete*/}

        // Stub finding no available rooms for initial dates
        when(hotelResource.findARoom(checkInDate, checkOutDate)).thenReturn(List.of());

        // Shift dates by 7 days
        Calendar cal = Calendar.getInstance();
        assertNotNull(checkInDate);
        cal.setTime(checkInDate);
        cal.add(Calendar.DATE, 7);
        Date checkInDateNext = cal.getTime();

        assertNotNull(checkOutDate);
        cal.setTime(checkOutDate);
        cal.add(Calendar.DATE, 7);
        Date checkOutDateNext = cal.getTime();

        // Stub available rooms for the next seven days
        when(hotelResource.findARoom(checkInDateNext, checkOutDateNext)).thenReturn(roomsNextDays);

        // Run this test
        mainMenu.open();

        assertAll(
            () -> assertTrue(outContent.toString().contains("No rooms found for selected dates. Trying to find" +
                    " a room in the next 7 days")),
            () -> assertTrue(outContent.toString().contains(message)),
            // Redirecting back to the main menu and exiting
            () -> assertTrue(outContent.toString().endsWith("Exiting the app\r\n"))
        );
    }

    private static Stream<Arguments> provide_roomsAndInfoMessage() {
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        Collection<IRoom> roomsNextDays = List.of(room);
        return Stream.of(
                Arguments.of(List.of(), "No free rooms in the next 7 days found. Try " +
                        "different dates"),
                Arguments.of(roomsNextDays, "You can book following rooms")
        );
    }

    @Test
    void findAndReserveARoom_noBooking_backToMainMenu() {
        // Stub user's input with no to question about booking a room
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        when(scanner.nextLine()).thenReturn("1", checkInString, checkOutString, "n", "5");

        // Prepare dates
        Date checkInDate = null;
        Date checkOutDate = null;
        dateFormat.setLenient(false);
        try {
            checkInDate = dateFormat.parse(checkInString);
            checkOutDate = dateFormat.parse(checkOutString);
        } catch (ParseException e) {/*obsolete*/}

        // Stub finding available room
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        when(hotelResource.findARoom(checkInDate, checkOutDate)).thenReturn(List.of(room));

        // Run this test
        mainMenu.open();

        assertAll(
            () -> assertTrue(outContent.toString().contains("Would you like to book one of the rooms above? " +
                    "(y/n)")),
            // Redirecting back to the main menu and exiting
            () -> assertTrue(outContent.toString().endsWith("Exiting the app\r\n"))
        );
    }

    @Test
    void findAndReserveARoom_invalidAnswerAboutBooking() {
        // Stub user's input with invalid answer about booking a room
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        when(scanner.nextLine()).thenReturn("1", checkInString, checkOutString, "a");

        // Prepare dates
        Date checkInDate = null;
        Date checkOutDate = null;
        dateFormat.setLenient(false);
        try {
            checkInDate = dateFormat.parse(checkInString);
            checkOutDate = dateFormat.parse(checkOutString);
        } catch (ParseException e) {/*obsolete*/}

        // Stub finding available room
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        when(hotelResource.findARoom(checkInDate, checkOutDate)).thenReturn(List.of(room));

        // Force exiting the app after incorrect input
        when(exitHelper.exit()).thenReturn(true);

        // Run this test
        mainMenu.open();

        assertAll(
            () -> assertTrue(outContent.toString().contains("Would you like to book one of the rooms above? " +
                    "(y/n)")),
            () -> assertTrue(outContent.toString().endsWith("Enter \"y\" for yes or \"n\" for no\r\n"))
        );
    }

    @Test
    void findAndReserveARoom_noUserAccount_backToMainMenu() {
        // Stub user's input
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        when(scanner.nextLine()).thenReturn("1", checkInString, checkOutString, "y", "n", "5");

        // Prepare dates
        Date checkInDate = null;
        Date checkOutDate = null;
        dateFormat.setLenient(false);
        try {
            checkInDate = dateFormat.parse(checkInString);
            checkOutDate = dateFormat.parse(checkOutString);
        } catch (ParseException e) {/*obsolete*/}

        // Stub finding available room
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        when(hotelResource.findARoom(checkInDate, checkOutDate)).thenReturn(List.of(room));

        // Run this test
        mainMenu.open();

        assertAll(
            () -> assertTrue(outContent.toString().contains("Do you have an account?")),
            () -> assertTrue(outContent.toString().contains("Please create an account in main menu")),
            () -> assertTrue(outContent.toString().endsWith("Exiting the app\r\n"))
        );
    }

    @Test
    void findAndReserveARoom_invalidAnswerAboutAccount() {
        // Stub user's input with invalid answer about having and account
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        when(scanner.nextLine()).thenReturn("1", checkInString, checkOutString, "y", "a");

        // Prepare dates
        Date checkInDate = null;
        Date checkOutDate = null;
        dateFormat.setLenient(false);
        try {
            checkInDate = dateFormat.parse(checkInString);
            checkOutDate = dateFormat.parse(checkOutString);
        } catch (ParseException e) {/*obsolete*/}

        // Stub finding available room
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        when(hotelResource.findARoom(checkInDate, checkOutDate)).thenReturn(List.of(room));

        // Force exiting the app after incorrect input
        when(exitHelper.exit()).thenReturn(true);

        // Run this test
        mainMenu.open();

        assertAll(
            () -> assertTrue(outContent.toString().contains("Do you have an account?")),
            () -> assertTrue(outContent.toString().endsWith("Enter \"y\" for yes or \"n\" for no\r\n"))
        );
    }

    @Test
    void findAndReserveARoom_customerDoesNotExist_backToMainMenu() {
        // Stub user's input
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        String email = "i@z.com";
        when(scanner.nextLine()).thenReturn("1", checkInString, checkOutString, "y", "y",
                email, "5");

        // Prepare dates
        Date checkInDate = null;
        Date checkOutDate = null;
        dateFormat.setLenient(false);
        try {
            checkInDate = dateFormat.parse(checkInString);
            checkOutDate = dateFormat.parse(checkOutString);
        } catch (ParseException e) {/*obsolete*/}

        // Stub finding available room
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        when(hotelResource.findARoom(checkInDate, checkOutDate)).thenReturn(List.of(room));

        // Stub that customer is not registered
        when(hotelResource.getCustomer(email)).thenReturn(null);

        // Run this test
        mainMenu.open();

        assertAll(
            () -> assertTrue(outContent.toString().contains("Please enter your email")),
            () -> assertTrue(outContent.toString().contains("You are still not registered with this email. " +
                    "Please create an account")),
            () -> assertTrue(outContent.toString().endsWith("Exiting the app\r\n"))
        );
    }

    @ParameterizedTest(name = "[{index}] Picked room: {0}, Message: {1}")
    @MethodSource("provide_roomNumberAndMessage")
    void findAndReserveARoom_invalidRoomNumber(String roomNumber, String message) {
        // Stub user's input: invalid room number to book
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        String email = "i@z.com";
        when(scanner.nextLine()).thenReturn("1", checkInString, checkOutString, "y", "y",
                email, roomNumber);

        // Prepare dates
        Date checkInDate = null;
        Date checkOutDate = null;
        dateFormat.setLenient(false);
        try {
            checkInDate = dateFormat.parse(checkInString);
            checkOutDate = dateFormat.parse(checkOutString);
        } catch (ParseException e) {/*obsolete*/}

        // Stub finding available room
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        when(hotelResource.findARoom(checkInDate, checkOutDate)).thenReturn(List.of(room));

        // Stub that customer exists
        var customer = new Customer("I", "Z", email);
        when(hotelResource.getCustomer(email)).thenReturn(customer);

        // Force exiting the app after incorrect input
        when(exitHelper.exit()).thenReturn(true);

        // Run this test
        mainMenu.open();

        assertAll(
            () -> assertTrue(outContent.toString().contains("Please enter which room to book")),
            () -> assertTrue(outContent.toString().endsWith(message + "\r\n"))
        );
    }

    private static Stream<Arguments> provide_roomNumberAndMessage() {
        return Stream.of(
                Arguments.of("a", "Room number should be an integer number"),
                Arguments.of("2", "The room you picked is actually not " +
                        "available. Please enter a room number from the the list above")
        );
    }

    @Test
    void findAndReserveARoom_success_backToMainMenu() {
        // Stub user's input
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        String email = "i@z.com";
        String roomNumberToBook = "1";
        when(scanner.nextLine()).thenReturn("1", checkInString, checkOutString, "y", "y",
                email, roomNumberToBook, "5");

        // Prepare dates
        Date checkInDate = null;
        Date checkOutDate = null;
        dateFormat.setLenient(false);
        try {
            checkInDate = dateFormat.parse(checkInString);
            checkOutDate = dateFormat.parse(checkOutString);
        } catch (ParseException e) {/*obsolete*/}

        // Stub finding available room
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        when(hotelResource.findARoom(checkInDate, checkOutDate)).thenReturn(List.of(room));

        // Stub that customer exists
        var customer = new Customer("I", "Z", email);
        when(hotelResource.getCustomer(email)).thenReturn(customer);

        // Stub getting the room
        when(hotelResource.getRoom(roomNumberToBook)).thenReturn(room);

        // Stub making a reservations
        var reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        when(hotelResource.bookARoom(email, room, checkInDate, checkOutDate)).thenReturn(reservation);

        // Run this test
        mainMenu.open();

        assertAll(
            () -> assertTrue(outContent.toString().contains(reservation.toString())),
            () -> assertTrue(outContent.toString().endsWith("Exiting the app\r\n"))
        );
    }
}
