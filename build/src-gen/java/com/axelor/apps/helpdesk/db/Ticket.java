package com.axelor.apps.helpdesk.db;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.Timer;
import com.axelor.apps.crm.db.Lead;
import com.axelor.apps.project.db.Project;
import com.axelor.auth.db.AuditableModel;
import com.axelor.auth.db.User;
import com.axelor.db.annotations.Track;
import com.axelor.db.annotations.TrackEvent;
import com.axelor.db.annotations.TrackField;
import com.axelor.db.annotations.TrackMessage;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "HELPDESK_TICKET", indexes = { @Index(columnList = "lead"), @Index(columnList = "project"), @Index(columnList = "customer"), @Index(columnList = "contact_partner"), @Index(columnList = "sla_policy"), @Index(columnList = "assigned_to_user"), @Index(columnList = "responsible_user"), @Index(columnList = "ticket_type") })
@Track(fields = { @TrackField(name = "ticketSeq"), @TrackField(name = "subject"), @TrackField(name = "statusSelect", on = TrackEvent.UPDATE) }, messages = { @TrackMessage(message = "Ticket created", condition = "true", on = TrackEvent.CREATE), @TrackMessage(message = "Ticket In Progress", condition = "statusSelect == 1", tag = "info"), @TrackMessage(message = "Ticket In Resolved", condition = "statusSelect == 2", tag = "success"), @TrackMessage(message = "Ticket Closed", condition = "statusSelect == 3", tag = "info") })
public class Ticket extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HELPDESK_TICKET_SEQ")
	@SequenceGenerator(name = "HELPDESK_TICKET_SEQ", sequenceName = "HELPDESK_TICKET_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Lead")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Lead lead;

	@Widget(title = "Ticket Number", readonly = true)
	private String ticketSeq;

	@Widget(title = "Subject")
	@NotNull
	private String subject;

	@Widget(title = "Project")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Project project;

	@Widget(title = "Customer")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner customer;

	@Widget(title = "Customer contact")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner contactPartner;

	@Widget(title = "SLA Policy")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Sla slaPolicy;

	@Widget(title = "Assigned to")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private User assignedToUser;

	@Widget(title = "User in charge of the issue")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private User responsibleUser;

	@Widget(title = "Status", selection = "helpdesk.status.select")
	private Integer statusSelect = 0;

	@Widget(title = "Ticket type")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private TicketType ticketType;

	@Widget(title = "Start date")
	private LocalDateTime startDateT;

	@Widget(title = "End date")
	private LocalDateTime endDateT;

	@Widget(title = "Deadline")
	private LocalDateTime deadlineDateT;

	@Widget(title = "SLA completed")
	private Boolean isSlaCompleted = Boolean.FALSE;

	@Widget(title = "Duration")
	private Long duration = 0L;

	@Widget(title = "Description")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String description;

	@Widget(title = "Progress (%)", selection = "helpdesk.ticket.progress.select")
	private Integer progressSelect = 0;

	@Widget(title = "Priority", selection = "helpdesk.priority.select")
	private Integer prioritySelect = 2;

	private String mailSubject;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Timer> timerList;

	@Widget(title = "Real total duration (Hours)")
	@DecimalMin("0")
	private BigDecimal realTotalDuration = new BigDecimal("0");

	@Widget(readonly = true)
	private Boolean timerState = Boolean.FALSE;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Ticket() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Lead getLead() {
		return lead;
	}

	public void setLead(Lead lead) {
		this.lead = lead;
	}

	public String getTicketSeq() {
		return ticketSeq;
	}

	public void setTicketSeq(String ticketSeq) {
		this.ticketSeq = ticketSeq;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Partner getCustomer() {
		return customer;
	}

	public void setCustomer(Partner customer) {
		this.customer = customer;
	}

	public Partner getContactPartner() {
		return contactPartner;
	}

	public void setContactPartner(Partner contactPartner) {
		this.contactPartner = contactPartner;
	}

	public Sla getSlaPolicy() {
		return slaPolicy;
	}

	public void setSlaPolicy(Sla slaPolicy) {
		this.slaPolicy = slaPolicy;
	}

	public User getAssignedToUser() {
		return assignedToUser;
	}

	public void setAssignedToUser(User assignedToUser) {
		this.assignedToUser = assignedToUser;
	}

	public User getResponsibleUser() {
		return responsibleUser;
	}

	public void setResponsibleUser(User responsibleUser) {
		this.responsibleUser = responsibleUser;
	}

	public Integer getStatusSelect() {
		return statusSelect == null ? 0 : statusSelect;
	}

	public void setStatusSelect(Integer statusSelect) {
		this.statusSelect = statusSelect;
	}

	public TicketType getTicketType() {
		return ticketType;
	}

	public void setTicketType(TicketType ticketType) {
		this.ticketType = ticketType;
	}

	public LocalDateTime getStartDateT() {
		return startDateT;
	}

	public void setStartDateT(LocalDateTime startDateT) {
		this.startDateT = startDateT;
	}

	public LocalDateTime getEndDateT() {
		return endDateT;
	}

	public void setEndDateT(LocalDateTime endDateT) {
		this.endDateT = endDateT;
	}

	public LocalDateTime getDeadlineDateT() {
		return deadlineDateT;
	}

	public void setDeadlineDateT(LocalDateTime deadlineDateT) {
		this.deadlineDateT = deadlineDateT;
	}

	public Boolean getIsSlaCompleted() {
		return isSlaCompleted == null ? Boolean.FALSE : isSlaCompleted;
	}

	public void setIsSlaCompleted(Boolean isSlaCompleted) {
		this.isSlaCompleted = isSlaCompleted;
	}

	public Long getDuration() {
		return duration == null ? 0L : duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getProgressSelect() {
		return progressSelect == null ? 0 : progressSelect;
	}

	public void setProgressSelect(Integer progressSelect) {
		this.progressSelect = progressSelect;
	}

	public Integer getPrioritySelect() {
		return prioritySelect == null ? 0 : prioritySelect;
	}

	public void setPrioritySelect(Integer prioritySelect) {
		this.prioritySelect = prioritySelect;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public List<Timer> getTimerList() {
		return timerList;
	}

	public void setTimerList(List<Timer> timerList) {
		this.timerList = timerList;
	}

	/**
	 * Add the given {@link Timer} item to the {@code timerList}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addTimerListItem(Timer item) {
		if (getTimerList() == null) {
			setTimerList(new ArrayList<>());
		}
		getTimerList().add(item);
	}

	/**
	 * Remove the given {@link Timer} item from the {@code timerList}.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeTimerListItem(Timer item) {
		if (getTimerList() == null) {
			return;
		}
		getTimerList().remove(item);
	}

	/**
	 * Clear the {@code timerList} collection.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 */
	public void clearTimerList() {
		if (getTimerList() != null) {
			getTimerList().clear();
		}
	}

	public BigDecimal getRealTotalDuration() {
		return realTotalDuration == null ? BigDecimal.ZERO : realTotalDuration;
	}

	public void setRealTotalDuration(BigDecimal realTotalDuration) {
		this.realTotalDuration = realTotalDuration;
	}

	public Boolean getTimerState() {
		return timerState == null ? Boolean.FALSE : timerState;
	}

	public void setTimerState(Boolean timerState) {
		this.timerState = timerState;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof Ticket)) return false;

		final Ticket other = (Ticket) obj;
		if (this.getId() != null || other.getId() != null) {
			return Objects.equals(this.getId(), other.getId());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("id", getId())
			.add("ticketSeq", getTicketSeq())
			.add("subject", getSubject())
			.add("statusSelect", getStatusSelect())
			.add("startDateT", getStartDateT())
			.add("endDateT", getEndDateT())
			.add("deadlineDateT", getDeadlineDateT())
			.add("isSlaCompleted", getIsSlaCompleted())
			.add("duration", getDuration())
			.add("progressSelect", getProgressSelect())
			.add("prioritySelect", getPrioritySelect())
			.add("mailSubject", getMailSubject())
			.omitNullValues()
			.toString();
	}
}
