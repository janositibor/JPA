package model;

import java.util.List;

public class Actor {
    private long id;
    private String name;
    private int yob;
    private List<Movie> movies;

    public Actor(String name, int yob) {
        this.name = name;
        this.yob = yob;
    }

    public Actor(long id, String name, int yob) {
        this(name,yob);
        this.id = id;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getYob() {
        return yob;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", yob=" + yob +
                '}';
    }
}
