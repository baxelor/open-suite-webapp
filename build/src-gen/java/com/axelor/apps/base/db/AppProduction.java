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
import javax.validation.constraints.Max;

import org.hibernate.annotations.Type;

import com.axelor.apps.production.db.CostSheetGroup;
import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Track;
import com.axelor.db.annotations.TrackEvent;
import com.axelor.db.annotations.TrackField;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "BASE_APP_PRODUCTION", indexes = { @Index(columnList = "app"), @Index(columnList = "work_center_product"), @Index(columnList = "work_center_cost_sheet_group"), @Index(columnList = "product_cost_sheet_group"), @Index(columnList = "cycle_unit"), @Index(columnList = "barcode_type_config") })
@Track(fields = { @TrackField(name = "prodOrderMgtOnSO", on = TrackEvent.UPDATE), @TrackField(name = "productionOrderGenerationAuto", on = TrackEvent.UPDATE), @TrackField(name = "oneProdOrderPerSO", on = TrackEvent.UPDATE), @TrackField(name = "autoPlanManufOrderFromSO", on = TrackEvent.UPDATE), @TrackField(name = "workCenterProduct", on = TrackEvent.UPDATE), @TrackField(name = "workCenterCostSheetGroup", on = TrackEvent.UPDATE), @TrackField(name = "productCostSheetGroup", on = TrackEvent.UPDATE), @TrackField(name = "cycleUnit", on = TrackEvent.UPDATE), @TrackField(name = "manageResidualProductOnBom", on = TrackEvent.UPDATE), @TrackField(name = "subtractProdResidualOnCostSheet", on = TrackEvent.UPDATE), @TrackField(name = "manageBillOfMaterialVersion", on = TrackEvent.UPDATE), @TrackField(name = "manageProdProcessVersion", on = TrackEvent.UPDATE), @TrackField(name = "enableConfigurator", on = TrackEvent.UPDATE), @TrackField(name = "manageWorkshop", on = TrackEvent.UPDATE), @TrackField(name = "nbDecimalDigitForBomQty", on = TrackEvent.UPDATE), @TrackField(name = "barcodeTypeConfig", on = TrackEvent.UPDATE), @TrackField(name = "printPlannedDateOnManufOrder", on = TrackEvent.UPDATE), @TrackField(name = "manageOutsourcing", on = TrackEvent.UPDATE), @TrackField(name = "manageMpsCharge", on = TrackEvent.UPDATE), @TrackField(name = "manageBusinessProduction", on = TrackEvent.UPDATE), @TrackField(name = "enableTimesheetOnManufOrder", on = TrackEvent.UPDATE) })
public class AppProduction extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BASE_APP_PRODUCTION_SEQ")
	@SequenceGenerator(name = "BASE_APP_PRODUCTION_SEQ", sequenceName = "BASE_APP_PRODUCTION_SEQ", allocationSize = 1)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private App app;

	@Widget(title = "Production order management from sale order")
	private Boolean prodOrderMgtOnSO = Boolean.FALSE;

	@Widget(title = "Generate production orders automatically")
	private Boolean productionOrderGenerationAuto = Boolean.FALSE;

	@Widget(title = "One production order per sale order")
	private Boolean oneProdOrderPerSO = Boolean.TRUE;

	@Widget(title = "Automatically plan the manuf. orders generated from sale order")
	private Boolean autoPlanManufOrderFromSO = Boolean.FALSE;

	@Widget(title = "Default work center product")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Product workCenterProduct;

	@Widget(title = "Cost sheet group for work center")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private CostSheetGroup workCenterCostSheetGroup;

	@Widget(title = "Cost sheet group for product")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private CostSheetGroup productCostSheetGroup;

	@Widget(title = "Cycle unit")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Unit cycleUnit;

	@Widget(title = "Manage residual products on BOM")
	private Boolean manageResidualProductOnBom = Boolean.FALSE;

	@Widget(title = "Subtract the product residuals cost on Cost sheet")
	private Boolean subtractProdResidualOnCostSheet = Boolean.FALSE;

	@Widget(title = "Manage bill of materials versions")
	private Boolean manageBillOfMaterialVersion = Boolean.FALSE;

	@Widget(title = "Manage production process versions")
	private Boolean manageProdProcessVersion = Boolean.FALSE;

	@Widget(title = "Manage cost sheet group")
	private Boolean manageCostSheetGroup = Boolean.FALSE;

	@Widget(title = "Enable production configurator")
	private Boolean enableConfigurator = Boolean.FALSE;

	@Widget(title = "Manage workshop")
	private Boolean manageWorkshop = Boolean.FALSE;

	@Widget(title = "Nb of digits for BOM quantities")
	@Max(10)
	private Integer nbDecimalDigitForBomQty = 2;

	@Widget(title = "Barcode Type")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private BarcodeTypeConfig barcodeTypeConfig;

	@Widget(title = "Print planned date on manuf order ?")
	private Boolean printPlannedDateOnManufOrder = Boolean.TRUE;

	@Widget(title = "Status considered to filter a manuf order", selection = "production.manuf.order.status.select")
	private String mOFilterOnStockDetailStatusSelect = "4,5";

	@Widget(title = "Manage outsourcing")
	private Boolean manageOutsourcing = Boolean.FALSE;

	@Widget(title = "Manage Master Production Scheduling Charge")
	private Boolean manageMpsCharge = Boolean.FALSE;

	@Widget(title = "Enable Tool Management")
	private Boolean enableToolManagement = Boolean.FALSE;

	@Widget(title = "Automatically plan after fusion")
	private Boolean isManufOrderPlannedAfterMerge = Boolean.FALSE;

	@Widget(title = "Manage work center group")
	private Boolean manageWorkCenterGroup = Boolean.FALSE;

	@Widget(title = "Manage business production")
	private Boolean manageBusinessProduction = Boolean.FALSE;

	@Widget(title = "Enable timesheet on manuf order")
	private Boolean enableTimesheetOnManufOrder = Boolean.FALSE;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public AppProduction() {
	}

	public AppProduction(BarcodeTypeConfig barcodeTypeConfig) {
		this.barcodeTypeConfig = barcodeTypeConfig;
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

	public Boolean getProdOrderMgtOnSO() {
		return prodOrderMgtOnSO == null ? Boolean.FALSE : prodOrderMgtOnSO;
	}

	public void setProdOrderMgtOnSO(Boolean prodOrderMgtOnSO) {
		this.prodOrderMgtOnSO = prodOrderMgtOnSO;
	}

	public Boolean getProductionOrderGenerationAuto() {
		return productionOrderGenerationAuto == null ? Boolean.FALSE : productionOrderGenerationAuto;
	}

	public void setProductionOrderGenerationAuto(Boolean productionOrderGenerationAuto) {
		this.productionOrderGenerationAuto = productionOrderGenerationAuto;
	}

	public Boolean getOneProdOrderPerSO() {
		return oneProdOrderPerSO == null ? Boolean.FALSE : oneProdOrderPerSO;
	}

	public void setOneProdOrderPerSO(Boolean oneProdOrderPerSO) {
		this.oneProdOrderPerSO = oneProdOrderPerSO;
	}

	public Boolean getAutoPlanManufOrderFromSO() {
		return autoPlanManufOrderFromSO == null ? Boolean.FALSE : autoPlanManufOrderFromSO;
	}

	public void setAutoPlanManufOrderFromSO(Boolean autoPlanManufOrderFromSO) {
		this.autoPlanManufOrderFromSO = autoPlanManufOrderFromSO;
	}

	public Product getWorkCenterProduct() {
		return workCenterProduct;
	}

	public void setWorkCenterProduct(Product workCenterProduct) {
		this.workCenterProduct = workCenterProduct;
	}

	public CostSheetGroup getWorkCenterCostSheetGroup() {
		return workCenterCostSheetGroup;
	}

	public void setWorkCenterCostSheetGroup(CostSheetGroup workCenterCostSheetGroup) {
		this.workCenterCostSheetGroup = workCenterCostSheetGroup;
	}

	public CostSheetGroup getProductCostSheetGroup() {
		return productCostSheetGroup;
	}

	public void setProductCostSheetGroup(CostSheetGroup productCostSheetGroup) {
		this.productCostSheetGroup = productCostSheetGroup;
	}

	public Unit getCycleUnit() {
		return cycleUnit;
	}

	public void setCycleUnit(Unit cycleUnit) {
		this.cycleUnit = cycleUnit;
	}

	public Boolean getManageResidualProductOnBom() {
		return manageResidualProductOnBom == null ? Boolean.FALSE : manageResidualProductOnBom;
	}

	public void setManageResidualProductOnBom(Boolean manageResidualProductOnBom) {
		this.manageResidualProductOnBom = manageResidualProductOnBom;
	}

	public Boolean getSubtractProdResidualOnCostSheet() {
		return subtractProdResidualOnCostSheet == null ? Boolean.FALSE : subtractProdResidualOnCostSheet;
	}

	public void setSubtractProdResidualOnCostSheet(Boolean subtractProdResidualOnCostSheet) {
		this.subtractProdResidualOnCostSheet = subtractProdResidualOnCostSheet;
	}

	public Boolean getManageBillOfMaterialVersion() {
		return manageBillOfMaterialVersion == null ? Boolean.FALSE : manageBillOfMaterialVersion;
	}

	public void setManageBillOfMaterialVersion(Boolean manageBillOfMaterialVersion) {
		this.manageBillOfMaterialVersion = manageBillOfMaterialVersion;
	}

	public Boolean getManageProdProcessVersion() {
		return manageProdProcessVersion == null ? Boolean.FALSE : manageProdProcessVersion;
	}

	public void setManageProdProcessVersion(Boolean manageProdProcessVersion) {
		this.manageProdProcessVersion = manageProdProcessVersion;
	}

	public Boolean getManageCostSheetGroup() {
		return manageCostSheetGroup == null ? Boolean.FALSE : manageCostSheetGroup;
	}

	public void setManageCostSheetGroup(Boolean manageCostSheetGroup) {
		this.manageCostSheetGroup = manageCostSheetGroup;
	}

	public Boolean getEnableConfigurator() {
		return enableConfigurator == null ? Boolean.FALSE : enableConfigurator;
	}

	public void setEnableConfigurator(Boolean enableConfigurator) {
		this.enableConfigurator = enableConfigurator;
	}

	public Boolean getManageWorkshop() {
		return manageWorkshop == null ? Boolean.FALSE : manageWorkshop;
	}

	public void setManageWorkshop(Boolean manageWorkshop) {
		this.manageWorkshop = manageWorkshop;
	}

	public Integer getNbDecimalDigitForBomQty() {
		return nbDecimalDigitForBomQty == null ? 0 : nbDecimalDigitForBomQty;
	}

	public void setNbDecimalDigitForBomQty(Integer nbDecimalDigitForBomQty) {
		this.nbDecimalDigitForBomQty = nbDecimalDigitForBomQty;
	}

	public BarcodeTypeConfig getBarcodeTypeConfig() {
		return barcodeTypeConfig;
	}

	public void setBarcodeTypeConfig(BarcodeTypeConfig barcodeTypeConfig) {
		this.barcodeTypeConfig = barcodeTypeConfig;
	}

	public Boolean getPrintPlannedDateOnManufOrder() {
		return printPlannedDateOnManufOrder == null ? Boolean.FALSE : printPlannedDateOnManufOrder;
	}

	public void setPrintPlannedDateOnManufOrder(Boolean printPlannedDateOnManufOrder) {
		this.printPlannedDateOnManufOrder = printPlannedDateOnManufOrder;
	}

	public String getmOFilterOnStockDetailStatusSelect() {
		return mOFilterOnStockDetailStatusSelect;
	}

	public void setmOFilterOnStockDetailStatusSelect(String mOFilterOnStockDetailStatusSelect) {
		this.mOFilterOnStockDetailStatusSelect = mOFilterOnStockDetailStatusSelect;
	}

	public Boolean getManageOutsourcing() {
		return manageOutsourcing == null ? Boolean.FALSE : manageOutsourcing;
	}

	public void setManageOutsourcing(Boolean manageOutsourcing) {
		this.manageOutsourcing = manageOutsourcing;
	}

	public Boolean getManageMpsCharge() {
		return manageMpsCharge == null ? Boolean.FALSE : manageMpsCharge;
	}

	public void setManageMpsCharge(Boolean manageMpsCharge) {
		this.manageMpsCharge = manageMpsCharge;
	}

	public Boolean getEnableToolManagement() {
		return enableToolManagement == null ? Boolean.FALSE : enableToolManagement;
	}

	public void setEnableToolManagement(Boolean enableToolManagement) {
		this.enableToolManagement = enableToolManagement;
	}

	public Boolean getIsManufOrderPlannedAfterMerge() {
		return isManufOrderPlannedAfterMerge == null ? Boolean.FALSE : isManufOrderPlannedAfterMerge;
	}

	public void setIsManufOrderPlannedAfterMerge(Boolean isManufOrderPlannedAfterMerge) {
		this.isManufOrderPlannedAfterMerge = isManufOrderPlannedAfterMerge;
	}

	public Boolean getManageWorkCenterGroup() {
		return manageWorkCenterGroup == null ? Boolean.FALSE : manageWorkCenterGroup;
	}

	public void setManageWorkCenterGroup(Boolean manageWorkCenterGroup) {
		this.manageWorkCenterGroup = manageWorkCenterGroup;
	}

	public Boolean getManageBusinessProduction() {
		return manageBusinessProduction == null ? Boolean.FALSE : manageBusinessProduction;
	}

	public void setManageBusinessProduction(Boolean manageBusinessProduction) {
		this.manageBusinessProduction = manageBusinessProduction;
	}

	public Boolean getEnableTimesheetOnManufOrder() {
		return enableTimesheetOnManufOrder == null ? Boolean.FALSE : enableTimesheetOnManufOrder;
	}

	public void setEnableTimesheetOnManufOrder(Boolean enableTimesheetOnManufOrder) {
		this.enableTimesheetOnManufOrder = enableTimesheetOnManufOrder;
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
		if (!(obj instanceof AppProduction)) return false;

		final AppProduction other = (AppProduction) obj;
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
			.add("prodOrderMgtOnSO", getProdOrderMgtOnSO())
			.add("productionOrderGenerationAuto", getProductionOrderGenerationAuto())
			.add("oneProdOrderPerSO", getOneProdOrderPerSO())
			.add("autoPlanManufOrderFromSO", getAutoPlanManufOrderFromSO())
			.add("manageResidualProductOnBom", getManageResidualProductOnBom())
			.add("subtractProdResidualOnCostSheet", getSubtractProdResidualOnCostSheet())
			.add("manageBillOfMaterialVersion", getManageBillOfMaterialVersion())
			.add("manageProdProcessVersion", getManageProdProcessVersion())
			.add("manageCostSheetGroup", getManageCostSheetGroup())
			.add("enableConfigurator", getEnableConfigurator())
			.add("manageWorkshop", getManageWorkshop())
			.omitNullValues()
			.toString();
	}
}
