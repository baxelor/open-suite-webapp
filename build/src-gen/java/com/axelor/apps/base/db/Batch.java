package com.axelor.apps.base.db;

import java.time.LocalDate;
import java.time.ZonedDateTime;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.axelor.apps.account.db.AccountingBatch;
import com.axelor.apps.account.db.InvoiceBatch;
import com.axelor.apps.bankpayment.db.BankOrder;
import com.axelor.apps.bankpayment.db.BankPaymentBatch;
import com.axelor.apps.businessproject.db.ProjectInvoicingAssistantBatch;
import com.axelor.apps.contract.db.ContractBatch;
import com.axelor.apps.crm.db.CrmBatch;
import com.axelor.apps.hr.db.HrBatch;
import com.axelor.apps.production.db.ProductionBatch;
import com.axelor.apps.sale.db.SaleBatch;
import com.axelor.apps.supplychain.db.SupplychainBatch;
import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Widget;
import com.axelor.meta.db.MetaFile;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "BASE_BATCH", indexes = { @Index(columnList = "alarm_engine_batch"), @Index(columnList = "base_batch"), @Index(columnList = "mail_batch"), @Index(columnList = "meta_file"), @Index(columnList = "invoice_batch"), @Index(columnList = "accounting_batch"), @Index(columnList = "bank_order"), @Index(columnList = "bank_payment_batch"), @Index(columnList = "hr_batch"), @Index(columnList = "crm_batch"), @Index(columnList = "sale_batch"), @Index(columnList = "supplychain_batch"), @Index(columnList = "contract_batch"), @Index(columnList = "project_invoicing_assistant_batch"), @Index(columnList = "production_batch") })
public class Batch extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BASE_BATCH_SEQ")
	@SequenceGenerator(name = "BASE_BATCH_SEQ", sequenceName = "BASE_BATCH_SEQ", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AlarmEngineBatch alarmEngineBatch;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private BaseBatch baseBatch;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private MailBatch mailBatch;

	@Widget(title = "Start Date", readonly = true)
	private ZonedDateTime startDate;

	@Widget(title = "End Date", readonly = true)
	private ZonedDateTime endDate;

	@Widget(title = "Duration", readonly = true)
	private Long duration = 0L;

	@Widget(title = "Succeeded")
	private Integer done = 0;

	@Widget(title = "Anomaly")
	private Integer anomaly = 0;

	@Widget(title = "Comments")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String comments;

	@Widget(title = "File")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private MetaFile metaFile;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private InvoiceBatch invoiceBatch;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AccountingBatch accountingBatch;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private BankOrder bankOrder;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private BankPaymentBatch bankPaymentBatch;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private HrBatch hrBatch;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private CrmBatch crmBatch;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private SaleBatch saleBatch;

	@Widget(title = "Move date")
	private LocalDate moveDate;

	@Widget(title = "Type", selection = "supplychain.supplychain.batch.accounting.cut.off.type.select")
	private Integer accountingCutOffTypeSelect = 0;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private SupplychainBatch supplychainBatch;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ContractBatch contractBatch;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ProjectInvoicingAssistantBatch projectInvoicingAssistantBatch;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ProductionBatch productionBatch;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Batch() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public AlarmEngineBatch getAlarmEngineBatch() {
		return alarmEngineBatch;
	}

	public void setAlarmEngineBatch(AlarmEngineBatch alarmEngineBatch) {
		this.alarmEngineBatch = alarmEngineBatch;
	}

	public BaseBatch getBaseBatch() {
		return baseBatch;
	}

	public void setBaseBatch(BaseBatch baseBatch) {
		this.baseBatch = baseBatch;
	}

	public MailBatch getMailBatch() {
		return mailBatch;
	}

	public void setMailBatch(MailBatch mailBatch) {
		this.mailBatch = mailBatch;
	}

	public ZonedDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(ZonedDateTime startDate) {
		this.startDate = startDate;
	}

	public ZonedDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(ZonedDateTime endDate) {
		this.endDate = endDate;
	}

	public Long getDuration() {
		return duration == null ? 0L : duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Integer getDone() {
		return done == null ? 0 : done;
	}

	public void setDone(Integer done) {
		this.done = done;
	}

	public Integer getAnomaly() {
		return anomaly == null ? 0 : anomaly;
	}

	public void setAnomaly(Integer anomaly) {
		this.anomaly = anomaly;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public MetaFile getMetaFile() {
		return metaFile;
	}

	public void setMetaFile(MetaFile metaFile) {
		this.metaFile = metaFile;
	}

	public InvoiceBatch getInvoiceBatch() {
		return invoiceBatch;
	}

	public void setInvoiceBatch(InvoiceBatch invoiceBatch) {
		this.invoiceBatch = invoiceBatch;
	}

	public AccountingBatch getAccountingBatch() {
		return accountingBatch;
	}

	public void setAccountingBatch(AccountingBatch accountingBatch) {
		this.accountingBatch = accountingBatch;
	}

	public BankOrder getBankOrder() {
		return bankOrder;
	}

	public void setBankOrder(BankOrder bankOrder) {
		this.bankOrder = bankOrder;
	}

	public BankPaymentBatch getBankPaymentBatch() {
		return bankPaymentBatch;
	}

	public void setBankPaymentBatch(BankPaymentBatch bankPaymentBatch) {
		this.bankPaymentBatch = bankPaymentBatch;
	}

	public HrBatch getHrBatch() {
		return hrBatch;
	}

	public void setHrBatch(HrBatch hrBatch) {
		this.hrBatch = hrBatch;
	}

	public CrmBatch getCrmBatch() {
		return crmBatch;
	}

	public void setCrmBatch(CrmBatch crmBatch) {
		this.crmBatch = crmBatch;
	}

	public SaleBatch getSaleBatch() {
		return saleBatch;
	}

	public void setSaleBatch(SaleBatch saleBatch) {
		this.saleBatch = saleBatch;
	}

	public LocalDate getMoveDate() {
		return moveDate;
	}

	public void setMoveDate(LocalDate moveDate) {
		this.moveDate = moveDate;
	}

	public Integer getAccountingCutOffTypeSelect() {
		return accountingCutOffTypeSelect == null ? 0 : accountingCutOffTypeSelect;
	}

	public void setAccountingCutOffTypeSelect(Integer accountingCutOffTypeSelect) {
		this.accountingCutOffTypeSelect = accountingCutOffTypeSelect;
	}

	public SupplychainBatch getSupplychainBatch() {
		return supplychainBatch;
	}

	public void setSupplychainBatch(SupplychainBatch supplychainBatch) {
		this.supplychainBatch = supplychainBatch;
	}

	public ContractBatch getContractBatch() {
		return contractBatch;
	}

	public void setContractBatch(ContractBatch contractBatch) {
		this.contractBatch = contractBatch;
	}

	public ProjectInvoicingAssistantBatch getProjectInvoicingAssistantBatch() {
		return projectInvoicingAssistantBatch;
	}

	public void setProjectInvoicingAssistantBatch(ProjectInvoicingAssistantBatch projectInvoicingAssistantBatch) {
		this.projectInvoicingAssistantBatch = projectInvoicingAssistantBatch;
	}

	public ProductionBatch getProductionBatch() {
		return productionBatch;
	}

	public void setProductionBatch(ProductionBatch productionBatch) {
		this.productionBatch = productionBatch;
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
		if (!(obj instanceof Batch)) return false;

		final Batch other = (Batch) obj;
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
			.add("startDate", getStartDate())
			.add("endDate", getEndDate())
			.add("duration", getDuration())
			.add("done", getDone())
			.add("anomaly", getAnomaly())
			.add("moveDate", getMoveDate())
			.add("accountingCutOffTypeSelect", getAccountingCutOffTypeSelect())
			.omitNullValues()
			.toString();
	}
}
