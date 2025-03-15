package service;

import model.Actor;
import repository.*;
import model.Movie;

import java.time.LocalDate;
import java.util.List;

public class Service {
    private ActorRepository actorRepository;
    private MovieRepository movieRepository;
    private ActorMovieRepository actorMovieRepository;

//    public Service(DB db) {
//        actorRepository=new ActorRepository(db.getDataSource());
//        movieRepository=new MovieRepository(db.getDataSource());
//        actorMovieRepository=new ActorMovieRepository(db.getDataSource());
//    }
//
//    public Actor saveActor(String name, int yob){
//        Actor actor=new Actor(name,yob);
//        if(!containsActor(actor)){
//            actorRepository.saveBasicAndGetGeneratedKey(actor);
//        }
//        return actorRepository.findActor(actor).orElseThrow(()->new IllegalArgumentException("Can't found actor: "+actor));
//    }
//    public Actor findActor(Actor actor){
//        Actor output= actorRepository.findActor(actor).orElseThrow(() -> new IllegalArgumentException("No actor found: "+actor));
//        List<Movie> movies=actorMovieRepository.findMoviesForActor(output);
//        output.setMovies(movies);
//        return output;
//    }
//
//    public boolean containsActor(Actor actor){
//        return actorRepository.findActor(actor).isPresent();
//    }
//
//    public List<Actor> findAllActor(){
//        return actorRepository.findAllActor();
//    }
//
//    public Movie saveMovie(String title, LocalDate releaseDate){
//        Movie movie=new Movie(title,releaseDate);
//        if(!containsMovie(movie)){
//            movieRepository.saveBasicAndGetGeneratedKey(movie);
//        }
//        return movieRepository.findMovie(movie).orElseThrow(()->new IllegalArgumentException("Can't found movie: "+movie));
//    }
//    public boolean containsMovie(Movie movie){
//        return movieRepository.findMovie(movie).isPresent();
//    }
//    public Movie findMovie(Movie movie){
//        Movie output= movieRepository.findMovie(movie).orElseThrow(() -> new IllegalArgumentException("No movie found: "+movie));
//        List<Actor> actors=actorMovieRepository.findActorsForMovie(output);
//        output.setActors(actors);
//        return output;
//    }
//
//    public void saveMovieWithActors(String title, LocalDate releaseDate, List<Actor> actors){
//        Movie movie=saveMovie(title,releaseDate);
//        for (Actor actorToCheck:actors) {
//            if(!containsActor(actorToCheck)){
//                saveActor(actorToCheck.getName(),actorToCheck.getYob());
//            }
//            Actor actor=actorRepository.findActor(actorToCheck).orElseThrow(()->new IllegalStateException("Can't found actor!"));
//            actorMovieRepository.save(actor,movie);
//        }
//    }
}
