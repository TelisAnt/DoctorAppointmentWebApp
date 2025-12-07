package mainpackage;

public class Patient extends Users {
    private final long amka;

    public Patient(String username, String name, String surname, String email, int age, String password, long phone, long amka) {
        super(username, name, surname, email, age, password, phone);
        this.amka = amka;
    }

    // Getter για το AMKA
    public long getAmka() {
        return amka;
    }

    public void registration() { // Εγγραφή του ασθενή
        System.out.println("Ο ασθενής θέλει να κάνει εγγραφή!");
    }

    public void appointmentSpecificDoctor() {
        System.out.println("Το ραντεβού με το καρδιολόγο προγραμματίστηκε");
    }

    public void appointmentAnyDoctor() {
        System.out.println("Το ραντεβού με το γιατρό προγραμματίστηκε");
    }

    public void showCatalog() {
        System.out.println("Εμφάνιση καταλόγου!");
    }
}
