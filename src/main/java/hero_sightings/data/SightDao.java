package hero_sightings.data;

import hero_sightings.models.Hero;
import hero_sightings.models.Location;
import hero_sightings.models.Sight;

import java.sql.Date;
import java.util.List;

public interface SightDao {
    Sight addSight(Sight sight);
    List<Sight> getAllSights();
    Sight getSightById(int id);
    boolean updateSight(Sight sight);
    boolean deleteSight(int id);

    // requirement
    List<Hero> getHeroesByLocation(int locationId);
    List<Location> getLocationsByHero(int heroId);
    List<Sight> getSightingByDate(Date date);
}
