package be;

public class Operator {

    private int id;
    private String name;
    private String role;
    private String qrToken; //Access change by QRcode

    public Operator(int id, String name, String role, String qrToken) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.qrToken = qrToken;
    }


    public Operator() {
        this(0, "", "", null);
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

    public String getQrToken() {
        return qrToken;
    }

    public void setQrToken(String qrToken) {
        this.qrToken = qrToken;
    }

    @Override
    public String toString() {
        return name + " (" + role + ")";
    }
}
