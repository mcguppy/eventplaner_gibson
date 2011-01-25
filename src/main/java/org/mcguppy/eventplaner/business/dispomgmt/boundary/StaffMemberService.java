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
import javax.validation.ConstraintViolationException;
import org.mcguppy.eventplaner.business.dispomgmt.domain.Shift;
import org.mcguppy.eventplaner.business.dispomgmt.domain.StaffMember;

/**
 *
 * @author stefan meichtry
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class StaffMemberService extends CrudBaseService<StaffMember> {

    @PersistenceContext
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StaffMemberService() {
        super(StaffMember.class);
    }

    @Override
    public void create(StaffMember staffMember) {
        if (staffMember.getShifts() == null) {
            staffMember.setShifts(new ArrayList<Shift>());
        }

        if (staffMember.getResponsibleShifts() == null) {
            staffMember.setResponsibleShifts(new ArrayList<Shift>());
        }

        List<Shift> attachedShiftCollection = new ArrayList<Shift>();
        for (Shift shiftToAttach : staffMember.getShifts()) {
            shiftToAttach = em.getReference(shiftToAttach.getClass(), shiftToAttach.getId());
            attachedShiftCollection.add(shiftToAttach);
        }
        staffMember.setShifts(attachedShiftCollection);

        List<Shift> attachedResponsibleShiftCollection = new ArrayList<Shift>();
        for (Shift responsibleShiftToAttach : staffMember.getResponsibleShifts()) {
            responsibleShiftToAttach = em.getReference(responsibleShiftToAttach.getClass(), responsibleShiftToAttach.getId());
            attachedResponsibleShiftCollection.add(responsibleShiftToAttach);
        }

        staffMember.setResponsibleShifts(attachedResponsibleShiftCollection);

        em.persist(staffMember);
        for (Shift shift : staffMember.getShifts()) {
            shift.getStaffMembers().add(staffMember);
            shift = em.merge(shift);
        }

        for (Shift responsibleShift : staffMember.getResponsibleShifts()) {
            responsibleShift.setResponsibleStaffMember(staffMember);
            responsibleShift = em.merge(responsibleShift);
        }
    }

    @Override
    public void edit(StaffMember staffMember) {
        StaffMember persistentStaffMember = em.find(StaffMember.class, staffMember.getId());
        Collection<Shift> shiftCollectionOld = persistentStaffMember.getShifts();
        Collection<Shift> shiftCollectionNew = staffMember.getShifts();
        List<Shift> attachedShiftCollectionNew = new ArrayList<Shift>();
        for (Shift shiftCollectionNewShiftToAttach : shiftCollectionNew) {
            shiftCollectionNewShiftToAttach = em.getReference(shiftCollectionNewShiftToAttach.getClass(), shiftCollectionNewShiftToAttach.getId());
            attachedShiftCollectionNew.add(shiftCollectionNewShiftToAttach);
        }
        shiftCollectionNew = attachedShiftCollectionNew;
        staffMember.setShifts(shiftCollectionNew);

        Collection<Shift> responsibleShiftCollectionOld = persistentStaffMember.getResponsibleShifts();
        Collection<Shift> responsibleShiftCollectionNew = staffMember.getResponsibleShifts();
        List<Shift> attachedResponsibleShiftCollectionNew = new ArrayList<Shift>();
        for (Shift shiftCollectionNewResponsibleShiftToAttach : responsibleShiftCollectionNew) {
            shiftCollectionNewResponsibleShiftToAttach = em.getReference(shiftCollectionNewResponsibleShiftToAttach.getClass(), shiftCollectionNewResponsibleShiftToAttach.getId());
            attachedResponsibleShiftCollectionNew.add(shiftCollectionNewResponsibleShiftToAttach);
        }
        responsibleShiftCollectionNew = attachedResponsibleShiftCollectionNew;
        staffMember.setResponsibleShifts(responsibleShiftCollectionNew);

        staffMember = em.merge(staffMember);


        for (Shift shiftCollectionNewShift : shiftCollectionNew) {
            if (!shiftCollectionOld.contains(shiftCollectionNewShift)) {
                shiftCollectionNewShift.getStaffMembers().add(staffMember);
                shiftCollectionNewShift = em.merge(shiftCollectionNewShift);
            }
        }

        for (Shift shiftCollectionNewResponsibleShift : responsibleShiftCollectionNew) {
            if (!responsibleShiftCollectionOld.contains(shiftCollectionNewResponsibleShift)) {
                shiftCollectionNewResponsibleShift.setResponsibleStaffMember(staffMember);
                shiftCollectionNewResponsibleShift = em.merge(shiftCollectionNewResponsibleShift);
            }
        }
    }

    @Override
    public void remove(StaffMember staffMember) {
        StaffMember persistentStaffMember = null;
        try {
            persistentStaffMember = em.getReference(StaffMember.class, staffMember.getId());
            persistentStaffMember.getId();
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException(ResourceBundle.getBundle("msgs").getString("errorStaffMemberMissing"));
        }

        for (Shift shiftToRemoveStaffMember : persistentStaffMember.getShifts()) {
            if (shiftToRemoveStaffMember.getStaffMembers().contains(persistentStaffMember)) {
                shiftToRemoveStaffMember.getStaffMembers().remove(persistentStaffMember);
                em.merge(shiftToRemoveStaffMember);
            }
        }

        for (Shift responsibleShiftToRemoveStaffMember : persistentStaffMember.getResponsibleShifts()) {
            responsibleShiftToRemoveStaffMember.setResponsibleStaffMember(null);
            em.merge(responsibleShiftToRemoveStaffMember);
        }
        em.remove(persistentStaffMember);
    }


}
