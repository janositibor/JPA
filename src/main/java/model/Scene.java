package model;

import javax.persistence.*;

@Entity
@Table(name = "scenes")
public class Scene {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private int length;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    public Scene() {
    }

    public Scene(String title, String description, int length) {
        this.title = title;
        this.description = description;
        this.length = length;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getLength() {
        return length;
    }

    public Movie getMovie() {
        return movie;
    }
}
