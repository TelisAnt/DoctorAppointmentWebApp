package mainpackage;

public class Administrator extends Users {

    public Administrator(String username, String password) {
        // Καλούμε τον constructor της γονικής κλάσης με default τιμές για τα υπόλοιπα πεδία
        super(username, "Admin", "Admin", "admin@clinic.com", 0, password, 0L);
    }


    public void removeDoctor(int doctorId) {
        System.out.println("Ο διαχειριστής " + getUsername() + " αφαίρεσε τον γιατρό με ID: " + doctorId);
    }

    @Override
    public void login() {
        System.out.println("Ο διαχειριστής " + getUsername() + " συνδέθηκε");
    }

    @Override
    public void logout() {
        System.out.println("Ο διαχειριστής " + getUsername() + " αποσυνδέθηκε");
    }
}