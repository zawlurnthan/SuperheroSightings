package hero_sightings.controllers;

import hero_sightings.data.HeroDao;
import hero_sightings.data.LocationDao;
import hero_sightings.data.SightDao;
import hero_sightings.models.Sight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    SightDao sDao;
    HeroDao hDao;
    LocationDao lDao;

    @Autowired
    public HomeController(SightDao sDao, HeroDao hDao, LocationDao lDao) {
        this.sDao = sDao;
        this.hDao = hDao;
        this.lDao = lDao;
    }

    @GetMapping
    public String home(Model model) {
        List<Sight> sights = sDao.getAllSights().stream().limit(10).toList();
        model.addAttribute("sights", sights);
        return "index";
    }
}
