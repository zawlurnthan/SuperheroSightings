package hero_sightings.controllers;

import hero_sightings.data.HeroDao;
import hero_sightings.data.LocationDao;
import hero_sightings.data.OrganizationDao;
import hero_sightings.models.Hero;
import hero_sightings.models.Location;
import hero_sightings.models.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

@Controller
public class OrganizationController {

    OrganizationDao dao;
    HeroDao hDao;
    LocationDao lDao;
    Set<ConstraintViolation<Organization>> violations = new HashSet<>();

    @Autowired
    public OrganizationController(OrganizationDao dao, HeroDao hDao, LocationDao lDao) {
        this.dao = dao;
        this.hDao = hDao;
        this.lDao = lDao;
    }

    @GetMapping("/organizations")
    public String displayOrganizations(Model model) {
        // get organizations form database
        List<Organization> organizations = dao.getAllOrganizations();
        List<Location> locations = lDao.getAllLocations();
        List<Hero> heroes = hDao.getAllHeroes();
        // send them to the front
        model.addAttribute("organizations", organizations);
        model.addAttribute("locations", locations);
        model.addAttribute("heroes", heroes);
        model.addAttribute("errors", violations);
        return "organization/organizations";
    }


    @PostMapping("/addOrganization")
    public String addOrganization(Organization organization, HttpServletRequest request) {
        // get location id
        int locationId = Integer.parseInt(request.getParameter("locationId"));
        // get a series of id string
        String[] memberIds = request.getParameterValues("heroId");

        System.out.println("Array Length: " + memberIds.length);

        List<Hero> heroes = Arrays.stream(memberIds).map(Integer::parseInt).map(x -> hDao.getHeroById(x)).toList();
        // set organization location and members
        organization.setLocation(lDao.getLocationById(locationId));
        organization.setMembers(heroes);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(organization);

        if(violations.isEmpty()) {
            // save organization to the database
            dao.addOrganization(organization);
        }
        return "redirect:/organizations";
    }

    @GetMapping("orgDetail")
    public String organizationDetail(Integer id, Model model) {
        Organization org = dao.getOrganizationById(id);
        model.addAttribute("org", org);
        return "organization/orgDetail";
    }

    @GetMapping("/deleteOrgConfirm")
    // send confirmation page before deleting
    public String deleteConfirm(Integer id, Model model) {
        model.addAttribute("id", id);
        return "organization/deleteOrganization";
    }

    @GetMapping("/deleteOrganization")
    public String deleteOrganization(Integer id) {
        dao.deleteOrganization(id);
        return "redirect:/organizations";
    }

    @GetMapping("/updateOrganization")
    public String updateOrganization(@RequestParam int id, Model model) {
        // get organization by id
        Organization organization = dao.getOrganizationById(id);
        model.addAttribute("organization", organization);

        List<Hero> heroes = hDao.getAllHeroes();
        model.addAttribute("heroes", heroes);

        List<Location> locations = lDao.getAllLocations();
        model.addAttribute("locations", locations);

        return "organization/editOrg";
    }

    @PostMapping("/updateOrganization")
    public String updateOrg(Organization organization, HttpServletRequest request, BindingResult result, Model model) {
        int locationId = Integer.parseInt(request.getParameter("locationId"));
        // set organization location and members
        organization.setLocation(lDao.getLocationById(locationId));

        // get a series of member id string
        String[] memberIds = request.getParameterValues("heroId");
        List<Hero> heroes = new ArrayList<>();

        if (memberIds != null) {
            heroes = Arrays.stream(memberIds).map(Integer::parseInt).map(x -> hDao.getHeroById(x)).toList();
        } else {
            FieldError error = new FieldError("organization", "members", "Must include one member");
            result.addError(error);
        }
        organization.setMembers(heroes);

        if (result.hasErrors()) {
            model.addAttribute("locations", lDao.getAllLocations());
            model.addAttribute("heroes", hDao.getAllHeroes());
            model.addAttribute("organization", organization);
        }

        dao.updateOrganization(organization);
        return "redirect:/organizations";
    }
}
