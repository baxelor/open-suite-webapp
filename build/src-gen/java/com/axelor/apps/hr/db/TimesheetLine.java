package com.axelor.apps.hr.db;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.axelor.apps.base.db.Product;
import com.axelor.apps.production.db.ManufOrder;
import com.axelor.apps.production.db.OperationOrder;
import com.axelor.apps.project.db.Project;
import com.axelor.apps.project.db.ProjectPlanningTime;
import com.axelor.apps.project.db.ProjectTask;
import com.axelor.auth.db.AuditableModel;
import com.axelor.auth.db.User;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Cacheable
@Table(name = "HR_TIMESHEET_LINE", uniqueConstraints = { @UniqueConstraint(columnNames = { "timesheet", "project", "product", "date_val", "enableEditor" }) }, indexes = { @Index(columnList = "project"), @Index(columnList = "product"), @Index(columnList = "user_id"), @Index(columnList = "timesheet"), @Index(columnList = "fullName"), @Index(columnList = "project_planning_time"), @Index(columnList = "project_task"), @Index(columnList = "operation_order"), @Index(columnList = "manuf_order") })
public class TimesheetLine extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HR_TIMESHEET_LINE_SEQ")
	@SequenceGenerator(name = "HR_TIMESHEET_LINE_SEQ", sequenceName = "HR_TIMESHEET_LINE_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Sequence", readonly = true)
	private Integer sequence = 0;

	@Widget(title = "Project")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Project project;

	@Widget(title = "Activity")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Product product;

	@Widget(title = "User", readonly = true)
	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private User user;

	@NotNull
	@Column(name = "date_val")
	private LocalDate date;

	@Widget(title = "Time Sheet", readonly = true)
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Timesheet timesheet;

	@Widget(title = "Duration")
	private BigDecimal hoursDuration = BigDecimal.ZERO;

	@Widget(title = "Duration")
	private BigDecimal duration = BigDecimal.ZERO;

	@Widget(title = "Duration adjust for customer")
	@Column(nullable = true)
	private BigDecimal durationForCustomer;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String comments;

	private Boolean toInvoice = Boolean.FALSE;

	@Widget(title = "Invoiced", readonly = true)
	private Boolean invoiced = Boolean.FALSE;

	@NameColumn
	private String fullName;

	@Widget(title = "Enable editor")
	@Column(nullable = true)
	private Boolean enableEditor;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ProjectPlanningTime projectPlanningTime;

	@Widget(title = "Task")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ProjectTask projectTask;

	@Widget(title = "Operation order")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private OperationOrder operationOrder;

	@Widget(title = "Manuf order")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ManufOrder manufOrder;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public TimesheetLine() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSequence() {
		return sequence == null ? 0 : sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Timesheet getTimesheet() {
		return timesheet;
	}

	public void setTimesheet(Timesheet timesheet) {
		this.timesheet = timesheet;
	}

	public BigDecimal getHoursDuration() {
		return hoursDuration == null ? BigDecimal.ZERO : hoursDuration;
	}

	public void setHoursDuration(BigDecimal hoursDuration) {
		this.hoursDuration = hoursDuration;
	}

	public BigDecimal getDuration() {
		return duration == null ? BigDecimal.ZERO : duration;
	}

	public void setDuration(BigDecimal duration) {
		this.duration = duration;
	}

	public BigDecimal getDurationForCustomer() {
		return durationForCustomer;
	}

	public void setDurationForCustomer(BigDecimal durationForCustomer) {
		this.durationForCustomer = durationForCustomer;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Boolean getToInvoice() {
		return toInvoice == null ? Boolean.FALSE : toInvoice;
	}

	public void setToInvoice(Boolean toInvoice) {
		this.toInvoice = toInvoice;
	}

	public Boolean getInvoiced() {
		return invoiced == null ? Boolean.FALSE : invoiced;
	}

	public void setInvoiced(Boolean invoiced) {
		this.invoiced = invoiced;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Boolean getEnableEditor() {
		return enableEditor;
	}

	public void setEnableEditor(Boolean enableEditor) {
		this.enableEditor = enableEditor;
	}

	public ProjectPlanningTime getProjectPlanningTime() {
		return projectPlanningTime;
	}

	public void setProjectPlanningTime(ProjectPlanningTime projectPlanningTime) {
		this.projectPlanningTime = projectPlanningTime;
	}

	public ProjectTask getProjectTask() {
		return projectTask;
	}

	public void setProjectTask(ProjectTask projectTask) {
		this.projectTask = projectTask;
	}

	public OperationOrder getOperationOrder() {
		return operationOrder;
	}

	public void setOperationOrder(OperationOrder operationOrder) {
		this.operationOrder = operationOrder;
	}

	public ManufOrder getManufOrder() {
		return manufOrder;
	}

	public void setManufOrder(ManufOrder manufOrder) {
		this.manufOrder = manufOrder;
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
		if (!(obj instanceof TimesheetLine)) return false;

		final TimesheetLine other = (TimesheetLine) obj;
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
			.add("sequence", getSequence())
			.add("date", getDate())
			.add("hoursDuration", getHoursDuration())
			.add("duration", getDuration())
			.add("durationForCustomer", getDurationForCustomer())
			.add("toInvoice", getToInvoice())
			.add("invoiced", getInvoiced())
			.add("fullName", getFullName())
			.add("enableEditor", getEnableEditor())
			.omitNullValues()
			.toString();
	}
}
