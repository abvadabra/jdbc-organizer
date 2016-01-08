package ru.redenergy.organizer;


import ru.redenergy.organizer.entity.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Application {

    private final String INITIAL_QUERY = "CREATE TABLE IF NOT EXISTS `events` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `date` timestamp NOT NULL,\n" +
            "  `title` varchar(255) COLLATE utf8_bin NOT NULL,\n" +
            "  `text` varchar(255) COLLATE utf8_bin NOT NULL,\n" +
            "  `priority` varchar(255) COLLATE utf8_bin NOT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";

    private final String ADD_EVENT_QUERY = "INSERT INTO `events`(`date`, `title`, `text`, `priority`)" +
                                            " VALUES (?, ?, ?, ?)";
    private final String GET_EVENTS_QUERY = "SELECT * FROM `events`";

    private final String FIND_EVENT_QUERY = "SELECT * FROM `events` WHERE `title` = ?";

    private final String jdbcPath;
    private final String jdbcLogin;
    private final String jdbcPassword;

    /**
     * Cached event list, used in notifications
     */
    private List<Event> cachedEvents = new ArrayList<Event>();

    public Application(String jdbcPath, String jdbcLogin, String jdbcPassword) {
        this.jdbcPath = jdbcPath;
        this.jdbcLogin = jdbcLogin;
        this.jdbcPassword = jdbcPassword;
    }

    public void bootstrap() throws SQLException {
        performUpdate(setupConnection(), INITIAL_QUERY);
    }

    public List<Event> findEvents(String searchedTitle) throws SQLException {
        ResultSet findQuery = executeQuery(setupConnection(), FIND_EVENT_QUERY, searchedTitle);
        List<Event> events = new ArrayList<Event>();
        while(findQuery.next()){
            Timestamp date = findQuery.getTimestamp(2);
            String title = findQuery.getString(3);
            String text = findQuery.getString(4);
            Priority priority = Priority.valueOf(findQuery.getString(5));
            events.add(new Event(date, title, text, priority));
        }
        findQuery.close();
        return events;
    }

    public void addEvent(Event event) throws SQLException {
        performUpdate(setupConnection(), ADD_EVENT_QUERY, event.getDate(), event.getTitle(), event.getText(), event.getPriority().name());
        cachedEvents = getEvents(); //after every update we should update local cache to display notifications
    }

    public List<Event> getEvents() throws SQLException {
        ResultSet eventsQuery = executeQuery(setupConnection(), GET_EVENTS_QUERY);
        List<Event> events = new ArrayList<Event>();
        while(eventsQuery.next()){
            Timestamp date = eventsQuery.getTimestamp(2);
            String title = eventsQuery.getString(3);
            String text = eventsQuery.getString(4);
            Priority priority = Priority.valueOf(eventsQuery.getString(5));
            events.add(new Event(date, title, text, priority));
        }
        eventsQuery.close();
        cachedEvents = events;
        return cachedEvents;
    }

    public List<Event> getCachedEvents(){
        return cachedEvents;
    }


    private PreparedStatement prepareStatement(Connection connection, String query, Object ... data) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        for(int i = 0; i < data.length; i++){
            Object value = data[i];
            if(value instanceof Timestamp){
                statement.setTimestamp(i + 1, (Timestamp) value);
            } else if(value instanceof String){
                statement.setString(i + 1, (String)value);
            }
        }
        return statement;
    }

    //NOTE: DO NOT FORGET TO CLOSE RESULT SET
    private ResultSet executeQuery(Connection connection, String query, Object ... data) throws SQLException {
        return prepareStatement(connection, query, data).executeQuery();
    }

    private int performUpdate(Connection connection, String query, Object ... data) throws SQLException {
        int updated = prepareStatement(connection, query, data).executeUpdate();
        connection.close();
        return updated;
    }

    private Connection setupConnection() throws SQLException {
        return setupConnection(jdbcPath, jdbcLogin, jdbcPassword);
    }

    private Connection setupConnection(String jdbcPath, String jdbcLogin, String jdbcPassword) throws SQLException {
        return DriverManager.getConnection(jdbcPath, jdbcLogin, jdbcPassword);
    }
}
