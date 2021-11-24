package com.axelor.apps.production.db;

import java.math.BigDecimal;
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
import javax.validation.constraints.Digits;

import org.hibernate.annotations.Type;

import com.axelor.apps.base.db.Company;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.db.Unit;
import com.axelor.apps.stock.db.StockLocation;
import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.Track;
import com.axelor.db.annotations.TrackField;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "PRODUCTION_BILL_OF_MATERIAL", indexes = { @Index(columnList = "name"), @Index(columnList = "product"), @Index(columnList = "unit"), @Index(columnList = "prod_process"), @Index(columnList = "company"), @Index(columnList = "workshop_stock_location"), @Index(columnList = "fullName"), @Index(columnList = "original_bill_of_material"), @Index(columnList = "machine_type"), @Index(columnList = "product") })
@Track(fields = { @TrackField(name = "name"), @TrackField(name = "product"), @TrackField(name = "qty"), @TrackField(name = "priority"), @TrackField(name = "defineSubBillOfMaterial"), @TrackField(name = "unit"), @TrackField(name = "prodProcess"), @TrackField(name = "costPrice"), @TrackField(name = "company"), @TrackField(name = "hasNoManageStock"), @TrackField(name = "personalized"), @TrackField(name = "wasteRate"), @TrackField(name = "statusSelect"), @TrackField(name = "workshopStockLocation"), @TrackField(name = "fullName"), @TrackField(name = "versionNumber"), @TrackField(name = "originalBillOfMaterial"), @TrackField(name = "note") })
public class BillOfMaterial extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCTION_BILL_OF_MATERIAL_SEQ")
	@SequenceGenerator(name = "PRODUCTION_BILL_OF_MATERIAL_SEQ", sequenceName = "PRODUCTION_BILL_OF_MATERIAL_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Label")
	private String name;

	@Widget(title = "Qty")
	@Digits(integer = 10, fraction = 10)
	private BigDecimal qty = BigDecimal.ZERO;

	@Widget(title = "Priority")
	private Integer priority = 0;

	@Widget(title = "Define sub bill of materials")
	private Boolean defineSubBillOfMaterial = Boolean.FALSE;

	@Widget(title = "Unit")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Unit unit;

	@Widget(title = "Production process")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ProdProcess prodProcess;

	@Widget(title = "BOMs")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@OrderBy("priority")
	private Set<BillOfMaterial> billOfMaterialSet;

	@Widget(title = "Residual products")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "billOfMaterial", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProdResidualProduct> prodResidualProductList;

	@Widget(title = "Cost price")
	@Digits(integer = 10, fraction = 10)
	private BigDecimal costPrice = BigDecimal.ZERO;

	@Widget(title = "Company")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Company company;

	@Widget(title = "Not manage stock")
	private Boolean hasNoManageStock = Boolean.FALSE;

	@Widget(title = "Personalized")
	private Boolean personalized = Boolean.FALSE;

	@Widget(title = "Waste rate (%)")
	private BigDecimal wasteRate = BigDecimal.ZERO;

	@Widget(title = "Status", readonly = true, selection = "production.bill.of.material.status.select")
	private Integer statusSelect = 1;

	@Widget(title = "Workshop")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private StockLocation workshopStockLocation;

	@Widget(title = "Cost sheets")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "billOfMaterial", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CostSheet> costSheetList;

	@Widget(title = "Label")
	@NameColumn
	private String fullName;

	@Widget(title = "Version number")
	private Integer versionNumber = 1;

	@Widget(title = "Original bill of materials")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private BillOfMaterial originalBillOfMaterial;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentBom", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TempBomTree> bomChildTreeList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bom", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TempBomTree> bomTreeList;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String note;

	@Widget(title = "Type", selection = "manuf.order.type.select")
	private Integer typeSelect = 1;

	@Widget(title = "Machine type")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private MachineType machineType;

	@Widget(title = "Product")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Product product;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public BillOfMaterial() {
	}

	public BillOfMaterial(String name) {
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

	public BigDecimal getQty() {
		return qty == null ? BigDecimal.ZERO : qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public Integer getPriority() {
		return priority == null ? 0 : priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Boolean getDefineSubBillOfMaterial() {
		return defineSubBillOfMaterial == null ? Boolean.FALSE : defineSubBillOfMaterial;
	}

	public void setDefineSubBillOfMaterial(Boolean defineSubBillOfMaterial) {
		this.defineSubBillOfMaterial = defineSubBillOfMaterial;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public ProdProcess getProdProcess() {
		return prodProcess;
	}

	public void setProdProcess(ProdProcess prodProcess) {
		this.prodProcess = prodProcess;
	}

	public Set<BillOfMaterial> getBillOfMaterialSet() {
		return billOfMaterialSet;
	}

	public void setBillOfMaterialSet(Set<BillOfMaterial> billOfMaterialSet) {
		this.billOfMaterialSet = billOfMaterialSet;
	}

	/**
	 * Add the given {@link BillOfMaterial} item to the {@code billOfMaterialSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addBillOfMaterialSetItem(BillOfMaterial item) {
		if (getBillOfMaterialSet() == null) {
			setBillOfMaterialSet(new HashSet<>());
		}
		getBillOfMaterialSet().add(item);
	}

	/**
	 * Remove the given {@link BillOfMaterial} item from the {@code billOfMaterialSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeBillOfMaterialSetItem(BillOfMaterial item) {
		if (getBillOfMaterialSet() == null) {
			return;
		}
		getBillOfMaterialSet().remove(item);
	}

	/**
	 * Clear the {@code billOfMaterialSet} collection.
	 *
	 */
	public void clearBillOfMaterialSet() {
		if (getBillOfMaterialSet() != null) {
			getBillOfMaterialSet().clear();
		}
	}

	public List<ProdResidualProduct> getProdResidualProductList() {
		return prodResidualProductList;
	}

	public void setProdResidualProductList(List<ProdResidualProduct> prodResidualProductList) {
		this.prodResidualProductList = prodResidualProductList;
	}

	/**
	 * Add the given {@link ProdResidualProduct} item to the {@code prodResidualProductList}.
	 *
	 * <p>
	 * It sets {@code item.billOfMaterial = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addProdResidualProductListItem(ProdResidualProduct item) {
		if (getProdResidualProductList() == null) {
			setProdResidualProductList(new ArrayList<>());
		}
		getProdResidualProductList().add(item);
		item.setBillOfMaterial(this);
	}

	/**
	 * Remove the given {@link ProdResidualProduct} item from the {@code prodResidualProductList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeProdResidualProductListItem(ProdResidualProduct item) {
		if (getProdResidualProductList() == null) {
			return;
		}
		getProdResidualProductList().remove(item);
	}

	/**
	 * Clear the {@code prodResidualProductList} collection.
	 *
	 * <p>
	 * If you have to query {@link ProdResidualProduct} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearProdResidualProductList() {
		if (getProdResidualProductList() != null) {
			getProdResidualProductList().clear();
		}
	}

	public BigDecimal getCostPrice() {
		return costPrice == null ? BigDecimal.ZERO : costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Boolean getHasNoManageStock() {
		return hasNoManageStock == null ? Boolean.FALSE : hasNoManageStock;
	}

	public void setHasNoManageStock(Boolean hasNoManageStock) {
		this.hasNoManageStock = hasNoManageStock;
	}

	public Boolean getPersonalized() {
		return personalized == null ? Boolean.FALSE : personalized;
	}

	public void setPersonalized(Boolean personalized) {
		this.personalized = personalized;
	}

	public BigDecimal getWasteRate() {
		return wasteRate == null ? BigDecimal.ZERO : wasteRate;
	}

	public void setWasteRate(BigDecimal wasteRate) {
		this.wasteRate = wasteRate;
	}

	public Integer getStatusSelect() {
		return statusSelect == null ? 0 : statusSelect;
	}

	public void setStatusSelect(Integer statusSelect) {
		this.statusSelect = statusSelect;
	}

	public StockLocation getWorkshopStockLocation() {
		return workshopStockLocation;
	}

	public void setWorkshopStockLocation(StockLocation workshopStockLocation) {
		this.workshopStockLocation = workshopStockLocation;
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
	 * It sets {@code item.billOfMaterial = this} to ensure the proper relationship.
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
		item.setBillOfMaterial(this);
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getVersionNumber() {
		return versionNumber == null ? 0 : versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public BillOfMaterial getOriginalBillOfMaterial() {
		return originalBillOfMaterial;
	}

	public void setOriginalBillOfMaterial(BillOfMaterial originalBillOfMaterial) {
		this.originalBillOfMaterial = originalBillOfMaterial;
	}

	public List<TempBomTree> getBomChildTreeList() {
		return bomChildTreeList;
	}

	public void setBomChildTreeList(List<TempBomTree> bomChildTreeList) {
		this.bomChildTreeList = bomChildTreeList;
	}

	/**
	 * Add the given {@link TempBomTree} item to the {@code bomChildTreeList}.
	 *
	 * <p>
	 * It sets {@code item.parentBom = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addBomChildTreeListItem(TempBomTree item) {
		if (getBomChildTreeList() == null) {
			setBomChildTreeList(new ArrayList<>());
		}
		getBomChildTreeList().add(item);
		item.setParentBom(this);
	}

	/**
	 * Remove the given {@link TempBomTree} item from the {@code bomChildTreeList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeBomChildTreeListItem(TempBomTree item) {
		if (getBomChildTreeList() == null) {
			return;
		}
		getBomChildTreeList().remove(item);
	}

	/**
	 * Clear the {@code bomChildTreeList} collection.
	 *
	 * <p>
	 * If you have to query {@link TempBomTree} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearBomChildTreeList() {
		if (getBomChildTreeList() != null) {
			getBomChildTreeList().clear();
		}
	}

	public List<TempBomTree> getBomTreeList() {
		return bomTreeList;
	}

	public void setBomTreeList(List<TempBomTree> bomTreeList) {
		this.bomTreeList = bomTreeList;
	}

	/**
	 * Add the given {@link TempBomTree} item to the {@code bomTreeList}.
	 *
	 * <p>
	 * It sets {@code item.bom = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addBomTreeListItem(TempBomTree item) {
		if (getBomTreeList() == null) {
			setBomTreeList(new ArrayList<>());
		}
		getBomTreeList().add(item);
		item.setBom(this);
	}

	/**
	 * Remove the given {@link TempBomTree} item from the {@code bomTreeList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeBomTreeListItem(TempBomTree item) {
		if (getBomTreeList() == null) {
			return;
		}
		getBomTreeList().remove(item);
	}

	/**
	 * Clear the {@code bomTreeList} collection.
	 *
	 * <p>
	 * If you have to query {@link TempBomTree} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearBomTreeList() {
		if (getBomTreeList() != null) {
			getBomTreeList().clear();
		}
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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
		if (!(obj instanceof BillOfMaterial)) return false;

		final BillOfMaterial other = (BillOfMaterial) obj;
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
			.add("qty", getQty())
			.add("priority", getPriority())
			.add("defineSubBillOfMaterial", getDefineSubBillOfMaterial())
			.add("costPrice", getCostPrice())
			.add("hasNoManageStock", getHasNoManageStock())
			.add("personalized", getPersonalized())
			.add("wasteRate", getWasteRate())
			.add("statusSelect", getStatusSelect())
			.add("fullName", getFullName())
			.add("versionNumber", getVersionNumber())
			.omitNullValues()
			.toString();
	}
}
