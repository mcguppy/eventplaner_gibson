package org.mcguppy.eventplaner.business.dispomgmt.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * this class is the base for all admin entity classes
 * @author stefan meichtry
 */
@MappedSuperclass
public class MyEntityBase implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private long version;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATE_DATE")
    private Date createDate;
    @Column(name="CREATOR_NAME")
    private String creatorName;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="MODIFIED_DATE")
    private Date modifiedDate;
    @Column(name="MODIFIER_NAME")
    private String modifierName;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    	@PrePersist
	public void initTimeStamps() {
		if (createDate == null) {
			createDate = new Date();
		}
		modifiedDate = createDate;
	}

	@PreUpdate
	public void updateTimeStamp() {
		modifiedDate = new Date();
	}
    
    
}
