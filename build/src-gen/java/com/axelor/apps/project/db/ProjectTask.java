package com.axelor.apps.project.db;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.base.db.Currency;
import com.axelor.apps.base.db.Frequency;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.db.Timer;
import com.axelor.apps.base.db.Unit;
import com.axelor.apps.businesssupport.db.ProjectVersion;
import com.axelor.apps.sale.db.SaleOrderLine;
import com.axelor.auth.db.AuditableModel;
import com.axelor.auth.db.User;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.Track;
import com.axelor.db.annotations.TrackEvent;
import com.axelor.db.annotations.TrackField;
import com.axelor.db.annotations.TrackMessage;
import com.axelor.db.annotations.Widget;
import com.axelor.meta.db.MetaFile;
import com.axelor.team.db.Team;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "PROJECT_PROJECT_TASK", indexes = { @Index(columnList = "name"), @Index(columnList = "assigned_to"), @Index(columnList = "frequency"), @Index(columnList = "next_project_task"), @Index(columnList = "project"), @Index(columnList = "fullName"), @Index(columnList = "parent_task"), @Index(columnList = "project_task_category"), @Index(columnList = "team"), @Index(columnList = "product"), @Index(columnList = "unit"), @Index(columnList = "meta_file"), @Index(columnList = "project_task_section"), @Index(columnList = "customer_referral"), @Index(columnList = "sale_order_line"), @Index(columnList = "invoice_line"), @Index(columnList = "target_version") })
@Track(fields = { @TrackField(name = "name"), @TrackField(name = "taskDate"), @TrackField(name = "status"), @TrackField(name = "taskDuration"), @TrackField(name = "taskDeadline"), @TrackField(name = "projectTaskCategory"), @TrackField(name = "progressSelect"), @TrackField(name = "assignedTo"), @TrackField(name = "invoicingType"), @TrackField(name = "customerReferral"), @TrackField(name = "isOrderAccepted"), @TrackField(name = "assignment"), @TrackField(name = "targetVersion") }, messages = { @TrackMessage(message = "Task updated", condition = "true", on = TrackEvent.UPDATE) })
public class ProjectTask extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_PROJECT_TASK_SEQ")
	@SequenceGenerator(name = "PROJECT_PROJECT_TASK_SEQ", sequenceName = "PROJECT_PROJECT_TASK_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Name")
	@NotNull
	private String name;

	@Widget(title = "Assigned to")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private User assignedTo;

	@Widget(title = "Task date")
	private LocalDate taskDate;

	@Widget(title = "Status", selection = "project.task.status")
	private String status;

	@Widget(title = "Last status", selection = "project.task.status")
	private String statusBeforeComplete;

	@Widget(title = "Priority", selection = "project.task.priority")
	private String priority;

	@Widget(title = "Task deadline")
	private LocalDate taskDeadline;

	@Widget(title = "Task duration")
	private Integer taskDuration = 0;

	@Widget(title = "Sequence")
	private Integer sequence = 0;

	@Widget(title = "Frequency")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Frequency frequency;

	@Widget(title = "Next task", readonly = true)
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ProjectTask nextProjectTask;

	@Widget(title = "First", readonly = true)
	private Boolean isFirst = Boolean.FALSE;

	@Widget(title = "Apply modifications to next tasks")
	private Boolean doApplyToAllNextTasks = Boolean.FALSE;

	@Widget(title = "Date or frequency changed", readonly = true)
	private Boolean hasDateOrFrequencyChanged = Boolean.FALSE;

	@Widget(title = "Type", selection = "project.task.type.select")
	private String typeSelect = "task";

	@Widget(title = "Project")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Project project;

	@Widget(title = "Name", search = { "id", "name" })
	@NameColumn
	private String fullName;

	@Widget(title = "Parent task")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ProjectTask parentTask;

	@Widget(title = "Category")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ProjectTaskCategory projectTaskCategory;

	@Widget(title = "Progress", selection = "project.task.progress.select")
	private Integer progressSelect = 0;

	@Widget(title = "Team")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Team team;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentTask", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProjectTask> projectTaskList;

	@Widget(title = "Followers")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<User> membersUserSet;

	@Widget(title = "Description")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String description;

	@Widget(title = "Product")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Product product;

	@Widget(title = "Unit")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Unit unit;

	@Widget(title = "Quantity")
	private BigDecimal quantity = BigDecimal.ZERO;

	@Widget(title = "Unit price")
	private BigDecimal unitPrice = BigDecimal.ZERO;

	@Widget(title = "Currency")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Currency currency;

	@Widget(title = "Planned progress")
	private BigDecimal plannedProgress = BigDecimal.ZERO;

	@Widget(title = "Predecessors tasks")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<ProjectTask> finishToStartSet;

	@Widget(title = "Tasks to start before start")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<ProjectTask> startToStartSet;

	@Widget(title = "Tasks to finish before finish")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<ProjectTask> finishToFinishSet;

	@Widget(title = "Duration hours")
	private BigDecimal durationHours = BigDecimal.ZERO;

	@Widget(title = "Task end")
	private LocalDate taskEndDate;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Timer> timerList;

	@Widget(title = "Estimated time")
	private BigDecimal budgetedTime = BigDecimal.ZERO;

	@Widget(title = "Signature")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private MetaFile metaFile;

	@Widget(title = "Tags")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<ProjectTaskTag> projectTaskTagSet;

	@Widget(title = "Section")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ProjectTaskSection projectTaskSection;

	@Widget(title = "Total planned hours")
	private BigDecimal totalPlannedHrs = BigDecimal.ZERO;

	@Widget(title = "Total real hours")
	private BigDecimal totalRealHrs = BigDecimal.ZERO;

	@Widget(title = "Project planning time lines")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectTask", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProjectPlanningTime> projectPlanningTimeList;

	@Widget(title = "To invoice")
	private Boolean toInvoice = Boolean.FALSE;

	@Widget(title = "Invoiced")
	private Boolean invoiced = Boolean.FALSE;

	@Widget(title = "ProjectTask.exTaxTotal")
	private BigDecimal exTaxTotal = BigDecimal.ZERO;

	@Widget(title = "Discount type", selection = "base.price.list.line.amount.type.select")
	private Integer discountTypeSelect = 3;

	@Widget(title = "Discount amount")
	@Digits(integer = 10, fraction = 10)
	private BigDecimal discountAmount = BigDecimal.ZERO;

	@Widget(title = "Unit price discounted")
	@Digits(integer = 10, fraction = 10)
	private BigDecimal priceDiscounted = BigDecimal.ZERO;

	@Widget(title = "Invoicing Type", selection = "business.project.task.invoicing.type.select")
	private Integer invoicingType = 3;

	@Widget(title = "Paid")
	private Boolean isPaid = Boolean.FALSE;

	@Widget(title = "Customer referral")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private User customerReferral;

	@Widget(title = "Related sale order line")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private SaleOrderLine saleOrderLine;

	@Widget(title = "Related Invoice line")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private InvoiceLine invoiceLine;

	@Widget(title = "Refused")
	private Boolean isTaskRefused = Boolean.FALSE;

	@Widget(title = "Assignment", selection = "support.assignment.select")
	private Integer assignment = 0;

	@Widget(title = "Private")
	private Boolean isPrivate = Boolean.FALSE;

	@Widget(title = "Order Accepted")
	private Boolean isOrderAccepted = Boolean.FALSE;

	@Widget(title = "Internal Description", multiline = true)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String internalDescription;

	@Widget(title = "Target version")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ProjectVersion targetVersion;

	@Widget(title = "Order Proposed")
	private Boolean isOrderProposed = Boolean.FALSE;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public ProjectTask() {
	}

	public ProjectTask(String name) {
		this.name = name;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(User assignedTo) {
		this.assignedTo = assignedTo;
	}

	public LocalDate getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(LocalDate taskDate) {
		this.taskDate = taskDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusBeforeComplete() {
		return statusBeforeComplete;
	}

	public void setStatusBeforeComplete(String statusBeforeComplete) {
		this.statusBeforeComplete = statusBeforeComplete;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public LocalDate getTaskDeadline() {
		return taskDeadline;
	}

	public void setTaskDeadline(LocalDate taskDeadline) {
		this.taskDeadline = taskDeadline;
	}

	public Integer getTaskDuration() {
		return taskDuration == null ? 0 : taskDuration;
	}

	public void setTaskDuration(Integer taskDuration) {
		this.taskDuration = taskDuration;
	}

	public Integer getSequence() {
		return sequence == null ? 0 : sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Frequency getFrequency() {
		return frequency;
	}

	public void setFrequency(Frequency frequency) {
		this.frequency = frequency;
	}

	public ProjectTask getNextProjectTask() {
		return nextProjectTask;
	}

	public void setNextProjectTask(ProjectTask nextProjectTask) {
		this.nextProjectTask = nextProjectTask;
	}

	public Boolean getIsFirst() {
		return isFirst == null ? Boolean.FALSE : isFirst;
	}

	public void setIsFirst(Boolean isFirst) {
		this.isFirst = isFirst;
	}

	public Boolean getDoApplyToAllNextTasks() {
		return doApplyToAllNextTasks == null ? Boolean.FALSE : doApplyToAllNextTasks;
	}

	public void setDoApplyToAllNextTasks(Boolean doApplyToAllNextTasks) {
		this.doApplyToAllNextTasks = doApplyToAllNextTasks;
	}

	public Boolean getHasDateOrFrequencyChanged() {
		return hasDateOrFrequencyChanged == null ? Boolean.FALSE : hasDateOrFrequencyChanged;
	}

	public void setHasDateOrFrequencyChanged(Boolean hasDateOrFrequencyChanged) {
		this.hasDateOrFrequencyChanged = hasDateOrFrequencyChanged;
	}

	public String getTypeSelect() {
		return typeSelect;
	}

	public void setTypeSelect(String typeSelect) {
		this.typeSelect = typeSelect;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public ProjectTask getParentTask() {
		return parentTask;
	}

	public void setParentTask(ProjectTask parentTask) {
		this.parentTask = parentTask;
	}

	public ProjectTaskCategory getProjectTaskCategory() {
		return projectTaskCategory;
	}

	public void setProjectTaskCategory(ProjectTaskCategory projectTaskCategory) {
		this.projectTaskCategory = projectTaskCategory;
	}

	public Integer getProgressSelect() {
		return progressSelect == null ? 0 : progressSelect;
	}

	public void setProgressSelect(Integer progressSelect) {
		this.progressSelect = progressSelect;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public List<ProjectTask> getProjectTaskList() {
		return projectTaskList;
	}

	public void setProjectTaskList(List<ProjectTask> projectTaskList) {
		this.projectTaskList = projectTaskList;
	}

	/**
	 * Add the given {@link ProjectTask} item to the {@code projectTaskList}.
	 *
	 * <p>
	 * It sets {@code item.parentTask = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addProjectTaskListItem(ProjectTask item) {
		if (getProjectTaskList() == null) {
			setProjectTaskList(new ArrayList<>());
		}
		getProjectTaskList().add(item);
		item.setParentTask(this);
	}

	/**
	 * Remove the given {@link ProjectTask} item from the {@code projectTaskList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeProjectTaskListItem(ProjectTask item) {
		if (getProjectTaskList() == null) {
			return;
		}
		getProjectTaskList().remove(item);
	}

	/**
	 * Clear the {@code projectTaskList} collection.
	 *
	 * <p>
	 * If you have to query {@link ProjectTask} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearProjectTaskList() {
		if (getProjectTaskList() != null) {
			getProjectTaskList().clear();
		}
	}

	public Set<User> getMembersUserSet() {
		return membersUserSet;
	}

	public void setMembersUserSet(Set<User> membersUserSet) {
		this.membersUserSet = membersUserSet;
	}

	/**
	 * Add the given {@link User} item to the {@code membersUserSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addMembersUserSetItem(User item) {
		if (getMembersUserSet() == null) {
			setMembersUserSet(new HashSet<>());
		}
		getMembersUserSet().add(item);
	}

	/**
	 * Remove the given {@link User} item from the {@code membersUserSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeMembersUserSetItem(User item) {
		if (getMembersUserSet() == null) {
			return;
		}
		getMembersUserSet().remove(item);
	}

	/**
	 * Clear the {@code membersUserSet} collection.
	 *
	 */
	public void clearMembersUserSet() {
		if (getMembersUserSet() != null) {
			getMembersUserSet().clear();
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public BigDecimal getQuantity() {
		return quantity == null ? BigDecimal.ZERO : quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice == null ? BigDecimal.ZERO : unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public BigDecimal getPlannedProgress() {
		return plannedProgress == null ? BigDecimal.ZERO : plannedProgress;
	}

	public void setPlannedProgress(BigDecimal plannedProgress) {
		this.plannedProgress = plannedProgress;
	}

	public Set<ProjectTask> getFinishToStartSet() {
		return finishToStartSet;
	}

	public void setFinishToStartSet(Set<ProjectTask> finishToStartSet) {
		this.finishToStartSet = finishToStartSet;
	}

	/**
	 * Add the given {@link ProjectTask} item to the {@code finishToStartSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addFinishToStartSetItem(ProjectTask item) {
		if (getFinishToStartSet() == null) {
			setFinishToStartSet(new HashSet<>());
		}
		getFinishToStartSet().add(item);
	}

	/**
	 * Remove the given {@link ProjectTask} item from the {@code finishToStartSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeFinishToStartSetItem(ProjectTask item) {
		if (getFinishToStartSet() == null) {
			return;
		}
		getFinishToStartSet().remove(item);
	}

	/**
	 * Clear the {@code finishToStartSet} collection.
	 *
	 */
	public void clearFinishToStartSet() {
		if (getFinishToStartSet() != null) {
			getFinishToStartSet().clear();
		}
	}

	public Set<ProjectTask> getStartToStartSet() {
		return startToStartSet;
	}

	public void setStartToStartSet(Set<ProjectTask> startToStartSet) {
		this.startToStartSet = startToStartSet;
	}

	/**
	 * Add the given {@link ProjectTask} item to the {@code startToStartSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addStartToStartSetItem(ProjectTask item) {
		if (getStartToStartSet() == null) {
			setStartToStartSet(new HashSet<>());
		}
		getStartToStartSet().add(item);
	}

	/**
	 * Remove the given {@link ProjectTask} item from the {@code startToStartSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeStartToStartSetItem(ProjectTask item) {
		if (getStartToStartSet() == null) {
			return;
		}
		getStartToStartSet().remove(item);
	}

	/**
	 * Clear the {@code startToStartSet} collection.
	 *
	 */
	public void clearStartToStartSet() {
		if (getStartToStartSet() != null) {
			getStartToStartSet().clear();
		}
	}

	public Set<ProjectTask> getFinishToFinishSet() {
		return finishToFinishSet;
	}

	public void setFinishToFinishSet(Set<ProjectTask> finishToFinishSet) {
		this.finishToFinishSet = finishToFinishSet;
	}

	/**
	 * Add the given {@link ProjectTask} item to the {@code finishToFinishSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addFinishToFinishSetItem(ProjectTask item) {
		if (getFinishToFinishSet() == null) {
			setFinishToFinishSet(new HashSet<>());
		}
		getFinishToFinishSet().add(item);
	}

	/**
	 * Remove the given {@link ProjectTask} item from the {@code finishToFinishSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeFinishToFinishSetItem(ProjectTask item) {
		if (getFinishToFinishSet() == null) {
			return;
		}
		getFinishToFinishSet().remove(item);
	}

	/**
	 * Clear the {@code finishToFinishSet} collection.
	 *
	 */
	public void clearFinishToFinishSet() {
		if (getFinishToFinishSet() != null) {
			getFinishToFinishSet().clear();
		}
	}

	public BigDecimal getDurationHours() {
		return durationHours == null ? BigDecimal.ZERO : durationHours;
	}

	public void setDurationHours(BigDecimal durationHours) {
		this.durationHours = durationHours;
	}

	public LocalDate getTaskEndDate() {
		return taskEndDate;
	}

	public void setTaskEndDate(LocalDate taskEndDate) {
		this.taskEndDate = taskEndDate;
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

	public BigDecimal getBudgetedTime() {
		return budgetedTime == null ? BigDecimal.ZERO : budgetedTime;
	}

	public void setBudgetedTime(BigDecimal budgetedTime) {
		this.budgetedTime = budgetedTime;
	}

	public MetaFile getMetaFile() {
		return metaFile;
	}

	public void setMetaFile(MetaFile metaFile) {
		this.metaFile = metaFile;
	}

	public Set<ProjectTaskTag> getProjectTaskTagSet() {
		return projectTaskTagSet;
	}

	public void setProjectTaskTagSet(Set<ProjectTaskTag> projectTaskTagSet) {
		this.projectTaskTagSet = projectTaskTagSet;
	}

	/**
	 * Add the given {@link ProjectTaskTag} item to the {@code projectTaskTagSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addProjectTaskTagSetItem(ProjectTaskTag item) {
		if (getProjectTaskTagSet() == null) {
			setProjectTaskTagSet(new HashSet<>());
		}
		getProjectTaskTagSet().add(item);
	}

	/**
	 * Remove the given {@link ProjectTaskTag} item from the {@code projectTaskTagSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeProjectTaskTagSetItem(ProjectTaskTag item) {
		if (getProjectTaskTagSet() == null) {
			return;
		}
		getProjectTaskTagSet().remove(item);
	}

	/**
	 * Clear the {@code projectTaskTagSet} collection.
	 *
	 */
	public void clearProjectTaskTagSet() {
		if (getProjectTaskTagSet() != null) {
			getProjectTaskTagSet().clear();
		}
	}

	public ProjectTaskSection getProjectTaskSection() {
		return projectTaskSection;
	}

	public void setProjectTaskSection(ProjectTaskSection projectTaskSection) {
		this.projectTaskSection = projectTaskSection;
	}

	public BigDecimal getTotalPlannedHrs() {
		return totalPlannedHrs == null ? BigDecimal.ZERO : totalPlannedHrs;
	}

	public void setTotalPlannedHrs(BigDecimal totalPlannedHrs) {
		this.totalPlannedHrs = totalPlannedHrs;
	}

	public BigDecimal getTotalRealHrs() {
		return totalRealHrs == null ? BigDecimal.ZERO : totalRealHrs;
	}

	public void setTotalRealHrs(BigDecimal totalRealHrs) {
		this.totalRealHrs = totalRealHrs;
	}

	public List<ProjectPlanningTime> getProjectPlanningTimeList() {
		return projectPlanningTimeList;
	}

	public void setProjectPlanningTimeList(List<ProjectPlanningTime> projectPlanningTimeList) {
		this.projectPlanningTimeList = projectPlanningTimeList;
	}

	/**
	 * Add the given {@link ProjectPlanningTime} item to the {@code projectPlanningTimeList}.
	 *
	 * <p>
	 * It sets {@code item.projectTask = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addProjectPlanningTimeListItem(ProjectPlanningTime item) {
		if (getProjectPlanningTimeList() == null) {
			setProjectPlanningTimeList(new ArrayList<>());
		}
		getProjectPlanningTimeList().add(item);
		item.setProjectTask(this);
	}

	/**
	 * Remove the given {@link ProjectPlanningTime} item from the {@code projectPlanningTimeList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeProjectPlanningTimeListItem(ProjectPlanningTime item) {
		if (getProjectPlanningTimeList() == null) {
			return;
		}
		getProjectPlanningTimeList().remove(item);
	}

	/**
	 * Clear the {@code projectPlanningTimeList} collection.
	 *
	 * <p>
	 * If you have to query {@link ProjectPlanningTime} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearProjectPlanningTimeList() {
		if (getProjectPlanningTimeList() != null) {
			getProjectPlanningTimeList().clear();
		}
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

	public BigDecimal getExTaxTotal() {
		return exTaxTotal == null ? BigDecimal.ZERO : exTaxTotal;
	}

	public void setExTaxTotal(BigDecimal exTaxTotal) {
		this.exTaxTotal = exTaxTotal;
	}

	public Integer getDiscountTypeSelect() {
		return discountTypeSelect == null ? 0 : discountTypeSelect;
	}

	public void setDiscountTypeSelect(Integer discountTypeSelect) {
		this.discountTypeSelect = discountTypeSelect;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount == null ? BigDecimal.ZERO : discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getPriceDiscounted() {
		return priceDiscounted == null ? BigDecimal.ZERO : priceDiscounted;
	}

	public void setPriceDiscounted(BigDecimal priceDiscounted) {
		this.priceDiscounted = priceDiscounted;
	}

	public Integer getInvoicingType() {
		return invoicingType == null ? 0 : invoicingType;
	}

	public void setInvoicingType(Integer invoicingType) {
		this.invoicingType = invoicingType;
	}

	public Boolean getIsPaid() {
		return isPaid == null ? Boolean.FALSE : isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	public User getCustomerReferral() {
		return customerReferral;
	}

	public void setCustomerReferral(User customerReferral) {
		this.customerReferral = customerReferral;
	}

	public SaleOrderLine getSaleOrderLine() {
		return saleOrderLine;
	}

	public void setSaleOrderLine(SaleOrderLine saleOrderLine) {
		this.saleOrderLine = saleOrderLine;
	}

	public InvoiceLine getInvoiceLine() {
		return invoiceLine;
	}

	public void setInvoiceLine(InvoiceLine invoiceLine) {
		this.invoiceLine = invoiceLine;
	}

	public Boolean getIsTaskRefused() {
		return isTaskRefused == null ? Boolean.FALSE : isTaskRefused;
	}

	public void setIsTaskRefused(Boolean isTaskRefused) {
		this.isTaskRefused = isTaskRefused;
	}

	public Integer getAssignment() {
		return assignment == null ? 0 : assignment;
	}

	public void setAssignment(Integer assignment) {
		this.assignment = assignment;
	}

	public Boolean getIsPrivate() {
		return isPrivate == null ? Boolean.FALSE : isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public Boolean getIsOrderAccepted() {
		return isOrderAccepted == null ? Boolean.FALSE : isOrderAccepted;
	}

	public void setIsOrderAccepted(Boolean isOrderAccepted) {
		this.isOrderAccepted = isOrderAccepted;
	}

	public String getInternalDescription() {
		return internalDescription;
	}

	public void setInternalDescription(String internalDescription) {
		this.internalDescription = internalDescription;
	}

	public ProjectVersion getTargetVersion() {
		return targetVersion;
	}

	public void setTargetVersion(ProjectVersion targetVersion) {
		this.targetVersion = targetVersion;
	}

	public Boolean getIsOrderProposed() {
		return isOrderProposed == null ? Boolean.FALSE : isOrderProposed;
	}

	public void setIsOrderProposed(Boolean isOrderProposed) {
		this.isOrderProposed = isOrderProposed;
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
		if (!(obj instanceof ProjectTask)) return false;

		final ProjectTask other = (ProjectTask) obj;
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
			.add("name", getName())
			.add("taskDate", getTaskDate())
			.add("status", getStatus())
			.add("statusBeforeComplete", getStatusBeforeComplete())
			.add("priority", getPriority())
			.add("taskDeadline", getTaskDeadline())
			.add("taskDuration", getTaskDuration())
			.add("sequence", getSequence())
			.add("isFirst", getIsFirst())
			.add("doApplyToAllNextTasks", getDoApplyToAllNextTasks())
			.add("hasDateOrFrequencyChanged", getHasDateOrFrequencyChanged())
			.omitNullValues()
			.toString();
	}
}
