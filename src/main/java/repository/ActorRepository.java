package repository;

import model.Actor;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class ActorRepository {
    private EntityManagerFactory entityManagerFactory;

    public ActorRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Actor save(Actor actor) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(actor);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return actor;
    }
    public void delete(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            Actor actorToDelete = em.getReference(Actor.class, id);
            em.remove(actorToDelete);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Optional<Actor> find(Actor actor) {
        EntityManager em = entityManagerFactory.createEntityManager();
        String jpqlString = "SELECT a FROM Actor a WHERE a.name=:name AND a.yob=:yob";
        TypedQuery<Actor> query = em.createQuery(jpqlString, Actor.class);
        query.setParameter("name", actor.getName());
        query.setParameter("yob", actor.getYob());
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException nre) {
            return Optional.empty();
        }
    }

    public Optional<Actor> findById(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Actor actor = em.find(Actor.class, id);
            return Optional.ofNullable(actor);
        } finally {
            em.close();
        }
    }

    public void update(Actor actor) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(actor);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }


    public List<Actor> findAllActor() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("select a from Actor a order by a.id", Actor.class).getResultList();
        } finally {
            em.close();
        }
    }
}
