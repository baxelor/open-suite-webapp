package com.axelor.apps.base.db;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Track;
import com.axelor.db.annotations.TrackEvent;
import com.axelor.db.annotations.TrackField;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "BASE_APP_PURCHASE", indexes = { @Index(columnList = "app"), @Index(columnList = "purchase_unit") })
@Track(fields = { @TrackField(name = "managePurchaseOrderVersion", on = TrackEvent.UPDATE), @TrackField(name = "managePurchasesUnits", on = TrackEvent.UPDATE), @TrackField(name = "manageMultiplePurchaseQuantity", on = TrackEvent.UPDATE), @TrackField(name = "isEnabledProductDescriptionCopy", on = TrackEvent.UPDATE), @TrackField(name = "manageSupplierCatalog", on = TrackEvent.UPDATE), @TrackField(name = "isDisplayPurchaseOrderLineNumber", on = TrackEvent.UPDATE), @TrackField(name = "supplierRequestMgt", on = TrackEvent.UPDATE) })
public class AppPurchase extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BASE_APP_PURCHASE_SEQ")
	@SequenceGenerator(name = "BASE_APP_PURCHASE_SEQ", sequenceName = "BASE_APP_PURCHASE_SEQ", allocationSize = 1)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private App app;

	@Widget(title = "Manage purchase order versions")
	private Boolean managePurchaseOrderVersion = Boolean.FALSE;

	@Widget(title = "Manage purchases unit on products")
	private Boolean managePurchasesUnits = Boolean.FALSE;

	@Widget(title = "Manage multiple purchase quantity")
	private Boolean manageMultiplePurchaseQuantity = Boolean.FALSE;

	@Widget(title = "Enable product description copy")
	private Boolean isEnabledProductDescriptionCopy = Boolean.FALSE;

	@Widget(title = "Manage supplier catalog")
	private Boolean manageSupplierCatalog = Boolean.FALSE;

	@Widget(title = "Display purchase order line number")
	private Boolean isDisplayPurchaseOrderLineNumber = Boolean.FALSE;

	@Widget(title = "Filter products by trading name")
	private Boolean enablePurchasesProductByTradName = Boolean.FALSE;

	@Widget(title = "Default unit")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Unit purchaseUnit;

	@Widget(title = "Manage supplier consultations")
	private Boolean supplierRequestMgt = Boolean.FALSE;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public AppPurchase() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public Boolean getManagePurchaseOrderVersion() {
		return managePurchaseOrderVersion == null ? Boolean.FALSE : managePurchaseOrderVersion;
	}

	public void setManagePurchaseOrderVersion(Boolean managePurchaseOrderVersion) {
		this.managePurchaseOrderVersion = managePurchaseOrderVersion;
	}

	public Boolean getManagePurchasesUnits() {
		return managePurchasesUnits == null ? Boolean.FALSE : managePurchasesUnits;
	}

	public void setManagePurchasesUnits(Boolean managePurchasesUnits) {
		this.managePurchasesUnits = managePurchasesUnits;
	}

	public Boolean getManageMultiplePurchaseQuantity() {
		return manageMultiplePurchaseQuantity == null ? Boolean.FALSE : manageMultiplePurchaseQuantity;
	}

	public void setManageMultiplePurchaseQuantity(Boolean manageMultiplePurchaseQuantity) {
		this.manageMultiplePurchaseQuantity = manageMultiplePurchaseQuantity;
	}

	public Boolean getIsEnabledProductDescriptionCopy() {
		return isEnabledProductDescriptionCopy == null ? Boolean.FALSE : isEnabledProductDescriptionCopy;
	}

	public void setIsEnabledProductDescriptionCopy(Boolean isEnabledProductDescriptionCopy) {
		this.isEnabledProductDescriptionCopy = isEnabledProductDescriptionCopy;
	}

	public Boolean getManageSupplierCatalog() {
		return manageSupplierCatalog == null ? Boolean.FALSE : manageSupplierCatalog;
	}

	public void setManageSupplierCatalog(Boolean manageSupplierCatalog) {
		this.manageSupplierCatalog = manageSupplierCatalog;
	}

	public Boolean getIsDisplayPurchaseOrderLineNumber() {
		return isDisplayPurchaseOrderLineNumber == null ? Boolean.FALSE : isDisplayPurchaseOrderLineNumber;
	}

	public void setIsDisplayPurchaseOrderLineNumber(Boolean isDisplayPurchaseOrderLineNumber) {
		this.isDisplayPurchaseOrderLineNumber = isDisplayPurchaseOrderLineNumber;
	}

	public Boolean getEnablePurchasesProductByTradName() {
		return enablePurchasesProductByTradName == null ? Boolean.FALSE : enablePurchasesProductByTradName;
	}

	public void setEnablePurchasesProductByTradName(Boolean enablePurchasesProductByTradName) {
		this.enablePurchasesProductByTradName = enablePurchasesProductByTradName;
	}

	public Unit getPurchaseUnit() {
		return purchaseUnit;
	}

	public void setPurchaseUnit(Unit purchaseUnit) {
		this.purchaseUnit = purchaseUnit;
	}

	public Boolean getSupplierRequestMgt() {
		return supplierRequestMgt == null ? Boolean.FALSE : supplierRequestMgt;
	}

	public void setSupplierRequestMgt(Boolean supplierRequestMgt) {
		this.supplierRequestMgt = supplierRequestMgt;
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
		if (!(obj instanceof AppPurchase)) return false;

		final AppPurchase other = (AppPurchase) obj;
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
			.add("managePurchaseOrderVersion", getManagePurchaseOrderVersion())
			.add("managePurchasesUnits", getManagePurchasesUnits())
			.add("manageMultiplePurchaseQuantity", getManageMultiplePurchaseQuantity())
			.add("isEnabledProductDescriptionCopy", getIsEnabledProductDescriptionCopy())
			.add("manageSupplierCatalog", getManageSupplierCatalog())
			.add("isDisplayPurchaseOrderLineNumber", getIsDisplayPurchaseOrderLineNumber())
			.add("enablePurchasesProductByTradName", getEnablePurchasesProductByTradName())
			.add("supplierRequestMgt", getSupplierRequestMgt())
			.omitNullValues()
			.toString();
	}
}
