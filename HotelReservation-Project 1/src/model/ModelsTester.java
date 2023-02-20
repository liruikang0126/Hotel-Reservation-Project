package model;

public class ModelsTester {
    public static void main(String[] args) {
        //Room room1 = new FreeRoom("1314", RoomType.DOUBLE);
        //System.out.println(room1);
        //System.out.println(new FreeRoom("1109B", RoomType.SINGLE));
        Customer customer = new Customer("first", "second", "j@domain.com");
        System.out.println(customer);

        Customer customer1 = new Customer("first", "second", "email");
        System.out.println(customer1);
    }
}
