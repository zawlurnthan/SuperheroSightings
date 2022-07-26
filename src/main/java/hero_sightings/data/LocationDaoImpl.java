package hero_sightings.data;

import hero_sightings.models.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.List;

@Repository
public class LocationDaoImpl implements LocationDao{

    JdbcTemplate jdbc;

    @Autowired
    public LocationDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    @Transactional
    public Location addLocation(Location location) {
        final String sql = "INSERT INTO location(name, address, coordinate, description) VALUES(?, ?, ?, ?);";
        jdbc.update(sql, location.getName(), location.getAddress(), location.getCoordinate(), location.getDescription());

        int id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        location.setId(id);
        return location;
    }

    @Override
    public List<Location> getAllLocations() {
        final String sql = "SELECT * FROM location;";
        return jdbc.query(sql, new LocationMapper());
    }

    @Override
    public Location getLocationById(int id) {
        // catch exception if location is null
        try {
            final String sql = "SELECT * FROM location WHERE id = ?;";
            return jdbc.queryForObject(sql, new LocationMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }

    }

    @Override
    public boolean updateLocation(Location location) {
        final String sql = "UPDATE location SET name = ?, " +
                "address = ?, coordinate = ?, " +
                "description = ? WHERE id = ?;";

        return jdbc.update(sql,
                location.getName(),
                location.getAddress(),
                location.getCoordinate(),
                location.getDescription(),
                location.getId()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteLocation(int id) {
        // delete sight first in order to delete location
        final String DELETE_SIGHT_BY_LOCATION = "DELETE FROM sight WHERE locationId = ?";
        jdbc.update(DELETE_SIGHT_BY_LOCATION, id);

        //delete organization
        final String DELETE_ORGANIZATION = "DELETE FROM organization WHERE locationId = ?";
        jdbc.update(DELETE_ORGANIZATION, id);

        // then delete location
        final String DELETE_LOCATION = "DELETE FROM location WHERE id = ?;";
        return jdbc.update(DELETE_LOCATION, id) > 0;
    }

    public static final class LocationMapper implements RowMapper<Location> {

        @Override
        public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
            Location location = new Location();
            location.setId(rs.getInt("id"));
            location.setName(rs.getString("name"));
            location.setAddress(rs.getString("address"));
            location.setCoordinate(rs.getString("coordinate"));
            location.setDescription(rs.getString("description"));
            return location;
        }
    }
}
