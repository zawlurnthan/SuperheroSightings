package hero_sightings.data;

import hero_sightings.models.Location;
import hero_sightings.models.Organization;
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
class LocationDaoImplTest {

    @Autowired
    LocationDao dao;
    @Autowired
    SightDao sDao;
    @Autowired
    OrganizationDao oDao;
    Location location;

    @BeforeEach
    void setUp() {
        location = new Location();
        location.setName("");
        location.setAddress("");
        location.setCoordinate("");
        location.setDescription("");
        dao.addLocation(location);
    }

    @AfterEach
    void tearDown() {
        List<Sight> sights = sDao.getAllSights();
        for (Sight sight : sights) {
            sDao.deleteSight(sight.getId());
        }

        List<Organization> organizations = oDao.getAllOrganizations();
        for (Organization org : organizations) {
            oDao.deleteOrganization(org.getId());
        }

        List<Location> locations = dao.getAllLocations();
        for (Location location : locations) {
            dao.deleteLocation(location.getId());
        }
    }

    @Test
    void testAddLocation() {
        Location retrievedLocation = dao.getLocationById(location.getId());
        assertEquals(location, retrievedLocation, "Check if two locations are equal");
        assertNotNull(retrievedLocation, "Result should not be null");

        // invalid input test
        Location invalid = new Location();

        try {
            dao.addLocation(invalid);
        } catch (DataIntegrityViolationException ex) {
            System.out.println("\nThis must be null!\n");
        }

        Location invalidRetrieved = dao.getLocationById(invalid.getId());
        assertNull(invalidRetrieved, "Result should be null");
    }

    @Test
    void testGetAllLocations() {
        List<Location> locations = dao.getAllLocations();

        assertNotNull(locations, "Check if list is not null");
        assertEquals(1, locations.size(), "Check if the number of list size");
        assertTrue(locations.contains(location), "Check if there is location");
    }

    @Test
    void testGetLocationById() {
        Location retrieved = dao.getLocationById(location.getId());
        assertEquals(location, retrieved, "check if two locations are equal");
    }

    @Test
    void testUpdateLocation() {
        // get name and address to compare
        String name = location.getName();
        String address = location.getAddress();

        // reset name and address of location
        location.setName("Stone Mountain");
        location.setAddress("Georgia");

        boolean updated = dao.updateLocation(location);
        Location retrieved = dao.getLocationById(location.getId());

        // check if updated info are same
        assertNotEquals(name, retrieved.getName(), "Check if names are same");
        assertNotEquals(address, retrieved.getAddress(), "Check if address are same");

        assertTrue(true, "Check if update is true");
        assertTrue(updated, "Check if it's updated");
    }

    @Test
    void testDeleteLocation() {
        boolean deleted = dao.deleteLocation(location.getId());
        assertTrue(deleted, "Check if it's delete");
        assertTrue(true, "check if delete is true");

        Location deletedLocation = dao.getLocationById(location.getId());
        assertNull(deletedLocation, "Result should be null");
    }
}