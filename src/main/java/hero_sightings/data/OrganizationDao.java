package hero_sightings.data;

import hero_sightings.models.Hero;
import hero_sightings.models.Organization;

import java.util.List;

public interface OrganizationDao {
    Organization getOrganizationById(int id);
    List<Organization> getAllOrganizations();
    Organization addOrganization(Organization organization);
    boolean updateOrganization(Organization organization);
    boolean deleteOrganization(int id);

    // requirement
    List<Hero> getHeroesByOrganization(int orgId);
    List<Organization> getOrganizationByHero(int heroId);
}
