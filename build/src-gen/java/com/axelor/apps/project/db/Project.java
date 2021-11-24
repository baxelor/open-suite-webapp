package com.axelor.apps.project.db;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.axelor.apps.account.db.InvoiceLine;
import com.axelor.apps.base.db.Address;
import com.axelor.apps.base.db.Company;
import com.axelor.apps.base.db.Currency;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.PriceList;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.businessproject.db.ManualElement;
import com.axelor.apps.businesssupport.db.ProjectAnnouncement;
import com.axelor.apps.businesssupport.db.ProjectVersion;
import com.axelor.apps.hr.db.ExpenseLine;
import com.axelor.apps.hr.db.TimesheetLine;
import com.axelor.apps.production.db.ProductionOrder;
import com.axelor.apps.purchase.db.PurchaseOrderLine;
import com.axelor.apps.sale.db.SaleOrderLine;
import com.axelor.auth.db.AuditableModel;
import com.axelor.auth.db.User;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.Widget;
import com.axelor.team.db.Team;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "PROJECT_PROJECT", indexes = { @Index(columnList = "name"), @Index(columnList = "team"), @Index(columnList = "assigned_to"), @Index(columnList = "parent_project"), @Index(columnList = "client_partner"), @Index(columnList = "contact_partner"), @Index(columnList = "company"), @Index(columnList = "fullName"), @Index(columnList = "currency"), @Index(columnList = "price_list"), @Index(columnList = "customer_address"), @Index(columnList = "name"), @Index(columnList = "code"), @Index(columnList = "fullName") })
public class Project extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_PROJECT_SEQ")
	@SequenceGenerator(name = "PROJECT_PROJECT_SEQ", sequenceName = "PROJECT_PROJECT_SEQ", allocationSize = 1)
	private Long id;

	@Widget(multiline = true)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String description;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Team team;

	@Widget(title = "Project Folders")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<ProjectFolder> projectFolderSet;

	@Widget(title = "Child Projects")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentProject", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Project> childProjectList;

	@Widget(title = "Assigned to")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private User assignedTo;

	@Widget(title = "Status", selection = "project.status.select")
	private Integer statusSelect = 1;

	@Widget(title = "Type of authorized categories")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<ProjectTaskCategory> projectTaskCategorySet;

	@Widget(title = "Tasks")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProjectTask> projectTaskList;

	private Boolean synchronize = Boolean.FALSE;

	private Boolean extendsMembersFromParent = Boolean.FALSE;

	@Widget(title = "Parent project")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Project parentProject;

	@Widget(title = "Wiki")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Wiki> wikiList;

	@Widget(title = "Resource booking")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ResourceBooking> resourceBookingList;

	@Widget(title = "Membres")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<User> membersUserSet;

	@Widget(title = "Customer")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner clientPartner;

	@Widget(title = "Customer Contact")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner contactPartner;

	@Widget(title = "From Date")
	private LocalDateTime fromDate;

	@Widget(title = "To Date")
	private LocalDateTime toDate;

	@Widget(title = "Due Date")
	private LocalDateTime dueDate;

	@Widget(title = "Estimated time (in days)")
	private BigDecimal estimatedTimeDays = BigDecimal.ZERO;

	@Widget(title = "Estimated time (in hours)")
	private BigDecimal estimatedTimeHrs = BigDecimal.ZERO;

	@Widget(title = "Time Spent")
	private BigDecimal timeSpent = BigDecimal.ZERO;

	@Widget(title = "Progress (%)")
	private BigDecimal progress = BigDecimal.ZERO;

	@Widget(title = "Sequence")
	private BigDecimal sequence = BigDecimal.ZERO;

	private Integer orderByState = 0;

	@Widget(help = "If this option is disabled this project cannot be selected on a timesheet.", massUpdate = true)
	private Boolean imputable = Boolean.TRUE;

	@Widget(title = "Finish tasks to start")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Project> finishToStartTaskSet;

	@Widget(title = "Start tasks to start")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Project> startToStartTaskSet;

	@Widget(title = "Finish tasks to finish")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Project> finishToFinishTaskSet;

	@Widget(title = "Start tasks to finish")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Project> startToFinishTaskSet;

	@Widget(title = "Company")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Company company;

	@Widget(title = "Type of authorized activities")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Product> productSet;

	@Widget(title = "Exclude planning")
	private Boolean excludePlanning = Boolean.FALSE;

	@Widget(title = "Project/Phase")
	private Boolean isProject = Boolean.TRUE;

	@Widget(title = "Show phases elements")
	private Boolean isShowPhasesElements = Boolean.TRUE;

	@Widget(selection = "project.project.project.type.select")
	private Integer projectTypeSelect = 1;

	@Basic
	@Type(type = "com.axelor.db.hibernate.type.ValueEnumType")
	private GenProjTypePerOrderLine genProjTypePerOrderLine = GenProjTypePerOrderLine.BUSINESS_PROJECT;

	@Widget(title = "Logged Time")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<TimesheetLine> timesheetLineList;

	@Widget(title = "Total planned hours")
	private BigDecimal totalPlannedHrs = BigDecimal.ZERO;

	@Widget(title = "Total real hours")
	private BigDecimal totalRealHrs = BigDecimal.ZERO;

	@Widget(title = "Exclude timesheet editor")
	private Boolean excludeTimesheetEditor = Boolean.FALSE;

	@Widget(title = "Currency", massUpdate = true)
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Currency currency;

	@Widget(title = "Invoicing sequence", selection = "business.project.invoicing.sequence.select")
	private Integer invoicingSequenceSelect = 0;

	@Widget(title = "Invoicing comment")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String invoicingComment;

	@Widget(title = "Finalized Orders Total", readonly = true)
	private BigDecimal totalSaleOrdersFinalized = BigDecimal.ZERO;

	@Widget(title = "Confirmed Orders Total", readonly = true)
	private BigDecimal totalSaleOrdersConfirmed = BigDecimal.ZERO;

	@Widget(title = "Invoiced Orders Total", readonly = true)
	private BigDecimal totalSaleOrdersInvoiced = BigDecimal.ZERO;

	@Widget(title = "Price list")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private PriceList priceList;

	@Widget(title = "Finalized Orders Total", readonly = true)
	private BigDecimal totalPurchaseOrdersFinalized = BigDecimal.ZERO;

	@Widget(title = "Confirmed Orders Total", readonly = true)
	private BigDecimal totalPurchaseOrdersConfirmed = BigDecimal.ZERO;

	@Widget(title = "Invoiced Orders Total", readonly = true)
	private BigDecimal totalPurchaseOrdersInvoiced = BigDecimal.ZERO;

	@Widget(title = "Planned Times Total", readonly = true)
	private BigDecimal totalTimesPlanned = BigDecimal.ZERO;

	@Widget(title = "Realised Times Total", readonly = true)
	private BigDecimal totalTimesRealised = BigDecimal.ZERO;

	@Widget(title = "ExpensesTotal", readonly = true)
	private BigDecimal totalExpenses = BigDecimal.ZERO;

	@Widget(title = "Estimated Costs Total", readonly = true)
	private BigDecimal totalEstimatedCosts = BigDecimal.ZERO;

	@Widget(title = "Real Costs Total", readonly = true)
	private BigDecimal totalRealCosts = BigDecimal.ZERO;

	@Widget(title = "Produced Turnover", readonly = true)
	private BigDecimal totalProducedTurnOver = BigDecimal.ZERO;

	@Widget(title = "Estimated Margin", readonly = true)
	private BigDecimal estimatedMargin = BigDecimal.ZERO;

	@Widget(title = "Real Margin (Time)", readonly = true)
	private BigDecimal realTimesMargin = BigDecimal.ZERO;

	@Widget(title = "Real Margin (Invoicing)", readonly = true)
	private BigDecimal realInvoicingMargin = BigDecimal.ZERO;

	@Widget(title = "Manual elements")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ManualElement> manualElementList;

	@Widget(readonly = true)
	private Boolean invoiced = Boolean.FALSE;

	@Widget(title = "Invoicing unit", selection = "hr.time.logging.preference.select")
	private String unitOnPrinting;

	@Widget(title = "Purchase order lines")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<PurchaseOrderLine> purchaseOrderLineList;

	@Widget(title = "Sale order lines")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<SaleOrderLine> saleOrderLineList;

	@Widget(title = "Expense lines")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<ExpenseLine> expensesLineList;

	@Widget(title = "Invoice lines")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<InvoiceLine> invoiceLineList;

	@Widget(title = "Business project")
	private Boolean isBusinessProject = Boolean.FALSE;

	@Widget(title = "To invoice")
	private Boolean toInvoice = Boolean.FALSE;

	@Widget(title = "Invoicing timesheet")
	private Boolean isInvoicingTimesheet = Boolean.FALSE;

	@Widget(title = "Invoicing Expenses")
	private Boolean isInvoicingExpenses = Boolean.FALSE;

	@Widget(title = "Invoicing Purchases")
	private Boolean isInvoicingPurchases = Boolean.FALSE;

	@Widget(title = "Consolidate phase invoicing")
	private Boolean consolidatePhaseWhenInvoicing = Boolean.TRUE;

	@Widget(title = "Address")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Address customerAddress;

	@Widget(title = "Production orders")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductionOrder> productionOrderList;

	@Widget(title = "Announcement")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProjectAnnouncement> announcementList;

	@Widget(title = "Project.version")
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "projectSet", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<ProjectVersion> roadmapSet;

	@NotNull
	private String name;

	@Widget(title = "Code")
	private String code;

	@Widget(title = "Name")
	@NameColumn
	private String fullName;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Project() {
	}

	public Project(String name, String code) {
		this.name = name;
		this.code = code;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Set<ProjectFolder> getProjectFolderSet() {
		return projectFolderSet;
	}

	public void setProjectFolderSet(Set<ProjectFolder> projectFolderSet) {
		this.projectFolderSet = projectFolderSet;
	}

	/**
	 * Add the given {@link ProjectFolder} item to the {@code projectFolderSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addProjectFolderSetItem(ProjectFolder item) {
		if (getProjectFolderSet() == null) {
			setProjectFolderSet(new HashSet<>());
		}
		getProjectFolderSet().add(item);
	}

	/**
	 * Remove the given {@link ProjectFolder} item from the {@code projectFolderSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeProjectFolderSetItem(ProjectFolder item) {
		if (getProjectFolderSet() == null) {
			return;
		}
		getProjectFolderSet().remove(item);
	}

	/**
	 * Clear the {@code projectFolderSet} collection.
	 *
	 */
	public void clearProjectFolderSet() {
		if (getProjectFolderSet() != null) {
			getProjectFolderSet().clear();
		}
	}

	public List<Project> getChildProjectList() {
		return childProjectList;
	}

	public void setChildProjectList(List<Project> childProjectList) {
		this.childProjectList = childProjectList;
	}

	/**
	 * Add the given {@link Project} item to the {@code childProjectList}.
	 *
	 * <p>
	 * It sets {@code item.parentProject = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addChildProjectListItem(Project item) {
		if (getChildProjectList() == null) {
			setChildProjectList(new ArrayList<>());
		}
		getChildProjectList().add(item);
		item.setParentProject(this);
	}

	/**
	 * Remove the given {@link Project} item from the {@code childProjectList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeChildProjectListItem(Project item) {
		if (getChildProjectList() == null) {
			return;
		}
		getChildProjectList().remove(item);
	}

	/**
	 * Clear the {@code childProjectList} collection.
	 *
	 * <p>
	 * If you have to query {@link Project} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearChildProjectList() {
		if (getChildProjectList() != null) {
			getChildProjectList().clear();
		}
	}

	public User getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(User assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Integer getStatusSelect() {
		return statusSelect == null ? 0 : statusSelect;
	}

	public void setStatusSelect(Integer statusSelect) {
		this.statusSelect = statusSelect;
	}

	public Set<ProjectTaskCategory> getProjectTaskCategorySet() {
		return projectTaskCategorySet;
	}

	public void setProjectTaskCategorySet(Set<ProjectTaskCategory> projectTaskCategorySet) {
		this.projectTaskCategorySet = projectTaskCategorySet;
	}

	/**
	 * Add the given {@link ProjectTaskCategory} item to the {@code projectTaskCategorySet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addProjectTaskCategorySetItem(ProjectTaskCategory item) {
		if (getProjectTaskCategorySet() == null) {
			setProjectTaskCategorySet(new HashSet<>());
		}
		getProjectTaskCategorySet().add(item);
	}

	/**
	 * Remove the given {@link ProjectTaskCategory} item from the {@code projectTaskCategorySet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeProjectTaskCategorySetItem(ProjectTaskCategory item) {
		if (getProjectTaskCategorySet() == null) {
			return;
		}
		getProjectTaskCategorySet().remove(item);
	}

	/**
	 * Clear the {@code projectTaskCategorySet} collection.
	 *
	 */
	public void clearProjectTaskCategorySet() {
		if (getProjectTaskCategorySet() != null) {
			getProjectTaskCategorySet().clear();
		}
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
	 * It sets {@code item.project = this} to ensure the proper relationship.
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
		item.setProject(this);
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

	public Boolean getSynchronize() {
		return synchronize == null ? Boolean.FALSE : synchronize;
	}

	public void setSynchronize(Boolean synchronize) {
		this.synchronize = synchronize;
	}

	public Boolean getExtendsMembersFromParent() {
		return extendsMembersFromParent == null ? Boolean.FALSE : extendsMembersFromParent;
	}

	public void setExtendsMembersFromParent(Boolean extendsMembersFromParent) {
		this.extendsMembersFromParent = extendsMembersFromParent;
	}

	public Project getParentProject() {
		return parentProject;
	}

	public void setParentProject(Project parentProject) {
		this.parentProject = parentProject;
	}

	public List<Wiki> getWikiList() {
		return wikiList;
	}

	public void setWikiList(List<Wiki> wikiList) {
		this.wikiList = wikiList;
	}

	/**
	 * Add the given {@link Wiki} item to the {@code wikiList}.
	 *
	 * <p>
	 * It sets {@code item.project = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addWikiListItem(Wiki item) {
		if (getWikiList() == null) {
			setWikiList(new ArrayList<>());
		}
		getWikiList().add(item);
		item.setProject(this);
	}

	/**
	 * Remove the given {@link Wiki} item from the {@code wikiList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeWikiListItem(Wiki item) {
		if (getWikiList() == null) {
			return;
		}
		getWikiList().remove(item);
	}

	/**
	 * Clear the {@code wikiList} collection.
	 *
	 * <p>
	 * If you have to query {@link Wiki} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearWikiList() {
		if (getWikiList() != null) {
			getWikiList().clear();
		}
	}

	public List<ResourceBooking> getResourceBookingList() {
		return resourceBookingList;
	}

	public void setResourceBookingList(List<ResourceBooking> resourceBookingList) {
		this.resourceBookingList = resourceBookingList;
	}

	/**
	 * Add the given {@link ResourceBooking} item to the {@code resourceBookingList}.
	 *
	 * <p>
	 * It sets {@code item.project = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addResourceBookingListItem(ResourceBooking item) {
		if (getResourceBookingList() == null) {
			setResourceBookingList(new ArrayList<>());
		}
		getResourceBookingList().add(item);
		item.setProject(this);
	}

	/**
	 * Remove the given {@link ResourceBooking} item from the {@code resourceBookingList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeResourceBookingListItem(ResourceBooking item) {
		if (getResourceBookingList() == null) {
			return;
		}
		getResourceBookingList().remove(item);
	}

	/**
	 * Clear the {@code resourceBookingList} collection.
	 *
	 * <p>
	 * If you have to query {@link ResourceBooking} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearResourceBookingList() {
		if (getResourceBookingList() != null) {
			getResourceBookingList().clear();
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

	public Partner getClientPartner() {
		return clientPartner;
	}

	public void setClientPartner(Partner clientPartner) {
		this.clientPartner = clientPartner;
	}

	public Partner getContactPartner() {
		return contactPartner;
	}

	public void setContactPartner(Partner contactPartner) {
		this.contactPartner = contactPartner;
	}

	public LocalDateTime getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDateTime fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDateTime getToDate() {
		return toDate;
	}

	public void setToDate(LocalDateTime toDate) {
		this.toDate = toDate;
	}

	public LocalDateTime getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
	}

	public BigDecimal getEstimatedTimeDays() {
		return estimatedTimeDays == null ? BigDecimal.ZERO : estimatedTimeDays;
	}

	public void setEstimatedTimeDays(BigDecimal estimatedTimeDays) {
		this.estimatedTimeDays = estimatedTimeDays;
	}

	public BigDecimal getEstimatedTimeHrs() {
		return estimatedTimeHrs == null ? BigDecimal.ZERO : estimatedTimeHrs;
	}

	public void setEstimatedTimeHrs(BigDecimal estimatedTimeHrs) {
		this.estimatedTimeHrs = estimatedTimeHrs;
	}

	public BigDecimal getTimeSpent() {
		return timeSpent == null ? BigDecimal.ZERO : timeSpent;
	}

	public void setTimeSpent(BigDecimal timeSpent) {
		this.timeSpent = timeSpent;
	}

	public BigDecimal getProgress() {
		return progress == null ? BigDecimal.ZERO : progress;
	}

	public void setProgress(BigDecimal progress) {
		this.progress = progress;
	}

	public BigDecimal getSequence() {
		return sequence == null ? BigDecimal.ZERO : sequence;
	}

	public void setSequence(BigDecimal sequence) {
		this.sequence = sequence;
	}

	public Integer getOrderByState() {
		return orderByState == null ? 0 : orderByState;
	}

	public void setOrderByState(Integer orderByState) {
		this.orderByState = orderByState;
	}

	/**
	 * If this option is disabled this project cannot be selected on a timesheet.
	 *
	 * @return the property value
	 */
	public Boolean getImputable() {
		return imputable == null ? Boolean.FALSE : imputable;
	}

	public void setImputable(Boolean imputable) {
		this.imputable = imputable;
	}

	public Set<Project> getFinishToStartTaskSet() {
		return finishToStartTaskSet;
	}

	public void setFinishToStartTaskSet(Set<Project> finishToStartTaskSet) {
		this.finishToStartTaskSet = finishToStartTaskSet;
	}

	/**
	 * Add the given {@link Project} item to the {@code finishToStartTaskSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addFinishToStartTaskSetItem(Project item) {
		if (getFinishToStartTaskSet() == null) {
			setFinishToStartTaskSet(new HashSet<>());
		}
		getFinishToStartTaskSet().add(item);
	}

	/**
	 * Remove the given {@link Project} item from the {@code finishToStartTaskSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeFinishToStartTaskSetItem(Project item) {
		if (getFinishToStartTaskSet() == null) {
			return;
		}
		getFinishToStartTaskSet().remove(item);
	}

	/**
	 * Clear the {@code finishToStartTaskSet} collection.
	 *
	 */
	public void clearFinishToStartTaskSet() {
		if (getFinishToStartTaskSet() != null) {
			getFinishToStartTaskSet().clear();
		}
	}

	public Set<Project> getStartToStartTaskSet() {
		return startToStartTaskSet;
	}

	public void setStartToStartTaskSet(Set<Project> startToStartTaskSet) {
		this.startToStartTaskSet = startToStartTaskSet;
	}

	/**
	 * Add the given {@link Project} item to the {@code startToStartTaskSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addStartToStartTaskSetItem(Project item) {
		if (getStartToStartTaskSet() == null) {
			setStartToStartTaskSet(new HashSet<>());
		}
		getStartToStartTaskSet().add(item);
	}

	/**
	 * Remove the given {@link Project} item from the {@code startToStartTaskSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeStartToStartTaskSetItem(Project item) {
		if (getStartToStartTaskSet() == null) {
			return;
		}
		getStartToStartTaskSet().remove(item);
	}

	/**
	 * Clear the {@code startToStartTaskSet} collection.
	 *
	 */
	public void clearStartToStartTaskSet() {
		if (getStartToStartTaskSet() != null) {
			getStartToStartTaskSet().clear();
		}
	}

	public Set<Project> getFinishToFinishTaskSet() {
		return finishToFinishTaskSet;
	}

	public void setFinishToFinishTaskSet(Set<Project> finishToFinishTaskSet) {
		this.finishToFinishTaskSet = finishToFinishTaskSet;
	}

	/**
	 * Add the given {@link Project} item to the {@code finishToFinishTaskSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addFinishToFinishTaskSetItem(Project item) {
		if (getFinishToFinishTaskSet() == null) {
			setFinishToFinishTaskSet(new HashSet<>());
		}
		getFinishToFinishTaskSet().add(item);
	}

	/**
	 * Remove the given {@link Project} item from the {@code finishToFinishTaskSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeFinishToFinishTaskSetItem(Project item) {
		if (getFinishToFinishTaskSet() == null) {
			return;
		}
		getFinishToFinishTaskSet().remove(item);
	}

	/**
	 * Clear the {@code finishToFinishTaskSet} collection.
	 *
	 */
	public void clearFinishToFinishTaskSet() {
		if (getFinishToFinishTaskSet() != null) {
			getFinishToFinishTaskSet().clear();
		}
	}

	public Set<Project> getStartToFinishTaskSet() {
		return startToFinishTaskSet;
	}

	public void setStartToFinishTaskSet(Set<Project> startToFinishTaskSet) {
		this.startToFinishTaskSet = startToFinishTaskSet;
	}

	/**
	 * Add the given {@link Project} item to the {@code startToFinishTaskSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addStartToFinishTaskSetItem(Project item) {
		if (getStartToFinishTaskSet() == null) {
			setStartToFinishTaskSet(new HashSet<>());
		}
		getStartToFinishTaskSet().add(item);
	}

	/**
	 * Remove the given {@link Project} item from the {@code startToFinishTaskSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeStartToFinishTaskSetItem(Project item) {
		if (getStartToFinishTaskSet() == null) {
			return;
		}
		getStartToFinishTaskSet().remove(item);
	}

	/**
	 * Clear the {@code startToFinishTaskSet} collection.
	 *
	 */
	public void clearStartToFinishTaskSet() {
		if (getStartToFinishTaskSet() != null) {
			getStartToFinishTaskSet().clear();
		}
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Set<Product> getProductSet() {
		return productSet;
	}

	public void setProductSet(Set<Product> productSet) {
		this.productSet = productSet;
	}

	/**
	 * Add the given {@link Product} item to the {@code productSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addProductSetItem(Product item) {
		if (getProductSet() == null) {
			setProductSet(new HashSet<>());
		}
		getProductSet().add(item);
	}

	/**
	 * Remove the given {@link Product} item from the {@code productSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeProductSetItem(Product item) {
		if (getProductSet() == null) {
			return;
		}
		getProductSet().remove(item);
	}

	/**
	 * Clear the {@code productSet} collection.
	 *
	 */
	public void clearProductSet() {
		if (getProductSet() != null) {
			getProductSet().clear();
		}
	}

	public Boolean getExcludePlanning() {
		return excludePlanning == null ? Boolean.FALSE : excludePlanning;
	}

	public void setExcludePlanning(Boolean excludePlanning) {
		this.excludePlanning = excludePlanning;
	}

	public Boolean getIsProject() {
		return isProject == null ? Boolean.FALSE : isProject;
	}

	public void setIsProject(Boolean isProject) {
		this.isProject = isProject;
	}

	public Boolean getIsShowPhasesElements() {
		return isShowPhasesElements == null ? Boolean.FALSE : isShowPhasesElements;
	}

	public void setIsShowPhasesElements(Boolean isShowPhasesElements) {
		this.isShowPhasesElements = isShowPhasesElements;
	}

	public Integer getProjectTypeSelect() {
		return projectTypeSelect == null ? 0 : projectTypeSelect;
	}

	public void setProjectTypeSelect(Integer projectTypeSelect) {
		this.projectTypeSelect = projectTypeSelect;
	}

	public GenProjTypePerOrderLine getGenProjTypePerOrderLine() {
		return genProjTypePerOrderLine;
	}

	public void setGenProjTypePerOrderLine(GenProjTypePerOrderLine genProjTypePerOrderLine) {
		this.genProjTypePerOrderLine = genProjTypePerOrderLine;
	}

	public List<TimesheetLine> getTimesheetLineList() {
		return timesheetLineList;
	}

	public void setTimesheetLineList(List<TimesheetLine> timesheetLineList) {
		this.timesheetLineList = timesheetLineList;
	}

	/**
	 * Add the given {@link TimesheetLine} item to the {@code timesheetLineList}.
	 *
	 * <p>
	 * It sets {@code item.project = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addTimesheetLineListItem(TimesheetLine item) {
		if (getTimesheetLineList() == null) {
			setTimesheetLineList(new ArrayList<>());
		}
		getTimesheetLineList().add(item);
		item.setProject(this);
	}

	/**
	 * Remove the given {@link TimesheetLine} item from the {@code timesheetLineList}.
	 *
	 * <p>
	 * It sets {@code item.project = null} to break the relationship.
	 * </p>
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeTimesheetLineListItem(TimesheetLine item) {
		if (getTimesheetLineList() == null) {
			return;
		}
		getTimesheetLineList().remove(item);
		item.setProject(null);
	}

	/**
	 * Clear the {@code timesheetLineList} collection.
	 *
	 * <p>
	 * It sets {@code item.project = null} to break the relationship.
	 * </p>
	 */
	public void clearTimesheetLineList() {
		if (getTimesheetLineList() != null) {
			for (TimesheetLine item : getTimesheetLineList()) {
        item.setProject(null);
      }
			getTimesheetLineList().clear();
		}
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

	public Boolean getExcludeTimesheetEditor() {
		return excludeTimesheetEditor == null ? Boolean.FALSE : excludeTimesheetEditor;
	}

	public void setExcludeTimesheetEditor(Boolean excludeTimesheetEditor) {
		this.excludeTimesheetEditor = excludeTimesheetEditor;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Integer getInvoicingSequenceSelect() {
		return invoicingSequenceSelect == null ? 0 : invoicingSequenceSelect;
	}

	public void setInvoicingSequenceSelect(Integer invoicingSequenceSelect) {
		this.invoicingSequenceSelect = invoicingSequenceSelect;
	}

	public String getInvoicingComment() {
		return invoicingComment;
	}

	public void setInvoicingComment(String invoicingComment) {
		this.invoicingComment = invoicingComment;
	}

	public BigDecimal getTotalSaleOrdersFinalized() {
		return totalSaleOrdersFinalized == null ? BigDecimal.ZERO : totalSaleOrdersFinalized;
	}

	public void setTotalSaleOrdersFinalized(BigDecimal totalSaleOrdersFinalized) {
		this.totalSaleOrdersFinalized = totalSaleOrdersFinalized;
	}

	public BigDecimal getTotalSaleOrdersConfirmed() {
		return totalSaleOrdersConfirmed == null ? BigDecimal.ZERO : totalSaleOrdersConfirmed;
	}

	public void setTotalSaleOrdersConfirmed(BigDecimal totalSaleOrdersConfirmed) {
		this.totalSaleOrdersConfirmed = totalSaleOrdersConfirmed;
	}

	public BigDecimal getTotalSaleOrdersInvoiced() {
		return totalSaleOrdersInvoiced == null ? BigDecimal.ZERO : totalSaleOrdersInvoiced;
	}

	public void setTotalSaleOrdersInvoiced(BigDecimal totalSaleOrdersInvoiced) {
		this.totalSaleOrdersInvoiced = totalSaleOrdersInvoiced;
	}

	public PriceList getPriceList() {
		return priceList;
	}

	public void setPriceList(PriceList priceList) {
		this.priceList = priceList;
	}

	public BigDecimal getTotalPurchaseOrdersFinalized() {
		return totalPurchaseOrdersFinalized == null ? BigDecimal.ZERO : totalPurchaseOrdersFinalized;
	}

	public void setTotalPurchaseOrdersFinalized(BigDecimal totalPurchaseOrdersFinalized) {
		this.totalPurchaseOrdersFinalized = totalPurchaseOrdersFinalized;
	}

	public BigDecimal getTotalPurchaseOrdersConfirmed() {
		return totalPurchaseOrdersConfirmed == null ? BigDecimal.ZERO : totalPurchaseOrdersConfirmed;
	}

	public void setTotalPurchaseOrdersConfirmed(BigDecimal totalPurchaseOrdersConfirmed) {
		this.totalPurchaseOrdersConfirmed = totalPurchaseOrdersConfirmed;
	}

	public BigDecimal getTotalPurchaseOrdersInvoiced() {
		return totalPurchaseOrdersInvoiced == null ? BigDecimal.ZERO : totalPurchaseOrdersInvoiced;
	}

	public void setTotalPurchaseOrdersInvoiced(BigDecimal totalPurchaseOrdersInvoiced) {
		this.totalPurchaseOrdersInvoiced = totalPurchaseOrdersInvoiced;
	}

	public BigDecimal getTotalTimesPlanned() {
		return totalTimesPlanned == null ? BigDecimal.ZERO : totalTimesPlanned;
	}

	public void setTotalTimesPlanned(BigDecimal totalTimesPlanned) {
		this.totalTimesPlanned = totalTimesPlanned;
	}

	public BigDecimal getTotalTimesRealised() {
		return totalTimesRealised == null ? BigDecimal.ZERO : totalTimesRealised;
	}

	public void setTotalTimesRealised(BigDecimal totalTimesRealised) {
		this.totalTimesRealised = totalTimesRealised;
	}

	public BigDecimal getTotalExpenses() {
		return totalExpenses == null ? BigDecimal.ZERO : totalExpenses;
	}

	public void setTotalExpenses(BigDecimal totalExpenses) {
		this.totalExpenses = totalExpenses;
	}

	public BigDecimal getTotalEstimatedCosts() {
		return totalEstimatedCosts == null ? BigDecimal.ZERO : totalEstimatedCosts;
	}

	public void setTotalEstimatedCosts(BigDecimal totalEstimatedCosts) {
		this.totalEstimatedCosts = totalEstimatedCosts;
	}

	public BigDecimal getTotalRealCosts() {
		return totalRealCosts == null ? BigDecimal.ZERO : totalRealCosts;
	}

	public void setTotalRealCosts(BigDecimal totalRealCosts) {
		this.totalRealCosts = totalRealCosts;
	}

	public BigDecimal getTotalProducedTurnOver() {
		return totalProducedTurnOver == null ? BigDecimal.ZERO : totalProducedTurnOver;
	}

	public void setTotalProducedTurnOver(BigDecimal totalProducedTurnOver) {
		this.totalProducedTurnOver = totalProducedTurnOver;
	}

	public BigDecimal getEstimatedMargin() {
		return estimatedMargin == null ? BigDecimal.ZERO : estimatedMargin;
	}

	public void setEstimatedMargin(BigDecimal estimatedMargin) {
		this.estimatedMargin = estimatedMargin;
	}

	public BigDecimal getRealTimesMargin() {
		return realTimesMargin == null ? BigDecimal.ZERO : realTimesMargin;
	}

	public void setRealTimesMargin(BigDecimal realTimesMargin) {
		this.realTimesMargin = realTimesMargin;
	}

	public BigDecimal getRealInvoicingMargin() {
		return realInvoicingMargin == null ? BigDecimal.ZERO : realInvoicingMargin;
	}

	public void setRealInvoicingMargin(BigDecimal realInvoicingMargin) {
		this.realInvoicingMargin = realInvoicingMargin;
	}

	public List<ManualElement> getManualElementList() {
		return manualElementList;
	}

	public void setManualElementList(List<ManualElement> manualElementList) {
		this.manualElementList = manualElementList;
	}

	/**
	 * Add the given {@link ManualElement} item to the {@code manualElementList}.
	 *
	 * <p>
	 * It sets {@code item.project = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addManualElementListItem(ManualElement item) {
		if (getManualElementList() == null) {
			setManualElementList(new ArrayList<>());
		}
		getManualElementList().add(item);
		item.setProject(this);
	}

	/**
	 * Remove the given {@link ManualElement} item from the {@code manualElementList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeManualElementListItem(ManualElement item) {
		if (getManualElementList() == null) {
			return;
		}
		getManualElementList().remove(item);
	}

	/**
	 * Clear the {@code manualElementList} collection.
	 *
	 * <p>
	 * If you have to query {@link ManualElement} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearManualElementList() {
		if (getManualElementList() != null) {
			getManualElementList().clear();
		}
	}

	public Boolean getInvoiced() {
		return invoiced == null ? Boolean.FALSE : invoiced;
	}

	public void setInvoiced(Boolean invoiced) {
		this.invoiced = invoiced;
	}

	public String getUnitOnPrinting() {
		return unitOnPrinting;
	}

	public void setUnitOnPrinting(String unitOnPrinting) {
		this.unitOnPrinting = unitOnPrinting;
	}

	public List<PurchaseOrderLine> getPurchaseOrderLineList() {
		return purchaseOrderLineList;
	}

	public void setPurchaseOrderLineList(List<PurchaseOrderLine> purchaseOrderLineList) {
		this.purchaseOrderLineList = purchaseOrderLineList;
	}

	/**
	 * Add the given {@link PurchaseOrderLine} item to the {@code purchaseOrderLineList}.
	 *
	 * <p>
	 * It sets {@code item.project = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addPurchaseOrderLineListItem(PurchaseOrderLine item) {
		if (getPurchaseOrderLineList() == null) {
			setPurchaseOrderLineList(new ArrayList<>());
		}
		getPurchaseOrderLineList().add(item);
		item.setProject(this);
	}

	/**
	 * Remove the given {@link PurchaseOrderLine} item from the {@code purchaseOrderLineList}.
	 *
	 * <p>
	 * It sets {@code item.project = null} to break the relationship.
	 * </p>
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removePurchaseOrderLineListItem(PurchaseOrderLine item) {
		if (getPurchaseOrderLineList() == null) {
			return;
		}
		getPurchaseOrderLineList().remove(item);
		item.setProject(null);
	}

	/**
	 * Clear the {@code purchaseOrderLineList} collection.
	 *
	 * <p>
	 * It sets {@code item.project = null} to break the relationship.
	 * </p>
	 */
	public void clearPurchaseOrderLineList() {
		if (getPurchaseOrderLineList() != null) {
			for (PurchaseOrderLine item : getPurchaseOrderLineList()) {
        item.setProject(null);
      }
			getPurchaseOrderLineList().clear();
		}
	}

	public List<SaleOrderLine> getSaleOrderLineList() {
		return saleOrderLineList;
	}

	public void setSaleOrderLineList(List<SaleOrderLine> saleOrderLineList) {
		this.saleOrderLineList = saleOrderLineList;
	}

	/**
	 * Add the given {@link SaleOrderLine} item to the {@code saleOrderLineList}.
	 *
	 * <p>
	 * It sets {@code item.project = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addSaleOrderLineListItem(SaleOrderLine item) {
		if (getSaleOrderLineList() == null) {
			setSaleOrderLineList(new ArrayList<>());
		}
		getSaleOrderLineList().add(item);
		item.setProject(this);
	}

	/**
	 * Remove the given {@link SaleOrderLine} item from the {@code saleOrderLineList}.
	 *
	 * <p>
	 * It sets {@code item.project = null} to break the relationship.
	 * </p>
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeSaleOrderLineListItem(SaleOrderLine item) {
		if (getSaleOrderLineList() == null) {
			return;
		}
		getSaleOrderLineList().remove(item);
		item.setProject(null);
	}

	/**
	 * Clear the {@code saleOrderLineList} collection.
	 *
	 * <p>
	 * It sets {@code item.project = null} to break the relationship.
	 * </p>
	 */
	public void clearSaleOrderLineList() {
		if (getSaleOrderLineList() != null) {
			for (SaleOrderLine item : getSaleOrderLineList()) {
        item.setProject(null);
      }
			getSaleOrderLineList().clear();
		}
	}

	public List<ExpenseLine> getExpensesLineList() {
		return expensesLineList;
	}

	public void setExpensesLineList(List<ExpenseLine> expensesLineList) {
		this.expensesLineList = expensesLineList;
	}

	/**
	 * Add the given {@link ExpenseLine} item to the {@code expensesLineList}.
	 *
	 * <p>
	 * It sets {@code item.project = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addExpensesLineListItem(ExpenseLine item) {
		if (getExpensesLineList() == null) {
			setExpensesLineList(new ArrayList<>());
		}
		getExpensesLineList().add(item);
		item.setProject(this);
	}

	/**
	 * Remove the given {@link ExpenseLine} item from the {@code expensesLineList}.
	 *
	 * <p>
	 * It sets {@code item.project = null} to break the relationship.
	 * </p>
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeExpensesLineListItem(ExpenseLine item) {
		if (getExpensesLineList() == null) {
			return;
		}
		getExpensesLineList().remove(item);
		item.setProject(null);
	}

	/**
	 * Clear the {@code expensesLineList} collection.
	 *
	 * <p>
	 * It sets {@code item.project = null} to break the relationship.
	 * </p>
	 */
	public void clearExpensesLineList() {
		if (getExpensesLineList() != null) {
			for (ExpenseLine item : getExpensesLineList()) {
        item.setProject(null);
      }
			getExpensesLineList().clear();
		}
	}

	public List<InvoiceLine> getInvoiceLineList() {
		return invoiceLineList;
	}

	public void setInvoiceLineList(List<InvoiceLine> invoiceLineList) {
		this.invoiceLineList = invoiceLineList;
	}

	/**
	 * Add the given {@link InvoiceLine} item to the {@code invoiceLineList}.
	 *
	 * <p>
	 * It sets {@code item.project = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addInvoiceLineListItem(InvoiceLine item) {
		if (getInvoiceLineList() == null) {
			setInvoiceLineList(new ArrayList<>());
		}
		getInvoiceLineList().add(item);
		item.setProject(this);
	}

	/**
	 * Remove the given {@link InvoiceLine} item from the {@code invoiceLineList}.
	 *
	 * <p>
	 * It sets {@code item.project = null} to break the relationship.
	 * </p>
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeInvoiceLineListItem(InvoiceLine item) {
		if (getInvoiceLineList() == null) {
			return;
		}
		getInvoiceLineList().remove(item);
		item.setProject(null);
	}

	/**
	 * Clear the {@code invoiceLineList} collection.
	 *
	 * <p>
	 * It sets {@code item.project = null} to break the relationship.
	 * </p>
	 */
	public void clearInvoiceLineList() {
		if (getInvoiceLineList() != null) {
			for (InvoiceLine item : getInvoiceLineList()) {
        item.setProject(null);
      }
			getInvoiceLineList().clear();
		}
	}

	public Boolean getIsBusinessProject() {
		return isBusinessProject == null ? Boolean.FALSE : isBusinessProject;
	}

	public void setIsBusinessProject(Boolean isBusinessProject) {
		this.isBusinessProject = isBusinessProject;
	}

	public Boolean getToInvoice() {
		return toInvoice == null ? Boolean.FALSE : toInvoice;
	}

	public void setToInvoice(Boolean toInvoice) {
		this.toInvoice = toInvoice;
	}

	public Boolean getIsInvoicingTimesheet() {
		return isInvoicingTimesheet == null ? Boolean.FALSE : isInvoicingTimesheet;
	}

	public void setIsInvoicingTimesheet(Boolean isInvoicingTimesheet) {
		this.isInvoicingTimesheet = isInvoicingTimesheet;
	}

	public Boolean getIsInvoicingExpenses() {
		return isInvoicingExpenses == null ? Boolean.FALSE : isInvoicingExpenses;
	}

	public void setIsInvoicingExpenses(Boolean isInvoicingExpenses) {
		this.isInvoicingExpenses = isInvoicingExpenses;
	}

	public Boolean getIsInvoicingPurchases() {
		return isInvoicingPurchases == null ? Boolean.FALSE : isInvoicingPurchases;
	}

	public void setIsInvoicingPurchases(Boolean isInvoicingPurchases) {
		this.isInvoicingPurchases = isInvoicingPurchases;
	}

	public Boolean getConsolidatePhaseWhenInvoicing() {
		return consolidatePhaseWhenInvoicing == null ? Boolean.FALSE : consolidatePhaseWhenInvoicing;
	}

	public void setConsolidatePhaseWhenInvoicing(Boolean consolidatePhaseWhenInvoicing) {
		this.consolidatePhaseWhenInvoicing = consolidatePhaseWhenInvoicing;
	}

	public Address getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(Address customerAddress) {
		this.customerAddress = customerAddress;
	}

	public List<ProductionOrder> getProductionOrderList() {
		return productionOrderList;
	}

	public void setProductionOrderList(List<ProductionOrder> productionOrderList) {
		this.productionOrderList = productionOrderList;
	}

	/**
	 * Add the given {@link ProductionOrder} item to the {@code productionOrderList}.
	 *
	 * <p>
	 * It sets {@code item.project = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addProductionOrderListItem(ProductionOrder item) {
		if (getProductionOrderList() == null) {
			setProductionOrderList(new ArrayList<>());
		}
		getProductionOrderList().add(item);
		item.setProject(this);
	}

	/**
	 * Remove the given {@link ProductionOrder} item from the {@code productionOrderList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeProductionOrderListItem(ProductionOrder item) {
		if (getProductionOrderList() == null) {
			return;
		}
		getProductionOrderList().remove(item);
	}

	/**
	 * Clear the {@code productionOrderList} collection.
	 *
	 * <p>
	 * If you have to query {@link ProductionOrder} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearProductionOrderList() {
		if (getProductionOrderList() != null) {
			getProductionOrderList().clear();
		}
	}

	public List<ProjectAnnouncement> getAnnouncementList() {
		return announcementList;
	}

	public void setAnnouncementList(List<ProjectAnnouncement> announcementList) {
		this.announcementList = announcementList;
	}

	/**
	 * Add the given {@link ProjectAnnouncement} item to the {@code announcementList}.
	 *
	 * <p>
	 * It sets {@code item.project = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addAnnouncementListItem(ProjectAnnouncement item) {
		if (getAnnouncementList() == null) {
			setAnnouncementList(new ArrayList<>());
		}
		getAnnouncementList().add(item);
		item.setProject(this);
	}

	/**
	 * Remove the given {@link ProjectAnnouncement} item from the {@code announcementList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeAnnouncementListItem(ProjectAnnouncement item) {
		if (getAnnouncementList() == null) {
			return;
		}
		getAnnouncementList().remove(item);
	}

	/**
	 * Clear the {@code announcementList} collection.
	 *
	 * <p>
	 * If you have to query {@link ProjectAnnouncement} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearAnnouncementList() {
		if (getAnnouncementList() != null) {
			getAnnouncementList().clear();
		}
	}

	public Set<ProjectVersion> getRoadmapSet() {
		return roadmapSet;
	}

	public void setRoadmapSet(Set<ProjectVersion> roadmapSet) {
		this.roadmapSet = roadmapSet;
	}

	/**
	 * Add the given {@link ProjectVersion} item to the {@code roadmapSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addRoadmapSetItem(ProjectVersion item) {
		if (getRoadmapSet() == null) {
			setRoadmapSet(new HashSet<>());
		}
		getRoadmapSet().add(item);
	}

	/**
	 * Remove the given {@link ProjectVersion} item from the {@code roadmapSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeRoadmapSetItem(ProjectVersion item) {
		if (getRoadmapSet() == null) {
			return;
		}
		getRoadmapSet().remove(item);
	}

	/**
	 * Clear the {@code roadmapSet} collection.
	 *
	 */
	public void clearRoadmapSet() {
		if (getRoadmapSet() != null) {
			getRoadmapSet().clear();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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
		if (!(obj instanceof Project)) return false;

		final Project other = (Project) obj;
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
			.add("statusSelect", getStatusSelect())
			.add("synchronize", getSynchronize())
			.add("extendsMembersFromParent", getExtendsMembersFromParent())
			.add("fromDate", getFromDate())
			.add("toDate", getToDate())
			.add("dueDate", getDueDate())
			.add("estimatedTimeDays", getEstimatedTimeDays())
			.add("estimatedTimeHrs", getEstimatedTimeHrs())
			.add("timeSpent", getTimeSpent())
			.add("progress", getProgress())
			.add("sequence", getSequence())
			.omitNullValues()
			.toString();
	}
}
