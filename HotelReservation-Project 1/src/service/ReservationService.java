package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.*;

public class ReservationService {

    private static ReservationService instance;
    private static final int RECOMMENDED_ROOMS_DEFAULT_PLUS_DAYS = 7;
    private final Map<String, Reservation> mapOfReservations = new HashMap<>();
    private final Map<String, IRoom> mapOfRooms = new HashMap<>();

    private ReservationService() {}

    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    public void addRoom(IRoom room) {
        mapOfRooms.put(room.getRoomNumber(), room);
    }

    public IRoom getRoom(String roomId) {
        return mapOfRooms.get(roomId);
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation newReservation = new Reservation(customer, room, checkInDate, checkOutDate);
        mapOfReservations.put(customer.getEmail(), newReservation);
        return newReservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        return getAvailableRooms(checkInDate, checkOutDate);
    }


    public Collection<IRoom> searchForRecommendedRooms(Date checkInDate, Date checkOutDate) {
        return getAvailableRooms(addRecommendDays(checkInDate), addRecommendDays(checkOutDate));
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        Collection<Reservation> customerReservations = new ArrayList<>();
        customerReservations.add(mapOfReservations.get(customer.getEmail()));
        return customerReservations;
    }

    public void printAllReservation() {
        if (mapOfReservations.values().isEmpty()) {
            System.out.println("No reservation found.");
        }
        System.out.println(mapOfReservations.values());
    }

    public Collection<IRoom> getAllRooms() {
        return mapOfRooms.values();
    }


    public Date addRecommendDays(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, RECOMMENDED_ROOMS_DEFAULT_PLUS_DAYS);
        return calendar.getTime();
    }

    private Collection<IRoom> getAvailableRooms(Date checkInDate, Date checkOutDate) {
        ArrayList<IRoom> availableRooms = new ArrayList<>();
        for (Reservation reservation: mapOfReservations.values()) {
            if(reservation.getCheckInDate().after(checkOutDate) || reservation.getCheckOutDate().before(checkInDate)) {
                availableRooms.add(reservation.getRoom());
            }
        }
        return availableRooms;
    }

}
