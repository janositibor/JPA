package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Movie {
    private Long id;
    private String title;
    private LocalDate releaseDate;
    private List<Actor> actors=new ArrayList<>();
    private List<Integer> evaluation=new ArrayList<>();
    private int numberOfRatings;
    private double averageOfRatings;

    public Movie(String title, LocalDate releaseDate) {
        this.title = title;
        this.releaseDate = releaseDate;
    }

    public Movie(Long id, String title, LocalDate releaseDate) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public void setAverageOfRatings(double averageOfRatings) {
        this.averageOfRatings = averageOfRatings;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public List<Integer> getEvaluation() {
        return evaluation;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                '}';
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public double getAverageOfRatings() {
        return averageOfRatings;
    }
}
