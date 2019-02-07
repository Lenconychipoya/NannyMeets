package zw.co.appsareus.nannymeets.models;

public class Employer extends User {

    private int credits;

    public Employer() {
    }

    public Employer(String name, String surname, String dob, String gender, String id, String email, String phone, String address, String natID, String password, String accountType, String username, String city, String country) {
        super(name, surname, dob, gender, id, email, phone, address, natID, password, accountType, username, city, country);
    }

    public Employer(String name, String surname, String dob, String gender, String id, String email, String phone, String address, String natID, String password, String accountType, String username, String city, String country, int credits) {
        super(name, surname, dob, gender, id, email, phone, address, natID, password, accountType, username, city, country);
        this.credits = credits;
    }

    public int getCredits() {
        return credits;
    }

    public Employer setCredits(int credits) {
        this.credits = credits;
        return this;
    }
}
