package org.mcguppy.eventplaner.view.dispomgmt.controller;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.mcguppy.eventplaner.business.dispomgmt.boundary.StaffMemberService;
import org.mcguppy.eventplaner.business.dispomgmt.domain.StaffMember;
import org.mcguppy.eventplaner.jsf.util.JsfUtil;

@Named
@ConversationScoped
public class StaffMemberController implements Serializable {

    @EJB
    StaffMemberService service;
    private Integer id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
            String msg = ResourceBundle.getBundle("/Messages").getString("infoStaffMemberCreated");
            JsfUtil.addSuccessMessage(msg);
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
        System.out.println("Deleting " + getInstance());
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
