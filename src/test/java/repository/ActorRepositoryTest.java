package repository;

import model.Actor;
import org.assertj.core.groups.Tuple;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ActorRepositoryTest {
    private DB db=new DB("testdb","sa","");
    private ActorRepository actorRepository;
    @BeforeEach
    void init(){
        Flyway flyway=Flyway.configure().dataSource(db.getDataSource()).load();
        flyway.clean();
        flyway.migrate();
        actorRepository=new ActorRepository(db.getDataSource());
    }
    @Test
    void saveTest(){
        Actor actor1=new Actor("Scherer Peter",1961);
        long id1=actorRepository.saveBasicAndGetGeneratedKey(actor1).get();
        assertEquals(1L,id1);
        Actor actor2=new Actor("Mucsi Zoltan",1957);
        long id2=actorRepository.saveBasicAndGetGeneratedKey(actor2).get();
        assertEquals(2L,id2);
    }

    @Test
    void findActorTest(){
        Actor actor1=new Actor("Scherer Peter",1961);
        long id1=actorRepository.saveBasicAndGetGeneratedKey(actor1).get();
        Actor actor2=new Actor("Mucsi Zoltan",1957);
        long id2=actorRepository.saveBasicAndGetGeneratedKey(actor2).get();

        Actor getActor2=actorRepository.findActor(actor2).get();
        Actor getActor1=actorRepository.findActor(actor1).get();
        assertEquals(2L,getActor2.getId());
        assertEquals(1L,getActor1.getId());
    }
    @Test
    void notFoundActor(){
        Actor actor=new Actor("Mucsi Zoltan",1957);
        Optional<Actor> getActor=actorRepository.findActor(actor);
        assertFalse(getActor.isPresent());
    }
    @Test
    void findAllActorTest(){
        Actor actor1=new Actor("Scherer Peter",1961);
        long id1=actorRepository.saveBasicAndGetGeneratedKey(actor1).get();
        Actor actor2=new Actor("Mucsi Zoltan",1957);
        long id2=actorRepository.saveBasicAndGetGeneratedKey(actor2).get();

        List<Actor> actors=actorRepository.findAllActor();
        assertThat(actors)
                .hasSize(2)
                .extracting(Actor::getName,Actor::getYob)
                .containsExactly(Tuple.tuple("Scherer Peter",1961),Tuple.tuple("Mucsi Zoltan",1957));
    }
    @Test
    void updateActorTest(){
        Actor actor=new Actor("Scherer Peter",1960);
        actorRepository.saveBasicAndGetGeneratedKey(actor).get();
        Actor getActor=actorRepository.findActor(actor).get();
        actorRepository.updateActor(getActor,new Actor("Scherer Péter",1961));
        Actor getActor2=actorRepository.findActor(new Actor("Scherer Péter",1961)).get();

        assertEquals(1L,getActor2.getId());
        assertThat(getActor2)
                .hasFieldOrPropertyWithValue("name","Scherer Péter")
                .hasFieldOrPropertyWithValue("yob",1961);
    }
    @Test
    void deleteActorTest(){
        Actor actor1=new Actor("Scherer Peter",1961);
        long id1=actorRepository.saveBasicAndGetGeneratedKey(actor1).get();
        Actor actor2=new Actor("Mucsi Zoltan",1957);
        long id2=actorRepository.saveBasicAndGetGeneratedKey(actor2).get();

        List<Actor> actors=actorRepository.findAllActor();
        assertThat(actors)
                .hasSize(2)
                .extracting(Actor::getName,Actor::getYob)
                .containsExactly(Tuple.tuple("Scherer Peter",1961),Tuple.tuple("Mucsi Zoltan",1957));

        Actor getActor1=actorRepository.findActor(actor1).get();
        actorRepository.deleteActor(getActor1.getId());

        actors=actorRepository.findAllActor();
        assertThat(actors)
                .hasSize(1)
                .extracting(Actor::getName,Actor::getYob)
                .containsExactly(Tuple.tuple("Mucsi Zoltan",1957));
    }
}