package hero_sightings.data;

import hero_sightings.data.HeroDaoImpl.HeroMapper;
import hero_sightings.data.LocationDaoImpl.LocationMapper;
import hero_sightings.models.Hero;
import hero_sightings.models.Location;
import hero_sightings.models.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.List;

@Repository
public class OrganizationDaoImpl implements OrganizationDao{

    JdbcTemplate jdbc;
    LocationDao dao;

    @Autowired
    public OrganizationDaoImpl(JdbcTemplate jdbc, LocationDao dao) {
        this.jdbc = jdbc;
        this.dao = dao;
    }

    // get location
    private Location getLocationForOrganization(int orgId) {
        final String sql = "SELECT l.* FROM location l JOIN organization o ON o.locationId = l.id WHERE o.id = ?";
        return jdbc.queryForObject(sql, new LocationMapper(), orgId);
    }

    // get all heroes associate with organization
    private List<Hero> getMembersForOrganization(int orgId) {
        final String sql = "SELECT h.* FROM hero h JOIN hero_organization ho ON h.id = ho.heroId WHERE ho.organizationId = ?";
        return jdbc.query(sql, new HeroMapper(), orgId);
    }

    // set all associate locations and members of each organization
    private void associateLocationAndHeroes(List<Organization> organizations) {
        for (Organization org : organizations) {
            org.setLocation(getLocationForOrganization(org.getId()));
            org.setMembers(getMembersForOrganization(org.getId()));
        }
    }

    // insert data into hero-organization bridge table
    private void insertMembersToOrganization(Organization org) {
        final String sql = "INSERT INTO hero_organization(heroId, organizationId) VALUES(?, ?)";
        for(Hero hero : org.getMembers()) {
            jdbc.update(sql, hero.getId(), org.getId());
        }
    }

    @Override
    public Organization getOrganizationById(int id) {
        try {
            final String sql = "SELECT * FROM organization WHERE id = ?;";
            Organization org = jdbc.queryForObject(sql, new OrganizationMapper(), id);
            if (org != null) {
                // set location and members of organization
                org.setLocation(getLocationForOrganization(id));
                org.setMembers(getMembersForOrganization(id));
            }
            return org;
        }
        catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Organization> getAllOrganizations() {
        final String sql = "SELECT * FROM organization;";
        List<Organization> organizations = jdbc.query(sql, new OrganizationMapper());
        // set locations and members for each organization
        associateLocationAndHeroes(organizations);
        return organizations;
    }

    @Override
    @Transactional
    public Organization addOrganization(Organization org) {
        final String sql = "INSERT INTO organization(name, locationId, contactInfo, description) VALUES(?, ?, ?, ?);";
        jdbc.update(sql, org.getName(), org.getLocation().getId(), org.getContactInfo(), org.getDescription());

        // get id from database and set it to organization
        int id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        org.setId(id);
        // set members
        insertMembersToOrganization(org);
        return org;
    }

    @Override
    @Transactional
    public boolean updateOrganization(Organization org) {
        final String sql = "UPDATE organization SET " +
                "name = ?, " +
                "locationId = ?, " +
                "contactInfo = ?, " +
                "description = ? " +
                "WHERE id = ?;";

        int update = jdbc.update(sql,
                org.getName(),
                org.getLocation().getId(),
                org.getContactInfo(),
                org.getDescription(),
                org.getId());

        // delete old relations
        final String DELETE_HERO_ORGANIZATION = "DELETE FROM hero_organization WHERE organizationId = ?";
        jdbc.update(DELETE_HERO_ORGANIZATION, org.getId());
        // reset members
        insertMembersToOrganization(org);
        return update > 0;
    }

    @Override
    @Transactional
    public boolean deleteOrganization(int id) {
        // delete hero_organization first
        final String DELETE_HERO_ORGANIZATION = "DELETE FROM hero_organization WHERE organizationId = ?";
        jdbc.update(DELETE_HERO_ORGANIZATION, id);

        // delete organization then
        final String DELETE_ORGANIZATION = "DELETE FROM organization WHERE id = ?;";
        return jdbc.update(DELETE_ORGANIZATION, id) > 0;
    }

    @Override
    public List<Hero> getHeroesByOrganization(int orgId) {
        final String sql = "SELECT h.* FROM hero h JOIN hero_organization ho ON ho.heroId = h.id WHERE ho.organizationId = ?";
        return jdbc.query(sql, new HeroMapper(), orgId);
    }

    @Override
    public List<Organization> getOrganizationByHero(int heroId) {
        final String sql = "SELECT o.* FROM organization o JOIN hero_organization ho ON ho.organizationId = o.id WHERE ho.heroId = ?";
        List<Organization> organizations = jdbc.query(sql, new OrganizationMapper(), heroId);
        // set locations and members for each organization
        associateLocationAndHeroes(organizations);
        return organizations;
    }

    public static final class OrganizationMapper implements RowMapper<Organization> {
        @Override
        public Organization mapRow(ResultSet rs, int rowNum) throws SQLException {
            Organization org = new Organization();
            org.setId(rs.getInt("id"));
            org.setName(rs.getString("name"));
            org.setContactInfo(rs.getString("contactInfo"));
            org.setDescription(rs.getString("description"));
            return org;
        }
    }
}
