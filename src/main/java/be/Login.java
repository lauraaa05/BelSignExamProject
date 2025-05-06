package be;

class Login {

    private int operatorId;
    private String role;
    private String username;
    private String password;


    public Login(int operatorId, String role, String username, String password) {
        this.operatorId = operatorId;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
