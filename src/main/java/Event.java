package main.java;

import java.time.LocalDate;

public class Event extends Deadline{

    public LocalDate date;

    public Event(String task, boolean complete,LocalDate date) {
        super(task, complete,date);
    }

    @Override
    public String stringify(){
        if(this.complete == true) {
            return "[E][DONE] " + this.task + " " +"(at: " +this.deadline+")" ;
        }else{
            return "[E][UNDONE] " + this.task + " " +"(at: " +this.deadline+")" ;
        }
    }
}