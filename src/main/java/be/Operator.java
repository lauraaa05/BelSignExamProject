package be;

public class Operator implements User {

    private int id;
    private String name;
    private String role;
    private String qrToken; //Access change by QRcode
    private String firstName;
    private String lastName;

    public Operator(int id, String name, String role, String qrToken, String firstName, String lastName) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.qrToken = qrToken;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public Operator() {
        this(0, "", "", null,"","");
    }


    // Getters and Setters

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getQrToken() {
        return qrToken;
    }

    public void setQrToken(String qrToken) {
        this.qrToken = qrToken;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName() + " - " + getName() + " - " + getRole();
    }
}
