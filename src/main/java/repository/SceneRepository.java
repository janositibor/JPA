package repository;

import model.Scene;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Optional;

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
}
