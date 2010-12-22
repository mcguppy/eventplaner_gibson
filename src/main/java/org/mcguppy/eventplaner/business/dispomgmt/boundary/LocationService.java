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
public class LocationService extends CrudBaseService<Location> {

    @PersistenceContext
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LocationService() {
        super(Location.class);
    }

    @Override
    public void create(Location location) {
        Location persistentLocation = em.find(Location.class, location.getId());
        Collection<Shift> shiftCollectionOld = persistentLocation.getShifts();
        Collection<Shift> shiftCollectionNew = location.getShifts();
        List<Shift> attachedShiftCollectionNew = new ArrayList<Shift>();
        for (Shift shiftCollectionNewShiftToAttach : shiftCollectionNew) {
            shiftCollectionNewShiftToAttach = em.getReference(shiftCollectionNewShiftToAttach.getClass(), shiftCollectionNewShiftToAttach.getId());
            attachedShiftCollectionNew.add(shiftCollectionNewShiftToAttach);
        }
        shiftCollectionNew = attachedShiftCollectionNew;
        location.setShifts(shiftCollectionNew);
        location = em.merge(location);
        for (Shift shiftCollectionNewShift : shiftCollectionNew) {
            if (!shiftCollectionOld.contains(shiftCollectionNewShift)) {
                shiftCollectionNewShift.setLocation(location);
                shiftCollectionNewShift = em.merge(shiftCollectionNewShift);
            }
        }
    }

    @Override
    public void edit(Location location) {
        Location persistentLocation = em.find(Location.class, location.getId());
        Collection<Shift> shiftCollectionOld = persistentLocation.getShifts();
        Collection<Shift> shiftCollectionNew = location.getShifts();
        List<Shift> attachedShiftCollectionNew = new ArrayList<Shift>();
        for (Shift shiftCollectionNewShiftToAttach : shiftCollectionNew) {
            shiftCollectionNewShiftToAttach = em.getReference(shiftCollectionNewShiftToAttach.getClass(), shiftCollectionNewShiftToAttach.getId());
            attachedShiftCollectionNew.add(shiftCollectionNewShiftToAttach);
        }
        shiftCollectionNew = attachedShiftCollectionNew;
        location.setShifts(shiftCollectionNew);
        location = em.merge(location);
        for (Shift shiftCollectionNewShift : shiftCollectionNew) {
            if (!shiftCollectionOld.contains(shiftCollectionNewShift)) {
                shiftCollectionNewShift.setLocation(location);
                shiftCollectionNewShift = em.merge(shiftCollectionNewShift);
            }
        }
    }

    @Override
    public void remove(Location location) {
        Location persistentLocation = null;
        try {
            persistentLocation = em.getReference(Location.class, location.getId());
            persistentLocation.getId();
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException(ResourceBundle.getBundle("msgs").getString("errorLocationMissing"));
        }

        for (Shift shiftToRemove : persistentLocation.getShifts()) {
            for (StaffMember staffMemberToRemoveShift : shiftToRemove.getStaffMembers()) {
                if (staffMemberToRemoveShift.getShifts().contains(shiftToRemove)) {
                    staffMemberToRemoveShift.getShifts().remove(shiftToRemove);
                    em.merge(staffMemberToRemoveShift);
                }
            }
            em.remove(shiftToRemove);

        }
        em.remove(persistentLocation);
    }
}
