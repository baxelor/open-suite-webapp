package com.axelor.apps.supplychain.db;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;

import org.hibernate.annotations.Type;

import com.axelor.apps.base.db.Company;
import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Track;
import com.axelor.db.annotations.TrackEvent;
import com.axelor.db.annotations.TrackField;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "SUPPLYCHAIN_SUPPLY_CHAIN_CONFIG", indexes = { @Index(columnList = "company") })
@Track(fields = { @TrackField(name = "company", on = TrackEvent.UPDATE), @TrackField(name = "hasOutSmForStorableProduct", on = TrackEvent.UPDATE), @TrackField(name = "hasOutSmForNonStorableProduct", on = TrackEvent.UPDATE), @TrackField(name = "hasInSmForStorableProduct", on = TrackEvent.UPDATE), @TrackField(name = "hasInSmForNonStorableProduct", on = TrackEvent.UPDATE), @TrackField(name = "autoAllocateOnReceipt", on = TrackEvent.UPDATE), @TrackField(name = "autoAllocateOnAllocation", on = TrackEvent.UPDATE), @TrackField(name = "autoRequestReservedQty", on = TrackEvent.UPDATE), @TrackField(name = "autoAllocateOnAvailabilityRequest", on = TrackEvent.UPDATE), @TrackField(name = "saleOrderReservationDateSelect", on = TrackEvent.UPDATE), @TrackField(name = "defaultEstimatedDate", on = TrackEvent.UPDATE), @TrackField(name = "numberOfDays", on = TrackEvent.UPDATE), @TrackField(name = "defaultEstimatedDateForPurchaseOrder", on = TrackEvent.UPDATE), @TrackField(name = "numberOfDaysForPurchaseOrder", on = TrackEvent.UPDATE), @TrackField(name = "autoRequestReservedQtyOnManufOrder", on = TrackEvent.UPDATE) })
public class SupplyChainConfig extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUPPLYCHAIN_SUPPLY_CHAIN_CONFIG_SEQ")
	@SequenceGenerator(name = "SUPPLYCHAIN_SUPPLY_CHAIN_CONFIG_SEQ", sequenceName = "SUPPLYCHAIN_SUPPLY_CHAIN_CONFIG_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Company")
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Company company;

	@Widget(title = "Generate out. moves for products")
	private Boolean hasOutSmForStorableProduct = Boolean.FALSE;

	@Widget(title = "Generate out. moves for services")
	private Boolean hasOutSmForNonStorableProduct = Boolean.FALSE;

	@Widget(title = "Generate inc. moves for products")
	private Boolean hasInSmForStorableProduct = Boolean.FALSE;

	@Widget(title = "Generate inc. moves for services")
	private Boolean hasInSmForNonStorableProduct = Boolean.FALSE;

	@Widget(title = "Auto allocate stock on receipt")
	private Boolean autoAllocateOnReceipt = Boolean.FALSE;

	@Widget(title = "Auto allocate stock on other stock moves", help = "If true, after allocating quantity for a given stock move we allocate the remaining quantity in others stock moves.")
	private Boolean autoAllocateOnAllocation = Boolean.FALSE;

	@Widget(title = "Auto request reserved qty", help = "If true, the requested quantity for reservation will be equal to the quantity of the given sale order line.")
	private Boolean autoRequestReservedQty = Boolean.FALSE;

	@Widget(title = "Auto allocate during an availability request")
	private Boolean autoAllocateOnAvailabilityRequest = Boolean.FALSE;

	@Widget(title = "Sale order date used for stock reservation", selection = "supplychain.sale.order.reservation.date.field")
	private Integer saleOrderReservationDateSelect = 0;

	@Widget(title = "Default estimated date in stock move from sale order", selection = "supplychain.estimated.date.select")
	private Integer defaultEstimatedDate = 1;

	@Widget(title = "Number of days")
	@DecimalMin("0")
	private BigDecimal numberOfDays = BigDecimal.ZERO;

	@Widget(title = "Default estimated date in stock move from purchase order", selection = "supplychain.estimated.date.select")
	private Integer defaultEstimatedDateForPurchaseOrder = 1;

	@Widget(title = "Number of days")
	@DecimalMin("0")
	private BigDecimal numberOfDaysForPurchaseOrder = BigDecimal.ZERO;

	@Widget(title = "Activate partial invoicing for incoming moves")
	private Boolean activateIncStockMovePartialInvoicing = Boolean.TRUE;

	@Widget(title = "Activate partial invoicing for outgoing moves")
	private Boolean activateOutStockMovePartialInvoicing = Boolean.FALSE;

	@Widget(title = "Auto request qty for manuf orders", help = "If true, the components will be automatically requested for reservation on planning manufacturing orders.")
	private Boolean autoRequestReservedQtyOnManufOrder = Boolean.FALSE;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public SupplyChainConfig() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Boolean getHasOutSmForStorableProduct() {
		return hasOutSmForStorableProduct == null ? Boolean.FALSE : hasOutSmForStorableProduct;
	}

	public void setHasOutSmForStorableProduct(Boolean hasOutSmForStorableProduct) {
		this.hasOutSmForStorableProduct = hasOutSmForStorableProduct;
	}

	public Boolean getHasOutSmForNonStorableProduct() {
		return hasOutSmForNonStorableProduct == null ? Boolean.FALSE : hasOutSmForNonStorableProduct;
	}

	public void setHasOutSmForNonStorableProduct(Boolean hasOutSmForNonStorableProduct) {
		this.hasOutSmForNonStorableProduct = hasOutSmForNonStorableProduct;
	}

	public Boolean getHasInSmForStorableProduct() {
		return hasInSmForStorableProduct == null ? Boolean.FALSE : hasInSmForStorableProduct;
	}

	public void setHasInSmForStorableProduct(Boolean hasInSmForStorableProduct) {
		this.hasInSmForStorableProduct = hasInSmForStorableProduct;
	}

	public Boolean getHasInSmForNonStorableProduct() {
		return hasInSmForNonStorableProduct == null ? Boolean.FALSE : hasInSmForNonStorableProduct;
	}

	public void setHasInSmForNonStorableProduct(Boolean hasInSmForNonStorableProduct) {
		this.hasInSmForNonStorableProduct = hasInSmForNonStorableProduct;
	}

	public Boolean getAutoAllocateOnReceipt() {
		return autoAllocateOnReceipt == null ? Boolean.FALSE : autoAllocateOnReceipt;
	}

	public void setAutoAllocateOnReceipt(Boolean autoAllocateOnReceipt) {
		this.autoAllocateOnReceipt = autoAllocateOnReceipt;
	}

	/**
	 * If true, after allocating quantity for a given stock move we allocate the remaining quantity in others stock moves.
	 *
	 * @return the property value
	 */
	public Boolean getAutoAllocateOnAllocation() {
		return autoAllocateOnAllocation == null ? Boolean.FALSE : autoAllocateOnAllocation;
	}

	public void setAutoAllocateOnAllocation(Boolean autoAllocateOnAllocation) {
		this.autoAllocateOnAllocation = autoAllocateOnAllocation;
	}

	/**
	 * If true, the requested quantity for reservation will be equal to the quantity of the given sale order line.
	 *
	 * @return the property value
	 */
	public Boolean getAutoRequestReservedQty() {
		return autoRequestReservedQty == null ? Boolean.FALSE : autoRequestReservedQty;
	}

	public void setAutoRequestReservedQty(Boolean autoRequestReservedQty) {
		this.autoRequestReservedQty = autoRequestReservedQty;
	}

	public Boolean getAutoAllocateOnAvailabilityRequest() {
		return autoAllocateOnAvailabilityRequest == null ? Boolean.FALSE : autoAllocateOnAvailabilityRequest;
	}

	public void setAutoAllocateOnAvailabilityRequest(Boolean autoAllocateOnAvailabilityRequest) {
		this.autoAllocateOnAvailabilityRequest = autoAllocateOnAvailabilityRequest;
	}

	public Integer getSaleOrderReservationDateSelect() {
		return saleOrderReservationDateSelect == null ? 0 : saleOrderReservationDateSelect;
	}

	public void setSaleOrderReservationDateSelect(Integer saleOrderReservationDateSelect) {
		this.saleOrderReservationDateSelect = saleOrderReservationDateSelect;
	}

	public Integer getDefaultEstimatedDate() {
		return defaultEstimatedDate == null ? 0 : defaultEstimatedDate;
	}

	public void setDefaultEstimatedDate(Integer defaultEstimatedDate) {
		this.defaultEstimatedDate = defaultEstimatedDate;
	}

	public BigDecimal getNumberOfDays() {
		return numberOfDays == null ? BigDecimal.ZERO : numberOfDays;
	}

	public void setNumberOfDays(BigDecimal numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public Integer getDefaultEstimatedDateForPurchaseOrder() {
		return defaultEstimatedDateForPurchaseOrder == null ? 0 : defaultEstimatedDateForPurchaseOrder;
	}

	public void setDefaultEstimatedDateForPurchaseOrder(Integer defaultEstimatedDateForPurchaseOrder) {
		this.defaultEstimatedDateForPurchaseOrder = defaultEstimatedDateForPurchaseOrder;
	}

	public BigDecimal getNumberOfDaysForPurchaseOrder() {
		return numberOfDaysForPurchaseOrder == null ? BigDecimal.ZERO : numberOfDaysForPurchaseOrder;
	}

	public void setNumberOfDaysForPurchaseOrder(BigDecimal numberOfDaysForPurchaseOrder) {
		this.numberOfDaysForPurchaseOrder = numberOfDaysForPurchaseOrder;
	}

	public Boolean getActivateIncStockMovePartialInvoicing() {
		return activateIncStockMovePartialInvoicing == null ? Boolean.FALSE : activateIncStockMovePartialInvoicing;
	}

	public void setActivateIncStockMovePartialInvoicing(Boolean activateIncStockMovePartialInvoicing) {
		this.activateIncStockMovePartialInvoicing = activateIncStockMovePartialInvoicing;
	}

	public Boolean getActivateOutStockMovePartialInvoicing() {
		return activateOutStockMovePartialInvoicing == null ? Boolean.FALSE : activateOutStockMovePartialInvoicing;
	}

	public void setActivateOutStockMovePartialInvoicing(Boolean activateOutStockMovePartialInvoicing) {
		this.activateOutStockMovePartialInvoicing = activateOutStockMovePartialInvoicing;
	}

	/**
	 * If true, the components will be automatically requested for reservation on planning manufacturing orders.
	 *
	 * @return the property value
	 */
	public Boolean getAutoRequestReservedQtyOnManufOrder() {
		return autoRequestReservedQtyOnManufOrder == null ? Boolean.FALSE : autoRequestReservedQtyOnManufOrder;
	}

	public void setAutoRequestReservedQtyOnManufOrder(Boolean autoRequestReservedQtyOnManufOrder) {
		this.autoRequestReservedQtyOnManufOrder = autoRequestReservedQtyOnManufOrder;
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
		if (!(obj instanceof SupplyChainConfig)) return false;

		final SupplyChainConfig other = (SupplyChainConfig) obj;
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
			.add("hasOutSmForStorableProduct", getHasOutSmForStorableProduct())
			.add("hasOutSmForNonStorableProduct", getHasOutSmForNonStorableProduct())
			.add("hasInSmForStorableProduct", getHasInSmForStorableProduct())
			.add("hasInSmForNonStorableProduct", getHasInSmForNonStorableProduct())
			.add("autoAllocateOnReceipt", getAutoAllocateOnReceipt())
			.add("autoAllocateOnAllocation", getAutoAllocateOnAllocation())
			.add("autoRequestReservedQty", getAutoRequestReservedQty())
			.add("autoAllocateOnAvailabilityRequest", getAutoAllocateOnAvailabilityRequest())
			.add("saleOrderReservationDateSelect", getSaleOrderReservationDateSelect())
			.add("defaultEstimatedDate", getDefaultEstimatedDate())
			.add("numberOfDays", getNumberOfDays())
			.omitNullValues()
			.toString();
	}
}
