package hero_sightings.data;

import hero_sightings.data.HeroDaoImpl.HeroMapper;
import hero_sightings.data.LocationDaoImpl.LocationMapper;
import hero_sightings.models.Hero;
import hero_sightings.models.Location;
import hero_sightings.models.Sight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.List;

@Repository
public class SightDaoImpl implements SightDao{

    JdbcTemplate jdbc;
    HeroDao hDao;
    LocationDao lDao;

    @Autowired
    public SightDaoImpl(JdbcTemplate jdbc, HeroDao hDao, LocationDao lDao) {
        this.jdbc = jdbc;
        this.hDao = hDao;
        this.lDao = lDao;
    }

    @Override
    @Transactional
    public Sight addSight(Sight sight) {
        final String sql = "INSERT INTO sight(date, heroId, locationId) VALUES(?, ?, ?)";
        jdbc.update(sql, sight.getDate(), sight.getHero().getId(), sight.getLocation().getId());

        // get id from database and set it to sight object
        int id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        sight.setId(id);
        return sight;
    }

    @Override
    public List<Sight> getAllSights() {
        final String sql = "SELECT * FROM sight;";
        return jdbc.query(sql, new SightMapper());
    }

    @Override
    public Sight getSightById(int id) {
        try {
            final String sql = "SELECT * FROM sight WHERE id = ?;";
            return jdbc.queryForObject(sql, new SightMapper(), id);
        }
        catch (DataAccessException ex) {
            return null;
        }
    }


    @Override
    public boolean updateSight(Sight sight) {
        final String sql = "UPDATE sight SET " +
                "date = ?, heroId = ?, " +
                "locationId = ? WHERE id = ?;";

        return jdbc.update(sql,
                sight.getDate(),
                sight.getHero().getId(),
                sight.getLocation().getId(),
                sight.getId()) > 0;
    }

    @Override
    public boolean deleteSight(int id) {
        final String sql = "DELETE FROM sight WHERE id = ?;";
        return jdbc.update(sql, id) > 0;
    }

    @Override
    public List<Hero> getHeroesByLocation(int locationId) {
        final String sql = "SELECT h.* FROM hero h JOIN sight s ON s.heroId = h.id WHERE s.locationId = ?";
        return jdbc.query(sql, new HeroDaoImpl.HeroMapper(), locationId);
    }

    @Override
    public List<Location> getLocationsByHero(int heroId) {
        final String sql = "SELECT * FROM location l JOIN sight s ON s.locationId = l.id WHERE s.heroId = ?";
        return jdbc.query(sql, new LocationMapper(), heroId);
    }

    @Override
    public List<Sight> getSightingByDate(Date date) {
        final String sql = "SELECT * FROM sight WHERE date = ?";
        return jdbc.query(sql, new SightMapper(), date);
    }

    public final class SightMapper implements RowMapper<Sight> {

        @Override
        public Sight mapRow(ResultSet rs, int rowNum) throws SQLException {
            Sight sight = new Sight();
            sight.setId(rs.getInt("id"));
            sight.setDate(rs.getDate("date"));
            sight.setHero(hDao.getHeroById(rs.getInt("heroId")));
            sight.setLocation(lDao.getLocationById(rs.getInt("locationId")));
            return sight;
        }
    }
}
