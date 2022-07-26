package hero_sightings.controllers;

import hero_sightings.data.HeroDao;
import hero_sightings.data.LocationDao;
import hero_sightings.data.SightDao;
import hero_sightings.models.Hero;
import hero_sightings.models.Location;
import hero_sightings.models.Sight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Date;
import java.util.List;

@Controller
public class SightController {

    SightDao dao;
    HeroDao hDao;
    LocationDao lDao;

    @Autowired
    public SightController(SightDao dao, HeroDao hDao, LocationDao lDao) {
        this.dao = dao;
        this.hDao = hDao;
        this.lDao = lDao;
    }

    @GetMapping("/sights")
    public String displaySights(Model model) {
        // get all sights, heroes and locations from database
        List<Sight> sights = dao.getAllSights();
        List<Hero> heroes = hDao.getAllHeroes();
        List<Location> locations = lDao.getAllLocations();

        // send them to the front
        model.addAttribute("sights", sights);
        model.addAttribute("heroes", heroes);
        model.addAttribute("locations", locations);

        return "sight/sights";
    }

    @PostMapping("/addSight")
    public String saveSight(Sight sight, HttpServletRequest request, BindingResult result) {
        // get hero id and location id
        int heroId = Integer.parseInt(request.getParameter("heroId"));
        int locationId = Integer.parseInt(request.getParameter("locationId"));

        // set hero and location of sighting
        sight.setHero(hDao.getHeroById(heroId));
        sight.setLocation(lDao.getLocationById(locationId));

        dao.addSight(sight);
        return "redirect:/sights";
    }

    @GetMapping("/sightDetail")
    public String sightDetail(Integer id, Model model) {
        Sight sight = dao.getSightById(id);
        model.addAttribute("sight", sight);
        return "sight/sightDetail";
    }

    @GetMapping("/deleteSightConfirm")
    // send confirmation page before deleting
    public String deleteConfirm(Integer id, Model model) {
        model.addAttribute("id", id);
        return "sight/deleteSight";
    }

    @GetMapping("/deleteSight")
    public String deleteSight(@RequestParam int id) {
        dao.deleteSight(id);
        return "redirect:/sights";
    }

    @GetMapping("/updateSight")
    public String updateSight(@RequestParam int id, Model model) {
        Sight sight = dao.getSightById(id);
        model.addAttribute("sight", sight);

        List<Hero> heroes = hDao.getAllHeroes();
        model.addAttribute("heroes", heroes);

        List<Location> locations = lDao.getAllLocations();
        model.addAttribute("locations", locations);

        return "sight/editSight";
    }

    @PostMapping("/updateSight")
    public String editSight(Sight sight,  HttpServletRequest request) {
        // get hero id and location id
        int heroId = Integer.parseInt(request.getParameter("heroId"));
        int locationId = Integer.parseInt(request.getParameter("locationId"));
        // set hero and location
        sight.setHero(hDao.getHeroById(heroId));
        sight.setLocation(lDao.getLocationById(locationId));

        dao.updateSight(sight);
        return "redirect:/sights";
    }

}
