package com.axelor.apps.production.db;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
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

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axelor.apps.hr.db.TimesheetLine;
import com.axelor.apps.stock.db.StockMove;
import com.axelor.apps.stock.db.StockMoveLine;
import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.VirtualColumn;
import com.axelor.db.annotations.Widget;
import com.axelor.meta.db.MetaFile;
import com.google.common.base.MoreObjects;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "PRODUCTION_OPERATION_ORDER", indexes = { @Index(columnList = "name"), @Index(columnList = "manuf_order"), @Index(columnList = "work_center"), @Index(columnList = "machine"), @Index(columnList = "prod_process_line"), @Index(columnList = "bar_code"), @Index(columnList = "machine_tool") })
public class OperationOrder extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCTION_OPERATION_ORDER_SEQ")
	@SequenceGenerator(name = "PRODUCTION_OPERATION_ORDER_SEQ", sequenceName = "PRODUCTION_OPERATION_ORDER_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Priority")
	private Integer priority = 0;

	@Widget(title = "Name")
	private String name;

	@Widget(title = "Operation name")
	private String operationName;

	@Widget(title = "Manufacturing order")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ManufOrder manufOrder;

	@Widget(title = "Work center")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private WorkCenter workCenter;

	@Widget(title = "Machine")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Machine machine;

	@Widget(title = "Human resources")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "operationOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProdHumanResource> prodHumanResourceList;

	@Widget(title = "Products to consume")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "toConsumeOperationOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProdProduct> toConsumeProdProductList;

	@Widget(title = "Consumed products")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "consumedOperationOrder", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<StockMoveLine> consumedStockMoveLineList;

	@Widget(title = "Differences")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "diffConsumeOperationOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProdProduct> diffConsumeProdProductList;

	@Widget(title = "Status", selection = "production.manuf.order.status.select")
	private Integer statusSelect = 0;

	@Widget(title = "Operation")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ProdProcessLine prodProcessLine;

	@Widget(title = "Planned start")
	private LocalDateTime plannedStartDateT;

	@Widget(title = "Planned end")
	private LocalDateTime plannedEndDateT;

	@Widget(title = "Real start")
	private LocalDateTime realStartDateT;

	@Widget(title = "Real end")
	private LocalDateTime realEndDateT;

	@Widget(title = "Stock move in")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inOperationOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StockMove> inStockMoveList;

	@Widget(title = "Planned duration (hh:mm:ss)")
	private Long plannedDuration = 0L;

	@Widget(title = "Real duration (hh:mm:ss)")
	private Long realDuration = 0L;

	@VirtualColumn
	@Access(AccessType.PROPERTY)
	private Boolean isWithDifference = Boolean.FALSE;

	@Widget(title = "Operation Order Durations")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "operationOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OperationOrderDuration> operationOrderDurationList;

	@Widget(title = "Comments")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String comments;

	@Widget(title = "Barcode", readonly = true)
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private MetaFile barCode;

	@Widget(title = "Tools")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private MachineTool machineTool;

	@Widget(title = "Use this line in the generated purchase order")
	private Boolean useLineInGeneratedPurchaseOrder = Boolean.FALSE;

	@Widget(title = "Outsourcing")
	private Boolean outsourcing = Boolean.FALSE;

	@Widget(title = "To invoice")
	private Boolean isToInvoice = Boolean.FALSE;

	@Widget(title = "Timesheet lines")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "operationOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TimesheetLine> timesheetLineList;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public OperationOrder() {
	}

	public OperationOrder(Integer priority, String name, String operationName, ManufOrder manufOrder, WorkCenter workCenter, Machine machine, Integer statusSelect, ProdProcessLine prodProcessLine, MachineTool machineTool) {
		this.priority = priority;
		this.name = name;
		this.operationName = operationName;
		this.manufOrder = manufOrder;
		this.workCenter = workCenter;
		this.machine = machine;
		this.statusSelect = statusSelect;
		this.prodProcessLine = prodProcessLine;
		this.machineTool = machineTool;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPriority() {
		return priority == null ? 0 : priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public ManufOrder getManufOrder() {
		return manufOrder;
	}

	public void setManufOrder(ManufOrder manufOrder) {
		this.manufOrder = manufOrder;
	}

	public WorkCenter getWorkCenter() {
		return workCenter;
	}

	public void setWorkCenter(WorkCenter workCenter) {
		this.workCenter = workCenter;
	}

	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
	}

	public List<ProdHumanResource> getProdHumanResourceList() {
		return prodHumanResourceList;
	}

	public void setProdHumanResourceList(List<ProdHumanResource> prodHumanResourceList) {
		this.prodHumanResourceList = prodHumanResourceList;
	}

	/**
	 * Add the given {@link ProdHumanResource} item to the {@code prodHumanResourceList}.
	 *
	 * <p>
	 * It sets {@code item.operationOrder = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addProdHumanResourceListItem(ProdHumanResource item) {
		if (getProdHumanResourceList() == null) {
			setProdHumanResourceList(new ArrayList<>());
		}
		getProdHumanResourceList().add(item);
		item.setOperationOrder(this);
	}

	/**
	 * Remove the given {@link ProdHumanResource} item from the {@code prodHumanResourceList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeProdHumanResourceListItem(ProdHumanResource item) {
		if (getProdHumanResourceList() == null) {
			return;
		}
		getProdHumanResourceList().remove(item);
	}

	/**
	 * Clear the {@code prodHumanResourceList} collection.
	 *
	 * <p>
	 * If you have to query {@link ProdHumanResource} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearProdHumanResourceList() {
		if (getProdHumanResourceList() != null) {
			getProdHumanResourceList().clear();
		}
	}

	public List<ProdProduct> getToConsumeProdProductList() {
		return toConsumeProdProductList;
	}

	public void setToConsumeProdProductList(List<ProdProduct> toConsumeProdProductList) {
		this.toConsumeProdProductList = toConsumeProdProductList;
	}

	/**
	 * Add the given {@link ProdProduct} item to the {@code toConsumeProdProductList}.
	 *
	 * <p>
	 * It sets {@code item.toConsumeOperationOrder = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addToConsumeProdProductListItem(ProdProduct item) {
		if (getToConsumeProdProductList() == null) {
			setToConsumeProdProductList(new ArrayList<>());
		}
		getToConsumeProdProductList().add(item);
		item.setToConsumeOperationOrder(this);
	}

	/**
	 * Remove the given {@link ProdProduct} item from the {@code toConsumeProdProductList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeToConsumeProdProductListItem(ProdProduct item) {
		if (getToConsumeProdProductList() == null) {
			return;
		}
		getToConsumeProdProductList().remove(item);
	}

	/**
	 * Clear the {@code toConsumeProdProductList} collection.
	 *
	 * <p>
	 * If you have to query {@link ProdProduct} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearToConsumeProdProductList() {
		if (getToConsumeProdProductList() != null) {
			getToConsumeProdProductList().clear();
		}
	}

	public List<StockMoveLine> getConsumedStockMoveLineList() {
		return consumedStockMoveLineList;
	}

	public void setConsumedStockMoveLineList(List<StockMoveLine> consumedStockMoveLineList) {
		this.consumedStockMoveLineList = consumedStockMoveLineList;
	}

	/**
	 * Add the given {@link StockMoveLine} item to the {@code consumedStockMoveLineList}.
	 *
	 * <p>
	 * It sets {@code item.consumedOperationOrder = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addConsumedStockMoveLineListItem(StockMoveLine item) {
		if (getConsumedStockMoveLineList() == null) {
			setConsumedStockMoveLineList(new ArrayList<>());
		}
		getConsumedStockMoveLineList().add(item);
		item.setConsumedOperationOrder(this);
	}

	/**
	 * Remove the given {@link StockMoveLine} item from the {@code consumedStockMoveLineList}.
	 *
	 * <p>
	 * It sets {@code item.consumedOperationOrder = null} to break the relationship.
	 * </p>
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeConsumedStockMoveLineListItem(StockMoveLine item) {
		if (getConsumedStockMoveLineList() == null) {
			return;
		}
		getConsumedStockMoveLineList().remove(item);
		item.setConsumedOperationOrder(null);
	}

	/**
	 * Clear the {@code consumedStockMoveLineList} collection.
	 *
	 * <p>
	 * It sets {@code item.consumedOperationOrder = null} to break the relationship.
	 * </p>
	 */
	public void clearConsumedStockMoveLineList() {
		if (getConsumedStockMoveLineList() != null) {
			for (StockMoveLine item : getConsumedStockMoveLineList()) {
        item.setConsumedOperationOrder(null);
      }
			getConsumedStockMoveLineList().clear();
		}
	}

	public List<ProdProduct> getDiffConsumeProdProductList() {
		return diffConsumeProdProductList;
	}

	public void setDiffConsumeProdProductList(List<ProdProduct> diffConsumeProdProductList) {
		this.diffConsumeProdProductList = diffConsumeProdProductList;
	}

	/**
	 * Add the given {@link ProdProduct} item to the {@code diffConsumeProdProductList}.
	 *
	 * <p>
	 * It sets {@code item.diffConsumeOperationOrder = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addDiffConsumeProdProductListItem(ProdProduct item) {
		if (getDiffConsumeProdProductList() == null) {
			setDiffConsumeProdProductList(new ArrayList<>());
		}
		getDiffConsumeProdProductList().add(item);
		item.setDiffConsumeOperationOrder(this);
	}

	/**
	 * Remove the given {@link ProdProduct} item from the {@code diffConsumeProdProductList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeDiffConsumeProdProductListItem(ProdProduct item) {
		if (getDiffConsumeProdProductList() == null) {
			return;
		}
		getDiffConsumeProdProductList().remove(item);
	}

	/**
	 * Clear the {@code diffConsumeProdProductList} collection.
	 *
	 * <p>
	 * If you have to query {@link ProdProduct} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearDiffConsumeProdProductList() {
		if (getDiffConsumeProdProductList() != null) {
			getDiffConsumeProdProductList().clear();
		}
	}

	public Integer getStatusSelect() {
		return statusSelect == null ? 0 : statusSelect;
	}

	public void setStatusSelect(Integer statusSelect) {
		this.statusSelect = statusSelect;
	}

	public ProdProcessLine getProdProcessLine() {
		return prodProcessLine;
	}

	public void setProdProcessLine(ProdProcessLine prodProcessLine) {
		this.prodProcessLine = prodProcessLine;
	}

	public LocalDateTime getPlannedStartDateT() {
		return plannedStartDateT;
	}

	public void setPlannedStartDateT(LocalDateTime plannedStartDateT) {
		this.plannedStartDateT = plannedStartDateT;
	}

	public LocalDateTime getPlannedEndDateT() {
		return plannedEndDateT;
	}

	public void setPlannedEndDateT(LocalDateTime plannedEndDateT) {
		this.plannedEndDateT = plannedEndDateT;
	}

	public LocalDateTime getRealStartDateT() {
		return realStartDateT;
	}

	public void setRealStartDateT(LocalDateTime realStartDateT) {
		this.realStartDateT = realStartDateT;
	}

	public LocalDateTime getRealEndDateT() {
		return realEndDateT;
	}

	public void setRealEndDateT(LocalDateTime realEndDateT) {
		this.realEndDateT = realEndDateT;
	}

	public List<StockMove> getInStockMoveList() {
		return inStockMoveList;
	}

	public void setInStockMoveList(List<StockMove> inStockMoveList) {
		this.inStockMoveList = inStockMoveList;
	}

	/**
	 * Add the given {@link StockMove} item to the {@code inStockMoveList}.
	 *
	 * <p>
	 * It sets {@code item.inOperationOrder = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addInStockMoveListItem(StockMove item) {
		if (getInStockMoveList() == null) {
			setInStockMoveList(new ArrayList<>());
		}
		getInStockMoveList().add(item);
		item.setInOperationOrder(this);
	}

	/**
	 * Remove the given {@link StockMove} item from the {@code inStockMoveList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeInStockMoveListItem(StockMove item) {
		if (getInStockMoveList() == null) {
			return;
		}
		getInStockMoveList().remove(item);
	}

	/**
	 * Clear the {@code inStockMoveList} collection.
	 *
	 * <p>
	 * If you have to query {@link StockMove} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearInStockMoveList() {
		if (getInStockMoveList() != null) {
			getInStockMoveList().clear();
		}
	}

	public Long getPlannedDuration() {
		return plannedDuration == null ? 0L : plannedDuration;
	}

	public void setPlannedDuration(Long plannedDuration) {
		this.plannedDuration = plannedDuration;
	}

	public Long getRealDuration() {
		return realDuration == null ? 0L : realDuration;
	}

	public void setRealDuration(Long realDuration) {
		this.realDuration = realDuration;
	}

	public Boolean getIsWithDifference() {
		try {
			isWithDifference = computeIsWithDifference();
		} catch (NullPointerException e) {
			Logger logger = LoggerFactory.getLogger(getClass());
			logger.error("NPE in function field: getIsWithDifference()", e);
		}
		return isWithDifference;
	}

	protected Boolean computeIsWithDifference() {
		if(diffConsumeProdProductList == null || diffConsumeProdProductList.isEmpty())
		  	return false;
		return true;
	}

	public void setIsWithDifference(Boolean isWithDifference) {
		this.isWithDifference = isWithDifference;
	}

	public List<OperationOrderDuration> getOperationOrderDurationList() {
		return operationOrderDurationList;
	}

	public void setOperationOrderDurationList(List<OperationOrderDuration> operationOrderDurationList) {
		this.operationOrderDurationList = operationOrderDurationList;
	}

	/**
	 * Add the given {@link OperationOrderDuration} item to the {@code operationOrderDurationList}.
	 *
	 * <p>
	 * It sets {@code item.operationOrder = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addOperationOrderDurationListItem(OperationOrderDuration item) {
		if (getOperationOrderDurationList() == null) {
			setOperationOrderDurationList(new ArrayList<>());
		}
		getOperationOrderDurationList().add(item);
		item.setOperationOrder(this);
	}

	/**
	 * Remove the given {@link OperationOrderDuration} item from the {@code operationOrderDurationList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeOperationOrderDurationListItem(OperationOrderDuration item) {
		if (getOperationOrderDurationList() == null) {
			return;
		}
		getOperationOrderDurationList().remove(item);
	}

	/**
	 * Clear the {@code operationOrderDurationList} collection.
	 *
	 * <p>
	 * If you have to query {@link OperationOrderDuration} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearOperationOrderDurationList() {
		if (getOperationOrderDurationList() != null) {
			getOperationOrderDurationList().clear();
		}
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public MetaFile getBarCode() {
		return barCode;
	}

	public void setBarCode(MetaFile barCode) {
		this.barCode = barCode;
	}

	public MachineTool getMachineTool() {
		return machineTool;
	}

	public void setMachineTool(MachineTool machineTool) {
		this.machineTool = machineTool;
	}

	public Boolean getUseLineInGeneratedPurchaseOrder() {
		return useLineInGeneratedPurchaseOrder == null ? Boolean.FALSE : useLineInGeneratedPurchaseOrder;
	}

	public void setUseLineInGeneratedPurchaseOrder(Boolean useLineInGeneratedPurchaseOrder) {
		this.useLineInGeneratedPurchaseOrder = useLineInGeneratedPurchaseOrder;
	}

	public Boolean getOutsourcing() {
		return outsourcing == null ? Boolean.FALSE : outsourcing;
	}

	public void setOutsourcing(Boolean outsourcing) {
		this.outsourcing = outsourcing;
	}

	public Boolean getIsToInvoice() {
		return isToInvoice == null ? Boolean.FALSE : isToInvoice;
	}

	public void setIsToInvoice(Boolean isToInvoice) {
		this.isToInvoice = isToInvoice;
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
	 * It sets {@code item.operationOrder = this} to ensure the proper relationship.
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
		item.setOperationOrder(this);
	}

	/**
	 * Remove the given {@link TimesheetLine} item from the {@code timesheetLineList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeTimesheetLineListItem(TimesheetLine item) {
		if (getTimesheetLineList() == null) {
			return;
		}
		getTimesheetLineList().remove(item);
	}

	/**
	 * Clear the {@code timesheetLineList} collection.
	 *
	 * <p>
	 * If you have to query {@link TimesheetLine} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearTimesheetLineList() {
		if (getTimesheetLineList() != null) {
			getTimesheetLineList().clear();
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
		if (!(obj instanceof OperationOrder)) return false;

		final OperationOrder other = (OperationOrder) obj;
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
			.add("priority", getPriority())
			.add("name", getName())
			.add("operationName", getOperationName())
			.add("statusSelect", getStatusSelect())
			.add("plannedStartDateT", getPlannedStartDateT())
			.add("plannedEndDateT", getPlannedEndDateT())
			.add("realStartDateT", getRealStartDateT())
			.add("realEndDateT", getRealEndDateT())
			.add("plannedDuration", getPlannedDuration())
			.add("realDuration", getRealDuration())
			.add("useLineInGeneratedPurchaseOrder", getUseLineInGeneratedPurchaseOrder())
			.omitNullValues()
			.toString();
	}
}
