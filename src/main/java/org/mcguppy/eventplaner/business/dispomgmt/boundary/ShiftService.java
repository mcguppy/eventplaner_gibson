package org.mcguppy.eventplaner.business.dispomgmt.boundary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.mcguppy.eventplaner.business.dispomgmt.domain.Location;
import org.mcguppy.eventplaner.business.dispomgmt.domain.Shift;
import org.mcguppy.eventplaner.business.dispomgmt.domain.StaffMember;

/**
 *
 * @author stefan meichtry
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ShiftService extends CrudBaseService<Shift> {

    @PersistenceContext
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ShiftService() {
        super(Shift.class);
    }

    @Override
    public void create(Shift shift) {
        if (shift.getStaffMembers() == null) {
            shift.setStaffMembers(new ArrayList<StaffMember>());
        }

        Location location = shift.getLocation();
        if (location != null) {
            location = em.getReference(location.getClass(), location.getId());
            shift.setLocation(location);
        }

        if (null != shift.getResponsible()) {
            StaffMember responsible = shift.getResponsible();
            responsible = em.getReference(responsible.getClass(), responsible.getId());
            shift.setResponsible(responsible);
        }

        List<StaffMember> attachedStaffMemberCollection = new ArrayList<StaffMember>();
        for (StaffMember staffMemberToAttach : shift.getStaffMembers()) {
            staffMemberToAttach = em.getReference(staffMemberToAttach.getClass(), staffMemberToAttach.getId());
            attachedStaffMemberCollection.add(staffMemberToAttach);
        }
        shift.setStaffMembers(attachedStaffMemberCollection);
        em.persist(shift);

        if (location != null) {
            location.getShifts().add(shift);
            location = em.merge(location);
        }

        if (null != shift.getResponsible()) {
            StaffMember responsible = shift.getResponsible();
            responsible.getResponsibleShifts().add(shift);
            responsible = em.merge(responsible);
        }

        for (StaffMember staffMember : shift.getStaffMembers()) {
            staffMember.getShifts().add(shift);
            staffMember = em.merge(staffMember);
        }
    }

    @Override
    public void edit(Shift shift) {
        Shift persistentShift = em.find(Shift.class, shift.getId());
        Collection<StaffMember> staffMemberCollectionOld = persistentShift.getStaffMembers();

        Collection<StaffMember> staffMemberCollectionNew = shift.getStaffMembers();
        List<StaffMember> attachedStaffMemberCollectionNew = new ArrayList<StaffMember>();
        for (StaffMember staffMemberCollectionNewStaffMemberToAttach : staffMemberCollectionNew) {
            staffMemberCollectionNewStaffMemberToAttach = em.getReference(staffMemberCollectionNewStaffMemberToAttach.getClass(), staffMemberCollectionNewStaffMemberToAttach.getId());
            attachedStaffMemberCollectionNew.add(staffMemberCollectionNewStaffMemberToAttach);
        }
        staffMemberCollectionNew = attachedStaffMemberCollectionNew;
        shift.setStaffMembers(staffMemberCollectionNew);

        StaffMember responsibleOld = null;
        StaffMember responsibleNew = null;
        boolean shifResponsibleChanged = false;

        if (null != shift.getResponsible()) {
            responsibleNew = em.getReference(shift.getResponsible().getClass(), shift.getResponsible().getId());
            if (!shift.getStaffMembers().contains(responsibleNew)) {    // make sure, the responsibl is also a shift staffMember
                responsibleNew = null;
            }
        }

        if (null != persistentShift.getResponsible()) {
            responsibleOld = persistentShift.getResponsible();
        }

        if (null != responsibleNew && null != responsibleOld) {   // old and new shift has a responsible
            // check if new an old are different
            if (responsibleNew.getId() != responsibleOld.getId()) {     // shift responsible has changed
                shifResponsibleChanged = true;
                // remove the responsability from the old responsible first
                if (responsibleOld.getResponsibleShifts().contains(persistentShift)) {
                    responsibleOld.getResponsibleShifts().remove(persistentShift);
                    em.persist(responsibleOld);
                }
            }
        } else if (null == responsibleNew && null != responsibleOld) {      // there is no more responsible for this shift
            shifResponsibleChanged = true;
            // remove the responsability from the old responsible first
            if (responsibleOld.getResponsibleShifts().contains(persistentShift)) {
                responsibleOld.getResponsibleShifts().remove(persistentShift);
                em.persist(responsibleOld);
            }
        } else if (null != responsibleNew && null == responsibleOld) {     // there is a shift responsible
            shifResponsibleChanged = true;
        }

        shift.setResponsible(responsibleNew);

        shift = em.merge(shift);

        for (StaffMember staffMemberCollectionNewStaffMember : staffMemberCollectionNew) {
            if (!staffMemberCollectionOld.contains(staffMemberCollectionNewStaffMember)) {
                staffMemberCollectionNewStaffMember.getShifts().add(shift);
                staffMemberCollectionNewStaffMember = em.merge(staffMemberCollectionNewStaffMember);
            }
        }
        for (StaffMember staffMemberCollectionOldStaffMember : staffMemberCollectionOld) {
            if (!staffMemberCollectionNew.contains(staffMemberCollectionOldStaffMember)) {
                staffMemberCollectionOldStaffMember.getShifts().remove(shift);
                staffMemberCollectionOldStaffMember = em.merge(staffMemberCollectionOldStaffMember);
            }
        }

        if (shifResponsibleChanged == true && null != responsibleNew) {
            responsibleNew.getResponsibleShifts().add(shift);
            responsibleNew = em.merge(responsibleNew);
        }
    }

    @Override
    public void remove(Shift shift) {
        Shift persistentShift = null;
        try {
            persistentShift = em.getReference(Shift.class, shift.getId());
            persistentShift.getId();
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException(ResourceBundle.getBundle("msgs").getString("errorShiftMissing"));
        }

        Location location = persistentShift.getLocation();
        if (location.getShifts().contains(persistentShift)) {
            location.getShifts().remove(persistentShift);
            em.persist(location);
        }


        if (null != shift.getResponsible()) {
            StaffMember responsible = persistentShift.getResponsible();
            if (responsible.getResponsibleShifts().contains(persistentShift)) {
                responsible.getResponsibleShifts().remove(persistentShift);
                em.persist(responsible);
            }
        }

        for (StaffMember staffMemberToRemoveStaffMember : persistentShift.getStaffMembers()) {
            if (staffMemberToRemoveStaffMember.getShifts().contains(persistentShift)) {
                staffMemberToRemoveStaffMember.getShifts().remove(persistentShift);
                em.merge(staffMemberToRemoveStaffMember);
            }
        }
        em.remove(persistentShift);
    }
}
