package ru.redenergy.organizer.thread;

import ru.redenergy.organizer.Organizer;
import ru.redenergy.organizer.Priority;
import ru.redenergy.organizer.entity.Event;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class CommandThread extends Thread {

    private Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        super.run();
        while(!interrupted()){
            String input = scanner.nextLine();
            if(input != null && !input.isEmpty()){
                handleCommand(input);
            }
        }
    }

    private void handleCommand(String command){
        String[] args = command.split(" ");
        if("display all".equalsIgnoreCase(command)){
           displayAllEvents();
        } else if("display".equalsIgnoreCase(args[0])){
            findEvent(args);
        } else if("add".equalsIgnoreCase(args[0])){
            addEvent(args);
        }
    }

    private void findEvent(String[] args){
        try {
            System.out.println("FOUND BY REQUEST \"" + args[1] + "\":");
            List<Event> events = Organizer.application.findEvents(args[1]);
            for(Event event: events){
                System.out.println(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addEvent(String[] args){
        try {
            Organizer.application.addEvent(new Event(new Timestamp(Long.parseLong(args[1])), args[2], args[3], Priority.valueOf(args[4])));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayAllEvents(){
        try {
            List<Event> events = Organizer.application.getEvents();
            for(Event event: events){
                System.out.println(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
