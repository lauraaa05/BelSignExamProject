package be;

public class Operator {

    private int id;
    private String name;
    private String role;

    public Operator(int id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }


    // Getters and Setters

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
