package com.udacity.hotel.ui;

import com.udacity.hotel.api.HotelResource;
import com.udacity.hotel.model.*;
import org.junit.jupiter.api.*;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainMenuServiceFindAndReserveARoomTest {

    private MainMenuService mainMenuService;

    private final static int YEAR_NOW = 2022;
    private final static int MONTH_NOW = Calendar.AUGUST;
    private final static int DAY_NOW = 8;
    private static ByteArrayOutputStream outContent;
    private Date nowStubbed;
    private DateFormat dateFormat;
    private ReservationFactory reservationFactory;

    @Mock
    HotelResource hotelResource;
    @Mock
    Scanner scanner;
    @Mock
    ExitHelper exitHelper;

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
        mainMenuService = new MainMenuService(nowStubbed, hotelResource, scanner, exitHelper, dateFormat);
        reservationFactory = new ReservationFactory();
    }

    @AfterAll
    static void cleanAll() {
        // Restore the standard out
        System.setOut(System.out);
    }

    // FIXME: continue here. Move tests below
    @Test
    void findAndReserveARoom_invalidCheckIn() {
        // Stub user's input:
        when(scanner.nextLine()).thenReturn("a");

        // Force exiting the app after incorrect input
        when(exitHelper.exitNested()).thenReturn(true);

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Enter check-in date in format mm/dd/yyyy " +
                        "Example: 05/30/2022")),
                () -> assertTrue(outContent.toString().endsWith("Renter the date in format mm/dd/yyyy\r\n"))
        );
    }

    @Test
    void findAndReserveARoom_parseExceptionCheckIn() {
        // Stub user's input:
        when(scanner.nextLine()).thenReturn("05/30/2023");

        // Stub failing to parse input date
        DateFormat dateFormatStubbed = mock(SimpleDateFormat.class);
        try {
            when(dateFormatStubbed.parse(any()))
                    .thenReturn(null)
                    .thenThrow(new ParseException("test error", 0));
        }
        catch (ParseException e) {/*obsolete*/}

        // Force exiting the app after incorrect input
        when(exitHelper.exitNested()).thenReturn(true);

        // Instantiate SUT with stubbed dateFormat
        MainMenuService mainMenuService = new MainMenuService(nowStubbed, hotelResource, scanner, exitHelper,
                dateFormatStubbed);

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Enter check-in date in format mm/dd/yyyy " +
                        "Example: 05/30/2022")),
                () -> assertTrue(outContent.toString().endsWith("Try entering the date again\r\n"))
        );
    }
    @Test
    void findAndReserveARoom_checkInIsInThePast() {
        // Stub user's input: check-in date is in the past
        when(scanner.nextLine()).thenReturn("01/01/2019");

        // Force exiting the app after incorrect input
        when(exitHelper.exitNested()).thenReturn(true);

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Enter check-in date in format mm/dd/yyyy " +
                        "Example: 05/30/2022")),
                () -> assertTrue(outContent.toString().endsWith("This date is in the past. " +
                        "Please reenter the date\r\n"))
        );
    }

    @Test
    void findAndReserveARoom_invalidCheckOut() {
        // Stub user's input:
        when(scanner.nextLine()).thenReturn("05/30/2023", "a");

        // Force exiting the app after incorrect input
        when(exitHelper.exitNested()).thenReturn(true);

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Enter check-out date in format mm/dd/yyyy " +
                        "Example: 05/30/2022")),
                () -> assertTrue(outContent.toString().endsWith("Renter the date in format mm/dd/yyyy\r\n"))
        );
    }

    @Test
    void findAndReserveARoom_parseExceptionCheckOut() {
        // Stub user's input:
        when(scanner.nextLine()).thenReturn("05/30/2023", "06/10/2023");

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
//        var mainMenu = new MainMenuManager(adminMenuManager, hotelResource, scanner, dateFormat, exitHelper, nowStubbed);
        var mainMenuService = new MainMenuService(nowStubbed, hotelResource, scanner, exitHelper, dateFormat);

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Enter check-out date in format mm/dd/yyyy " +
                        "Example: 05/30/2022")),
                () -> assertTrue(outContent.toString().endsWith("Try entering the date again\r\n"))
        );
    }

    @Test
    void findAndReserveARoom_checkOutIsInThePast() {
        // Stub user's input: check-out date is in the past
        when(scanner.nextLine()).thenReturn("05/30/2023", "01/20/2019");

        // Force exiting the app after incorrect input
        when(exitHelper.exitNested()).thenReturn(true);

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Enter check-out date in format mm/dd/yyyy " +
                        "Example: 05/30/2022")),
                () -> assertTrue(outContent.toString().endsWith("This date is in the past. " +
                        "Please reenter the date\r\n"))
        );
    }

    @Test
    void findAndReserveARoom_checkInAfterCheckout() {
        // Stub user's input:
        when(scanner.nextLine()).thenReturn("06/10/2023", "05/30/2023");

        // Force exiting the app after incorrect input
        when(exitHelper.exit()).thenReturn(true);

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertTrue(outContent.toString().endsWith("Your check-in date is later than checkout " +
                "date. Please reenter dates\r\n"));
    }

    @ParameterizedTest(name = "[{index}] Message: {1}")
    @MethodSource("provide_roomsAndInfoMessage")
    void findAndReserveARoom_noRoomsOnTheDates(
            Collection<IRoom> roomsNextDays, String message) {
        // Stub user's input
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        when(scanner.nextLine()).thenReturn(checkInString, checkOutString);

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
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> assertTrue(outContent.toString().contains("No rooms found for selected dates. Trying to find" +
                        " a room in the next 7 days")),
                () -> assertTrue(outContent.toString().contains(message))
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
    void findAndReserveARoom_noBooking() {
        // Stub user's input with no to question about booking a room
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        when(scanner.nextLine()).thenReturn(checkInString, checkOutString, "n");

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
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> assertTrue(outContent.toString().contains(room.toString())),
                () -> assertTrue(outContent.toString().endsWith("Would you like to book one of the rooms above? " +
                        "(y/n)\r\n"))
        );
    }

    @Test
    void findAndReserveARoom_invalidAnswerAboutBooking() {
        // Stub user's input with invalid answer about booking a room
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        when(scanner.nextLine()).thenReturn(checkInString, checkOutString, "a");

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
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Would you like to book one of the rooms above? " +
                        "(y/n)")),
                () -> assertTrue(outContent.toString().endsWith("Enter \"y\" for yes or \"n\" for no\r\n"))
        );
    }

    @Test
    void findAndReserveARoom_noUserAccount() {
        // Stub user's input
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        when(scanner.nextLine()).thenReturn(checkInString, checkOutString, "y", "n");

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
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Do you have an account?")),
                () -> assertTrue(outContent.toString().endsWith("Please create an account in main menu\r\n"))
        );
    }

    @Test
    void findAndReserveARoom_invalidAnswerAboutAccount() {
        // Stub user's input with invalid answer about having and account
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        when(scanner.nextLine()).thenReturn(checkInString, checkOutString, "y", "a");

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
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Do you have an account?")),
                () -> assertTrue(outContent.toString().endsWith("Enter \"y\" for yes or \"n\" for no\r\n"))
        );
    }

    @Test
    void findAndReserveARoom_customerDoesNotExist() {
        // Stub user's input
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        String email = "i@z.com";
        when(scanner.nextLine()).thenReturn(checkInString, checkOutString, "y", "y", email);

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
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Please enter your email")),
                () -> assertTrue(outContent.toString().contains("You are still not registered with this email. " +
                        "Please create an account\r\n"))
        );
    }

    @ParameterizedTest(name = "[{index}] Picked room: {0}, Message: {1}")
    @MethodSource("provide_roomNumberAndMessage")
    void findAndReserveARoom_invalidRoomNumber(String roomNumber, String message) {
        // Stub user's input: invalid room number to book
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        String email = "i@z.com";
        when(scanner.nextLine()).thenReturn(checkInString, checkOutString, "y", "y",
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
        mainMenuService.findAndReserveARoom();

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
    void findAndReserveARoom_success() {
        // Stub user's input
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        String email = "i@z.com";
        String roomNumberToBook = "1";
        when(scanner.nextLine()).thenReturn(checkInString, checkOutString, "y", "y",
                email, roomNumberToBook);

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
        var reservation = reservationFactory.create(customer, room, checkInDate, checkOutDate);
        when(hotelResource.bookARoom(email, room, checkInDate, checkOutDate)).thenReturn(reservation);

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertTrue(outContent.toString().endsWith(reservation + "\r\n"));
    }
}
