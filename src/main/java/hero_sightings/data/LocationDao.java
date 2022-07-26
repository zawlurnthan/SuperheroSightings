package hero_sightings.data;

import hero_sightings.models.Hero;
import hero_sightings.models.Location;

import java.util.List;

public interface LocationDao {
    Location addLocation(Location location);
    List<Location> getAllLocations();
    Location getLocationById(int id);
    boolean updateLocation(Location location);
    boolean deleteLocation(int id);
}
