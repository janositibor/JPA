package model;

import javax.persistence.Embeddable;
import java.time.LocalTime;

@Embeddable
public class Show {
    private String cinema;
    private LocalTime startTime;

    public Show(String cinema, LocalTime startTime) {
        this.cinema = cinema;
        this.startTime = startTime;
    }

    public Show() {
    }

    public void setCinema(String cinema) {
        this.cinema = cinema;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public String getCinema() {
        return cinema;
    }

    public LocalTime getStartTime() {
        return startTime;
    }
}
