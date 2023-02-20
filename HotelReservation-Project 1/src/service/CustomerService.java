package service;

import model.Customer;

import java.util.*;

public class CustomerService {

    private static CustomerService instance;
    Map<String, Customer> mapOfCustomers = new HashMap<String, Customer>();
    ArrayList<Customer> customerList = new ArrayList<Customer>();

    private CustomerService(){}

    public static CustomerService getInstance() {
        if (instance == null) {
            instance = new CustomerService();
        }
        return instance;
    }

    public void addCustomer(String email, String firstName, String lastName) {
        Customer newCustomer = new Customer(email, firstName, lastName);
        customerList.add(newCustomer);
        mapOfCustomers.put(email, newCustomer);
    }

    public Customer getCustomer(String customerEmail) {
        return mapOfCustomers.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers() {
        return customerList;
    }

}
