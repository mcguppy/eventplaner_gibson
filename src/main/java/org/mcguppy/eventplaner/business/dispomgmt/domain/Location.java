package org.mcguppy.eventplaner.business.dispomgmt.domain;

import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author stefan meichtry
 */
@Entity
@Table(name="location")
public class Location extends MyEntityBase implements Comparable {
    @NotNull
    private String locationName;
    private String description;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
    private Collection<Shift> shifts;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Collection<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(Collection<Shift> shifts) {
        this.shifts = shifts;
    }

    public int getShiftsSize() {
        if (null == this.shifts) {
            return 0;
        }
        return this.shifts.size();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Location)) {
            return false;
        }
        Location other = (Location) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.mcguppy.eventplaner.business.dispomgmt.domain.Location[id=" + this.getId() + "]";
    }

    @Override
    public int compareTo(Object o) {
         Location location = (Location) o;
        return this.locationName.compareTo(location.locationName);
    }
}
