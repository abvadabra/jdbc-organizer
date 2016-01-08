package ru.redenergy.organizer.entity;


import ru.redenergy.organizer.Priority;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Event {

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd h:mm");

    private Timestamp date;
    private String title;
    private String text;
    private Priority priority;

    public Event(Timestamp date, String title, String text, Priority priority) {
        this.date = date;
        this.title = title;
        this.text = text;
        this.priority = priority;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public Priority getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "Event{" +
                "date=" + dateFormatter.format(date) +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", priority=" + priority +
                '}';
    }
}
