package com.axelor.apps.contract.db;

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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.axelor.apps.base.db.Batch;
import com.axelor.apps.base.db.Company;
import com.axelor.apps.base.db.Currency;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.project.db.Project;
import com.axelor.auth.db.AuditableModel;
import com.axelor.auth.db.User;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "CONTRACT_CONTRACT", indexes = { @Index(columnList = "name"), @Index(columnList = "company"), @Index(columnList = "partner"), @Index(columnList = "terminated_by_user"), @Index(columnList = "current_invoice_period"), @Index(columnList = "currency"), @Index(columnList = "current_contract_version"), @Index(columnList = "next_version"), @Index(columnList = "project") })
public class Contract extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTRACT_CONTRACT_SEQ")
	@SequenceGenerator(name = "CONTRACT_CONTRACT_SEQ", sequenceName = "CONTRACT_CONTRACT_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Type", selection = "contract.target.type.select")
	@NotNull
	private Integer targetTypeSelect = 1;

	@Widget(title = "Number of finished periods")
	private Integer periodNumber = 0;

	@Widget(title = "Status", selection = "contract.status.select")
	@NotNull
	private Integer statusSelect = 1;

	@Widget(title = "Number of renewal")
	private Integer renewalNumber = 0;

	@Widget(title = "Contract version")
	private Integer versionNumber = -1;

	@Widget(title = "Name")
	@NameColumn
	private String name;

	@Widget(title = "Notes", multiline = true)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String note;

	@Widget(title = "Contract N°", readonly = true)
	private String contractId;

	@Widget(title = "Manage invoices")
	private Boolean isInvoicingManagement = Boolean.FALSE;

	@Widget(title = "Consumption management")
	private Boolean isConsumptionManagement = Boolean.FALSE;

	@Widget(title = "Additional benefit management")
	private Boolean isAdditionaBenefitManagement = Boolean.FALSE;

	@Widget(title = "To closed")
	private Boolean toClosed = Boolean.FALSE;

	@Widget(title = "Terminated manually")
	private Boolean terminatedManually = Boolean.FALSE;

	@Widget(title = "First period end date")
	private LocalDate firstPeriodEndDate;

	@Widget(title = "Start date")
	private LocalDate startDate;

	@Widget(title = "End date")
	private LocalDate endDate;

	@Widget(title = "Terminated date")
	private LocalDate terminatedDate;

	@Widget(title = "Engagement start date")
	private LocalDate engagementStartDate;

	@Widget(title = "Termination demand date")
	private LocalDate terminationDemandDate;

	@Widget(title = "Last renewal date")
	private LocalDate lastRenewalDate;

	@Widget(title = "Start of next invoicing period")
	private LocalDate invoicePeriodStartDate;

	@Widget(title = "End of next invoicing period")
	private LocalDate invoicePeriodEndDate;

	@Widget(title = "Invoicing date")
	private LocalDate invoicingDate;

	@Widget(title = "Company")
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Company company;

	@Widget(title = "Partner")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner partner;

	@Widget(title = "Terminated By")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private User terminatedByUser;

	@Widget(title = "Current invoice period")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private InvoicePeriod currentInvoicePeriod;

	@Widget(title = "Currency")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Currency currency;

	@Widget(title = "Current version")
	@NotNull
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ContractVersion currentContractVersion;

	@Widget(title = "Next version")
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ContractVersion nextVersion;

	@Widget(title = "Next Invoice Additional Benefit")
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<ContractLine> additionalBenefitContractLineList;

	@Widget(title = "Invoice period history")
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<InvoicePeriod> historyInvoicePeriodList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contractHistory", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("createdOn DESC")
	private List<ContractVersion> versionHistory;

	@Widget(title = "Consumption for next invoice")
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<ConsumptionLine> consumptionLineList;

	@Widget(title = "Batches")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Batch> batchSet;

	@Widget(title = "Project")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Project project;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Contract() {
	}

	public Contract(String name) {
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

	public Integer getTargetTypeSelect() {
		return targetTypeSelect == null ? 0 : targetTypeSelect;
	}

	public void setTargetTypeSelect(Integer targetTypeSelect) {
		this.targetTypeSelect = targetTypeSelect;
	}

	public Integer getPeriodNumber() {
		return periodNumber == null ? 0 : periodNumber;
	}

	public void setPeriodNumber(Integer periodNumber) {
		this.periodNumber = periodNumber;
	}

	public Integer getStatusSelect() {
		return statusSelect == null ? 0 : statusSelect;
	}

	public void setStatusSelect(Integer statusSelect) {
		this.statusSelect = statusSelect;
	}

	public Integer getRenewalNumber() {
		return renewalNumber == null ? 0 : renewalNumber;
	}

	public void setRenewalNumber(Integer renewalNumber) {
		this.renewalNumber = renewalNumber;
	}

	public Integer getVersionNumber() {
		return versionNumber == null ? 0 : versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public Boolean getIsInvoicingManagement() {
		return isInvoicingManagement == null ? Boolean.FALSE : isInvoicingManagement;
	}

	public void setIsInvoicingManagement(Boolean isInvoicingManagement) {
		this.isInvoicingManagement = isInvoicingManagement;
	}

	public Boolean getIsConsumptionManagement() {
		return isConsumptionManagement == null ? Boolean.FALSE : isConsumptionManagement;
	}

	public void setIsConsumptionManagement(Boolean isConsumptionManagement) {
		this.isConsumptionManagement = isConsumptionManagement;
	}

	public Boolean getIsAdditionaBenefitManagement() {
		return isAdditionaBenefitManagement == null ? Boolean.FALSE : isAdditionaBenefitManagement;
	}

	public void setIsAdditionaBenefitManagement(Boolean isAdditionaBenefitManagement) {
		this.isAdditionaBenefitManagement = isAdditionaBenefitManagement;
	}

	public Boolean getToClosed() {
		return toClosed == null ? Boolean.FALSE : toClosed;
	}

	public void setToClosed(Boolean toClosed) {
		this.toClosed = toClosed;
	}

	public Boolean getTerminatedManually() {
		return terminatedManually == null ? Boolean.FALSE : terminatedManually;
	}

	public void setTerminatedManually(Boolean terminatedManually) {
		this.terminatedManually = terminatedManually;
	}

	public LocalDate getFirstPeriodEndDate() {
		return firstPeriodEndDate;
	}

	public void setFirstPeriodEndDate(LocalDate firstPeriodEndDate) {
		this.firstPeriodEndDate = firstPeriodEndDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalDate getTerminatedDate() {
		return terminatedDate;
	}

	public void setTerminatedDate(LocalDate terminatedDate) {
		this.terminatedDate = terminatedDate;
	}

	public LocalDate getEngagementStartDate() {
		return engagementStartDate;
	}

	public void setEngagementStartDate(LocalDate engagementStartDate) {
		this.engagementStartDate = engagementStartDate;
	}

	public LocalDate getTerminationDemandDate() {
		return terminationDemandDate;
	}

	public void setTerminationDemandDate(LocalDate terminationDemandDate) {
		this.terminationDemandDate = terminationDemandDate;
	}

	public LocalDate getLastRenewalDate() {
		return lastRenewalDate;
	}

	public void setLastRenewalDate(LocalDate lastRenewalDate) {
		this.lastRenewalDate = lastRenewalDate;
	}

	public LocalDate getInvoicePeriodStartDate() {
		return invoicePeriodStartDate;
	}

	public void setInvoicePeriodStartDate(LocalDate invoicePeriodStartDate) {
		this.invoicePeriodStartDate = invoicePeriodStartDate;
	}

	public LocalDate getInvoicePeriodEndDate() {
		return invoicePeriodEndDate;
	}

	public void setInvoicePeriodEndDate(LocalDate invoicePeriodEndDate) {
		this.invoicePeriodEndDate = invoicePeriodEndDate;
	}

	public LocalDate getInvoicingDate() {
		return invoicingDate;
	}

	public void setInvoicingDate(LocalDate invoicingDate) {
		this.invoicingDate = invoicingDate;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public User getTerminatedByUser() {
		return terminatedByUser;
	}

	public void setTerminatedByUser(User terminatedByUser) {
		this.terminatedByUser = terminatedByUser;
	}

	public InvoicePeriod getCurrentInvoicePeriod() {
		return currentInvoicePeriod;
	}

	public void setCurrentInvoicePeriod(InvoicePeriod currentInvoicePeriod) {
		this.currentInvoicePeriod = currentInvoicePeriod;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public ContractVersion getCurrentContractVersion() {
		return currentContractVersion;
	}

	public void setCurrentContractVersion(ContractVersion currentContractVersion) {
		this.currentContractVersion = currentContractVersion;
	}

	public ContractVersion getNextVersion() {
		return nextVersion;
	}

	public void setNextVersion(ContractVersion nextVersion) {
		this.nextVersion = nextVersion;
	}

	public List<ContractLine> getAdditionalBenefitContractLineList() {
		return additionalBenefitContractLineList;
	}

	public void setAdditionalBenefitContractLineList(List<ContractLine> additionalBenefitContractLineList) {
		this.additionalBenefitContractLineList = additionalBenefitContractLineList;
	}

	/**
	 * Add the given {@link ContractLine} item to the {@code additionalBenefitContractLineList}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addAdditionalBenefitContractLineListItem(ContractLine item) {
		if (getAdditionalBenefitContractLineList() == null) {
			setAdditionalBenefitContractLineList(new ArrayList<>());
		}
		getAdditionalBenefitContractLineList().add(item);
	}

	/**
	 * Remove the given {@link ContractLine} item from the {@code additionalBenefitContractLineList}.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeAdditionalBenefitContractLineListItem(ContractLine item) {
		if (getAdditionalBenefitContractLineList() == null) {
			return;
		}
		getAdditionalBenefitContractLineList().remove(item);
	}

	/**
	 * Clear the {@code additionalBenefitContractLineList} collection.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 */
	public void clearAdditionalBenefitContractLineList() {
		if (getAdditionalBenefitContractLineList() != null) {
			getAdditionalBenefitContractLineList().clear();
		}
	}

	public List<InvoicePeriod> getHistoryInvoicePeriodList() {
		return historyInvoicePeriodList;
	}

	public void setHistoryInvoicePeriodList(List<InvoicePeriod> historyInvoicePeriodList) {
		this.historyInvoicePeriodList = historyInvoicePeriodList;
	}

	/**
	 * Add the given {@link InvoicePeriod} item to the {@code historyInvoicePeriodList}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addHistoryInvoicePeriodListItem(InvoicePeriod item) {
		if (getHistoryInvoicePeriodList() == null) {
			setHistoryInvoicePeriodList(new ArrayList<>());
		}
		getHistoryInvoicePeriodList().add(item);
	}

	/**
	 * Remove the given {@link InvoicePeriod} item from the {@code historyInvoicePeriodList}.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeHistoryInvoicePeriodListItem(InvoicePeriod item) {
		if (getHistoryInvoicePeriodList() == null) {
			return;
		}
		getHistoryInvoicePeriodList().remove(item);
	}

	/**
	 * Clear the {@code historyInvoicePeriodList} collection.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 */
	public void clearHistoryInvoicePeriodList() {
		if (getHistoryInvoicePeriodList() != null) {
			getHistoryInvoicePeriodList().clear();
		}
	}

	public List<ContractVersion> getVersionHistory() {
		return versionHistory;
	}

	public void setVersionHistory(List<ContractVersion> versionHistory) {
		this.versionHistory = versionHistory;
	}

	/**
	 * Add the given {@link ContractVersion} item to the {@code versionHistory}.
	 *
	 * <p>
	 * It sets {@code item.contractHistory = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addVersionHistory(ContractVersion item) {
		if (getVersionHistory() == null) {
			setVersionHistory(new ArrayList<>());
		}
		getVersionHistory().add(item);
		item.setContractHistory(this);
	}

	/**
	 * Remove the given {@link ContractVersion} item from the {@code versionHistory}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeVersionHistory(ContractVersion item) {
		if (getVersionHistory() == null) {
			return;
		}
		getVersionHistory().remove(item);
	}

	/**
	 * Clear the {@code versionHistory} collection.
	 *
	 * <p>
	 * If you have to query {@link ContractVersion} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearVersionHistory() {
		if (getVersionHistory() != null) {
			getVersionHistory().clear();
		}
	}

	public List<ConsumptionLine> getConsumptionLineList() {
		return consumptionLineList;
	}

	public void setConsumptionLineList(List<ConsumptionLine> consumptionLineList) {
		this.consumptionLineList = consumptionLineList;
	}

	/**
	 * Add the given {@link ConsumptionLine} item to the {@code consumptionLineList}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addConsumptionLineListItem(ConsumptionLine item) {
		if (getConsumptionLineList() == null) {
			setConsumptionLineList(new ArrayList<>());
		}
		getConsumptionLineList().add(item);
	}

	/**
	 * Remove the given {@link ConsumptionLine} item from the {@code consumptionLineList}.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeConsumptionLineListItem(ConsumptionLine item) {
		if (getConsumptionLineList() == null) {
			return;
		}
		getConsumptionLineList().remove(item);
	}

	/**
	 * Clear the {@code consumptionLineList} collection.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 */
	public void clearConsumptionLineList() {
		if (getConsumptionLineList() != null) {
			getConsumptionLineList().clear();
		}
	}

	public Set<Batch> getBatchSet() {
		return batchSet;
	}

	public void setBatchSet(Set<Batch> batchSet) {
		this.batchSet = batchSet;
	}

	/**
	 * Add the given {@link Batch} item to the {@code batchSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addBatchSetItem(Batch item) {
		if (getBatchSet() == null) {
			setBatchSet(new HashSet<>());
		}
		getBatchSet().add(item);
	}

	/**
	 * Remove the given {@link Batch} item from the {@code batchSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeBatchSetItem(Batch item) {
		if (getBatchSet() == null) {
			return;
		}
		getBatchSet().remove(item);
	}

	/**
	 * Clear the {@code batchSet} collection.
	 *
	 */
	public void clearBatchSet() {
		if (getBatchSet() != null) {
			getBatchSet().clear();
		}
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
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
		if (!(obj instanceof Contract)) return false;

		final Contract other = (Contract) obj;
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
			.add("targetTypeSelect", getTargetTypeSelect())
			.add("periodNumber", getPeriodNumber())
			.add("statusSelect", getStatusSelect())
			.add("renewalNumber", getRenewalNumber())
			.add("versionNumber", getVersionNumber())
			.add("name", getName())
			.add("contractId", getContractId())
			.add("isInvoicingManagement", getIsInvoicingManagement())
			.add("isConsumptionManagement", getIsConsumptionManagement())
			.add("isAdditionaBenefitManagement", getIsAdditionaBenefitManagement())
			.add("toClosed", getToClosed())
			.omitNullValues()
			.toString();
	}
}
