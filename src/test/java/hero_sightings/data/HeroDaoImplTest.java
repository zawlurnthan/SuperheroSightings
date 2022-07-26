package hero_sightings.data;

import hero_sightings.models.Hero;
import hero_sightings.models.Sight;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HeroDaoImplTest {

    @Autowired
    HeroDao dao;
    @Autowired
    SightDao sDao;
    Hero hero;

    @BeforeEach
    void setUp() {
        hero = new Hero();
        hero.setName("Power Ranger");
        hero.setPower("fast");
        hero.setGoodGuy(true);
        hero.setDescription("");
        dao.addHero(hero);
    }

    @AfterEach
    void tearDown() {
        List<Sight> sights = sDao.getAllSights();
        for (Sight sight : sights) {
            sDao.deleteSight(sight.getId());
        }

        List<Hero> heroes = dao.getAllHeroes();
        for (Hero hero : heroes) {
            dao.deleteHero(hero.getId());
        }
    }

    @Test
    void addHeroTest() {
        Hero retrievedHero = dao.getHeroById(hero.getId());
        assertEquals(hero, retrievedHero, "check if two heroes are equal");

        // invalid input test
        Hero invalidHero = new Hero();

        try {
            dao.addHero(invalidHero);
        } catch (DataIntegrityViolationException ex) {
            System.out.println("\nThis's bad input!\n");
        }

        Hero invalidRetrieved = dao.getHeroById(invalidHero.getId());
        assertNull(invalidRetrieved, "Result should be null");
    }

    @Test
    void getAllHeroesTest() {
        List<Hero> heroes = dao.getAllHeroes();

        assertNotNull(heroes, "list should not null");
        assertEquals(1, heroes.size(), "check the size of teh list");
        assertTrue(heroes.contains(hero), "check if there is hero");
    }

    @Test
    void getHeroByIdTest() {
        Hero retrievedHero = dao.getHeroById(hero.getId());
        assertEquals(hero, retrievedHero, "check if two heroes are equal");
    }

    @Test
    void updateHeroTest() {
        // get name and power to compare
        String name = hero.getName();
        String power = hero.getPower();

        // reset name and power of hero
        hero.setName("Captain America");
        hero.setPower("electric");

        // update hero and retrieve it
        boolean updated = dao.updateHero(hero);
        Hero retrieved = dao.getHeroById(hero.getId());

        // check if updated info are same
        assertNotEquals(name, retrieved.getName(), "Check if names are same");
        assertNotEquals(power, retrieved.getPower(), "Check if powers are same");

        assertTrue(true, "Check if update is true");
        assertTrue(updated, "Check if it's updated");

        assertEquals(hero, retrieved, "Check if it's same object");
    }

    @Test
    void deleteHeroTest() {
        boolean deleted = dao.deleteHero(hero.getId());

        assertTrue(deleted, "Check if it's delete");
        assertTrue(true, "check if delete is true");
    }
}