package hero_sightings.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Location {
    private int id;

    @NotBlank(message = "Name must not be empty.")
    @Size(max = 25, message = "Name must be less than 25 characters.")
    private String name;

    @NotBlank(message = "Address must not be empty.")
    @Size(max = 50, message = "Address must be less than 50 characters.")
    private String address;

    @Size(max = 25, message = "Coordinate must be less than 25 characters.")
    private String coordinate;

    @Size(max = 200, message = "Description must be less than 200 characters.")
    private String description;

    public Location() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Location location)) return false;
//
//        if (getId() != location.getId()) return false;
//        if (!getName().equals(location.getName())) return false;
//        if (!getAddress().equals(location.getAddress())) return false;
//        if (!getCoordinate().equals(location.getCoordinate())) return false;
//        return getDescription().equals(location.getDescription());
//    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getAddress().hashCode();
        result = 31 * result + getCoordinate().hashCode();
        result = 31 * result + getDescription().hashCode();
        return result;
    }
}
