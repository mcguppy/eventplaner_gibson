package org.mcguppy.eventplaner.business.dispomgmt.domain;

import java.util.Collection;
import javax.persistence.Column;
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
@Table(name = "STAFFMEMBER")
public class StaffMember extends MyEntityBase implements Comparable {

    @NotNull(message = "{org.mcguppy.constraints.NotNull.message}")
    @Column(name = "TITLE")
    private String title;
    @NotNull(message = "{org.mcguppy.constraints.NotNull.message}")
    @Size(min = 2, max = 50, message = "{org.mcguppy.constraints.Size.message}")
    @Column(name = "FIRST_NAME")
    private String firstName;
    @NotNull(message = "{org.mcguppy.constraints.NotNull.message}")
    @Size(min = 2, max = 50, message = "{org.mcguppy.constraints.Size.message}")
    @Column(name = "LAST_NAME")
    private String lastName;
    @Size(max = 50, message = "{org.mcguppy.constraints.Size.Max.message}")
    @Column(name = "STREET")
    private String street;
    @NotNull(message = "{org.mcguppy.constraints.NotNull.message}")
    @Pattern(regexp = "[0-9]{4}", message = "{org.mcguppy.constraints.Zip.message}")
    @Column(name = "ZIP_CODE")
    private String zip;
    @NotNull(message = "{org.mcguppy.constraints.NotNull.message}")
    @Size(min = 2, max = 50, message = "{org.mcguppy.constraints.Size.message}")
    @Column(name = "CITY")
    private String city;
    @Pattern(regexp = "[0-9]{3}[ ]?[0-9]{3}[ ]?[0-9]{2}[ ]?[0-9]{2}", message = "{org.mcguppy.constraints.PhoneNumber.message}")
    @Column(name = "PHONE_NUMBER")
    private String phoneNr;
    @Pattern(regexp = "[0-9]{3}[ ]?[0-9]{3}[ ]?[0-9]{2}[ ]?[0-9]{2}", message = "{org.mcguppy.constraints.PhoneNumber.message}")
    @Column(name = "CELL_PHONE_NUMBER")
    private String cellPhoneNr;
    @Email(message = "{org.mcguppy.constraints.Mail.message}")
    @Column(name = "MAIL_ADDRESS")
    private String mailAddress;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "staffMembers")
    private Collection<Shift> shifts;
    @OneToMany(mappedBy = "responsibleStaffMember")
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

    public String getName() {
        return lastName + " " + firstName;
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
