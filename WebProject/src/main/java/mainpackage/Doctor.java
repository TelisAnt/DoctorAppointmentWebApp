package mainpackage;

public class Doctor extends Users {
    private String specialty;
    private int id;  // Προσθήκη πεδίου id

    // Constructor με id
    public Doctor(int id, String username, String name, String specialty, String email, String password) {
        super(username, name, "", email, 0, password, 0L);
        this.specialty = specialty;
        this.id = id;
    }

    // Προσθήκη getter για id
    public int getId() {
        return id;
    }
    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}