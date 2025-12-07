package mainpackage;

public class Availability {
    private int id;
    private int doctorId;
    private int day;
    private int month;
    private int year;
    private int hour;
    private String doctorName;
    private String specialty;

    // Κενός constructor
    public Availability() {}

    // Constructor χωρίς γιατρό
    public Availability(int id, int doctorId, int day, int month, int year, int hour) {
        this.id = id;
        this.doctorId = doctorId;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
    }

    // Constructor με γιατρό
    public Availability(int id, int doctorId, int day, int month, int year, int hour, String doctorName, String specialty) {
        this.id = id;
        this.doctorId = doctorId;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.doctorName = doctorName;
        this.specialty = specialty;
    }

    // Getters
    public int getId() { return id; }
    public int getDoctorId() { return doctorId; }
    public int getDay() { return day; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
    public int getHour() { return hour; }
    public String getDoctorName() { return doctorName; }
    public String getSpecialty() { return specialty; }

    @Override
    public String toString() {
        return doctorName + " (" + specialty + ") - " + day + "/" + month + "/" + year + " " + hour + ":00";
    }
}
