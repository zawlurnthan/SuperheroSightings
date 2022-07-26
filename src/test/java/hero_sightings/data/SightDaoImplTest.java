package hero_sightings.data;

import hero_sightings.models.Hero;
import hero_sightings.models.Location;
import hero_sightings.models.Sight;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SightDaoImplTest {

    @Autowired
    SightDao dao;
    @Autowired
    HeroDao hDao;
    @Autowired
    LocationDao lDao;

    Sight sight;
    Hero hero;
    Location location;

    @BeforeEach
    void setUp() {
        // create hero and save it
        hero = new Hero();
        hero.setName("");
        hero.setPower("");
        hero.setDescription("");
        hDao.addHero(hero);

        // create location and save it
        location = new Location();
        location.setName("");
        location.setAddress("");
        location.setCoordinate("");
        location.setDescription("");
        lDao.addLocation(location);

        // create sight
        sight =  new Sight();
        sight.setDate(Date.valueOf(LocalDate.now()));
        sight.setHero(hero);
        sight.setLocation(location);
        dao.addSight(sight);
    }

    @AfterEach
    void tearDown() {
        List<Sight> sights = dao.getAllSights();
        for (Sight s : sights) {
            dao.deleteSight(s.getId());
        }

        List<Hero> heroes = hDao.getAllHeroes();
        for (Hero hero : heroes) {
            hDao.deleteHero(hero.getId());
        }

        List<Location> locations = lDao.getAllLocations();
        for (Location location : locations) {
            lDao.deleteLocation(location.getId());
        }
    }

    @Test
    void testAddSight() {
        Sight retrieved = dao.getSightById(sight.getId());
        assertEquals(retrieved, sight, "Check if two sights are equal");

        // invalid input test
        Sight invalid = new Sight();

        try {
            dao.addSight(invalid);
        } catch (NullPointerException e) {
            System.out.println("\nIt'll be always null.\n");
        }

        Sight invalidRetrieved = dao.getSightById(invalid.getId());
        assertNull(invalidRetrieved, "Should be null");
    }

    @Test
    void testGetAllSights() {
//        dao.addSight(sight);
        List<Sight> sights = dao.getAllSights();

        assertNotNull(sights, "Check if list is not null");
        assertEquals(1, sights.size(), "Check if the number of list size");
        assertTrue(sights.contains(sight), "Check if there is sight");
    }

    @Test
    void testGetSightById() {
        Sight retrieved = dao.getSightById(sight.getId());
        assertEquals(sight, retrieved, "check if two sights are equal");
    }

    @Test
    void testUpdateSight() {
        // get hero and location to compare
        Hero hero = sight.getHero();
        Location location = sight.getLocation();

        // create new hero
        Hero newHero = new Hero();
        newHero.setName("New Name");
        newHero.setPower("New Power");
        newHero.setDescription("New Description");
        hDao.addHero(newHero);

        // create new location
        Location newLocation = new Location();
        newLocation.setName("New Loc");
        newLocation.setAddress("new Add");
        newLocation.setCoordinate("New Coordinate");
        newLocation.setDescription("New Desc");
        lDao.addLocation(newLocation);

        // set new hero and new location
        sight.setHero(newHero);
        sight.setLocation(newLocation);

        boolean updated = dao.updateSight(sight);
        Sight retrieved = dao.getSightById(sight.getId());

        // check if updated info are same
        assertNotEquals(hero, retrieved.getHero(), "Check if heroes are same");
        assertNotEquals(location, retrieved.getLocation(), "Check if locations are same");

        assertTrue(true, "Check if update is true");
        assertTrue(updated, "Check if it's updated");
    }

    @Test
    void testDeleteSight() {
        boolean deleted = dao.deleteSight(sight.getId());

        assertTrue(deleted, "Check if it's delete");
        assertTrue(true, "check if delete is true");
    }

    @Test
    void getHeroesByLocation() {
        // get heroes by location
        List<Hero> heroes = dao.getHeroesByLocation(location.getId());

        assertNotNull(heroes, "Check if list is not null");
        assertEquals(1, heroes.size(), "Check if the number of list size");
        assertTrue(heroes.contains(hero), "Check if there is hero");
    }

    @Test
    void getLocationsByHero() {
        // get locations by hero
        List<Location> locations = dao.getLocationsByHero(hero.getId());

        assertNotNull(locations, "Check if list is not null");
        assertEquals(1, locations.size(), "Check if the number of list size");
        assertTrue(locations.contains(location), "Check if there is location");
    }

    @Test
    void getSightingByDate() {
        // get sights by date
        Date date = sight.getDate();
        List<Sight> sights = dao.getSightingByDate(date);

        assertNotNull(sights, "Check if list is not null");
        assertEquals(1, sights.size(), "Check if the number of list size");
        assertTrue(sights.contains(sight), "Check if there is sight");
    }
}