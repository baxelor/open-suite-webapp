package com.axelor.apps.production.db;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.axelor.apps.base.db.Product;
import com.axelor.apps.hr.db.Employee;
import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "PRODUCTION_PROD_HUMAN_RESOURCE", indexes = { @Index(columnList = "work_center"), @Index(columnList = "product"), @Index(columnList = "cost_sheet_group"), @Index(columnList = "operation_order"), @Index(columnList = "employee") })
public class ProdHumanResource extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCTION_PROD_HUMAN_RESOURCE_SEQ")
	@SequenceGenerator(name = "PRODUCTION_PROD_HUMAN_RESOURCE_SEQ", sequenceName = "PRODUCTION_PROD_HUMAN_RESOURCE_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Work center")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private WorkCenter workCenter;

	@Widget(title = "Profile")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Product product;

	@Widget(title = "Duration")
	private Long duration = 0L;

	@Widget(title = "Cost sheet group")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private CostSheetGroup costSheetGroup;

	@Widget(title = "Operation order")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private OperationOrder operationOrder;

	@Widget(title = "Employee")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Employee employee;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public ProdHumanResource() {
	}

	public ProdHumanResource(Product product, Long duration) {
		this.product = product;
		this.duration = duration;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public WorkCenter getWorkCenter() {
		return workCenter;
	}

	public void setWorkCenter(WorkCenter workCenter) {
		this.workCenter = workCenter;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Long getDuration() {
		return duration == null ? 0L : duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public CostSheetGroup getCostSheetGroup() {
		return costSheetGroup;
	}

	public void setCostSheetGroup(CostSheetGroup costSheetGroup) {
		this.costSheetGroup = costSheetGroup;
	}

	public OperationOrder getOperationOrder() {
		return operationOrder;
	}

	public void setOperationOrder(OperationOrder operationOrder) {
		this.operationOrder = operationOrder;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
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
		if (!(obj instanceof ProdHumanResource)) return false;

		final ProdHumanResource other = (ProdHumanResource) obj;
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
			.add("duration", getDuration())
			.omitNullValues()
			.toString();
	}
}
