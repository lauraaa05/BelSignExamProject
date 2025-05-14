package be;

public interface User {
    int getId();
    String getName();
    void setName(String name);
    String getRole();
    void setRole(String role);

    String getFirstName();
    void setFirstName(String firstName);
    String getLastName();
    void setLastName(String lastName);
    String getEmail();
    void setEmail(String email);
    String getPassword();
    void setPassword(String password);
}
