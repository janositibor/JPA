package repository;

import model.Movie;
import model.Show;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MovieRepository {
    private EntityManagerFactory entityManagerFactory;

    public MovieRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Movie save(Movie movie) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(movie);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return movie;
    }

    public Optional<Movie> find(Movie movie) {
        EntityManager em = entityManagerFactory.createEntityManager();
        String jpqlString = "SELECT m FROM Movie m WHERE m.title=:title AND m.releaseDate=:releaseDate";
        TypedQuery<Movie> query = em.createQuery(jpqlString, Movie.class);
        query.setParameter("title", movie.getTitle());
        query.setParameter("releaseDate", movie.getReleaseDate());
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException nre) {
            return Optional.empty();
        }
    }

    public Optional<Movie> findById(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Movie movie = em.find(Movie.class, id);
            return Optional.ofNullable(movie);
        } finally {
            em.close();
        }

    }

    public void update(Movie movie) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(movie);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void setRatings(Long id, List<Integer> ratings) {
        for (Integer rating : ratings) {
            if (rating > 10) {
                throw new IllegalStateException("Invalid rating: " + rating);
            }
        }
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            Movie movie = em.find(Movie.class, id);
            movie.setRatings(ratings);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Optional<Movie> findMovieWithRatings(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Movie movie = em.createQuery("SELECT m FROM Movie m LEFT JOIN FETCH m.ratings WHERE m.id=:id", Movie.class)
                    .setParameter("id", id)
                    .getSingleResult();
            movie.setNumberOfRatings(movie.getRatings().size());
            movie.setAverageOfRatings(movie.getRatings().stream().collect(Collectors.averagingInt(x -> x.intValue())));
            return Optional.of(movie);
        } catch (NoResultException nre) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
    public void setShows(Long id, List<Show> shows) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            Movie movie = em.find(Movie.class, id);
            movie.setShows(shows);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    public void addShow(Long id, Show show) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            Movie movie = em.find(Movie.class, id);
            movie.addShow(show);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    public Optional<Movie> findMovieWithShows(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Movie movie = em.createQuery("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.shows WHERE m.id=:id", Movie.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.of(movie);
        } catch (NoResultException nre) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

}
