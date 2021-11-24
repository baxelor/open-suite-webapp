package com.axelor.apps.base.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.EqualsInclude;
import com.axelor.db.annotations.Widget;
import com.axelor.meta.db.MetaFile;
import com.google.common.base.MoreObjects;

@Entity
@Cacheable
@Table(name = "BASE_APP", indexes = { @Index(columnList = "name"), @Index(columnList = "image") })
public class App extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BASE_APP_SEQ")
	@SequenceGenerator(name = "BASE_APP_SEQ", sequenceName = "BASE_APP_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Name", translatable = true)
	@NotNull
	private String name;

	@EqualsInclude
	@Widget(title = "Code")
	@NotNull
	@Column(unique = true)
	private String code;

	@Widget(title = "Description", translatable = true)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String description;

	@Widget(title = "Modules contains in the app")
	private String modules;

	@Widget(title = "Depends on")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<App> dependsOnSet;

	@Widget(title = "Image")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private MetaFile image;

	@Widget(title = "Init data loaded")
	private Boolean initDataLoaded = Boolean.FALSE;

	@Widget(title = "Demo data loaded")
	private Boolean demoDataLoaded = Boolean.FALSE;

	@Widget(title = "Roles imported")
	private Boolean isRolesImported = Boolean.FALSE;

	@Widget(title = "Installed")
	private Boolean active = Boolean.FALSE;

	@Widget(title = "Sequence")
	private Integer sequence = 0;

	@Widget(title = "Install order")
	private Integer installOrder = 0;

	@Widget(title = "Language", selection = "select.language")
	private String languageSelect;

	@Widget(title = "Access config")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "app", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AccessConfig> accessConfigList;

	@Widget(title = "Custom")
	private Boolean isCustom = Boolean.FALSE;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppBase appBase;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppAccount appAccount;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppInvoice appInvoice;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppBudget appBudget;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppBankPayment appBankPayment;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppBpm appBpm;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppProject appProject;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppEmployee appEmployee;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppExpense appExpense;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppExtraHours appExthrs;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppLeave appLeave;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppTimesheet appTimesheet;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppCrm appCrm;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppSale appSale;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppPurchase appPurchase;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppPurchaseRequest appPurchaseRequest;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppStock appStock;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppSupplychain appSupplychain;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppContract appContract;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppBusinessProject appBusinessProject;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppProduction appProduction;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppBusinessSupport appBusinessSupport;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppCashManagement appCashManagement;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppHelpdesk appHelpdesk;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppPortal appPortal;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppFleet appFleet;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppMaintenance appMaintenance;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppMarketing appMarketing;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppMobile appMobile;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppQuality appQuality;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppSupplierPortal appSupplierPortal;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppAppraisal appAppraisal;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppRecruitment appRecruitment;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppTraining appTraining;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "app", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private AppGst appGst;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public App() {
	}

	public App(String name, String code) {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModules() {
		return modules;
	}

	public void setModules(String modules) {
		this.modules = modules;
	}

	public Set<App> getDependsOnSet() {
		return dependsOnSet;
	}

	public void setDependsOnSet(Set<App> dependsOnSet) {
		this.dependsOnSet = dependsOnSet;
	}

	/**
	 * Add the given {@link App} item to the {@code dependsOnSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addDependsOnSetItem(App item) {
		if (getDependsOnSet() == null) {
			setDependsOnSet(new HashSet<>());
		}
		getDependsOnSet().add(item);
	}

	/**
	 * Remove the given {@link App} item from the {@code dependsOnSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeDependsOnSetItem(App item) {
		if (getDependsOnSet() == null) {
			return;
		}
		getDependsOnSet().remove(item);
	}

	/**
	 * Clear the {@code dependsOnSet} collection.
	 *
	 */
	public void clearDependsOnSet() {
		if (getDependsOnSet() != null) {
			getDependsOnSet().clear();
		}
	}

	public MetaFile getImage() {
		return image;
	}

	public void setImage(MetaFile image) {
		this.image = image;
	}

	public Boolean getInitDataLoaded() {
		return initDataLoaded == null ? Boolean.FALSE : initDataLoaded;
	}

	public void setInitDataLoaded(Boolean initDataLoaded) {
		this.initDataLoaded = initDataLoaded;
	}

	public Boolean getDemoDataLoaded() {
		return demoDataLoaded == null ? Boolean.FALSE : demoDataLoaded;
	}

	public void setDemoDataLoaded(Boolean demoDataLoaded) {
		this.demoDataLoaded = demoDataLoaded;
	}

	public Boolean getIsRolesImported() {
		return isRolesImported == null ? Boolean.FALSE : isRolesImported;
	}

	public void setIsRolesImported(Boolean isRolesImported) {
		this.isRolesImported = isRolesImported;
	}

	public Boolean getActive() {
		return active == null ? Boolean.FALSE : active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getSequence() {
		return sequence == null ? 0 : sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Integer getInstallOrder() {
		return installOrder == null ? 0 : installOrder;
	}

	public void setInstallOrder(Integer installOrder) {
		this.installOrder = installOrder;
	}

	public String getLanguageSelect() {
		return languageSelect;
	}

	public void setLanguageSelect(String languageSelect) {
		this.languageSelect = languageSelect;
	}

	public List<AccessConfig> getAccessConfigList() {
		return accessConfigList;
	}

	public void setAccessConfigList(List<AccessConfig> accessConfigList) {
		this.accessConfigList = accessConfigList;
	}

	/**
	 * Add the given {@link AccessConfig} item to the {@code accessConfigList}.
	 *
	 * <p>
	 * It sets {@code item.app = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addAccessConfigListItem(AccessConfig item) {
		if (getAccessConfigList() == null) {
			setAccessConfigList(new ArrayList<>());
		}
		getAccessConfigList().add(item);
		item.setApp(this);
	}

	/**
	 * Remove the given {@link AccessConfig} item from the {@code accessConfigList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeAccessConfigListItem(AccessConfig item) {
		if (getAccessConfigList() == null) {
			return;
		}
		getAccessConfigList().remove(item);
	}

	/**
	 * Clear the {@code accessConfigList} collection.
	 *
	 * <p>
	 * If you have to query {@link AccessConfig} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearAccessConfigList() {
		if (getAccessConfigList() != null) {
			getAccessConfigList().clear();
		}
	}

	public Boolean getIsCustom() {
		return isCustom == null ? Boolean.FALSE : isCustom;
	}

	public void setIsCustom(Boolean isCustom) {
		this.isCustom = isCustom;
	}

	public AppBase getAppBase() {
		return appBase;
	}

	public void setAppBase(AppBase appBase) {
		if (getAppBase() != null) {
			getAppBase().setApp(null);
		}
		if (appBase != null) {
			appBase.setApp(this);
		}
		this.appBase = appBase;
	}

	public AppAccount getAppAccount() {
		return appAccount;
	}

	public void setAppAccount(AppAccount appAccount) {
		if (getAppAccount() != null) {
			getAppAccount().setApp(null);
		}
		if (appAccount != null) {
			appAccount.setApp(this);
		}
		this.appAccount = appAccount;
	}

	public AppInvoice getAppInvoice() {
		return appInvoice;
	}

	public void setAppInvoice(AppInvoice appInvoice) {
		if (getAppInvoice() != null) {
			getAppInvoice().setApp(null);
		}
		if (appInvoice != null) {
			appInvoice.setApp(this);
		}
		this.appInvoice = appInvoice;
	}

	public AppBudget getAppBudget() {
		return appBudget;
	}

	public void setAppBudget(AppBudget appBudget) {
		if (getAppBudget() != null) {
			getAppBudget().setApp(null);
		}
		if (appBudget != null) {
			appBudget.setApp(this);
		}
		this.appBudget = appBudget;
	}

	public AppBankPayment getAppBankPayment() {
		return appBankPayment;
	}

	public void setAppBankPayment(AppBankPayment appBankPayment) {
		if (getAppBankPayment() != null) {
			getAppBankPayment().setApp(null);
		}
		if (appBankPayment != null) {
			appBankPayment.setApp(this);
		}
		this.appBankPayment = appBankPayment;
	}

	public AppBpm getAppBpm() {
		return appBpm;
	}

	public void setAppBpm(AppBpm appBpm) {
		if (getAppBpm() != null) {
			getAppBpm().setApp(null);
		}
		if (appBpm != null) {
			appBpm.setApp(this);
		}
		this.appBpm = appBpm;
	}

	public AppProject getAppProject() {
		return appProject;
	}

	public void setAppProject(AppProject appProject) {
		if (getAppProject() != null) {
			getAppProject().setApp(null);
		}
		if (appProject != null) {
			appProject.setApp(this);
		}
		this.appProject = appProject;
	}

	public AppEmployee getAppEmployee() {
		return appEmployee;
	}

	public void setAppEmployee(AppEmployee appEmployee) {
		if (getAppEmployee() != null) {
			getAppEmployee().setApp(null);
		}
		if (appEmployee != null) {
			appEmployee.setApp(this);
		}
		this.appEmployee = appEmployee;
	}

	public AppExpense getAppExpense() {
		return appExpense;
	}

	public void setAppExpense(AppExpense appExpense) {
		if (getAppExpense() != null) {
			getAppExpense().setApp(null);
		}
		if (appExpense != null) {
			appExpense.setApp(this);
		}
		this.appExpense = appExpense;
	}

	public AppExtraHours getAppExthrs() {
		return appExthrs;
	}

	public void setAppExthrs(AppExtraHours appExthrs) {
		if (getAppExthrs() != null) {
			getAppExthrs().setApp(null);
		}
		if (appExthrs != null) {
			appExthrs.setApp(this);
		}
		this.appExthrs = appExthrs;
	}

	public AppLeave getAppLeave() {
		return appLeave;
	}

	public void setAppLeave(AppLeave appLeave) {
		if (getAppLeave() != null) {
			getAppLeave().setApp(null);
		}
		if (appLeave != null) {
			appLeave.setApp(this);
		}
		this.appLeave = appLeave;
	}

	public AppTimesheet getAppTimesheet() {
		return appTimesheet;
	}

	public void setAppTimesheet(AppTimesheet appTimesheet) {
		if (getAppTimesheet() != null) {
			getAppTimesheet().setApp(null);
		}
		if (appTimesheet != null) {
			appTimesheet.setApp(this);
		}
		this.appTimesheet = appTimesheet;
	}

	public AppCrm getAppCrm() {
		return appCrm;
	}

	public void setAppCrm(AppCrm appCrm) {
		if (getAppCrm() != null) {
			getAppCrm().setApp(null);
		}
		if (appCrm != null) {
			appCrm.setApp(this);
		}
		this.appCrm = appCrm;
	}

	public AppSale getAppSale() {
		return appSale;
	}

	public void setAppSale(AppSale appSale) {
		if (getAppSale() != null) {
			getAppSale().setApp(null);
		}
		if (appSale != null) {
			appSale.setApp(this);
		}
		this.appSale = appSale;
	}

	public AppPurchase getAppPurchase() {
		return appPurchase;
	}

	public void setAppPurchase(AppPurchase appPurchase) {
		if (getAppPurchase() != null) {
			getAppPurchase().setApp(null);
		}
		if (appPurchase != null) {
			appPurchase.setApp(this);
		}
		this.appPurchase = appPurchase;
	}

	public AppPurchaseRequest getAppPurchaseRequest() {
		return appPurchaseRequest;
	}

	public void setAppPurchaseRequest(AppPurchaseRequest appPurchaseRequest) {
		if (getAppPurchaseRequest() != null) {
			getAppPurchaseRequest().setApp(null);
		}
		if (appPurchaseRequest != null) {
			appPurchaseRequest.setApp(this);
		}
		this.appPurchaseRequest = appPurchaseRequest;
	}

	public AppStock getAppStock() {
		return appStock;
	}

	public void setAppStock(AppStock appStock) {
		if (getAppStock() != null) {
			getAppStock().setApp(null);
		}
		if (appStock != null) {
			appStock.setApp(this);
		}
		this.appStock = appStock;
	}

	public AppSupplychain getAppSupplychain() {
		return appSupplychain;
	}

	public void setAppSupplychain(AppSupplychain appSupplychain) {
		if (getAppSupplychain() != null) {
			getAppSupplychain().setApp(null);
		}
		if (appSupplychain != null) {
			appSupplychain.setApp(this);
		}
		this.appSupplychain = appSupplychain;
	}

	public AppContract getAppContract() {
		return appContract;
	}

	public void setAppContract(AppContract appContract) {
		if (getAppContract() != null) {
			getAppContract().setApp(null);
		}
		if (appContract != null) {
			appContract.setApp(this);
		}
		this.appContract = appContract;
	}

	public AppBusinessProject getAppBusinessProject() {
		return appBusinessProject;
	}

	public void setAppBusinessProject(AppBusinessProject appBusinessProject) {
		if (getAppBusinessProject() != null) {
			getAppBusinessProject().setApp(null);
		}
		if (appBusinessProject != null) {
			appBusinessProject.setApp(this);
		}
		this.appBusinessProject = appBusinessProject;
	}

	public AppProduction getAppProduction() {
		return appProduction;
	}

	public void setAppProduction(AppProduction appProduction) {
		if (getAppProduction() != null) {
			getAppProduction().setApp(null);
		}
		if (appProduction != null) {
			appProduction.setApp(this);
		}
		this.appProduction = appProduction;
	}

	public AppBusinessSupport getAppBusinessSupport() {
		return appBusinessSupport;
	}

	public void setAppBusinessSupport(AppBusinessSupport appBusinessSupport) {
		if (getAppBusinessSupport() != null) {
			getAppBusinessSupport().setApp(null);
		}
		if (appBusinessSupport != null) {
			appBusinessSupport.setApp(this);
		}
		this.appBusinessSupport = appBusinessSupport;
	}

	public AppCashManagement getAppCashManagement() {
		return appCashManagement;
	}

	public void setAppCashManagement(AppCashManagement appCashManagement) {
		if (getAppCashManagement() != null) {
			getAppCashManagement().setApp(null);
		}
		if (appCashManagement != null) {
			appCashManagement.setApp(this);
		}
		this.appCashManagement = appCashManagement;
	}

	public AppHelpdesk getAppHelpdesk() {
		return appHelpdesk;
	}

	public void setAppHelpdesk(AppHelpdesk appHelpdesk) {
		if (getAppHelpdesk() != null) {
			getAppHelpdesk().setApp(null);
		}
		if (appHelpdesk != null) {
			appHelpdesk.setApp(this);
		}
		this.appHelpdesk = appHelpdesk;
	}

	public AppPortal getAppPortal() {
		return appPortal;
	}

	public void setAppPortal(AppPortal appPortal) {
		if (getAppPortal() != null) {
			getAppPortal().setApp(null);
		}
		if (appPortal != null) {
			appPortal.setApp(this);
		}
		this.appPortal = appPortal;
	}

	public AppFleet getAppFleet() {
		return appFleet;
	}

	public void setAppFleet(AppFleet appFleet) {
		if (getAppFleet() != null) {
			getAppFleet().setApp(null);
		}
		if (appFleet != null) {
			appFleet.setApp(this);
		}
		this.appFleet = appFleet;
	}

	public AppMaintenance getAppMaintenance() {
		return appMaintenance;
	}

	public void setAppMaintenance(AppMaintenance appMaintenance) {
		if (getAppMaintenance() != null) {
			getAppMaintenance().setApp(null);
		}
		if (appMaintenance != null) {
			appMaintenance.setApp(this);
		}
		this.appMaintenance = appMaintenance;
	}

	public AppMarketing getAppMarketing() {
		return appMarketing;
	}

	public void setAppMarketing(AppMarketing appMarketing) {
		if (getAppMarketing() != null) {
			getAppMarketing().setApp(null);
		}
		if (appMarketing != null) {
			appMarketing.setApp(this);
		}
		this.appMarketing = appMarketing;
	}

	public AppMobile getAppMobile() {
		return appMobile;
	}

	public void setAppMobile(AppMobile appMobile) {
		if (getAppMobile() != null) {
			getAppMobile().setApp(null);
		}
		if (appMobile != null) {
			appMobile.setApp(this);
		}
		this.appMobile = appMobile;
	}

	public AppQuality getAppQuality() {
		return appQuality;
	}

	public void setAppQuality(AppQuality appQuality) {
		if (getAppQuality() != null) {
			getAppQuality().setApp(null);
		}
		if (appQuality != null) {
			appQuality.setApp(this);
		}
		this.appQuality = appQuality;
	}

	public AppSupplierPortal getAppSupplierPortal() {
		return appSupplierPortal;
	}

	public void setAppSupplierPortal(AppSupplierPortal appSupplierPortal) {
		if (getAppSupplierPortal() != null) {
			getAppSupplierPortal().setApp(null);
		}
		if (appSupplierPortal != null) {
			appSupplierPortal.setApp(this);
		}
		this.appSupplierPortal = appSupplierPortal;
	}

	public AppAppraisal getAppAppraisal() {
		return appAppraisal;
	}

	public void setAppAppraisal(AppAppraisal appAppraisal) {
		if (getAppAppraisal() != null) {
			getAppAppraisal().setApp(null);
		}
		if (appAppraisal != null) {
			appAppraisal.setApp(this);
		}
		this.appAppraisal = appAppraisal;
	}

	public AppRecruitment getAppRecruitment() {
		return appRecruitment;
	}

	public void setAppRecruitment(AppRecruitment appRecruitment) {
		if (getAppRecruitment() != null) {
			getAppRecruitment().setApp(null);
		}
		if (appRecruitment != null) {
			appRecruitment.setApp(this);
		}
		this.appRecruitment = appRecruitment;
	}

	public AppTraining getAppTraining() {
		return appTraining;
	}

	public void setAppTraining(AppTraining appTraining) {
		if (getAppTraining() != null) {
			getAppTraining().setApp(null);
		}
		if (appTraining != null) {
			appTraining.setApp(this);
		}
		this.appTraining = appTraining;
	}

	public AppGst getAppGst() {
		return appGst;
	}

	public void setAppGst(AppGst appGst) {
		if (getAppGst() != null) {
			getAppGst().setApp(null);
		}
		if (appGst != null) {
			appGst.setApp(this);
		}
		this.appGst = appGst;
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
		if (!(obj instanceof App)) return false;

		final App other = (App) obj;
		if (this.getId() != null || other.getId() != null) {
			return Objects.equals(this.getId(), other.getId());
		}

		return Objects.equals(getCode(), other.getCode())
			&& (getCode() != null);
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
			.add("modules", getModules())
			.add("initDataLoaded", getInitDataLoaded())
			.add("demoDataLoaded", getDemoDataLoaded())
			.add("isRolesImported", getIsRolesImported())
			.add("active", getActive())
			.add("sequence", getSequence())
			.add("installOrder", getInstallOrder())
			.add("languageSelect", getLanguageSelect())
			.add("isCustom", getIsCustom())
			.omitNullValues()
			.toString();
	}
}
