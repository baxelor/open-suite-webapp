package com.axelor.apps.businessproject.db;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.axelor.apps.account.db.Invoice;
import com.axelor.apps.hr.db.ExpenseLine;
import com.axelor.apps.hr.db.TimesheetLine;
import com.axelor.apps.production.db.ManufOrder;
import com.axelor.apps.project.db.Project;
import com.axelor.apps.project.db.ProjectTask;
import com.axelor.apps.purchase.db.PurchaseOrderLine;
import com.axelor.apps.sale.db.SaleOrderLine;
import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Cacheable
@Table(name = "BUSINESSPROJECT_INVOICING_PROJECT", indexes = { @Index(columnList = "project"), @Index(columnList = "invoice") })
public class InvoicingProject extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BUSINESSPROJECT_INVOICING_PROJECT_SEQ")
	@SequenceGenerator(name = "BUSINESSPROJECT_INVOICING_PROJECT_SEQ", sequenceName = "BUSINESSPROJECT_INVOICING_PROJECT_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Business project")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Project project;

	@Widget(title = "Log Times")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<TimesheetLine> logTimesSet;

	@Widget(title = "Sale order lines")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<SaleOrderLine> saleOrderLineSet;

	@Widget(title = "Purchase order lines")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<PurchaseOrderLine> purchaseOrderLineSet;

	@Widget(title = "Expense Lines")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<ExpenseLine> expenseLineSet;

	@Widget(title = "Project")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Project> projectSet;

	@Widget(title = "Tasks")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<ProjectTask> projectTaskSet;

	@Widget(title = "Log Times Priority", selection = "invoicing.project.priority.select")
	private Integer logTimesSetPrioritySelect = 3;

	@Widget(title = "Sale order lines Priority", selection = "invoicing.project.priority.select")
	private Integer saleOrderLineSetPrioritySelect = 1;

	@Widget(title = "Purchase order lines Priority", selection = "invoicing.project.priority.select")
	private Integer purchaseOrderLineSetPrioritySelect = 2;

	@Widget(title = "Expense Lines Priority", selection = "invoicing.project.priority.select")
	private Integer expenseLineSetPrioritySelect = 4;

	@Widget(title = "Project Priority", selection = "invoicing.project.priority.select")
	private Integer projectSetPrioritySelect = 5;

	@Widget(title = "Task Priority", selection = "invoicing.project.priority.select")
	private Integer projectTaskSetPrioritySelect = 6;

	@Widget(title = "Invoice generated")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Invoice invoice;

	@Widget(title = "Deadline")
	private LocalDate deadlineDate;

	@Widget(title = "Comments")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String comments;

	@Widget(title = "Attach the Annex to the invoice")
	private Boolean attachAnnexToInvoice = Boolean.FALSE;

	@Widget(title = "Status", selection = "business.project.invoicing.project.status.select")
	private Integer statusSelect = 1;

	@Widget(title = "Consolidate phase invoicing")
	private Boolean consolidatePhaseWhenInvoicing = Boolean.FALSE;

	@Widget(title = "Manuf. Orders Selection")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<ManufOrder> manufOrderSet;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public InvoicingProject() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Set<TimesheetLine> getLogTimesSet() {
		return logTimesSet;
	}

	public void setLogTimesSet(Set<TimesheetLine> logTimesSet) {
		this.logTimesSet = logTimesSet;
	}

	/**
	 * Add the given {@link TimesheetLine} item to the {@code logTimesSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addLogTimesSetItem(TimesheetLine item) {
		if (getLogTimesSet() == null) {
			setLogTimesSet(new HashSet<>());
		}
		getLogTimesSet().add(item);
	}

	/**
	 * Remove the given {@link TimesheetLine} item from the {@code logTimesSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeLogTimesSetItem(TimesheetLine item) {
		if (getLogTimesSet() == null) {
			return;
		}
		getLogTimesSet().remove(item);
	}

	/**
	 * Clear the {@code logTimesSet} collection.
	 *
	 */
	public void clearLogTimesSet() {
		if (getLogTimesSet() != null) {
			getLogTimesSet().clear();
		}
	}

	public Set<SaleOrderLine> getSaleOrderLineSet() {
		return saleOrderLineSet;
	}

	public void setSaleOrderLineSet(Set<SaleOrderLine> saleOrderLineSet) {
		this.saleOrderLineSet = saleOrderLineSet;
	}

	/**
	 * Add the given {@link SaleOrderLine} item to the {@code saleOrderLineSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addSaleOrderLineSetItem(SaleOrderLine item) {
		if (getSaleOrderLineSet() == null) {
			setSaleOrderLineSet(new HashSet<>());
		}
		getSaleOrderLineSet().add(item);
	}

	/**
	 * Remove the given {@link SaleOrderLine} item from the {@code saleOrderLineSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeSaleOrderLineSetItem(SaleOrderLine item) {
		if (getSaleOrderLineSet() == null) {
			return;
		}
		getSaleOrderLineSet().remove(item);
	}

	/**
	 * Clear the {@code saleOrderLineSet} collection.
	 *
	 */
	public void clearSaleOrderLineSet() {
		if (getSaleOrderLineSet() != null) {
			getSaleOrderLineSet().clear();
		}
	}

	public Set<PurchaseOrderLine> getPurchaseOrderLineSet() {
		return purchaseOrderLineSet;
	}

	public void setPurchaseOrderLineSet(Set<PurchaseOrderLine> purchaseOrderLineSet) {
		this.purchaseOrderLineSet = purchaseOrderLineSet;
	}

	/**
	 * Add the given {@link PurchaseOrderLine} item to the {@code purchaseOrderLineSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addPurchaseOrderLineSetItem(PurchaseOrderLine item) {
		if (getPurchaseOrderLineSet() == null) {
			setPurchaseOrderLineSet(new HashSet<>());
		}
		getPurchaseOrderLineSet().add(item);
	}

	/**
	 * Remove the given {@link PurchaseOrderLine} item from the {@code purchaseOrderLineSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removePurchaseOrderLineSetItem(PurchaseOrderLine item) {
		if (getPurchaseOrderLineSet() == null) {
			return;
		}
		getPurchaseOrderLineSet().remove(item);
	}

	/**
	 * Clear the {@code purchaseOrderLineSet} collection.
	 *
	 */
	public void clearPurchaseOrderLineSet() {
		if (getPurchaseOrderLineSet() != null) {
			getPurchaseOrderLineSet().clear();
		}
	}

	public Set<ExpenseLine> getExpenseLineSet() {
		return expenseLineSet;
	}

	public void setExpenseLineSet(Set<ExpenseLine> expenseLineSet) {
		this.expenseLineSet = expenseLineSet;
	}

	/**
	 * Add the given {@link ExpenseLine} item to the {@code expenseLineSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addExpenseLineSetItem(ExpenseLine item) {
		if (getExpenseLineSet() == null) {
			setExpenseLineSet(new HashSet<>());
		}
		getExpenseLineSet().add(item);
	}

	/**
	 * Remove the given {@link ExpenseLine} item from the {@code expenseLineSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeExpenseLineSetItem(ExpenseLine item) {
		if (getExpenseLineSet() == null) {
			return;
		}
		getExpenseLineSet().remove(item);
	}

	/**
	 * Clear the {@code expenseLineSet} collection.
	 *
	 */
	public void clearExpenseLineSet() {
		if (getExpenseLineSet() != null) {
			getExpenseLineSet().clear();
		}
	}

	public Set<Project> getProjectSet() {
		return projectSet;
	}

	public void setProjectSet(Set<Project> projectSet) {
		this.projectSet = projectSet;
	}

	/**
	 * Add the given {@link Project} item to the {@code projectSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addProjectSetItem(Project item) {
		if (getProjectSet() == null) {
			setProjectSet(new HashSet<>());
		}
		getProjectSet().add(item);
	}

	/**
	 * Remove the given {@link Project} item from the {@code projectSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeProjectSetItem(Project item) {
		if (getProjectSet() == null) {
			return;
		}
		getProjectSet().remove(item);
	}

	/**
	 * Clear the {@code projectSet} collection.
	 *
	 */
	public void clearProjectSet() {
		if (getProjectSet() != null) {
			getProjectSet().clear();
		}
	}

	public Set<ProjectTask> getProjectTaskSet() {
		return projectTaskSet;
	}

	public void setProjectTaskSet(Set<ProjectTask> projectTaskSet) {
		this.projectTaskSet = projectTaskSet;
	}

	/**
	 * Add the given {@link ProjectTask} item to the {@code projectTaskSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addProjectTaskSetItem(ProjectTask item) {
		if (getProjectTaskSet() == null) {
			setProjectTaskSet(new HashSet<>());
		}
		getProjectTaskSet().add(item);
	}

	/**
	 * Remove the given {@link ProjectTask} item from the {@code projectTaskSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeProjectTaskSetItem(ProjectTask item) {
		if (getProjectTaskSet() == null) {
			return;
		}
		getProjectTaskSet().remove(item);
	}

	/**
	 * Clear the {@code projectTaskSet} collection.
	 *
	 */
	public void clearProjectTaskSet() {
		if (getProjectTaskSet() != null) {
			getProjectTaskSet().clear();
		}
	}

	public Integer getLogTimesSetPrioritySelect() {
		return logTimesSetPrioritySelect == null ? 0 : logTimesSetPrioritySelect;
	}

	public void setLogTimesSetPrioritySelect(Integer logTimesSetPrioritySelect) {
		this.logTimesSetPrioritySelect = logTimesSetPrioritySelect;
	}

	public Integer getSaleOrderLineSetPrioritySelect() {
		return saleOrderLineSetPrioritySelect == null ? 0 : saleOrderLineSetPrioritySelect;
	}

	public void setSaleOrderLineSetPrioritySelect(Integer saleOrderLineSetPrioritySelect) {
		this.saleOrderLineSetPrioritySelect = saleOrderLineSetPrioritySelect;
	}

	public Integer getPurchaseOrderLineSetPrioritySelect() {
		return purchaseOrderLineSetPrioritySelect == null ? 0 : purchaseOrderLineSetPrioritySelect;
	}

	public void setPurchaseOrderLineSetPrioritySelect(Integer purchaseOrderLineSetPrioritySelect) {
		this.purchaseOrderLineSetPrioritySelect = purchaseOrderLineSetPrioritySelect;
	}

	public Integer getExpenseLineSetPrioritySelect() {
		return expenseLineSetPrioritySelect == null ? 0 : expenseLineSetPrioritySelect;
	}

	public void setExpenseLineSetPrioritySelect(Integer expenseLineSetPrioritySelect) {
		this.expenseLineSetPrioritySelect = expenseLineSetPrioritySelect;
	}

	public Integer getProjectSetPrioritySelect() {
		return projectSetPrioritySelect == null ? 0 : projectSetPrioritySelect;
	}

	public void setProjectSetPrioritySelect(Integer projectSetPrioritySelect) {
		this.projectSetPrioritySelect = projectSetPrioritySelect;
	}

	public Integer getProjectTaskSetPrioritySelect() {
		return projectTaskSetPrioritySelect == null ? 0 : projectTaskSetPrioritySelect;
	}

	public void setProjectTaskSetPrioritySelect(Integer projectTaskSetPrioritySelect) {
		this.projectTaskSetPrioritySelect = projectTaskSetPrioritySelect;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public LocalDate getDeadlineDate() {
		return deadlineDate;
	}

	public void setDeadlineDate(LocalDate deadlineDate) {
		this.deadlineDate = deadlineDate;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Boolean getAttachAnnexToInvoice() {
		return attachAnnexToInvoice == null ? Boolean.FALSE : attachAnnexToInvoice;
	}

	public void setAttachAnnexToInvoice(Boolean attachAnnexToInvoice) {
		this.attachAnnexToInvoice = attachAnnexToInvoice;
	}

	public Integer getStatusSelect() {
		return statusSelect == null ? 0 : statusSelect;
	}

	public void setStatusSelect(Integer statusSelect) {
		this.statusSelect = statusSelect;
	}

	public Boolean getConsolidatePhaseWhenInvoicing() {
		return consolidatePhaseWhenInvoicing == null ? Boolean.FALSE : consolidatePhaseWhenInvoicing;
	}

	public void setConsolidatePhaseWhenInvoicing(Boolean consolidatePhaseWhenInvoicing) {
		this.consolidatePhaseWhenInvoicing = consolidatePhaseWhenInvoicing;
	}

	public Set<ManufOrder> getManufOrderSet() {
		return manufOrderSet;
	}

	public void setManufOrderSet(Set<ManufOrder> manufOrderSet) {
		this.manufOrderSet = manufOrderSet;
	}

	/**
	 * Add the given {@link ManufOrder} item to the {@code manufOrderSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addManufOrderSetItem(ManufOrder item) {
		if (getManufOrderSet() == null) {
			setManufOrderSet(new HashSet<>());
		}
		getManufOrderSet().add(item);
	}

	/**
	 * Remove the given {@link ManufOrder} item from the {@code manufOrderSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeManufOrderSetItem(ManufOrder item) {
		if (getManufOrderSet() == null) {
			return;
		}
		getManufOrderSet().remove(item);
	}

	/**
	 * Clear the {@code manufOrderSet} collection.
	 *
	 */
	public void clearManufOrderSet() {
		if (getManufOrderSet() != null) {
			getManufOrderSet().clear();
		}
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
		if (!(obj instanceof InvoicingProject)) return false;

		final InvoicingProject other = (InvoicingProject) obj;
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
			.add("logTimesSetPrioritySelect", getLogTimesSetPrioritySelect())
			.add("saleOrderLineSetPrioritySelect", getSaleOrderLineSetPrioritySelect())
			.add("purchaseOrderLineSetPrioritySelect", getPurchaseOrderLineSetPrioritySelect())
			.add("expenseLineSetPrioritySelect", getExpenseLineSetPrioritySelect())
			.add("projectSetPrioritySelect", getProjectSetPrioritySelect())
			.add("projectTaskSetPrioritySelect", getProjectTaskSetPrioritySelect())
			.add("deadlineDate", getDeadlineDate())
			.add("attachAnnexToInvoice", getAttachAnnexToInvoice())
			.add("statusSelect", getStatusSelect())
			.add("consolidatePhaseWhenInvoicing", getConsolidatePhaseWhenInvoicing())
			.omitNullValues()
			.toString();
	}
}
