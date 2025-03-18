package model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDate releaseDate;
    @ManyToMany(mappedBy = "movies")
    private List<Actor> actors = new ArrayList<>();
    @ElementCollection
    private List<Integer> ratings = new ArrayList<>();
    private int numberOfRatings;
    private double averageOfRatings;
    @ElementCollection
    @CollectionTable(name = "shows", joinColumns = @JoinColumn(name = "movie_id"))
    private List<Show> shows = new ArrayList<>();
    //    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "movie")
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Scene> scenes = new ArrayList<>();

    public Movie(String title, LocalDate releaseDate) {
        this.title = title;
        this.releaseDate = releaseDate;
    }

    public Movie(Long id, String title, LocalDate releaseDate) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
    }

    public Movie() {
    }

    public void addScene(Scene scene) {
        scenes.add(scene);
        scene.setMovie(this);
    }

    public void addShow(Show show) {
        shows.add(show);
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public void addActors(Actor actor) {
        this.actors.add(actor);
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

    public List<Integer> getRatings() {
        return ratings;
    }

    public void setRatings(List<Integer> ratings) {
        this.ratings = ratings;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }

    public List<Show> getShows() {
        return shows;
    }

    public List<Scene> getScenes() {
        return scenes;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                '}';
    }

    public void defineScenes(List<Scene> scenes) {
        this.scenes.clear();
        for (Scene scene : scenes) {
            addScene(scene);
        }
    }
}
