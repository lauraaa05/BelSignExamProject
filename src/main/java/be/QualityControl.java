package be;

public class QualityControl implements User {

    private int id;
    private String role;
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public QualityControl(int id, String name, String role, String firstName, String lastName) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public QualityControl(int id, String name, String password, String role, String firstName, String lastName) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public QualityControl(String firstName, String lastName, String username, String password, String email) {
        this(0, username, "Quality Control", firstName, lastName);
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {this.lastName = lastName;}

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
        return getFirstName() + " " + getLastName() + " - " + getName() + " - " + getRole();
    }
}