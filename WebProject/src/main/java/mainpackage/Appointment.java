package mainpackage;

public class Appointment {
    private int appointmentID;
    private int day;
    private int month;
    private int year;
    private String hour;
    private Patient patient;
    private String reason;

    private String doctorName;
    private String patientName;
    private String specialty;

    // Constructor για χρήση αλλού
    public Appointment(int appointmentID, int day, int month, int year, String hour, String reason) {
        this.appointmentID = appointmentID;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.reason = reason;
    }





    // Νέος constructor για εμφάνιση ραντεβού
    public Appointment(int appointmentID, int hour, int month, int year, String patientName, String reason, int day) {
        this.appointmentID = appointmentID;
        this.hour = String.valueOf(hour);
        this.month = month;
        this.year = year;
        this.reason = reason;
        this.day = day;
        this.patientName = patientName;
    }

    // Getters
    public int getAppointmentId() { return appointmentID; }
    public int getDay() { return day; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
    public String getHour() { return hour; }
    public String getReason() { return reason; }
    public Patient getPatient() { return patient; }
    public String getDoctorName() { return doctorName; }
    public String getPatientName() { return patientName; }
    public String getSpecialty() { return specialty; }

    // Setters
    public void setAppointmentID(int appointmentID) { this.appointmentID = appointmentID; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public void setPatient(Patient patient) { this.patient = patient; }

    // Βοηθητικές μέθοδοι
    public void printSchedule() {
        System.out.println("Έχεις ραντεβού στις: " + day + "/" + month + "/" + year + " στη " + hour);
    }

    public void cancel() {
        this.patient = null;
        this.day = 0;
        this.month = 0;
        this.year = 0;
        this.hour = null;
    }
}
