package hero_sightings.controllers;

import hero_sightings.data.HeroDao;
import hero_sightings.models.Hero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class HeroController {

    HeroDao dao;
    Set<ConstraintViolation<Hero>> violations = new HashSet<>();

    @Autowired
    public HeroController(HeroDao dao) {
        this.dao = dao;
    }

    @GetMapping("/heroes")
    public String displayHeroes(Model model) {
        // collect heroes from database
        List<Hero> heroes = dao.getAllHeroes();
        // send them to the front
        model.addAttribute("heroes", heroes);
        model.addAttribute("errors", violations);
        return "hero/heroes";
    }

    @PostMapping("/addHero")
    public String saveHero(Hero hero, @RequestParam("image")MultipartFile image) {
        // set file name
        String fileName = image.getOriginalFilename();
        hero.setPhoto(fileName);
        // save image into the img folder
        dao.saveImage(image);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(hero);

        if(violations.isEmpty()) {
            // save hero into database
            dao.addHero(hero);
        }
        return "redirect:/heroes";
    }

    @GetMapping("/heroDetail")
    public String heroDetail(Integer id, Model model) {
        Hero hero = dao.getHeroById(id);
        model.addAttribute("hero", hero);
        return "hero/heroDetail";
    }

    @GetMapping("/deleteHeroConfirm")
    // send confirmation page before deleting
    public String deleteConfirm(Integer id, Model model) {
        model.addAttribute("id", id);
        return "hero/deleteHero";
    }

    @GetMapping("/deleteHero")
    // delete hero
    public String deleteHero(Integer id) {
        dao.deleteHero(id);
        return "redirect:/heroes";
    }

    @GetMapping("/updateHero")
    public String editHero(@RequestParam int id, Model model) {
        // get hero by id from database
        Hero hero = dao.getHeroById(id);
        // sent hero to front
        model.addAttribute("hero", hero);
        return "hero/editHero";
    }

    @PostMapping("/updateHero")
    public String editHero(@Valid Hero hero, @RequestParam("image")MultipartFile image, BindingResult result) {
        if (result.hasErrors()) {
            return "hero/editHero";
        }
        // set file path with folder and file name
        String fileName = image.getOriginalFilename();
        hero.setPhoto(fileName);
        // save image into the img folder
        dao.saveImage(image);

        // update hero
        dao.updateHero(hero);
        return "redirect:/heroes";
    }
}
