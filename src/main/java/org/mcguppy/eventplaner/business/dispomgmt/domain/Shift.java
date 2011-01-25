package org.mcguppy.eventplaner.business.dispomgmt.domain;

import java.util.Collection;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author stefan meichtry
 */
@Entity
@Table(name = "SHIFT")
public class Shift extends MyEntityBase implements Comparable {

    @NotNull
    @Column(name = "REQUIRED_STAFF_NUMBER")
    private int requiredStaffNumber;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TIME")
    private Date startTime;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME")
    private Date endTime;
    @Column(name = "DESCRIPTION")
    private String description;
    @ManyToOne
    @JoinColumn
    @NotNull
    private Location location;
    @ManyToMany(fetch = FetchType.LAZY)
    Collection<StaffMember> staffMembers;
    @ManyToOne
    private StaffMember responsibleStaffMember;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getStaffNr() {
        return requiredStaffNumber;
    }

    public void setRequiredStaffNumber(int staffNr) {
        this.requiredStaffNumber = staffNr;
    }

    public int getRequiredStaffNumber() {
        return requiredStaffNumber;
    }
   
    public Collection<StaffMember> getStaffMembers() {
        return staffMembers;
    }

    public void setStaffMembers(Collection<StaffMember> staffMembers) {
        this.staffMembers = staffMembers;
    }

    public int getStaffMembersSize() {
        return staffMembers.size();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public StaffMember getResponsibleStaffMember() {
        return responsibleStaffMember;
    }

    public void setResponsibleStaffMember(StaffMember responsible) {
        this.responsibleStaffMember = responsible;
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
        if (!(object instanceof Shift)) {
            return false;
        }
        Shift other = (Shift) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.mcguppy.eventplaner.business.dispomgmt.domwin.Shift[id=" + this.getId() + "]";
    }

    @Override
    public int compareTo(Object o) {
        Shift shift = (Shift) o;
        if (this.startTime.compareTo(shift.startTime) == 0) {
            return this.endTime.compareTo(shift.endTime);
        }
        return this.startTime.compareTo(shift.startTime);
    }
}
