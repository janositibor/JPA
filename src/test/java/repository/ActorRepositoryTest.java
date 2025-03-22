package repository;

import model.Actor;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ActorRepositoryTest {
    private EntityManagerFactory factory;
    private ActorRepository actorRepository;

    @BeforeEach
    void init() {
//        factory = Persistence.createEntityManagerFactory("MariaDB-pu");
        factory = Persistence.createEntityManagerFactory("H2-pu");
        actorRepository = new ActorRepository(factory);
    }
    @AfterEach
    void close(){
        factory.close();
    }
    @Test
    void saveTest() {
        Actor actor1 = new Actor("Scherer Peter", 1961);
        Actor actor2 = new Actor("Mucsi Zoltan", 1957);

        long id1 = actorRepository.save(actor1).getId();
        long id2 = actorRepository.save(actor2).getId();
        assertEquals(1L, id1);
        assertEquals(2L, id2);
    }

    @Test
    void findActorTest() {
        Actor actor1 = new Actor("Scherer Peter", 1961);
        Actor actor2 = new Actor("Mucsi Zoltan", 1957);

        long id1 = actorRepository.save(actor1).getId();
        long id2 = actorRepository.save(actor2).getId();

        Actor getActor2 = actorRepository.find(actor2).get();
        Actor getActor1 = actorRepository.find(actor1).get();

        assertEquals(id2, getActor2.getId());
        assertEquals(id1, getActor1.getId());
    }

    @Test
    void findActorByIdTest() {
        Actor actor1 = new Actor("Scherer Peter", 1961);
        Actor actor2 = new Actor("Mucsi Zoltan", 1957);

        actorRepository.save(actor1);
        actorRepository.save(actor2);

        Actor getActor2 = actorRepository.findById(actor2.getId()).get();
        Actor getActor1 = actorRepository.findById(actor1.getId()).get();

        assertEquals(actor1.getName(), getActor1.getName());
        assertEquals(actor2.getName(), getActor2.getName());
        assertEquals(actor1.getYob(), getActor1.getYob());
        assertEquals(actor2.getYob(), getActor2.getYob());
    }

    @Test
    void notFoundActor() {
        Actor actor = new Actor("Mucsi Zoltan", 1957);
        Optional<Actor> getActor = actorRepository.find(actor);
        assertFalse(getActor.isPresent());
    }

    @Test
    void findAllActorTest() {
        Actor actor1 = new Actor("Scherer Peter", 1961);
        Actor actor2 = new Actor("Mucsi Zoltan", 1957);

        actorRepository.save(actor1);
        actorRepository.save(actor2);

        List<Actor> actors = actorRepository.findAllActor();
        assertThat(actors)
                .hasSize(2)
                .extracting(Actor::getName, Actor::getYob)
                .containsExactly(Tuple.tuple("Scherer Peter", 1961), Tuple.tuple("Mucsi Zoltan", 1957));
    }

    @Test
    void updateActorTest() {
        Actor actor = new Actor("Scherer Peter", 1961);
        actorRepository.save(actor);
        Actor getActor = actorRepository.find(actor).get();
        getActor.setYob(1960);
        getActor.setName("Scherer Péter");
        actorRepository.save(getActor);
        Actor getActor2 = actorRepository.find(new Actor("Scherer Péter", 1960)).get();

        assertEquals(actor.getId(), getActor2.getId());
        assertThat(getActor2)
                .hasFieldOrPropertyWithValue("name", "Scherer Péter")
                .hasFieldOrPropertyWithValue("yob", 1960);
    }

    @Test
    void deleteActorTest() {
        Actor actor1 = new Actor("Scherer Peter", 1961);
        Actor actor2 = new Actor("Mucsi Zoltan", 1957);

        actorRepository.save(actor1);
        actorRepository.save(actor2);

        List<Actor> actors = actorRepository.findAllActor();
        assertThat(actors)
                .hasSize(2)
                .extracting(Actor::getName, Actor::getYob)
                .containsExactly(Tuple.tuple("Scherer Peter", 1961), Tuple.tuple("Mucsi Zoltan", 1957));

        actorRepository.delete(actor1.getId());

        actors = actorRepository.findAllActor();
        assertThat(actors)
                .hasSize(1)
                .extracting(Actor::getName, Actor::getYob)
                .containsExactly(Tuple.tuple("Mucsi Zoltan", 1957));
    }
}