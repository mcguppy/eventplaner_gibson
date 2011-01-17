package org.mcguppy.eventplaner.business.dispomgmt.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    @NotNull(message="{validationFieldNotNull}")
    @Size(min = 1, message="{validationFieldMin1}")
    private String title;
    @NotNull(message="{validationFieldNotNull}")
    @Size(message="{validationFieldMin2}", min = 2 )
    private String firstName;
    @NotNull(message="{validationFieldNotNull}")
    @Size(min = 2, message="{validationFieldMin2}")
    private String lastName;
    private String street;
    @NotNull(message="{validationFieldNotNull}")
    @Pattern(regexp = "\\d{4}", message="{validationZip}")
    private String zip;
    @NotNull(message="{validationFieldNotNull}")
    @Size(min = 2, message="{validationFieldMin2}")
    private String city;
    private String phoneNr;
    private String cellPhoneNr;
    //@Pattern(regexp="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    @Email(message="{validationMailAddress}")
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
