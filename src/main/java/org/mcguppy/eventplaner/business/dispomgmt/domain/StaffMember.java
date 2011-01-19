package org.mcguppy.eventplaner.business.dispomgmt.domain;

import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;

/**
 *
 * @author stefan meichtry
 */
@Entity
@Table(name = "staffmember")
public class StaffMember extends MyEntityBase implements Comparable {

    @NotNull(message = "{notNull.error}")
    @Size(min = 1, message = "{sizeMin.error}")
    private String title;
    @NotNull(message = "{notNull.error}")
    @Size(min = 2, max = 50, message = "{sizeMinMax.error}")
    private String firstName;
    @NotNull(message = "{notNull.error}")
    @Size(min = 2, max = 50, message = "{sizeMinMax.error}")
    private String lastName;
    @Size(max = 50, message = "{sizeMax.error}")
    private String street;
    @Pattern(regexp = "[0-9]*", message = "{zipCode.error}")
    @Size(min = 4, max = 4, message = "{zipCode.error}")
    private String zip;
    @NotNull(message = "{notNull.error}")
    @Size(min = 2, max = 50, message = "{sizeMinMax.error}")
    private String city;
    @Pattern(regexp = "[0-9]{3}[ ]?[0-9]{3}[ ]?[0-9]{2}[ ]?[0-9]{2}", message = "{phoneNumber.error}")
    private String phoneNr;
    private String cellPhoneNr;
    @Email(message = "{mailAddress.error}")
    private String mailAddress;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "staffMembers")
    private Collection<Shift> shifts;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "responsible")
    private Collection<Shift> responsibleShifts;

    public String getCellPhoneNr() {
        return cellPhoneNr;
    }

    public void setCellPhoneNr(String cellPhoneNr) {
        this.cellPhoneNr = cellPhoneNr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Collection<Shift> getShifts() {
        return shifts;
    }

    public int getShiftsSize() {
        return shifts.size();
    }

    public void setShifts(Collection<Shift> shifts) {
        this.shifts = shifts;
    }

    public Collection<Shift> getResponsibleShifts() {
        return responsibleShifts;
    }

    public void setResponsibleShifts(Collection<Shift> responsibleShifts) {
        this.responsibleShifts = responsibleShifts;
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
        if (!(object instanceof StaffMember)) {
            return false;
        }
        StaffMember other = (StaffMember) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.mcguppy.eventplaner.business.dispomgmt.domain.StaffMember[id=" + this.getId() + "]";
    }

    @Override
    public int compareTo(Object o) {
        StaffMember staffMember = (StaffMember) o;
        return (this.lastName + this.firstName).compareTo(staffMember.lastName + staffMember.firstName);
    }
}
