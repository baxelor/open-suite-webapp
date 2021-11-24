package com.axelor.apps.production.db;

import java.math.BigDecimal;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.axelor.apps.base.db.Company;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.stock.db.StockLocation;
import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.Track;
import com.axelor.db.annotations.TrackField;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "PRODUCTION_PROD_PROCESS", indexes = { @Index(columnList = "name"), @Index(columnList = "code"), @Index(columnList = "company"), @Index(columnList = "fullName"), @Index(columnList = "stock_location"), @Index(columnList = "produced_product_stock_location"), @Index(columnList = "product"), @Index(columnList = "workshop_stock_location"), @Index(columnList = "original_prod_process"), @Index(columnList = "subcontractors"), @Index(columnList = "outsourcing_stock_location"), @Index(columnList = "machine_type") })
@Track(fields = { @TrackField(name = "name"), @TrackField(name = "code"), @TrackField(name = "outsourcing"), @TrackField(name = "company"), @TrackField(name = "fullName"), @TrackField(name = "statusSelect"), @TrackField(name = "isConsProOnOperation"), @TrackField(name = "stockLocation"), @TrackField(name = "producedProductStockLocation"), @TrackField(name = "product"), @TrackField(name = "workshopStockLocation"), @TrackField(name = "versionNumber"), @TrackField(name = "originalProdProcess"), @TrackField(name = "isEnabledForAllProducts"), @TrackField(name = "launchQty"), @TrackField(name = "stockMoveRealizeOrderSelect") })
public class ProdProcess extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCTION_PROD_PROCESS_SEQ")
	@SequenceGenerator(name = "PRODUCTION_PROD_PROCESS_SEQ", sequenceName = "PRODUCTION_PROD_PROCESS_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Phases")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "prodProcess", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("priority")
	private List<ProdProcessLine> prodProcessLineList;

	@Widget(title = "Label", massUpdate = true)
	@NotNull
	private String name;

	@Widget(title = "Code")
	private String code;

	@Widget(title = "Outsourcing")
	private Boolean outsourcing = Boolean.FALSE;

	@Widget(title = "Company")
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Company company;

	@Widget(title = "Label")
	@NameColumn
	private String fullName;

	@Widget(title = "Status", readonly = true, selection = "production.prod.process.status.select")
	private Integer statusSelect = 1;

	@Widget(title = "Manage consumed products on phases")
	private Boolean isConsProOnOperation = Boolean.FALSE;

	@Widget(title = "Production stock location", massUpdate = true)
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private StockLocation stockLocation;

	@Widget(title = "Stock location for produced product", massUpdate = true)
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private StockLocation producedProductStockLocation;

	@Widget(title = "Product")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Product product;

	@Widget(title = "Workshop", massUpdate = true)
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private StockLocation workshopStockLocation;

	@Widget(title = "Version number")
	private Integer versionNumber = 1;

	@Widget(title = "Original production process")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ProdProcess originalProdProcess;

	@Widget(title = "Enable the process for other products")
	private Boolean isEnabledForAllProducts = Boolean.FALSE;

	@Widget(title = "Launch quantity")
	@Digits(integer = 10, fraction = 10)
	private BigDecimal launchQty = BigDecimal.ZERO;

	@Widget(selection = "production.manuf.order.stock.move.realize.order.select", massUpdate = true)
	private Integer stockMoveRealizeOrderSelect = 1;

	@Widget(title = "Subcontractor")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner subcontractors;

	@Widget(title = "Outsourcing stock location")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private StockLocation outsourcingStockLocation;

	@Widget(title = "Generate a purchase order on MO planning")
	private Boolean generatePurchaseOrderOnMoPlanning = Boolean.FALSE;

	@Widget(title = "Type", selection = "manuf.order.type.select")
	private Integer typeSelect = 1;

	@Widget(title = "Machine type")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private MachineType machineType;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public ProdProcess() {
	}

	public ProdProcess(String name, String code) {
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

	public List<ProdProcessLine> getProdProcessLineList() {
		return prodProcessLineList;
	}

	public void setProdProcessLineList(List<ProdProcessLine> prodProcessLineList) {
		this.prodProcessLineList = prodProcessLineList;
	}

	/**
	 * Add the given {@link ProdProcessLine} item to the {@code prodProcessLineList}.
	 *
	 * <p>
	 * It sets {@code item.prodProcess = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addProdProcessLineListItem(ProdProcessLine item) {
		if (getProdProcessLineList() == null) {
			setProdProcessLineList(new ArrayList<>());
		}
		getProdProcessLineList().add(item);
		item.setProdProcess(this);
	}

	/**
	 * Remove the given {@link ProdProcessLine} item from the {@code prodProcessLineList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeProdProcessLineListItem(ProdProcessLine item) {
		if (getProdProcessLineList() == null) {
			return;
		}
		getProdProcessLineList().remove(item);
	}

	/**
	 * Clear the {@code prodProcessLineList} collection.
	 *
	 * <p>
	 * If you have to query {@link ProdProcessLine} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearProdProcessLineList() {
		if (getProdProcessLineList() != null) {
			getProdProcessLineList().clear();
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

	public Boolean getOutsourcing() {
		return outsourcing == null ? Boolean.FALSE : outsourcing;
	}

	public void setOutsourcing(Boolean outsourcing) {
		this.outsourcing = outsourcing;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getStatusSelect() {
		return statusSelect == null ? 0 : statusSelect;
	}

	public void setStatusSelect(Integer statusSelect) {
		this.statusSelect = statusSelect;
	}

	public Boolean getIsConsProOnOperation() {
		return isConsProOnOperation == null ? Boolean.FALSE : isConsProOnOperation;
	}

	public void setIsConsProOnOperation(Boolean isConsProOnOperation) {
		this.isConsProOnOperation = isConsProOnOperation;
	}

	public StockLocation getStockLocation() {
		return stockLocation;
	}

	public void setStockLocation(StockLocation stockLocation) {
		this.stockLocation = stockLocation;
	}

	public StockLocation getProducedProductStockLocation() {
		return producedProductStockLocation;
	}

	public void setProducedProductStockLocation(StockLocation producedProductStockLocation) {
		this.producedProductStockLocation = producedProductStockLocation;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public StockLocation getWorkshopStockLocation() {
		return workshopStockLocation;
	}

	public void setWorkshopStockLocation(StockLocation workshopStockLocation) {
		this.workshopStockLocation = workshopStockLocation;
	}

	public Integer getVersionNumber() {
		return versionNumber == null ? 0 : versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public ProdProcess getOriginalProdProcess() {
		return originalProdProcess;
	}

	public void setOriginalProdProcess(ProdProcess originalProdProcess) {
		this.originalProdProcess = originalProdProcess;
	}

	public Boolean getIsEnabledForAllProducts() {
		return isEnabledForAllProducts == null ? Boolean.FALSE : isEnabledForAllProducts;
	}

	public void setIsEnabledForAllProducts(Boolean isEnabledForAllProducts) {
		this.isEnabledForAllProducts = isEnabledForAllProducts;
	}

	public BigDecimal getLaunchQty() {
		return launchQty == null ? BigDecimal.ZERO : launchQty;
	}

	public void setLaunchQty(BigDecimal launchQty) {
		this.launchQty = launchQty;
	}

	public Integer getStockMoveRealizeOrderSelect() {
		return stockMoveRealizeOrderSelect == null ? 0 : stockMoveRealizeOrderSelect;
	}

	public void setStockMoveRealizeOrderSelect(Integer stockMoveRealizeOrderSelect) {
		this.stockMoveRealizeOrderSelect = stockMoveRealizeOrderSelect;
	}

	public Partner getSubcontractors() {
		return subcontractors;
	}

	public void setSubcontractors(Partner subcontractors) {
		this.subcontractors = subcontractors;
	}

	public StockLocation getOutsourcingStockLocation() {
		return outsourcingStockLocation;
	}

	public void setOutsourcingStockLocation(StockLocation outsourcingStockLocation) {
		this.outsourcingStockLocation = outsourcingStockLocation;
	}

	public Boolean getGeneratePurchaseOrderOnMoPlanning() {
		return generatePurchaseOrderOnMoPlanning == null ? Boolean.FALSE : generatePurchaseOrderOnMoPlanning;
	}

	public void setGeneratePurchaseOrderOnMoPlanning(Boolean generatePurchaseOrderOnMoPlanning) {
		this.generatePurchaseOrderOnMoPlanning = generatePurchaseOrderOnMoPlanning;
	}

	public Integer getTypeSelect() {
		return typeSelect == null ? 0 : typeSelect;
	}

	public void setTypeSelect(Integer typeSelect) {
		this.typeSelect = typeSelect;
	}

	public MachineType getMachineType() {
		return machineType;
	}

	public void setMachineType(MachineType machineType) {
		this.machineType = machineType;
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
		if (!(obj instanceof ProdProcess)) return false;

		final ProdProcess other = (ProdProcess) obj;
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
			.add("code", getCode())
			.add("outsourcing", getOutsourcing())
			.add("fullName", getFullName())
			.add("statusSelect", getStatusSelect())
			.add("isConsProOnOperation", getIsConsProOnOperation())
			.add("versionNumber", getVersionNumber())
			.add("isEnabledForAllProducts", getIsEnabledForAllProducts())
			.add("launchQty", getLaunchQty())
			.add("stockMoveRealizeOrderSelect", getStockMoveRealizeOrderSelect())
			.add("generatePurchaseOrderOnMoPlanning", getGeneratePurchaseOrderOnMoPlanning())
			.omitNullValues()
			.toString();
	}
}
