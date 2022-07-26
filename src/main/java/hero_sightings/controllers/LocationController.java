package hero_sightings.controllers;

import hero_sightings.data.LocationDao;
import hero_sightings.models.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class LocationController {

    LocationDao dao;
    Set<ConstraintViolation<Location>> violations = new HashSet<>();

    @Autowired
    public LocationController(LocationDao dao) {
        this.dao = dao;
    }

    @GetMapping("/locations")
    public String displayLocations(Model model) {
        List<Location> locations = dao.getAllLocations();
        model.addAttribute("locations" , locations);
        model.addAttribute("errors", violations);
        return "location/locations";
    }

    @PostMapping("/addLocation")
    public String saveLocation(Location location) {
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(location);

        if(violations.isEmpty()) {
            // save
            dao.addLocation(location);
        }
        return "redirect:/locations";
    }

    @GetMapping("/locationDetail")
    public String locationDetail(Integer id, Model model) {
        Location location = dao.getLocationById(id);
        model.addAttribute("location", location);
        return "location/locationDetail";
    }

    @GetMapping("/deleteLocationConfirm")
    // send confirmation page before deleting
    public String deleteConfirm(Integer id, Model model) {
        model.addAttribute("id", id);
        return "location/deleteLocation";
    }

    @GetMapping("/deleteLocation")
    public String deleteLocation(Integer id) {
        dao.deleteLocation(id);
        return "redirect:/locations";
    }

    @GetMapping("/updateLocation")
    public String updateLocation(@RequestParam int id, Model model) {
        Location location = dao.getLocationById(id);
        model.addAttribute("location", location);
        return "location/editLocation";
    }

    @PostMapping("/updateLocation")
    public String editLocation(@Valid Location location) {
        // save
        dao.updateLocation(location);
        return "redirect:/locations";
    }

}
