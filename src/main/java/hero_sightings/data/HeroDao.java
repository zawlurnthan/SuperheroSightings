package hero_sightings.data;

import hero_sightings.models.Hero;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface HeroDao {
    Hero addHero(Hero hero);
    List<Hero> getAllHeroes();
    Hero getHeroById(int id);
    boolean updateHero(Hero hero);
    boolean deleteHero(int id);

    void saveImage(MultipartFile imageFile);
}
