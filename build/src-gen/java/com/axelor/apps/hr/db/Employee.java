package com.axelor.apps.hr.db;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axelor.apps.base.db.BankDetails;
import com.axelor.apps.base.db.Batch;
import com.axelor.apps.base.db.Citizenship;
import com.axelor.apps.base.db.City;
import com.axelor.apps.base.db.Country;
import com.axelor.apps.base.db.Department;
import com.axelor.apps.base.db.EventsPlanning;
import com.axelor.apps.base.db.Partner;
import com.axelor.apps.base.db.Product;
import com.axelor.apps.base.db.WeeklyPlanning;
import com.axelor.apps.talent.db.Skill;
import com.axelor.auth.db.AuditableModel;
import com.axelor.auth.db.User;
import com.axelor.db.annotations.Track;
import com.axelor.db.annotations.TrackEvent;
import com.axelor.db.annotations.TrackField;
import com.axelor.db.annotations.VirtualColumn;
import com.axelor.db.annotations.Widget;
import com.axelor.db.converters.EncryptedStringConverter;
import com.google.common.base.MoreObjects;

@Entity
@Cacheable
@DynamicInsert
@DynamicUpdate
@Table(name = "HR_EMPLOYEE", indexes = { @Index(columnList = "contact_partner"), @Index(columnList = "manager_user"), @Index(columnList = "weekly_planning"), @Index(columnList = "product"), @Index(columnList = "public_holiday_events_planning"), @Index(columnList = "city_of_birth"), @Index(columnList = "citizenship"), @Index(columnList = "department_of_birth"), @Index(columnList = "country_of_birth"), @Index(columnList = "main_employment_contract"), @Index(columnList = "imposed_day_events_planning"), @Index(columnList = "name"), @Index(columnList = "bank_details") })
@Track(fields = { @TrackField(name = "fixedProPhone", on = TrackEvent.UPDATE), @TrackField(name = "mobileProPhone", on = TrackEvent.UPDATE), @TrackField(name = "phoneAtCustomer", on = TrackEvent.UPDATE), @TrackField(name = "managerUser", on = TrackEvent.UPDATE), @TrackField(name = "exportCode", on = TrackEvent.UPDATE), @TrackField(name = "weeklyPlanning", on = TrackEvent.UPDATE), @TrackField(name = "hrManager", on = TrackEvent.UPDATE), @TrackField(name = "external", on = TrackEvent.UPDATE), @TrackField(name = "bonusCoef", on = TrackEvent.UPDATE), @TrackField(name = "negativeValueLeave", on = TrackEvent.UPDATE), @TrackField(name = "lunchVoucherFormatSelect", on = TrackEvent.UPDATE), @TrackField(name = "companyCbSelect", on = TrackEvent.UPDATE), @TrackField(name = "bankDetails", on = TrackEvent.UPDATE) })
public class Employee extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HR_EMPLOYEE_SEQ")
	@SequenceGenerator(name = "HR_EMPLOYEE_SEQ", sequenceName = "HR_EMPLOYEE_SEQ", allocationSize = 1)
	private Long id;

	@JoinColumn(name = "user_id")
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "employee", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private User user;

	@Widget(title = "Contact")
	@NotNull
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner contactPartner;

	@Widget(title = "Hourly rate")
	@Digits(integer = 18, fraction = 2)
	private BigDecimal hourlyRate = BigDecimal.ZERO;

	@Widget(title = "Time logging preference", selection = "hr.time.logging.preference.select", massUpdate = true)
	private String timeLoggingPreferenceSelect;

	@Widget(title = "Weekly work hours")
	@Digits(integer = 18, fraction = 2)
	private BigDecimal weeklyWorkHours = BigDecimal.ZERO;

	@Widget(title = "Daily work hours")
	@Digits(integer = 18, fraction = 2)
	private BigDecimal dailyWorkHours = BigDecimal.ZERO;

	@Widget(title = "Manager", massUpdate = true)
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private User managerUser;

	@Widget(title = "Timesheet reminder")
	private Boolean timesheetReminder = Boolean.FALSE;

	@Widget(title = "External")
	private Boolean external = Boolean.FALSE;

	@Widget(massUpdate = true)
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private WeeklyPlanning weeklyPlanning;

	@Widget(title = "Leave List")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LeaveLine> leaveLineList;

	@Widget(title = "Allow negative value for leaves")
	private Boolean negativeValueLeave = Boolean.FALSE;

	@Widget(title = "Default Activity Product")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Product product;

	@Widget(title = "Public Holiday Planning", massUpdate = true)
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private EventsPlanning publicHolidayEventsPlanning;

	@Widget(title = "Employment contracts")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EmploymentContract> employmentContractList;

	@Widget(title = "HR manager")
	private Boolean hrManager = Boolean.FALSE;

	@Widget(title = "Batches")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Batch> batchSet;

	@Widget(title = "Birth date")
	private LocalDate birthDate;

	@Widget(title = "Marital status", selection = "hr.employee.marital.status")
	private String maritalStatus;

	@Widget(title = "City of birth")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private City cityOfBirth;

	@Widget(title = "Citizenship")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Citizenship citizenship;

	@Widget(title = "Department of birth", help = "For foreign-born employees, please enter the code 99")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Department departmentOfBirth;

	@Widget(title = "Country of birth")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Country countryOfBirth;

	@Widget(title = "Sex", selection = "employee.hr.sex.select")
	private String sexSelect;

	@Widget(title = "Work fixed phone")
	private String fixedProPhone;

	@Widget(title = "Work mobile phone")
	private String mobileProPhone;

	@Widget(title = "Phone at the customer")
	private String phoneAtCustomer;

	@Widget(title = "Emergency contact")
	private String emergencyContact;

	@Widget(title = "Phone")
	private String emergencyNumber;

	@Widget(title = "Emergency contact relationship")
	private String emergencyContactRelationship;

	@Widget(title = "Date of hire")
	private LocalDate hireDate;

	@Widget(title = "Seniority date")
	private LocalDate seniorityDate;

	@Widget(title = "Leaving date")
	private LocalDate leavingDate;

	@Widget(title = "Main employment contract")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private EmploymentContract mainEmploymentContract;

	@Widget(title = "Profit-sharing beneficiary", massUpdate = true)
	private Boolean profitSharingBeneficiary = Boolean.FALSE;

	@Widget(title = "Imposed day Planning", massUpdate = true)
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private EventsPlanning imposedDayEventsPlanning;

	@Widget(title = "Social security number")
	@Convert(converter = EncryptedStringConverter.class)
	private String socialSecurityNumber;

	@Widget(title = "Employee advances", readonly = true)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EmployeeAdvance> employeeAdvanceList;

	@Widget(title = "Lunch Voucher Format", selection = "hr.lunch.voucher.mgt.line.lunch.voucher.format.select", massUpdate = true)
	private Integer lunchVoucherFormatSelect = 0;

	@Widget(title = "Lunch voucher advances")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LunchVoucherAdvance> lunchVoucherAdvanceList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<KilometricLog> kilometricLogList;

	@Widget(title = "Coefficient for bonus")
	private BigDecimal bonusCoef = BigDecimal.ZERO;

	@Widget(title = "Export code")
	private String exportCode;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EmployeeVehicle> employeeVehicleList;

	@Widget(title = "Expense paid with company's credit card", selection = "hr.expense.company.cb.payment")
	private Integer companyCbSelect = 1;

	@Widget(title = "Company credit card details")
	private String companyCbDetails;

	@Widget(title = "Name", search = { "contactPartner" })
	@VirtualColumn
	@Access(AccessType.PROPERTY)
	private String name;

	@Widget(title = "Step by step", selection = "hr.employee.form.step.by.step.select")
	private Integer stepByStepSelect = 1;

	@Widget(title = "DPAEs")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DPAE> dpaeList;

	@Widget(title = "Files")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EmployeeFile> employeeFileList;

	@Widget(title = "Maiden name")
	private String maidenName;

	@Widget(title = "Marital name")
	private String maritalName;

	@Widget(title = "Timesheet imputed on", selection = "business.production.employee.timesheet.imputation.select")
	private Integer timesheetImputationSelect = 1;

	@Widget(title = "Bank")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private BankDetails bankDetails;

	@Widget(title = "Training skills")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Skill> trainingSkillSet;

	@Widget(title = "Experience skills")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Skill> experienceSkillSet;

	@Widget(title = "Other skills")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Skill> skillSet;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Employee() {
	}

	public Employee(String name) {
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		if (getUser() != null) {
			getUser().setEmployee(null);
		}
		if (user != null) {
			user.setEmployee(this);
		}
		this.user = user;
	}

	public Partner getContactPartner() {
		return contactPartner;
	}

	public void setContactPartner(Partner contactPartner) {
		this.contactPartner = contactPartner;
	}

	public BigDecimal getHourlyRate() {
		return hourlyRate == null ? BigDecimal.ZERO : hourlyRate;
	}

	public void setHourlyRate(BigDecimal hourlyRate) {
		this.hourlyRate = hourlyRate;
	}

	public String getTimeLoggingPreferenceSelect() {
		return timeLoggingPreferenceSelect;
	}

	public void setTimeLoggingPreferenceSelect(String timeLoggingPreferenceSelect) {
		this.timeLoggingPreferenceSelect = timeLoggingPreferenceSelect;
	}

	public BigDecimal getWeeklyWorkHours() {
		return weeklyWorkHours == null ? BigDecimal.ZERO : weeklyWorkHours;
	}

	public void setWeeklyWorkHours(BigDecimal weeklyWorkHours) {
		this.weeklyWorkHours = weeklyWorkHours;
	}

	public BigDecimal getDailyWorkHours() {
		return dailyWorkHours == null ? BigDecimal.ZERO : dailyWorkHours;
	}

	public void setDailyWorkHours(BigDecimal dailyWorkHours) {
		this.dailyWorkHours = dailyWorkHours;
	}

	public User getManagerUser() {
		return managerUser;
	}

	public void setManagerUser(User managerUser) {
		this.managerUser = managerUser;
	}

	public Boolean getTimesheetReminder() {
		return timesheetReminder == null ? Boolean.FALSE : timesheetReminder;
	}

	public void setTimesheetReminder(Boolean timesheetReminder) {
		this.timesheetReminder = timesheetReminder;
	}

	public Boolean getExternal() {
		return external == null ? Boolean.FALSE : external;
	}

	public void setExternal(Boolean external) {
		this.external = external;
	}

	public WeeklyPlanning getWeeklyPlanning() {
		return weeklyPlanning;
	}

	public void setWeeklyPlanning(WeeklyPlanning weeklyPlanning) {
		this.weeklyPlanning = weeklyPlanning;
	}

	public List<LeaveLine> getLeaveLineList() {
		return leaveLineList;
	}

	public void setLeaveLineList(List<LeaveLine> leaveLineList) {
		this.leaveLineList = leaveLineList;
	}

	/**
	 * Add the given {@link LeaveLine} item to the {@code leaveLineList}.
	 *
	 * <p>
	 * It sets {@code item.employee = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addLeaveLineListItem(LeaveLine item) {
		if (getLeaveLineList() == null) {
			setLeaveLineList(new ArrayList<>());
		}
		getLeaveLineList().add(item);
		item.setEmployee(this);
	}

	/**
	 * Remove the given {@link LeaveLine} item from the {@code leaveLineList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeLeaveLineListItem(LeaveLine item) {
		if (getLeaveLineList() == null) {
			return;
		}
		getLeaveLineList().remove(item);
	}

	/**
	 * Clear the {@code leaveLineList} collection.
	 *
	 * <p>
	 * If you have to query {@link LeaveLine} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearLeaveLineList() {
		if (getLeaveLineList() != null) {
			getLeaveLineList().clear();
		}
	}

	public Boolean getNegativeValueLeave() {
		return negativeValueLeave == null ? Boolean.FALSE : negativeValueLeave;
	}

	public void setNegativeValueLeave(Boolean negativeValueLeave) {
		this.negativeValueLeave = negativeValueLeave;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public EventsPlanning getPublicHolidayEventsPlanning() {
		return publicHolidayEventsPlanning;
	}

	public void setPublicHolidayEventsPlanning(EventsPlanning publicHolidayEventsPlanning) {
		this.publicHolidayEventsPlanning = publicHolidayEventsPlanning;
	}

	public List<EmploymentContract> getEmploymentContractList() {
		return employmentContractList;
	}

	public void setEmploymentContractList(List<EmploymentContract> employmentContractList) {
		this.employmentContractList = employmentContractList;
	}

	/**
	 * Add the given {@link EmploymentContract} item to the {@code employmentContractList}.
	 *
	 * <p>
	 * It sets {@code item.employee = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addEmploymentContractListItem(EmploymentContract item) {
		if (getEmploymentContractList() == null) {
			setEmploymentContractList(new ArrayList<>());
		}
		getEmploymentContractList().add(item);
		item.setEmployee(this);
	}

	/**
	 * Remove the given {@link EmploymentContract} item from the {@code employmentContractList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeEmploymentContractListItem(EmploymentContract item) {
		if (getEmploymentContractList() == null) {
			return;
		}
		getEmploymentContractList().remove(item);
	}

	/**
	 * Clear the {@code employmentContractList} collection.
	 *
	 * <p>
	 * If you have to query {@link EmploymentContract} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearEmploymentContractList() {
		if (getEmploymentContractList() != null) {
			getEmploymentContractList().clear();
		}
	}

	public Boolean getHrManager() {
		return hrManager == null ? Boolean.FALSE : hrManager;
	}

	public void setHrManager(Boolean hrManager) {
		this.hrManager = hrManager;
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

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public City getCityOfBirth() {
		return cityOfBirth;
	}

	public void setCityOfBirth(City cityOfBirth) {
		this.cityOfBirth = cityOfBirth;
	}

	public Citizenship getCitizenship() {
		return citizenship;
	}

	public void setCitizenship(Citizenship citizenship) {
		this.citizenship = citizenship;
	}

	/**
	 * For foreign-born employees, please enter the code 99
	 *
	 * @return the property value
	 */
	public Department getDepartmentOfBirth() {
		return departmentOfBirth;
	}

	public void setDepartmentOfBirth(Department departmentOfBirth) {
		this.departmentOfBirth = departmentOfBirth;
	}

	public Country getCountryOfBirth() {
		return countryOfBirth;
	}

	public void setCountryOfBirth(Country countryOfBirth) {
		this.countryOfBirth = countryOfBirth;
	}

	public String getSexSelect() {
		return sexSelect;
	}

	public void setSexSelect(String sexSelect) {
		this.sexSelect = sexSelect;
	}

	public String getFixedProPhone() {
		return fixedProPhone;
	}

	public void setFixedProPhone(String fixedProPhone) {
		this.fixedProPhone = fixedProPhone;
	}

	public String getMobileProPhone() {
		return mobileProPhone;
	}

	public void setMobileProPhone(String mobileProPhone) {
		this.mobileProPhone = mobileProPhone;
	}

	public String getPhoneAtCustomer() {
		return phoneAtCustomer;
	}

	public void setPhoneAtCustomer(String phoneAtCustomer) {
		this.phoneAtCustomer = phoneAtCustomer;
	}

	public String getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

	public String getEmergencyNumber() {
		return emergencyNumber;
	}

	public void setEmergencyNumber(String emergencyNumber) {
		this.emergencyNumber = emergencyNumber;
	}

	public String getEmergencyContactRelationship() {
		return emergencyContactRelationship;
	}

	public void setEmergencyContactRelationship(String emergencyContactRelationship) {
		this.emergencyContactRelationship = emergencyContactRelationship;
	}

	public LocalDate getHireDate() {
		return hireDate;
	}

	public void setHireDate(LocalDate hireDate) {
		this.hireDate = hireDate;
	}

	public LocalDate getSeniorityDate() {
		return seniorityDate;
	}

	public void setSeniorityDate(LocalDate seniorityDate) {
		this.seniorityDate = seniorityDate;
	}

	public LocalDate getLeavingDate() {
		return leavingDate;
	}

	public void setLeavingDate(LocalDate leavingDate) {
		this.leavingDate = leavingDate;
	}

	public EmploymentContract getMainEmploymentContract() {
		return mainEmploymentContract;
	}

	public void setMainEmploymentContract(EmploymentContract mainEmploymentContract) {
		this.mainEmploymentContract = mainEmploymentContract;
	}

	public Boolean getProfitSharingBeneficiary() {
		return profitSharingBeneficiary == null ? Boolean.FALSE : profitSharingBeneficiary;
	}

	public void setProfitSharingBeneficiary(Boolean profitSharingBeneficiary) {
		this.profitSharingBeneficiary = profitSharingBeneficiary;
	}

	public EventsPlanning getImposedDayEventsPlanning() {
		return imposedDayEventsPlanning;
	}

	public void setImposedDayEventsPlanning(EventsPlanning imposedDayEventsPlanning) {
		this.imposedDayEventsPlanning = imposedDayEventsPlanning;
	}

	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}

	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}

	public List<EmployeeAdvance> getEmployeeAdvanceList() {
		return employeeAdvanceList;
	}

	public void setEmployeeAdvanceList(List<EmployeeAdvance> employeeAdvanceList) {
		this.employeeAdvanceList = employeeAdvanceList;
	}

	/**
	 * Add the given {@link EmployeeAdvance} item to the {@code employeeAdvanceList}.
	 *
	 * <p>
	 * It sets {@code item.employee = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addEmployeeAdvanceListItem(EmployeeAdvance item) {
		if (getEmployeeAdvanceList() == null) {
			setEmployeeAdvanceList(new ArrayList<>());
		}
		getEmployeeAdvanceList().add(item);
		item.setEmployee(this);
	}

	/**
	 * Remove the given {@link EmployeeAdvance} item from the {@code employeeAdvanceList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeEmployeeAdvanceListItem(EmployeeAdvance item) {
		if (getEmployeeAdvanceList() == null) {
			return;
		}
		getEmployeeAdvanceList().remove(item);
	}

	/**
	 * Clear the {@code employeeAdvanceList} collection.
	 *
	 * <p>
	 * If you have to query {@link EmployeeAdvance} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearEmployeeAdvanceList() {
		if (getEmployeeAdvanceList() != null) {
			getEmployeeAdvanceList().clear();
		}
	}

	public Integer getLunchVoucherFormatSelect() {
		return lunchVoucherFormatSelect == null ? 0 : lunchVoucherFormatSelect;
	}

	public void setLunchVoucherFormatSelect(Integer lunchVoucherFormatSelect) {
		this.lunchVoucherFormatSelect = lunchVoucherFormatSelect;
	}

	public List<LunchVoucherAdvance> getLunchVoucherAdvanceList() {
		return lunchVoucherAdvanceList;
	}

	public void setLunchVoucherAdvanceList(List<LunchVoucherAdvance> lunchVoucherAdvanceList) {
		this.lunchVoucherAdvanceList = lunchVoucherAdvanceList;
	}

	/**
	 * Add the given {@link LunchVoucherAdvance} item to the {@code lunchVoucherAdvanceList}.
	 *
	 * <p>
	 * It sets {@code item.employee = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addLunchVoucherAdvanceListItem(LunchVoucherAdvance item) {
		if (getLunchVoucherAdvanceList() == null) {
			setLunchVoucherAdvanceList(new ArrayList<>());
		}
		getLunchVoucherAdvanceList().add(item);
		item.setEmployee(this);
	}

	/**
	 * Remove the given {@link LunchVoucherAdvance} item from the {@code lunchVoucherAdvanceList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeLunchVoucherAdvanceListItem(LunchVoucherAdvance item) {
		if (getLunchVoucherAdvanceList() == null) {
			return;
		}
		getLunchVoucherAdvanceList().remove(item);
	}

	/**
	 * Clear the {@code lunchVoucherAdvanceList} collection.
	 *
	 * <p>
	 * If you have to query {@link LunchVoucherAdvance} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearLunchVoucherAdvanceList() {
		if (getLunchVoucherAdvanceList() != null) {
			getLunchVoucherAdvanceList().clear();
		}
	}

	public List<KilometricLog> getKilometricLogList() {
		return kilometricLogList;
	}

	public void setKilometricLogList(List<KilometricLog> kilometricLogList) {
		this.kilometricLogList = kilometricLogList;
	}

	/**
	 * Add the given {@link KilometricLog} item to the {@code kilometricLogList}.
	 *
	 * <p>
	 * It sets {@code item.employee = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addKilometricLogListItem(KilometricLog item) {
		if (getKilometricLogList() == null) {
			setKilometricLogList(new ArrayList<>());
		}
		getKilometricLogList().add(item);
		item.setEmployee(this);
	}

	/**
	 * Remove the given {@link KilometricLog} item from the {@code kilometricLogList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeKilometricLogListItem(KilometricLog item) {
		if (getKilometricLogList() == null) {
			return;
		}
		getKilometricLogList().remove(item);
	}

	/**
	 * Clear the {@code kilometricLogList} collection.
	 *
	 * <p>
	 * If you have to query {@link KilometricLog} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearKilometricLogList() {
		if (getKilometricLogList() != null) {
			getKilometricLogList().clear();
		}
	}

	public BigDecimal getBonusCoef() {
		return bonusCoef == null ? BigDecimal.ZERO : bonusCoef;
	}

	public void setBonusCoef(BigDecimal bonusCoef) {
		this.bonusCoef = bonusCoef;
	}

	public String getExportCode() {
		return exportCode;
	}

	public void setExportCode(String exportCode) {
		this.exportCode = exportCode;
	}

	public List<EmployeeVehicle> getEmployeeVehicleList() {
		return employeeVehicleList;
	}

	public void setEmployeeVehicleList(List<EmployeeVehicle> employeeVehicleList) {
		this.employeeVehicleList = employeeVehicleList;
	}

	/**
	 * Add the given {@link EmployeeVehicle} item to the {@code employeeVehicleList}.
	 *
	 * <p>
	 * It sets {@code item.employee = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addEmployeeVehicleListItem(EmployeeVehicle item) {
		if (getEmployeeVehicleList() == null) {
			setEmployeeVehicleList(new ArrayList<>());
		}
		getEmployeeVehicleList().add(item);
		item.setEmployee(this);
	}

	/**
	 * Remove the given {@link EmployeeVehicle} item from the {@code employeeVehicleList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeEmployeeVehicleListItem(EmployeeVehicle item) {
		if (getEmployeeVehicleList() == null) {
			return;
		}
		getEmployeeVehicleList().remove(item);
	}

	/**
	 * Clear the {@code employeeVehicleList} collection.
	 *
	 * <p>
	 * If you have to query {@link EmployeeVehicle} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearEmployeeVehicleList() {
		if (getEmployeeVehicleList() != null) {
			getEmployeeVehicleList().clear();
		}
	}

	public Integer getCompanyCbSelect() {
		return companyCbSelect == null ? 0 : companyCbSelect;
	}

	public void setCompanyCbSelect(Integer companyCbSelect) {
		this.companyCbSelect = companyCbSelect;
	}

	public String getCompanyCbDetails() {
		return companyCbDetails;
	}

	public void setCompanyCbDetails(String companyCbDetails) {
		this.companyCbDetails = companyCbDetails;
	}

	public String getName() {
		try {
			name = computeName();
		} catch (NullPointerException e) {
			Logger logger = LoggerFactory.getLogger(getClass());
			logger.error("NPE in function field: getName()", e);
		}
		return name;
	}

	protected String computeName() {
		if(contactPartner != null && contactPartner.getFullName() != null) { return contactPartner.getFullName(); }
		return "";
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStepByStepSelect() {
		return stepByStepSelect == null ? 0 : stepByStepSelect;
	}

	public void setStepByStepSelect(Integer stepByStepSelect) {
		this.stepByStepSelect = stepByStepSelect;
	}

	public List<DPAE> getDpaeList() {
		return dpaeList;
	}

	public void setDpaeList(List<DPAE> dpaeList) {
		this.dpaeList = dpaeList;
	}

	/**
	 * Add the given {@link DPAE} item to the {@code dpaeList}.
	 *
	 * <p>
	 * It sets {@code item.employee = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addDpaeListItem(DPAE item) {
		if (getDpaeList() == null) {
			setDpaeList(new ArrayList<>());
		}
		getDpaeList().add(item);
		item.setEmployee(this);
	}

	/**
	 * Remove the given {@link DPAE} item from the {@code dpaeList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeDpaeListItem(DPAE item) {
		if (getDpaeList() == null) {
			return;
		}
		getDpaeList().remove(item);
	}

	/**
	 * Clear the {@code dpaeList} collection.
	 *
	 * <p>
	 * If you have to query {@link DPAE} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearDpaeList() {
		if (getDpaeList() != null) {
			getDpaeList().clear();
		}
	}

	public List<EmployeeFile> getEmployeeFileList() {
		return employeeFileList;
	}

	public void setEmployeeFileList(List<EmployeeFile> employeeFileList) {
		this.employeeFileList = employeeFileList;
	}

	/**
	 * Add the given {@link EmployeeFile} item to the {@code employeeFileList}.
	 *
	 * <p>
	 * It sets {@code item.employee = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addEmployeeFileListItem(EmployeeFile item) {
		if (getEmployeeFileList() == null) {
			setEmployeeFileList(new ArrayList<>());
		}
		getEmployeeFileList().add(item);
		item.setEmployee(this);
	}

	/**
	 * Remove the given {@link EmployeeFile} item from the {@code employeeFileList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeEmployeeFileListItem(EmployeeFile item) {
		if (getEmployeeFileList() == null) {
			return;
		}
		getEmployeeFileList().remove(item);
	}

	/**
	 * Clear the {@code employeeFileList} collection.
	 *
	 * <p>
	 * If you have to query {@link EmployeeFile} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearEmployeeFileList() {
		if (getEmployeeFileList() != null) {
			getEmployeeFileList().clear();
		}
	}

	public String getMaidenName() {
		return maidenName;
	}

	public void setMaidenName(String maidenName) {
		this.maidenName = maidenName;
	}

	public String getMaritalName() {
		return maritalName;
	}

	public void setMaritalName(String maritalName) {
		this.maritalName = maritalName;
	}

	public Integer getTimesheetImputationSelect() {
		return timesheetImputationSelect == null ? 0 : timesheetImputationSelect;
	}

	public void setTimesheetImputationSelect(Integer timesheetImputationSelect) {
		this.timesheetImputationSelect = timesheetImputationSelect;
	}

	public BankDetails getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(BankDetails bankDetails) {
		this.bankDetails = bankDetails;
	}

	public Set<Skill> getTrainingSkillSet() {
		return trainingSkillSet;
	}

	public void setTrainingSkillSet(Set<Skill> trainingSkillSet) {
		this.trainingSkillSet = trainingSkillSet;
	}

	/**
	 * Add the given {@link Skill} item to the {@code trainingSkillSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addTrainingSkillSetItem(Skill item) {
		if (getTrainingSkillSet() == null) {
			setTrainingSkillSet(new HashSet<>());
		}
		getTrainingSkillSet().add(item);
	}

	/**
	 * Remove the given {@link Skill} item from the {@code trainingSkillSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeTrainingSkillSetItem(Skill item) {
		if (getTrainingSkillSet() == null) {
			return;
		}
		getTrainingSkillSet().remove(item);
	}

	/**
	 * Clear the {@code trainingSkillSet} collection.
	 *
	 */
	public void clearTrainingSkillSet() {
		if (getTrainingSkillSet() != null) {
			getTrainingSkillSet().clear();
		}
	}

	public Set<Skill> getExperienceSkillSet() {
		return experienceSkillSet;
	}

	public void setExperienceSkillSet(Set<Skill> experienceSkillSet) {
		this.experienceSkillSet = experienceSkillSet;
	}

	/**
	 * Add the given {@link Skill} item to the {@code experienceSkillSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addExperienceSkillSetItem(Skill item) {
		if (getExperienceSkillSet() == null) {
			setExperienceSkillSet(new HashSet<>());
		}
		getExperienceSkillSet().add(item);
	}

	/**
	 * Remove the given {@link Skill} item from the {@code experienceSkillSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeExperienceSkillSetItem(Skill item) {
		if (getExperienceSkillSet() == null) {
			return;
		}
		getExperienceSkillSet().remove(item);
	}

	/**
	 * Clear the {@code experienceSkillSet} collection.
	 *
	 */
	public void clearExperienceSkillSet() {
		if (getExperienceSkillSet() != null) {
			getExperienceSkillSet().clear();
		}
	}

	public Set<Skill> getSkillSet() {
		return skillSet;
	}

	public void setSkillSet(Set<Skill> skillSet) {
		this.skillSet = skillSet;
	}

	/**
	 * Add the given {@link Skill} item to the {@code skillSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addSkillSetItem(Skill item) {
		if (getSkillSet() == null) {
			setSkillSet(new HashSet<>());
		}
		getSkillSet().add(item);
	}

	/**
	 * Remove the given {@link Skill} item from the {@code skillSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeSkillSetItem(Skill item) {
		if (getSkillSet() == null) {
			return;
		}
		getSkillSet().remove(item);
	}

	/**
	 * Clear the {@code skillSet} collection.
	 *
	 */
	public void clearSkillSet() {
		if (getSkillSet() != null) {
			getSkillSet().clear();
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
		if (!(obj instanceof Employee)) return false;

		final Employee other = (Employee) obj;
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
			.add("hourlyRate", getHourlyRate())
			.add("timeLoggingPreferenceSelect", getTimeLoggingPreferenceSelect())
			.add("weeklyWorkHours", getWeeklyWorkHours())
			.add("dailyWorkHours", getDailyWorkHours())
			.add("timesheetReminder", getTimesheetReminder())
			.add("external", getExternal())
			.add("negativeValueLeave", getNegativeValueLeave())
			.add("hrManager", getHrManager())
			.add("birthDate", getBirthDate())
			.add("maritalStatus", getMaritalStatus())
			.add("sexSelect", getSexSelect())
			.omitNullValues()
			.toString();
	}
}
