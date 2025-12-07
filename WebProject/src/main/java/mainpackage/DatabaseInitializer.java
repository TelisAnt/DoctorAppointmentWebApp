package mainpackage;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DatabaseInitializer implements ServletContextListener { //Εκτελειται αυτοματα οταν ξεκιναει η εφαρμογη
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            CreateDatabase.main(null); //Καλει αυτη την συναρτηση για να φτιαξει εαν δεν υπαρχει την βαση δεδομενων
            System.out.println("Database initialized successfully!");
        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}