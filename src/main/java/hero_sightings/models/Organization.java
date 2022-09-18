package hero_sightings.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class Organization {
    private int id;

//    @NotBlank(message = "Name must not be empty.")
//    @Size(max = 25, message = "Name must be less than 25 characters.")
    private String name;

//    @NotNull(message = "Location can't be null.")
    private Location location;

//    @Size(max = 50, message = "Contact Info must be less than 50 characters.")
    private String contactInfo;

//    @Size(max = 200, message = "Description must be less than 200 characters.")
    private String description;

//    @NotNull(message = "Members can't be null.")
    List<Hero> members;

    public Organization() {
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Hero> getMembers() {
        return members;
    }

    public void setMembers(List<Hero> members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organization org)) return false;

        if (getId() != org.getId()) return false;
        if (!getName().equals(org.getName())) return false;
        if (!getLocation().equals(org.getLocation())) return false;
        if (!getContactInfo().equals(org.getContactInfo())) return false;
        if (!getDescription().equals(org.getDescription())) return false;
        return getMembers().equals(org.getMembers());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getLocation().hashCode();
        result = 31 * result + getContactInfo().hashCode();
        result = 31 * result + getDescription().hashCode();
        result = 31 * result + getMembers().hashCode();
        return result;
    }
}
