package be;

public class Admin implements User {

    private int id;
    private String role;
    private String name;
    private String firstName;
    private String lastName;

    public Admin(int id, String role, String name, String firstName, String lastName) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Admin() { this(0, "", "", "", ""); }


    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {}

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

    @Override
    public String toString() {
        return name + " (" + role + ")";
    }
}
