package be;

public class Admin {

    private int id;
    private String role;
    private String name;

    public Admin(int id, String role, String name) {
        this.id = id;
        this.role = role;
        this.name = name;
    }

    public Admin() { this(0, "", ""); }


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
