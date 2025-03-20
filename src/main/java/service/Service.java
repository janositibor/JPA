package service;

import model.Actor;
import repository.*;
import model.Movie;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Service {
    private ActorRepository actorRepository;
    private MovieRepository movieRepository;
    private ActorMovieRepository actorMovieRepository;

    public Service(EntityManagerFactory factory) {
        actorRepository = new ActorRepository(factory);
        movieRepository = new MovieRepository(factory);
        actorMovieRepository = new ActorMovieRepository(factory);
    }

    public Actor saveActor(String name, int yob) {
        Actor actor = new Actor(name, yob);
        if (!containsActor(actor)) {
            actorRepository.save(actor);
        }
        return actorRepository.find(actor).orElseThrow(() -> new IllegalArgumentException("Can't found actor: " + actor));
    }

    public Actor findActor(Actor actor) {
        Optional<Actor> result = actorRepository.find(actor);
        if (result.isPresent()) {
            Actor getActor = result.get();
            Actor output = actorMovieRepository.findActorByIdWithMovies(getActor.getId()).orElseThrow(() -> new IllegalArgumentException("No actor found: " + actor));
            return output;
        } else {
            throw new IllegalArgumentException("No actor found: " + actor);
        }
    }


    public boolean containsActor(Actor actor) {
        return actorRepository.find(actor).isPresent();
    }

    public List<Actor> findAllActor() {
        return actorRepository.findAllActor();
    }

    public Movie saveMovie(String title, LocalDate releaseDate) {
        Movie movie = new Movie(title, releaseDate);
        if (!containsMovie(movie)) {
            movieRepository.save(movie);
        }
        return movieRepository.find(movie).orElseThrow(() -> new IllegalArgumentException("Can't found movie: " + movie));
    }

    public boolean containsMovie(Movie movie) {
        return movieRepository.find(movie).isPresent();
    }

    public Movie findMovie(Movie movie) {
        Optional<Movie> result = movieRepository.find(movie);
        if (result.isPresent()) {
            Movie getMovie = result.get();
            Movie output = actorMovieRepository.findMovieByIdWithActors(getMovie.getId()).orElseThrow(() -> new IllegalArgumentException("No movie found: " + movie));
            return output;
        } else {
            throw new IllegalArgumentException("No movie found: " + movie);
        }
    }

    public void saveMovieWithActors(String title, LocalDate releaseDate, List<Actor> actors) {
        Movie movie = saveMovie(title, releaseDate);
        List<Actor> actorsToSet=new ArrayList<>();
        for (Actor actorToCheck : actors) {
            Actor actor;
            if(!containsActor(actorToCheck)) {
                actor = actorRepository.save(actorToCheck);
            }
            else {
                actor=findActor(actorToCheck);
            }
            actorsToSet.add(actor);
        }
        movie.setActors(actorsToSet);
        movieRepository.save(movie);
    }
}
