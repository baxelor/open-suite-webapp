package com.axelor.apps.crm.db;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.axelor.apps.base.db.ICalendarEvent;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.marketing.db.Campaign;
import com.axelor.db.EntityHelper;
import com.axelor.db.annotations.Widget;
import com.axelor.team.db.Team;

@Entity
@Table(name = "CRM_EVENT", indexes = { @Index(columnList = "event_category"), @Index(columnList = "team"), @Index(columnList = "partner"), @Index(columnList = "contact_partner"), @Index(columnList = "lead"), @Index(columnList = "parent_event"), @Index(columnList = "recurrence_configuration"), @Index(columnList = "campaign") })
public class Event extends ICalendarEvent {

	@Widget(title = "Limit Date")
	private LocalDateTime limitDateT;

	@Widget(title = "Duration")
	private Long duration = 0L;

	@Widget(title = "Category")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private EventCategory eventCategory;

	@Widget(title = "Related to", selection = "crm.event.related.to.select")
	private String relatedToSelect;

	private Long relatedToSelectId = 0L;

	@Widget(title = "Status", selection = "crm.event.status.select")
	private Integer statusSelect = 0;

	@Widget(title = "Team")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Team team;

	@Widget(title = "UID (Calendar)")
	private String calendarEventUid;

	@Widget(title = "Reminders")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EventReminder> eventReminderList;

	@Widget(title = "Partner")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner partner;

	@Widget(title = "Contact")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner contactPartner;

	@Widget(title = "Lead")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Lead lead;

	@Widget(title = "Call type", selection = "crm.event.call.type.select")
	private Integer callTypeSelect = 0;

	@Widget(title = "Recurrent")
	private Boolean isRecurrent = Boolean.FALSE;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Event parentEvent;

	@Widget(title = "Priority", selection = "crm.event.priority.select")
	private Integer prioritySelect = 2;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private RecurrenceConfiguration recurrenceConfiguration;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CRM_EVENT_SEQ")
	@SequenceGenerator(name = "CRM_EVENT_SEQ", sequenceName = "CRM_EVENT_SEQ", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Campaign campaign;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Event() {
	}

	public LocalDateTime getLimitDateT() {
		return limitDateT;
	}

	public void setLimitDateT(LocalDateTime limitDateT) {
		this.limitDateT = limitDateT;
	}

	public Long getDuration() {
		return duration == null ? 0L : duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public EventCategory getEventCategory() {
		return eventCategory;
	}

	public void setEventCategory(EventCategory eventCategory) {
		this.eventCategory = eventCategory;
	}

	public String getRelatedToSelect() {
		return relatedToSelect;
	}

	public void setRelatedToSelect(String relatedToSelect) {
		this.relatedToSelect = relatedToSelect;
	}

	public Long getRelatedToSelectId() {
		return relatedToSelectId == null ? 0L : relatedToSelectId;
	}

	public void setRelatedToSelectId(Long relatedToSelectId) {
		this.relatedToSelectId = relatedToSelectId;
	}

	public Integer getStatusSelect() {
		return statusSelect == null ? 0 : statusSelect;
	}

	public void setStatusSelect(Integer statusSelect) {
		this.statusSelect = statusSelect;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public String getCalendarEventUid() {
		return calendarEventUid;
	}

	public void setCalendarEventUid(String calendarEventUid) {
		this.calendarEventUid = calendarEventUid;
	}

	public List<EventReminder> getEventReminderList() {
		return eventReminderList;
	}

	public void setEventReminderList(List<EventReminder> eventReminderList) {
		this.eventReminderList = eventReminderList;
	}

	/**
	 * Add the given {@link EventReminder} item to the {@code eventReminderList}.
	 *
	 * <p>
	 * It sets {@code item.event = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addEventReminderListItem(EventReminder item) {
		if (getEventReminderList() == null) {
			setEventReminderList(new ArrayList<>());
		}
		getEventReminderList().add(item);
		item.setEvent(this);
	}

	/**
	 * Remove the given {@link EventReminder} item from the {@code eventReminderList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeEventReminderListItem(EventReminder item) {
		if (getEventReminderList() == null) {
			return;
		}
		getEventReminderList().remove(item);
	}

	/**
	 * Clear the {@code eventReminderList} collection.
	 *
	 * <p>
	 * If you have to query {@link EventReminder} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearEventReminderList() {
		if (getEventReminderList() != null) {
			getEventReminderList().clear();
		}
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public Partner getContactPartner() {
		return contactPartner;
	}

	public void setContactPartner(Partner contactPartner) {
		this.contactPartner = contactPartner;
	}

	public Lead getLead() {
		return lead;
	}

	public void setLead(Lead lead) {
		this.lead = lead;
	}

	public Integer getCallTypeSelect() {
		return callTypeSelect == null ? 0 : callTypeSelect;
	}

	public void setCallTypeSelect(Integer callTypeSelect) {
		this.callTypeSelect = callTypeSelect;
	}

	public Boolean getIsRecurrent() {
		return isRecurrent == null ? Boolean.FALSE : isRecurrent;
	}

	public void setIsRecurrent(Boolean isRecurrent) {
		this.isRecurrent = isRecurrent;
	}

	public Event getParentEvent() {
		return parentEvent;
	}

	public void setParentEvent(Event parentEvent) {
		this.parentEvent = parentEvent;
	}

	public Integer getPrioritySelect() {
		return prioritySelect == null ? 0 : prioritySelect;
	}

	public void setPrioritySelect(Integer prioritySelect) {
		this.prioritySelect = prioritySelect;
	}

	public RecurrenceConfiguration getRecurrenceConfiguration() {
		return recurrenceConfiguration;
	}

	public void setRecurrenceConfiguration(RecurrenceConfiguration recurrenceConfiguration) {
		this.recurrenceConfiguration = recurrenceConfiguration;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	@Override
	public boolean equals(Object obj) {
		return EntityHelper.equals(this, obj);
	}

	@Override
	public int hashCode() {
		return EntityHelper.hashCode(this);
	}

	@Override
	public String toString() {
		return EntityHelper.toString(this);
	}
}
