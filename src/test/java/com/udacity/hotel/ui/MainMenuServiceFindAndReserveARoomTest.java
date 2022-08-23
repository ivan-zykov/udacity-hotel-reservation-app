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

    private Date nowStubbed;
    private DateFormat dateFormat;

    @Mock
    HotelResource hotelResource;
    @Mock
    Scanner scanner;
    @Mock
    ConsolePrinter consolePrinter;

    @BeforeEach
    void init() {
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        // Stub current date
        Calendar cal = Calendar.getInstance();
        cal.set(2022, Calendar.AUGUST, 8);
        nowStubbed = cal.getTime();
        mainMenuService = new MainMenuService(nowStubbed, hotelResource, scanner, dateFormat, consolePrinter);
    }

    @Test
    void findAndReserveARoom_invalidCheckIn() {
        // Stub user's input: invalid check-in date, rest is to terminate the execution
        when(scanner.nextLine()).thenReturn("a", "05/30/2023");

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Enter check-in date in format mm/dd/yyyy " +
                        "Example: 05/30/2022"),
                () -> verify(consolePrinter, times(1)).print("Renter the date in format " +
                        "mm/dd/yyyy")
        );
    }

    @Test
    void findAndReserveARoom_parseExceptionCheckIn() {
        // Stub user's input:
        String dateString = "05/30/2023";
        when(scanner.nextLine()).thenReturn(dateString);

        // Stub failing to parse input date and success afterwards
        Date dateObj = null;
        try {
            dateObj = dateFormat.parse(dateString);
        } catch (ParseException ex) {
            fail("Parsing date string failed in this test.");
        }
        DateFormat dateFormatStubbed = mock(SimpleDateFormat.class);
        try {
            when(dateFormatStubbed.parse(any()))
                    .thenReturn(null)
                    .thenThrow(new ParseException("test error", 0))
                    .thenReturn(dateObj);
        }
        catch (ParseException e) {/*obsolete*/}

        // Instantiate SUT with stubbed dateFormat
        MainMenuService mainMenuService = new MainMenuService(nowStubbed, hotelResource, scanner, dateFormatStubbed,
                consolePrinter);

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Enter check-in date in format mm/dd/yyyy " +
                        "Example: 05/30/2022"),
                () -> verify(consolePrinter, times(1)).print("Try entering the date again")
        );
    }
    @Test
    void findAndReserveARoom_checkInIsInThePast() {
        // Stub user's input: check-in date is in the past, correct date to terminate the test
        when(scanner.nextLine()).thenReturn("01/01/2019", "05/30/2023");

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Enter check-in date in format " +
                                "mm/dd/yyyy Example: 05/30/2022"),
                () -> verify(consolePrinter, times(1)).print("This date is in the past. " +
                        "Please reenter the date")
        );
    }

    @Test
    void findAndReserveARoom_invalidCheckOut() {
        // Stub user's input: valid check-in date, invalid check-date date, valid check-out to terminate this test
        when(scanner.nextLine()).thenReturn("05/30/2023", "a", "06/10/2023");

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Enter check-out date in format mm/dd/yyyy " +
                        "Example: 05/30/2022"),
                () -> verify(consolePrinter, times(1)).print("Renter the date in format " +
                        "mm/dd/yyyy")
        );
    }

    @Test
    void findAndReserveARoom_parseExceptionCheckOut() {
        // Stub user's input:
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        when(scanner.nextLine()).thenReturn(checkInString, checkOutString);

        // Stub failing to parse input date and parsing the rest correctly
        Date checkInObj = null;
        Date checkOutObj = null;
        try {
            checkInObj = dateFormat.parse(checkInString);
            checkOutObj = dateFormat.parse(checkOutString);
        } catch (ParseException ex) {
            fail("Parsing date string failed in this test.");
        }
        DateFormat dateFormatStubbed = mock(SimpleDateFormat.class);
        try {
            when(dateFormatStubbed.parse(any()))
                    .thenReturn(null)
                    .thenReturn(checkInObj)
                    .thenReturn(null)
                    .thenThrow(new ParseException("test error", 0))
                    .thenReturn(checkOutObj);
        } catch (ParseException e) {
            fail("Parsing date string failed in this test.");
        }

        // Instantiate SUT with stubbed dateFormat
        var mainMenuService = new MainMenuService(nowStubbed, hotelResource, scanner, dateFormatStubbed,
                consolePrinter);

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Enter check-out date in format mm/dd/yyyy " +
                        "Example: 05/30/2022"),
                () -> verify(consolePrinter, times(1)).print("Try entering the date again")
        );
    }

    @Test
    void findAndReserveARoom_checkOutIsInThePast() {
        // Stub user's input: check-out date is in the past, valid check-out to terminate this test
        when(scanner.nextLine()).thenReturn("05/30/2023", "01/20/2019", "06/10/2023");

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Enter check-out date in format mm/dd/yyyy " +
                        "Example: 05/30/2022"),
                () -> verify(consolePrinter, times(1)).print("This date is in the past. " +
                        "Please reenter the date")
        );
    }

    @Test
    void findAndReserveARoom_checkInAfterCheckout() {
        /* Stub user's input: check-in after check-out, afterwards, correct check-in and check-out to terminate this
        test */
        when(scanner.nextLine()).thenReturn("06/10/2023", "05/30/2023", "06/10/2023", "06/20/2023");

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> verify(consolePrinter, times(2)).print("Enter check-in date in format " +
                        "mm/dd/yyyy Example: 05/30/2022"),
                () -> verify(consolePrinter, times(2)).print("Enter check-out date in format " +
                                "mm/dd/yyyy Example: 05/30/2022"),
                () -> verify(consolePrinter, times(1)).print("Your check-in date is later than " +
                                "checkout date. Please reenter dates")
        );
    }

    @Test
    void findAndReserveARoom_noRoomsOnTheDates_noRoomsNextSevenDays() {
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
        } catch (ParseException e) {
            fail("Parsing date string failed in this test.");
        }

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

        // Stub no available rooms for the next seven days
        when(hotelResource.findARoom(checkInDateNext, checkOutDateNext)).thenReturn(List.of());

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("No rooms found for selected " +
                                "dates. Trying to find a room in the next 7 days"),
                () -> verify(consolePrinter, times(1)).print("No free rooms in the next 7 days " +
                                "found. Try different dates")
        );
    }

    @Test
    void findAndReserveARoom_noRoomsOnTheDates_foundRoomsNextSevenDays() {
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
        } catch (ParseException e) {
            fail("Parsing date string failed in this test.");
        }

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
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        when(hotelResource.findARoom(checkInDateNext, checkOutDateNext)).thenReturn(List.of(room));

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("No rooms found for selected " +
                                "dates. Trying to find a room in the next 7 days"),
                () -> verify(consolePrinter, times(1)).print("You can book following rooms from " +
                        checkInDateNext + " till " + checkOutDateNext + ":")
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
        } catch (ParseException e) {
            fail("Parsing date string failed in this test.");
        }

        // Stub finding available room
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        when(hotelResource.findARoom(checkInDate, checkOutDate)).thenReturn(List.of(room));

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> verify(consolePrinter, times(1)).print(room),
                () -> verify(consolePrinter, times(1)).print("Would you like to book one of the " +
                                "rooms above? (y/n)")
        );
    }

    @Test
    void findAndReserveARoom_invalidAnswerAboutBooking() {
        // Stub user's input with invalid answer about booking a room
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        when(scanner.nextLine()).thenReturn(checkInString, checkOutString, "a", "n");

        // Prepare dates
        Date checkInDate = null;
        Date checkOutDate = null;
        dateFormat.setLenient(false);
        try {
            checkInDate = dateFormat.parse(checkInString);
            checkOutDate = dateFormat.parse(checkOutString);
        } catch (ParseException e) {
            fail("Parsing date string failed in this test.");
        }

        // Stub finding available room
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        when(hotelResource.findARoom(checkInDate, checkOutDate)).thenReturn(List.of(room));

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Would you like to book one of the " +
                                "rooms above? (y/n)"),
                () -> verify(consolePrinter, times(1)).print("Enter \"y\" for yes or \"n\" for no")
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
        } catch (ParseException e) {
            fail("Parsing date string failed in this test.");
        }

        // Stub finding available room
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        when(hotelResource.findARoom(checkInDate, checkOutDate)).thenReturn(List.of(room));

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Do you have an account?"),
                () -> verify(consolePrinter, times(1)).print("Please create an account in main " +
                        "menu")
        );
    }

    @Test
    void findAndReserveARoom_invalidAnswerAboutAccount() {
        // Stub user's input with invalid answer about having and account
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        when(scanner.nextLine()).thenReturn(checkInString, checkOutString, "y", "a", "n");

        // Prepare dates
        Date checkInDate = null;
        Date checkOutDate = null;
        dateFormat.setLenient(false);
        try {
            checkInDate = dateFormat.parse(checkInString);
            checkOutDate = dateFormat.parse(checkOutString);
        } catch (ParseException e) {
            fail("Parsing date string failed in this test.");
        }

        // Stub finding available room
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        when(hotelResource.findARoom(checkInDate, checkOutDate)).thenReturn(List.of(room));

        //  Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Do you have an account?"),
                () -> verify(consolePrinter, times(1)).print("Enter \"y\" for yes or \"n\" for no")
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
        } catch (ParseException e) {
            fail("Parsing date string failed in this test.");
        }

        // Stub finding available room
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        when(hotelResource.findARoom(checkInDate, checkOutDate)).thenReturn(List.of(room));

        // Stub that customer is not registered
        when(hotelResource.getCustomer(email)).thenReturn(null);

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Please enter your email"),
                () -> verify(consolePrinter, times(1)).print("You are still not registered with " +
                                "this email. Please create an account")
        );
    }

    @ParameterizedTest(name = "[{index}] Picked room: {0}, Message: {1}")
    @MethodSource("provide_roomNumberAndMessage")
    void findAndReserveARoom_invalidRoomNumber(String roomNumber, String message) {
        // Stub user's input: invalid room number to book and available room number to terminate this test
        String checkInString = "05/30/2023";
        String checkOutString = "06/10/2023";
        String email = "i@z.com";
        when(scanner.nextLine()).thenReturn(checkInString, checkOutString, "y", "y",
                email, roomNumber, "1");

        // Prepare dates
        Date checkInDate = null;
        Date checkOutDate = null;
        dateFormat.setLenient(false);
        try {
            checkInDate = dateFormat.parse(checkInString);
            checkOutDate = dateFormat.parse(checkOutString);
        } catch (ParseException e) {
            fail("Parsing date string failed in this test.");
        }

        // Stub finding available room
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        when(hotelResource.findARoom(checkInDate, checkOutDate)).thenReturn(List.of(room));

        // Stub that customer exists
        var customer = new Customer("I", "Z", email);
        when(hotelResource.getCustomer(email)).thenReturn(customer);

        // Run this test
        mainMenuService.findAndReserveARoom();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Please enter which room to book"),
                () -> verify(consolePrinter, times(1)).print(message)
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
        } catch (ParseException e) {
            fail("Parsing date string failed in this test.");
        }

        // Stub finding available room
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        when(hotelResource.findARoom(checkInDate, checkOutDate)).thenReturn(List.of(room));

        // Stub that customer exists
        var customer = new Customer("I", "Z", email);
        when(hotelResource.getCustomer(email)).thenReturn(customer);

        // Stub getting the room
        when(hotelResource.getRoom(roomNumberToBook)).thenReturn(room);

        // Stub making a reservations
        var reservationFactory = new ReservationFactory();
        Reservation reservation = reservationFactory.create(customer, room, checkInDate, checkOutDate);
        when(hotelResource.bookARoom(email, room, checkInDate, checkOutDate)).thenReturn(reservation);

        // Run this test
        mainMenuService.findAndReserveARoom();

        verify(consolePrinter, times(1)).print(reservation);
    }
}
