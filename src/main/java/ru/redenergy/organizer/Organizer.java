package ru.redenergy.organizer;

import ru.redenergy.organizer.thread.CommandThread;
import ru.redenergy.organizer.thread.NotificationThread;

import java.sql.SQLException;

public class Organizer {

    public static Application application = new Application("jdbc:mysql://localhost:3306/organizer", "root", "mysql");

    public static void main(String[] args) throws SQLException {
        application.bootstrap();
        application.getEvents();
        new CommandThread().start();
        new NotificationThread().start();
    }



}
