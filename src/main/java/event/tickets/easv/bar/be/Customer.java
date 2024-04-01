package event.tickets.easv.bar.be;

import java.util.List;

public class Customer implements Entity<Customer> {

    private int id;
    private String mail;

    public Customer() {

    }

    public Customer(String mail) {
        this.mail = mail;
    }

    public Customer(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    public Customer (int id, Customer entity) {
        this(id, entity.mail());
    }


    public int id() {
        return id;
    }

    public String mail() {
        return mail;
    }

    public void setEmail(String mail) {
        this.mail = mail;
    }

    @Override
    public void update(Customer updatedData) {

    }

    @Override
    public void setAssociations(List<?> associations) {

    }
}
