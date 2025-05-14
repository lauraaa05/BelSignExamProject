package be;

public class Admin implements User {

    private int id;
    private String role;
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public Admin(int id, String role, String name, String firstName, String lastName, String email) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Admin() { this(0, "", "", "", "", ""); }


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

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return name + " (" + role + ")";
    }
}
