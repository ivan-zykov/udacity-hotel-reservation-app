# Udacity hotel reservation app

This is my implementation of the first (out of three) project in Udacity's Java Programing Nanodegree.

## How to clone the repo in terminal
```bash
git clone https://github.com/IvanZet/udacity-hotel-reservation-app.git ivans-hotel-app
```

## How to run it in IDEA
Open `src/HotelApplication.java` file and run the `main()` function

## How to run it in terminal
Navigate to `src/` dir of the project
```bash
cd ivans-hotel-app/src
```
Compile the project
```bash
javac -cp "." HotelApplication.java
```
Run the app
```bash
java -cp "." HotelApplication.java
```

## TODO
- [x] Add how run it
- [ ] Add description (with screenshots maybe)
- [ ] Add JavaDock for methods, especially public ones
- [ ] Add unit tests
- [ ] Clean FIXME and TODO tags
- [ ] Fix IDEA’s standard linter errors
- [ ] Add IDEA's plug-in to catch code style errors (Sonar) and fix errors

## TODO optional
- [x] Catch exceptions later in UI?
- [x] Ensure that entered check in date is earlier that check out date
- [x] Fix duplicate booking
- [x] Refactor suggesting other dates
- [ ] Sort elements in collections when printing them (reservations, customers)
- [ ] Add option to add a class for free room
- [x] Make classes final where applicable
- [x] Check PROJECT SPECIFICATION
- [ ] Add variables' values to exception messages
- [ ] When dates are printed to console, truncate time

## TODO new features
- [ ] Customize the find-a-room method to search for paid rooms or free rooms.
- [ ] Provide a menu option from the Admin menu to populate the system with test data (Customers, Rooms and Reservations).
- [ ] Allow the users to input how many days out the room recommendation should search if there are no available rooms.
