package hero_sightings.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Hero {
    private int id;
//    @NotBlank(message = "First name must not be empty.")
//    @Size(max = 25, message = "Name must be less than 25 characters.")
    private String name;

    private boolean goodGuy;

//    @NotBlank(message = "Power must not be empty.")
//    @Size(max = 25, message = "Power must be less than 25 characters.")
    private String power;

//    @Size(max = 200, message = "Description must be less than 200 characters.")
    private String description;

//    @Size(max = 60, message = "Photo name must be less than 60 characters, " +
//            "\nand file size should not be grater than 1024KB.")
    private String photo;

    public Hero() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGoodGuy() {
        return goodGuy;
    }

    public void setGoodGuy(boolean goodGuy) {
        this.goodGuy = goodGuy;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hero hero)) return false;

        if (getId() != hero.getId()) return false;
        if (isGoodGuy() != hero.isGoodGuy()) return false;
        if (!getName().equals(hero.getName())) return false;
        if (!getPower().equals(hero.getPower())) return false;
        if (!getDescription().equals(hero.getDescription())) return false;
        return getPhoto() != null ? getPhoto().equals(hero.getPhoto()) : hero.getPhoto() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getName().hashCode();
        result = 31 * result + (isGoodGuy() ? 1 : 0);
        result = 31 * result + getPower().hashCode();
        result = 31 * result + getDescription().hashCode();
        result = 31 * result + (getPhoto() != null ? getPhoto().hashCode() : 0);
        return result;
    }
}
