package mainpackage;

public class Users { // Κλάση των χρηστών

    private String username;
    private String password;
    private String name;
    private String surname;
    private int age;
    private String email;
    private long phone;
    private static int usersCounter = 0;

    public Users(String username, String name, String surname, String email, int age, String password, long phone) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.phone = phone;

        usersCounter++;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public long getPhone() {
        return phone;
    }

    public int getAge() {
        return age;
    }

    public static int getUsersCounter() {
        return usersCounter;
    }

    // Setters 
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // Μέθοδοι
    public void login() {
        System.out.println("Ο χρήστης " + username + " έκανε logged in");
    }

    public void logout() {
        System.out.println("Ο χρήστης " + username + " έκανε logged out");
    }
}
