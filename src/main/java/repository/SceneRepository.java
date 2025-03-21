package repository;

import model.Movie;
import model.Scene;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Optional;
import java.util.List;

public class SceneRepository {
    private EntityManagerFactory entityManagerFactory;

    public SceneRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Scene save(Scene scene) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            if (scene.getId() == null) {
                em.persist(scene);
            } else {
                em.merge(scene);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return scene;
    }

    public Optional<Scene> findById(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Scene scene = em.find(Scene.class, id);
            return Optional.ofNullable(scene);
        } finally {
            em.close();
        }
    }

    public Optional<Scene> find(Scene scene) {
        EntityManager em = entityManagerFactory.createEntityManager();
        String jpqlString = "SELECT s FROM Scene s WHERE s.title=:title AND s.description=:description AND s.length=:length";
        TypedQuery<Scene> query = em.createQuery(jpqlString, Scene.class);
        query.setParameter("title", scene.getTitle());
        query.setParameter("description", scene.getDescription());
        query.setParameter("length", scene.getLength());
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException nre) {
            return Optional.empty();
        }
    }
    public List<Scene> findByMovieId(Long movieId){
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT s FROM Scene s WHERE s.movie.id=:id ORDER BY s.id", Scene.class)
                    .setParameter("id",movieId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    public List<Scene> findLongerThan(int length){
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT s FROM Scene s WHERE s.length>:length ORDER BY s.id", Scene.class)
                    .setParameter("length",length)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    public List<Scene> findByLengthAndMovieTitle(int length,String movieTitle){
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT s FROM Scene s WHERE s.length=:length AND s.movie.title=:movieTitle ORDER BY s.id", Scene.class)
                    .setParameter("length",length)
                    .setParameter("movieTitle",movieTitle)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    public List<Movie> findMoviesWithScenesMoreThan(int numberOfScenes){
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT m FROM Movie m WHERE SIZE(m.scenes)>:numberOfScenes ORDER BY m.id", Movie.class)
                    .setParameter("numberOfScenes",numberOfScenes)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    public Movie findMovieWithTheMostScenes(){
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT m FROM Movie m WHERE SIZE(m.scenes)=(SELECT MAX(SIZE(m2.scenes)) FROM Movie m2)", Movie.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
    public List<Scene> findScenesFromMovieWithTheMostScenes(){
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT s FROM Scene s WHERE s.movie=(SELECT m FROM Movie m WHERE SIZE(m.scenes)=(SELECT MAX(SIZE(m2.scenes)) FROM Movie m2)) ORDER BY s.id", Scene.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Movie findMovieWithSceneTitle(String sceneTitle){
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT m FROM Movie m WHERE m = (SELECT s.movie FROM Scene s WHERE s.title=:sceneTitle)", Movie.class)
                    .setParameter("sceneTitle",sceneTitle)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
    public Double findAverageNumberOfScenes(){
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT AVG(SIZE(m.scenes)) FROM Movie m", Double.class)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}
