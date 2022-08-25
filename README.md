# Udacity hotel reservation app

## Description
This is my implementation of the first out of three projects in Udacity Java Programing Nanodegree.

I wrote this console app from scratch based on architecture suggested in the course. The project demonstrates:
- designing classes using OOP
- organizing and processing data with collections
- using common Java types
- using Maven to organise third-party libraries
- unit testing and mocking dependencies.

This app allows customers to find and book a hotel room based on room availability. Following screenshots
demonstrate the functionality implemented.

Main menu:\
![main menu](main.png)

Admin menu:\
![admin menu](admin.png)

## How to clone this repo in terminal
```bash
git clone https://github.com/IvanZet/udacity-hotel-reservation-app.git ivans-hotel-app
```

## How to run this app in IDE
Open
```text
src/main/java/com/udacity/hotel/HotelApplication.java
```
file and run the `main()` method.

## How to run this app in terminal
1. Navigate to the source root of the project
    ```bash
    cd ivans-hotel-app/src/main/java
    ```
2. Compile the project
    ```bash
    javac com/udacity/hotel/HotelApplication.java
    ```
3. Run the app
    ```bash
    java com.udacity.hotel.HotelApplication
    ```

## TODO
### Priority
- [x] Add how run it
- [x] Add description
- [x] Add JavaDock for methods, especially public ones
- [x] Add unit tests
- [x] Clean FIXME and TODO tags
- [x] Fix IDEA’s standard linter errors
- [x] Add IDEA's plug-in to catch code style errors (Sonar) and fix errors

### Optional
- [x] Catch exceptions later in UI?
- [x] Ensure that entered check in date is earlier that check out date
- [ ] Validate check-in and check-out dates in constructor of Reservation class
- [x] Fix duplicate booking
- [x] Refactor suggesting other dates
- [ ] Sort elements in collections when printing them (reservations, customers)
- [ ] Add option to add a room using FreeRoom class
- [x] Make classes final where applicable
- [x] Check PROJECT SPECIFICATION
- [ ] Add variables' values to exception messages
- [ ] When dates are printed to console, truncate time

### New features
- [ ] Customize the find-a-room method to search for paid rooms or free rooms.
- [ ] Provide a menu option from the Admin menu to populate the system with test data (Customers, Rooms and Reservations).
- [ ] Allow the users to input how many days out the room recommendation should search if there are no available rooms.
