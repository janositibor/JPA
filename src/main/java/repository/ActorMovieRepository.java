package repository;

import model.Actor;
import model.Movie;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Optional;

public class ActorMovieRepository {
    private EntityManagerFactory entityManagerFactory;

    public ActorMovieRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Optional<Movie> findMovieByIdWithActors(long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Movie movie = em.createQuery("select m from Movie m Left join fetch m.actors where m.id=:id", Movie.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.ofNullable(movie);
        } finally {
            em.close();
        }
    }

    public Optional<Actor> findActorByIdWithMovies(long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Actor actor = em.createQuery("select a from Actor a Left join fetch a.movies where a.id=:id", Actor.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.ofNullable(actor);
        } finally {
            em.close();
        }
    }
}
