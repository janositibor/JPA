package repository;

import model.Actor;
import model.ActorRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class ActorRepository{
    private JdbcTemplate jdbcTemplate;

    public ActorRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public Optional<Long> saveBasicAndGetGeneratedKey(Actor actor) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Number key;

        jdbcTemplate.update(new PreparedStatementCreator() {
                                @Override
                                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                    PreparedStatement ps =
                                            connection.prepareStatement("insert into actors(name,yob) values (?,?)",
                                                    Statement.RETURN_GENERATED_KEYS);
                                    ps.setString(1, actor.getName());
                                    ps.setInt(2, actor.getYob());
                                    return ps;
                                }
                            }, keyHolder
        );
        key=keyHolder.getKey();
        if(key!=null){
            return Optional.ofNullable(key.longValue());
        }
        else{
            throw new IllegalArgumentException("Error with actor saving!");
        }
    }

    public void updateActor(Actor actorToUpdate, Actor pattern){
        String sql = "UPDATE actors SET name = ?, yob=?  WHERE id = ?";
        jdbcTemplate.update(sql, pattern.getName(), pattern.getYob(),actorToUpdate.getId());
    }
    public void deleteActor(Long id){
        String sql = "DELETE FROM actors WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    public Optional<Actor> findActor(Actor actor) {
        List<Actor> actors=jdbcTemplate.query("select id, name,yob from actors where name LIKE ? and yob=?"
                , new ActorRowMapper()
                ,actor.getName(),actor.getYob());
        if(actors.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(actors.get(0));
    }

    public List<Actor> findAllActor(){
            return jdbcTemplate.query("select id, name, yob from actors order by id", new ActorRowMapper());
    }
}
