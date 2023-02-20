import api.HotelResource;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

public class MainMenu {
    private static final HotelResource hotelResource = HotelResource.getInstance();
    private static final String DEFAULT_DATE_FORMAT = "mm/dd/yyyy";

    public static void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        printMainMenu();

        String line = scanner.nextLine();

        try {
            do {
                if (line.length() == 1) {
                    switch (line.charAt(0)) {
                        case '1' -> findAndReserveRoom();
                        case '2' -> seeMyReservation();
                        case '3' -> createAccount();
                        case '4' -> AdminMenu.adminMenu();
                        case '5' -> System.exit(0);
                        default -> System.out.println("Invalid option\n");
                    }
                }
            } while (line.charAt(0) != 5 || line.length() != 1);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input received. Please try again...");
        }

    }

    private static void findAndReserveRoom() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Check-In Date mm/dd/yyyy");
        Date checkInDate = getInputDate(scanner);

        System.out.println("Enter Check-Out Date mm/dd/yyyy");
        Date checkOutDate = getInputDate(scanner);

        if (checkInDate != null && checkOutDate != null) {
            Collection<IRoom> availableRooms = hotelResource.findARoom(checkInDate, checkOutDate);

            if (availableRooms.isEmpty()) {
                System.out.println("No available rooms in the time you selected.\n" +
                        "We are trying to find alternative options for you...");
                Collection<IRoom> alternativeRooms = hotelResource.findAlternativeRooms(checkInDate, checkOutDate);

                if (alternativeRooms.isEmpty()) {
                    System.out.println("We're sorry that there is no available room. Back to the main menu...");
                    mainMenu();
                } else {
                    Date alternativeCheckIn = hotelResource.addDefaultPlusDays(checkInDate);
                    Date alternativeCheckOut = hotelResource.addDefaultPlusDays(checkOutDate);
                    System.out.println("The alternative options are found: \n" +
                            "The dates are between " + alternativeCheckIn + " and " + alternativeCheckOut);
                    printRooms(alternativeRooms);
                    reserveARoom(scanner, alternativeCheckIn, alternativeCheckOut, alternativeRooms);
                }
            } else {
                System.out.println("The available room options are found between your check-in and check-out dates: ");
                printRooms(availableRooms);
                reserveARoom(scanner, checkInDate, checkOutDate, availableRooms);
            }
        } else {
            System.out.println("Please enter a valid date. Try again.");
            findAndReserveRoom();
        }
    }

    private static void reserveARoom(Scanner scanner, Date checkIn, Date checkOut,
                                     Collection<IRoom> rooms) {
        System.out.println("Would you like to book a room? y/n");
        String bookAnswer = scanner.nextLine();

        if (Objects.equals(bookAnswer, "y")) {
            System.out.println("Do you already have an account? y/n");
            String haveAccountAnswer = scanner.nextLine();

            if (Objects.equals(haveAccountAnswer, "y")) {
                System.out.println("Enter your email in name@domain.com format:");
                String emailAnswer = scanner.nextLine();

                if (hotelResource.getCustomer(emailAnswer) == null) {
                    System.out.println("Account not found, please create a new account.");
                } else {
                    System.out.println("What room number would you like to reserve?");
                    String roomNumAnswer = scanner.nextLine();

                    if (rooms.stream().anyMatch(room -> room.getRoomNumber().equals(roomNumAnswer))) {
                        IRoom room = hotelResource.getRoom(roomNumAnswer);

                        Reservation reservation = hotelResource.bookARoom(emailAnswer, room, checkIn, checkOut);
                        System.out.println("Successfully booked. Thank you!\n" +
                                reservation.toString());
                    } else {
                        System.out.println("This room number is either invalid or unavailable.\n" +
                                "Start you reservation again...");
                    }
                }
                mainMenu();
            }
            if (Objects.equals(haveAccountAnswer, "n")) {
                System.out.println("Please create a new account first\n" +
                        "Switching to the account creating site...");
                createAccount();
            }
        } else if (Objects.equals(bookAnswer, "n")) {
            System.out.println("Jumping back to the main menu...");
            printMainMenu();
        } else {
            reserveARoom(scanner, checkIn, checkOut, rooms);
        }
    }

    private static Date getInputDate(Scanner scanner) {
        try {
            return new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(scanner.nextLine());
        } catch (ParseException e) {
            System.out.println("Error: Invalid date.");
            findAndReserveRoom();
        }
        return null;
    }

    private static void seeMyReservation() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your registered email in name@domain.com format:");
        String emailAnswer = scanner.nextLine();

        if (hotelResource.getCustomer(emailAnswer) == null) {
            System.out.println("Account not found, please try to create a new account first.");
            mainMenu();
        } else {
            System.out.println(hotelResource.getCustomerReservations(emailAnswer));
        }
    }

    private static void createAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your email that you want to register with us in name@domain.com format:");
        String emailAnswer = scanner.nextLine();

        try {
            if (hotelResource.getCustomer(emailAnswer) != null) {
                System.out.println("Account already exists. Please try another option in main menu.");
            } else {
                System.out.println("Please enter your first name:");
                String firstName = scanner.nextLine();
                System.out.println("Please enter your last name:");
                String lastName = scanner.nextLine();

                hotelResource.createACustomer(emailAnswer, firstName, lastName);
                System.out.println("Account created successfully. Jumping back to main menu now...");
            }
            mainMenu();
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
            createAccount();
        }
    }

    private static void printRooms(Collection<IRoom> rooms) {
        if (rooms.isEmpty()) {
            System.out.println("No room is found.");
        } else {
            rooms.forEach(System.out::println);
        }
    }

    public static void printMainMenu() {
        System.out.println("""
                Welcome to the Hotel Reservation Portal
                --------------------------------
                1. Find and reserve a room
                2. See my reservations
                3. Create an account
                4. Admin
                5. Exit
                --------------------------------
                Please select a number as the menu option:\s
                """);
    }
}
