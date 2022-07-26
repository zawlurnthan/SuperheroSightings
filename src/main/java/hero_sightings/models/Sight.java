package hero_sightings.models;

import javax.validation.constraints.NotNull;
import java.sql.Date;

public class Sight {
    private int id;

//    @NotNull(message = "Date can't be null and write as format.")
    private Date date;

//    @NotNull(message = "Hero can't be null.")
    private Hero hero;

//    @NotNull(message = "Location can't be null.")
    private Location location;

    public Sight() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sight sight)) return false;

        if (getId() != sight.getId()) return false;
        if (!getDate().equals(sight.getDate())) return false;
        if (!getHero().equals(sight.getHero())) return false;
        return getLocation().equals(sight.getLocation());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getDate().hashCode();
        result = 31 * result + getHero().hashCode();
        result = 31 * result + getLocation().hashCode();
        return result;
    }
}
