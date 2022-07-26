package hero_sightings.data;

import hero_sightings.models.Hero;
import hero_sightings.models.Location;
import hero_sightings.models.Organization;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrganizationDaoImplTest {

    @Autowired
    OrganizationDao dao;
    @Autowired
    LocationDao lDao;
    @Autowired
    HeroDao hDao;

    Organization org;
    Hero hero;
    Location location;

    @BeforeEach
    void setUp() {
        hero = new Hero();
        hero.setName("Hero");
        hero.setPower("");
        hero.setGoodGuy(true);
        hero.setDescription("");
        hDao.addHero(hero);

        List<Hero> members = new ArrayList<>();
        members.add(hero);

        location = new Location();
        location.setName("Stone Mountain");
        location.setAddress("213, abc st, Mesa");
        location.setCoordinate("0'1");
        location.setDescription("It's something awesome.");
        lDao.addLocation(location);

        org = new Organization();
        org.setName("");
        org.setLocation(location);
        org.setContactInfo("");
        org.setDescription("");
        org.setMembers(members);
        dao.addOrganization(org);
    }

    @AfterEach
    void tearDown() {
        List<Hero> heroes = hDao.getAllHeroes();
        for (Hero hero : heroes) {
            hDao.deleteHero(hero.getId());
        }

        List<Location> locations = lDao.getAllLocations();
        for (Location location : locations) {
            lDao.deleteLocation(location.getId());
        }

        List<Organization> organizations = dao.getAllOrganizations();
        for (Organization org : organizations) {
            dao.deleteOrganization(org.getId());
        }
    }

    @Test
    void addOrganizationTest() {
        Organization retrieved = dao.getOrganizationById(org.getId());
        assertEquals(retrieved, org, "Check if two organizations are equal");

        // invalid input test
        Organization invalid = new Organization();

        try {
            dao.addOrganization(invalid);
        } catch (NullPointerException e) {
            System.out.println("\nDefinitely null!\n");
        }

        Organization invalidRetrieved = dao.getOrganizationById(invalid.getId());
        assertNull(invalidRetrieved, "Should be null.");
    }

    @Test
    void getAllOrganizationsTest() {
        List<Organization> organizations = dao.getAllOrganizations();

        assertNotNull(organizations, "Check if list is not null");
        assertEquals(1, organizations.size(), "Check if the number of list size");
        assertTrue(organizations.contains(org), "Check if there is Organization");
    }

    @Test
    void getOrganizationByIdTest() {
        Organization retrieved = dao.getOrganizationById(org.getId());
        assertEquals(org, retrieved, "check if two organizations are equal");
    }

    @Test
    void updateOrganizationTest() {
        // get name and contact info to compare
        String name = org.getName();
        String contact = org.getContactInfo();

        // reset name and contact
        org.setName("Chicago Mob");
        org.setContactInfo("Good Well");

        boolean updated = dao.updateOrganization(org);
        Organization retrieved = dao.getOrganizationById(org.getId());

        // check if updated info are same
        assertNotEquals(name, retrieved.getName(), "Check if names are same");
        assertNotEquals(contact, retrieved.getContactInfo(), "Check if contacts are same");

        assertTrue(true, "Check if update is true");
        assertTrue(updated, "Check if it's updated");
    }

    @Test
    void deleteOrganizationTest() {
        boolean deleted = dao.deleteOrganization(org.getId());

        assertTrue(deleted, "Check if it's delete");
        assertTrue(true, "check if delete is true");
    }

    @Test
    void getHeroesByOrganization() {
        // get heroes by organization
        List<Hero> heroes = dao.getHeroesByOrganization(org.getId());

        assertNotNull(heroes, "Check if list is not null");
        assertEquals(1, heroes.size(), "Check if the number of list size");
        assertTrue(heroes.contains(hero), "Check if there is Hero");
    }

    @Test
    void getOrganizationByHero() {
        // get organizations by hero
        List<Organization> organizations = dao.getOrganizationByHero(hero.getId());

        assertNotNull(organizations, "Check if list is not null");
        assertEquals(1, organizations.size(), "Check if the number of list size");
        assertTrue(organizations.contains(org), "Check if there is Organization");
    }
}