package hero_sightings.data;

import hero_sightings.models.Hero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;

@Repository
public class HeroDaoImpl implements HeroDao{

    private final JdbcTemplate jdbc;

    @Autowired
    public HeroDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    @Transactional
    public Hero addHero(Hero hero) {
        final String sql = "INSERT INTO hero(name, goodGuy, power, description, photo) VALUES(?, ?, ?, ?, ?);";
        jdbc.update(sql, hero.getName(), hero.isGoodGuy(), hero.getPower(), hero.getDescription(), hero.getPhoto());

        int id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        hero.setId(id);
        return hero;
    }

    @Override
    public List<Hero> getAllHeroes() {
        final String sql = "SELECT * FROM hero;";
        return jdbc.query(sql, new HeroMapper());
    }

    @Override
    public Hero getHeroById(int id) {
        // catch exception if object is null
        try {
            final String sql = "SELECT * FROM hero WHERE id = ?;";
            return jdbc.queryForObject(sql, new HeroMapper(), id);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public boolean updateHero(Hero hero) {
        final String sql = "UPDATE hero SET name = ?, goodGuy = ?, power = ?, description = ?, photo = ? WHERE id = ?";
        return jdbc.update(sql, hero.getName(), hero.isGoodGuy(), hero.getPower(), hero.getDescription(), hero.getPhoto(), hero.getId()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteHero(int id) {
        // delete hero-organization first
        final String DELETE_HERO_ORGANIZATION_BY_HERO = "DELETE ho.* FROM hero_organization ho JOIN hero h ON ho.heroId = h.id WHERE h.id = ?";
        jdbc.update(DELETE_HERO_ORGANIZATION_BY_HERO, id);
        // delete sight second
        final String DELETE_SIGHT_BY_HERO = "DELETE FROM sight WHERE heroId = ?;";
        jdbc.update(DELETE_SIGHT_BY_HERO, id);
        // then delete hero
        final String DELETE_HERO = "DELETE FROM hero WHERE id = ?;";
        return jdbc.update(DELETE_HERO, id) > 0;
    }

    @Override
    public void saveImage(MultipartFile imageFile) {
        String pathStr = "src/main/resources/static/img/";
        try {
            // convert image as byte array
            byte[] bytes = imageFile.getBytes();
            // get file path with filename
            Path path = Paths.get(pathStr + imageFile.getOriginalFilename());
            Files.write(path, bytes);
        } catch (IOException e) {
            System.out.println("Image's required!");
        }
    }

    // convert data from database into Hero object
    public static final class HeroMapper implements RowMapper<Hero> {

        @Override
        public Hero mapRow(ResultSet rs, int rowNum) throws SQLException {
            Hero hero = new Hero();
            hero.setId(rs.getInt("id"));
            hero.setName(rs.getNString("name"));
            hero.setGoodGuy(rs.getBoolean("goodGuy"));
            hero.setPower(rs.getString("power"));
            hero.setDescription(rs.getString("description"));
            return hero;
        }
    }
}
