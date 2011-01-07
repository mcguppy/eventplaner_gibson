package org.mcguppy.eventplaner.view.dispomgmt.controller;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import org.mcguppy.eventplaner.business.dispomgmt.boundary.StaffMemberService;
import org.mcguppy.eventplaner.business.dispomgmt.domain.StaffMember;

@Named
public class StaffMemberController implements Serializable {

        @EJB
        StaffMemberService service;

	private Long id;
	private StaffMember instance;

	@Inject
	private Conversation conversation;

	public StaffMember getInstance() {
		if (instance == null) {
			if (id != null) {
				instance = loadInstance();
			} else {
				instance = createInstance();
			}
		}
		return instance;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StaffMember loadInstance() {
		return service.find(getId());
	}

	public StaffMember createInstance() {
		try {
			return StaffMember.class.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isManaged() {
		return getInstance().getId() != null;
	} 

	public String save() {
		if (isManaged()) {
			service.edit(instance);
		} else {
			service.create(instance);
		}
		conversation.end();
		return "saved";
	}

	public String cancel() {
		conversation.end();
		return "cancelled";
	}

	public void initConversation() {
		if (conversation.isTransient()) {
			conversation.begin();
		}
	}

	public String delete() {		
		System.out.println("Deleting "+getInstance());
		service.remove(instance);
		conversation.end();
		return "deleted";		
	}

        @Produces
        @Named
        public List<StaffMember> getStaffMembers() {
            return service.findAll();
        }
}
