import api.AdminResource;
import model.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class AdminMenu {
    private static final AdminResource adminResource = AdminResource.getInstance();

    public static void adminMenu() {
        Scanner scanner = new Scanner(System.in);
        printAdminMenu();

        String line = scanner.nextLine();

        try {
            do {
                if (line.length() == 1) {
                    switch (line.charAt(0)) {
                        case '1' -> seeAllCustomers();
                        case '2' -> seeAllRooms();
                        case '3' -> seeAllReservations();
                        case '4' -> addARoom();
                        case '5' -> MainMenu.mainMenu();
                    }
                }
            } while (line.length() != 1);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid input received. Please try again...");
        }
    }

    private static void printAdminMenu() {
        System.out.println("""
                Welcome to the Admin Menu
                --------------------------------
                1. See all Customers
                2. See all Rooms
                3. See all Reservations
                4. Add a Room
                5. Back to the Main Menu
                --------------------------------
                Please select a number as the menu option:\s
                """);
    }

    private static void seeAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            customers.forEach(System.out::println);
        }
    }

    private static void seeAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();

        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            rooms.forEach(System.out::println);
        }
    }

    private static void seeAllReservations() {
        adminResource.displayAllReservations();
    }

    private static void addARoom() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter a room number:");
        String roomNumAnswer = scanner.nextLine();
        System.out.println("Please enter the room price:");
        double roomPriceAnswer = enterRoomPrice(scanner);
        System.out.println("Please enter the room type:");
        RoomType roomTypeAnswer = enterRoomType(scanner);

        final Room newRoom = new Room(roomNumAnswer, roomPriceAnswer, roomTypeAnswer);

        adminResource.addRoom(Collections.singletonList(newRoom));
        System.out.println("Room added successfully!");

        System.out.println("Do you want to add another room? y/n");
        addAnotherRoom();
    }

    private static void addAnotherRoom() {
        final Scanner scanner = new Scanner(System.in);

        try {
            String anotherRoom;

            anotherRoom = scanner.nextLine();

            while ((anotherRoom.charAt(0) != 'y' && anotherRoom.charAt(0) != 'n')
                    || anotherRoom.length() != 1) {
                System.out.println("Please enter y (Yes) or n (No)");
                anotherRoom = scanner.nextLine();
            }

            if (anotherRoom.charAt(0) == 'y') {
                addARoom();
            } else if (anotherRoom.charAt(0) == 'n') {
                adminMenu();
            } else {
                addAnotherRoom();
            }
        } catch (StringIndexOutOfBoundsException ex) {
            addAnotherRoom();
        }
    }

    private static double enterRoomPrice(final Scanner scanner) {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid room price. Please enter a valid double number.");
            return enterRoomPrice(scanner);
        }
    }

    private static RoomType enterRoomType(final Scanner scanner) {
        try {
            return RoomType.valueOf(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid room type. Please choose 1 for single bed or 2 for double beds.");
            return enterRoomType(scanner);
        }
    }


}
