package ru.redenergy.organizer.thread;

import ru.redenergy.organizer.Organizer;
import ru.redenergy.organizer.entity.Event;

import java.util.List;

public class NotificationThread extends Thread {

    private static final long TEN_MINUTES_IN_MILLISECONDS = 600000L;

    @Override
    public void run() {
        super.run();
        while (!interrupted()){
            List<Event> events = Organizer.application.getCachedEvents();
            for(Event event: events){
                if((event.getDate().getTime() - System.currentTimeMillis()) == TEN_MINUTES_IN_MILLISECONDS){
                    System.out.println("NOTIFICATION:");
                    System.out.println(event);
                }
            }
        }
    }
}
