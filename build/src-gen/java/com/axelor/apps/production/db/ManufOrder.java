package com.axelor.apps.production.db;

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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.axelor.apps.base.db.CancelReason;
import com.axelor.apps.base.db.Company;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.db.Unit;
import com.axelor.apps.hr.db.TimesheetLine;
import com.axelor.apps.purchase.db.PurchaseOrder;
import com.axelor.apps.sale.db.SaleOrder;
import com.axelor.apps.stock.db.StockLocation;
import com.axelor.apps.stock.db.StockMove;
import com.axelor.apps.stock.db.StockMoveLine;
import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.Track;
import com.axelor.db.annotations.TrackField;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "PRODUCTION_MANUF_ORDER", uniqueConstraints = { @UniqueConstraint(columnNames = { "manufOrderSeq", "company" }) }, indexes = { @Index(columnList = "manuf_order_merge_result"), @Index(columnList = "company"), @Index(columnList = "client_partner"), @Index(columnList = "manufOrderSeq"), @Index(columnList = "unit"), @Index(columnList = "bill_of_material"), @Index(columnList = "product"), @Index(columnList = "prod_process"), @Index(columnList = "workshop_stock_location"), @Index(columnList = "waste_stock_move"), @Index(columnList = "cancel_reason"), @Index(columnList = "purchase_order"), @Index(columnList = "machine_type"), @Index(columnList = "machine") })
@Track(fields = { @TrackField(name = "qty"), @TrackField(name = "prioritySelect"), @TrackField(name = "plannedStartDateT"), @TrackField(name = "plannedEndDateT"), @TrackField(name = "clientPartner"), @TrackField(name = "statusSelect"), @TrackField(name = "cancelReasonStr") })
public class ManufOrder extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCTION_MANUF_ORDER_SEQ")
	@SequenceGenerator(name = "PRODUCTION_MANUF_ORDER_SEQ", sequenceName = "PRODUCTION_MANUF_ORDER_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Manufacturing order merge result")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ManufOrder manufOrderMergeResult;

	@Widget(title = "Production orders")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<ProductionOrder> productionOrderSet;

	@Widget(title = "Operation orders")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "manufOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("priority")
	private List<OperationOrder> operationOrderList;

	@Widget(title = "Qty")
	@Digits(integer = 10, fraction = 10)
	private BigDecimal qty = BigDecimal.ZERO;

	@Widget(title = "Company")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Company company;

	@Widget(title = "Customer")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner clientPartner;

	@Widget(title = "Sale orders")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<SaleOrder> saleOrderSet;

	@Widget(title = "MO")
	@NameColumn
	private String manufOrderSeq;

	@Widget(title = "Priority", selection = "production.order.priority.select")
	private Integer prioritySelect = 2;

	@Widget(title = "Type", selection = "manuf.order.type.select")
	private Integer typeSelect = 1;

	@Widget(title = "Products to consume")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "toConsumeManufOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProdProduct> toConsumeProdProductList;

	@Widget(title = "Consumed products")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "consumedManufOrder", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<StockMoveLine> consumedStockMoveLineList;

	@Widget(title = "Difference")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "diffConsumeManufOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProdProduct> diffConsumeProdProductList;

	@Widget(title = "Products to produce")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "toProduceManufOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProdProduct> toProduceProdProductList;

	@Widget(title = "Produced products")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "producedManufOrder", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<StockMoveLine> producedStockMoveLineList;

	@Widget(title = "Waste")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "wasteManufOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProdProduct> wasteProdProductList;

	@Widget(title = "Manage consumed products on operations")
	private Boolean isConsProOnOperation = Boolean.FALSE;

	@Widget(title = "Plan only product with missing quantities")
	private Boolean planOnlyMissingQuantities = Boolean.FALSE;

	@Widget(title = "Unit")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Unit unit;

	@Widget(title = "BoM")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private BillOfMaterial billOfMaterial;

	@Widget(title = "Product")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Product product;

	@Widget(title = "Cost sheet")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "manufOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CostSheet> costSheetList;

	@Widget(title = "Cost price")
	@Digits(integer = 10, fraction = 10)
	private BigDecimal costPrice = BigDecimal.ZERO;

	@Widget(title = "Production process")
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ProdProcess prodProcess;

	@Widget(title = "Planned start date")
	private LocalDateTime plannedStartDateT;

	@Widget(title = "Planned end date")
	private LocalDateTime plannedEndDateT;

	@Widget(title = "Workshop")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private StockLocation workshopStockLocation;

	@Widget(title = "Real start date")
	private LocalDateTime realStartDateT;

	@Widget(title = "Real end date")
	private LocalDateTime realEndDateT;

	@Widget(title = "Time difference (Minutes)")
	private BigDecimal endTimeDifference = BigDecimal.ZERO;

	@Widget(title = "Stock moves in")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "inManufOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StockMove> inStockMoveList;

	@Widget(title = "Stock moves out")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "outManufOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StockMove> outStockMoveList;

	@Widget(title = "Waste stock move")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private StockMove wasteStockMove;

	@Widget(title = "Waste description")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String wasteProdDescription;

	@Widget(title = "Status", selection = "production.manuf.order.status.select")
	private Integer statusSelect = 0;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String note;

	@Widget(title = "Cancel reason")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private CancelReason cancelReason;

	@Widget(title = "Cancel Reason")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String cancelReasonStr;

	@Widget(title = "MO's comment from sale order")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String moCommentFromSaleOrder;

	@Widget(title = "Outsourcing")
	private Boolean outsourcing = Boolean.FALSE;

	@Widget(title = "Purchase order")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private PurchaseOrder purchaseOrder;

	@Widget(readonly = true)
	private Boolean invoiced = Boolean.FALSE;

	@Widget(title = "To Invoice")
	private Boolean isToInvoice = Boolean.FALSE;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "manufOrder", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TimesheetLine> timesheetLine;

	@Widget(title = "Machine type")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private MachineType machineType;

	@Widget(title = "Machine")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Machine machine;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public ManufOrder() {
	}

	public ManufOrder(BigDecimal qty, Company company, String manufOrderSeq, Integer prioritySelect, Boolean isConsProOnOperation, BillOfMaterial billOfMaterial, Product product, ProdProcess prodProcess, LocalDateTime plannedStartDateT, LocalDateTime plannedEndDateT, Integer statusSelect, Boolean outsourcing) {
		this.qty = qty;
		this.company = company;
		this.manufOrderSeq = manufOrderSeq;
		this.prioritySelect = prioritySelect;
		this.isConsProOnOperation = isConsProOnOperation;
		this.billOfMaterial = billOfMaterial;
		this.product = product;
		this.prodProcess = prodProcess;
		this.plannedStartDateT = plannedStartDateT;
		this.plannedEndDateT = plannedEndDateT;
		this.statusSelect = statusSelect;
		this.outsourcing = outsourcing;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public ManufOrder getManufOrderMergeResult() {
		return manufOrderMergeResult;
	}

	public void setManufOrderMergeResult(ManufOrder manufOrderMergeResult) {
		this.manufOrderMergeResult = manufOrderMergeResult;
	}

	public Set<ProductionOrder> getProductionOrderSet() {
		return productionOrderSet;
	}

	public void setProductionOrderSet(Set<ProductionOrder> productionOrderSet) {
		this.productionOrderSet = productionOrderSet;
	}

	/**
	 * Add the given {@link ProductionOrder} item to the {@code productionOrderSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addProductionOrderSetItem(ProductionOrder item) {
		if (getProductionOrderSet() == null) {
			setProductionOrderSet(new HashSet<>());
		}
		getProductionOrderSet().add(item);
	}

	/**
	 * Remove the given {@link ProductionOrder} item from the {@code productionOrderSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeProductionOrderSetItem(ProductionOrder item) {
		if (getProductionOrderSet() == null) {
			return;
		}
		getProductionOrderSet().remove(item);
	}

	/**
	 * Clear the {@code productionOrderSet} collection.
	 *
	 */
	public void clearProductionOrderSet() {
		if (getProductionOrderSet() != null) {
			getProductionOrderSet().clear();
		}
	}

	public List<OperationOrder> getOperationOrderList() {
		return operationOrderList;
	}

	public void setOperationOrderList(List<OperationOrder> operationOrderList) {
		this.operationOrderList = operationOrderList;
	}

	/**
	 * Add the given {@link OperationOrder} item to the {@code operationOrderList}.
	 *
	 * <p>
	 * It sets {@code item.manufOrder = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addOperationOrderListItem(OperationOrder item) {
		if (getOperationOrderList() == null) {
			setOperationOrderList(new ArrayList<>());
		}
		getOperationOrderList().add(item);
		item.setManufOrder(this);
	}

	/**
	 * Remove the given {@link OperationOrder} item from the {@code operationOrderList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeOperationOrderListItem(OperationOrder item) {
		if (getOperationOrderList() == null) {
			return;
		}
		getOperationOrderList().remove(item);
	}

	/**
	 * Clear the {@code operationOrderList} collection.
	 *
	 * <p>
	 * If you have to query {@link OperationOrder} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearOperationOrderList() {
		if (getOperationOrderList() != null) {
			getOperationOrderList().clear();
		}
	}

	public BigDecimal getQty() {
		return qty == null ? BigDecimal.ZERO : qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Partner getClientPartner() {
		return clientPartner;
	}

	public void setClientPartner(Partner clientPartner) {
		this.clientPartner = clientPartner;
	}

	public Set<SaleOrder> getSaleOrderSet() {
		return saleOrderSet;
	}

	public void setSaleOrderSet(Set<SaleOrder> saleOrderSet) {
		this.saleOrderSet = saleOrderSet;
	}

	/**
	 * Add the given {@link SaleOrder} item to the {@code saleOrderSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addSaleOrderSetItem(SaleOrder item) {
		if (getSaleOrderSet() == null) {
			setSaleOrderSet(new HashSet<>());
		}
		getSaleOrderSet().add(item);
	}

	/**
	 * Remove the given {@link SaleOrder} item from the {@code saleOrderSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeSaleOrderSetItem(SaleOrder item) {
		if (getSaleOrderSet() == null) {
			return;
		}
		getSaleOrderSet().remove(item);
	}

	/**
	 * Clear the {@code saleOrderSet} collection.
	 *
	 */
	public void clearSaleOrderSet() {
		if (getSaleOrderSet() != null) {
			getSaleOrderSet().clear();
		}
	}

	public String getManufOrderSeq() {
		return manufOrderSeq;
	}

	public void setManufOrderSeq(String manufOrderSeq) {
		this.manufOrderSeq = manufOrderSeq;
	}

	public Integer getPrioritySelect() {
		return prioritySelect == null ? 0 : prioritySelect;
	}

	public void setPrioritySelect(Integer prioritySelect) {
		this.prioritySelect = prioritySelect;
	}

	public Integer getTypeSelect() {
		return typeSelect == null ? 0 : typeSelect;
	}

	public void setTypeSelect(Integer typeSelect) {
		this.typeSelect = typeSelect;
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
	 * It sets {@code item.toConsumeManufOrder = this} to ensure the proper relationship.
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
		item.setToConsumeManufOrder(this);
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
	 * It sets {@code item.consumedManufOrder = this} to ensure the proper relationship.
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
		item.setConsumedManufOrder(this);
	}

	/**
	 * Remove the given {@link StockMoveLine} item from the {@code consumedStockMoveLineList}.
	 *
	 * <p>
	 * It sets {@code item.consumedManufOrder = null} to break the relationship.
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
		item.setConsumedManufOrder(null);
	}

	/**
	 * Clear the {@code consumedStockMoveLineList} collection.
	 *
	 * <p>
	 * It sets {@code item.consumedManufOrder = null} to break the relationship.
	 * </p>
	 */
	public void clearConsumedStockMoveLineList() {
		if (getConsumedStockMoveLineList() != null) {
			for (StockMoveLine item : getConsumedStockMoveLineList()) {
        item.setConsumedManufOrder(null);
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
	 * It sets {@code item.diffConsumeManufOrder = this} to ensure the proper relationship.
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
		item.setDiffConsumeManufOrder(this);
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

	public List<ProdProduct> getToProduceProdProductList() {
		return toProduceProdProductList;
	}

	public void setToProduceProdProductList(List<ProdProduct> toProduceProdProductList) {
		this.toProduceProdProductList = toProduceProdProductList;
	}

	/**
	 * Add the given {@link ProdProduct} item to the {@code toProduceProdProductList}.
	 *
	 * <p>
	 * It sets {@code item.toProduceManufOrder = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addToProduceProdProductListItem(ProdProduct item) {
		if (getToProduceProdProductList() == null) {
			setToProduceProdProductList(new ArrayList<>());
		}
		getToProduceProdProductList().add(item);
		item.setToProduceManufOrder(this);
	}

	/**
	 * Remove the given {@link ProdProduct} item from the {@code toProduceProdProductList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeToProduceProdProductListItem(ProdProduct item) {
		if (getToProduceProdProductList() == null) {
			return;
		}
		getToProduceProdProductList().remove(item);
	}

	/**
	 * Clear the {@code toProduceProdProductList} collection.
	 *
	 * <p>
	 * If you have to query {@link ProdProduct} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearToProduceProdProductList() {
		if (getToProduceProdProductList() != null) {
			getToProduceProdProductList().clear();
		}
	}

	public List<StockMoveLine> getProducedStockMoveLineList() {
		return producedStockMoveLineList;
	}

	public void setProducedStockMoveLineList(List<StockMoveLine> producedStockMoveLineList) {
		this.producedStockMoveLineList = producedStockMoveLineList;
	}

	/**
	 * Add the given {@link StockMoveLine} item to the {@code producedStockMoveLineList}.
	 *
	 * <p>
	 * It sets {@code item.producedManufOrder = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addProducedStockMoveLineListItem(StockMoveLine item) {
		if (getProducedStockMoveLineList() == null) {
			setProducedStockMoveLineList(new ArrayList<>());
		}
		getProducedStockMoveLineList().add(item);
		item.setProducedManufOrder(this);
	}

	/**
	 * Remove the given {@link StockMoveLine} item from the {@code producedStockMoveLineList}.
	 *
	 * <p>
	 * It sets {@code item.producedManufOrder = null} to break the relationship.
	 * </p>
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeProducedStockMoveLineListItem(StockMoveLine item) {
		if (getProducedStockMoveLineList() == null) {
			return;
		}
		getProducedStockMoveLineList().remove(item);
		item.setProducedManufOrder(null);
	}

	/**
	 * Clear the {@code producedStockMoveLineList} collection.
	 *
	 * <p>
	 * It sets {@code item.producedManufOrder = null} to break the relationship.
	 * </p>
	 */
	public void clearProducedStockMoveLineList() {
		if (getProducedStockMoveLineList() != null) {
			for (StockMoveLine item : getProducedStockMoveLineList()) {
        item.setProducedManufOrder(null);
      }
			getProducedStockMoveLineList().clear();
		}
	}

	public List<ProdProduct> getWasteProdProductList() {
		return wasteProdProductList;
	}

	public void setWasteProdProductList(List<ProdProduct> wasteProdProductList) {
		this.wasteProdProductList = wasteProdProductList;
	}

	/**
	 * Add the given {@link ProdProduct} item to the {@code wasteProdProductList}.
	 *
	 * <p>
	 * It sets {@code item.wasteManufOrder = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addWasteProdProductListItem(ProdProduct item) {
		if (getWasteProdProductList() == null) {
			setWasteProdProductList(new ArrayList<>());
		}
		getWasteProdProductList().add(item);
		item.setWasteManufOrder(this);
	}

	/**
	 * Remove the given {@link ProdProduct} item from the {@code wasteProdProductList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeWasteProdProductListItem(ProdProduct item) {
		if (getWasteProdProductList() == null) {
			return;
		}
		getWasteProdProductList().remove(item);
	}

	/**
	 * Clear the {@code wasteProdProductList} collection.
	 *
	 * <p>
	 * If you have to query {@link ProdProduct} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearWasteProdProductList() {
		if (getWasteProdProductList() != null) {
			getWasteProdProductList().clear();
		}
	}

	public Boolean getIsConsProOnOperation() {
		return isConsProOnOperation == null ? Boolean.FALSE : isConsProOnOperation;
	}

	public void setIsConsProOnOperation(Boolean isConsProOnOperation) {
		this.isConsProOnOperation = isConsProOnOperation;
	}

	public Boolean getPlanOnlyMissingQuantities() {
		return planOnlyMissingQuantities == null ? Boolean.FALSE : planOnlyMissingQuantities;
	}

	public void setPlanOnlyMissingQuantities(Boolean planOnlyMissingQuantities) {
		this.planOnlyMissingQuantities = planOnlyMissingQuantities;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public BillOfMaterial getBillOfMaterial() {
		return billOfMaterial;
	}

	public void setBillOfMaterial(BillOfMaterial billOfMaterial) {
		this.billOfMaterial = billOfMaterial;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<CostSheet> getCostSheetList() {
		return costSheetList;
	}

	public void setCostSheetList(List<CostSheet> costSheetList) {
		this.costSheetList = costSheetList;
	}

	/**
	 * Add the given {@link CostSheet} item to the {@code costSheetList}.
	 *
	 * <p>
	 * It sets {@code item.manufOrder = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addCostSheetListItem(CostSheet item) {
		if (getCostSheetList() == null) {
			setCostSheetList(new ArrayList<>());
		}
		getCostSheetList().add(item);
		item.setManufOrder(this);
	}

	/**
	 * Remove the given {@link CostSheet} item from the {@code costSheetList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeCostSheetListItem(CostSheet item) {
		if (getCostSheetList() == null) {
			return;
		}
		getCostSheetList().remove(item);
	}

	/**
	 * Clear the {@code costSheetList} collection.
	 *
	 * <p>
	 * If you have to query {@link CostSheet} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearCostSheetList() {
		if (getCostSheetList() != null) {
			getCostSheetList().clear();
		}
	}

	public BigDecimal getCostPrice() {
		return costPrice == null ? BigDecimal.ZERO : costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public ProdProcess getProdProcess() {
		return prodProcess;
	}

	public void setProdProcess(ProdProcess prodProcess) {
		this.prodProcess = prodProcess;
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

	public StockLocation getWorkshopStockLocation() {
		return workshopStockLocation;
	}

	public void setWorkshopStockLocation(StockLocation workshopStockLocation) {
		this.workshopStockLocation = workshopStockLocation;
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

	public BigDecimal getEndTimeDifference() {
		return endTimeDifference == null ? BigDecimal.ZERO : endTimeDifference;
	}

	public void setEndTimeDifference(BigDecimal endTimeDifference) {
		this.endTimeDifference = endTimeDifference;
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
	 * It sets {@code item.inManufOrder = this} to ensure the proper relationship.
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
		item.setInManufOrder(this);
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

	public List<StockMove> getOutStockMoveList() {
		return outStockMoveList;
	}

	public void setOutStockMoveList(List<StockMove> outStockMoveList) {
		this.outStockMoveList = outStockMoveList;
	}

	/**
	 * Add the given {@link StockMove} item to the {@code outStockMoveList}.
	 *
	 * <p>
	 * It sets {@code item.outManufOrder = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addOutStockMoveListItem(StockMove item) {
		if (getOutStockMoveList() == null) {
			setOutStockMoveList(new ArrayList<>());
		}
		getOutStockMoveList().add(item);
		item.setOutManufOrder(this);
	}

	/**
	 * Remove the given {@link StockMove} item from the {@code outStockMoveList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeOutStockMoveListItem(StockMove item) {
		if (getOutStockMoveList() == null) {
			return;
		}
		getOutStockMoveList().remove(item);
	}

	/**
	 * Clear the {@code outStockMoveList} collection.
	 *
	 * <p>
	 * If you have to query {@link StockMove} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearOutStockMoveList() {
		if (getOutStockMoveList() != null) {
			getOutStockMoveList().clear();
		}
	}

	public StockMove getWasteStockMove() {
		return wasteStockMove;
	}

	public void setWasteStockMove(StockMove wasteStockMove) {
		this.wasteStockMove = wasteStockMove;
	}

	public String getWasteProdDescription() {
		return wasteProdDescription;
	}

	public void setWasteProdDescription(String wasteProdDescription) {
		this.wasteProdDescription = wasteProdDescription;
	}

	public Integer getStatusSelect() {
		return statusSelect == null ? 0 : statusSelect;
	}

	public void setStatusSelect(Integer statusSelect) {
		this.statusSelect = statusSelect;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public CancelReason getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(CancelReason cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getCancelReasonStr() {
		return cancelReasonStr;
	}

	public void setCancelReasonStr(String cancelReasonStr) {
		this.cancelReasonStr = cancelReasonStr;
	}

	public String getMoCommentFromSaleOrder() {
		return moCommentFromSaleOrder;
	}

	public void setMoCommentFromSaleOrder(String moCommentFromSaleOrder) {
		this.moCommentFromSaleOrder = moCommentFromSaleOrder;
	}

	public Boolean getOutsourcing() {
		return outsourcing == null ? Boolean.FALSE : outsourcing;
	}

	public void setOutsourcing(Boolean outsourcing) {
		this.outsourcing = outsourcing;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public Boolean getInvoiced() {
		return invoiced == null ? Boolean.FALSE : invoiced;
	}

	public void setInvoiced(Boolean invoiced) {
		this.invoiced = invoiced;
	}

	public Boolean getIsToInvoice() {
		return isToInvoice == null ? Boolean.FALSE : isToInvoice;
	}

	public void setIsToInvoice(Boolean isToInvoice) {
		this.isToInvoice = isToInvoice;
	}

	public List<TimesheetLine> getTimesheetLine() {
		return timesheetLine;
	}

	public void setTimesheetLine(List<TimesheetLine> timesheetLine) {
		this.timesheetLine = timesheetLine;
	}

	/**
	 * Add the given {@link TimesheetLine} item to the {@code timesheetLine}.
	 *
	 * <p>
	 * It sets {@code item.manufOrder = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addTimesheetLine(TimesheetLine item) {
		if (getTimesheetLine() == null) {
			setTimesheetLine(new ArrayList<>());
		}
		getTimesheetLine().add(item);
		item.setManufOrder(this);
	}

	/**
	 * Remove the given {@link TimesheetLine} item from the {@code timesheetLine}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeTimesheetLine(TimesheetLine item) {
		if (getTimesheetLine() == null) {
			return;
		}
		getTimesheetLine().remove(item);
	}

	/**
	 * Clear the {@code timesheetLine} collection.
	 *
	 * <p>
	 * If you have to query {@link TimesheetLine} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearTimesheetLine() {
		if (getTimesheetLine() != null) {
			getTimesheetLine().clear();
		}
	}

	public MachineType getMachineType() {
		return machineType;
	}

	public void setMachineType(MachineType machineType) {
		this.machineType = machineType;
	}

	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
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
		if (!(obj instanceof ManufOrder)) return false;

		final ManufOrder other = (ManufOrder) obj;
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
			.add("qty", getQty())
			.add("manufOrderSeq", getManufOrderSeq())
			.add("prioritySelect", getPrioritySelect())
			.add("typeSelect", getTypeSelect())
			.add("isConsProOnOperation", getIsConsProOnOperation())
			.add("planOnlyMissingQuantities", getPlanOnlyMissingQuantities())
			.add("costPrice", getCostPrice())
			.add("plannedStartDateT", getPlannedStartDateT())
			.add("plannedEndDateT", getPlannedEndDateT())
			.add("realStartDateT", getRealStartDateT())
			.add("realEndDateT", getRealEndDateT())
			.omitNullValues()
			.toString();
	}
}
