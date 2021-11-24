package com.axelor.apps.production.db;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axelor.apps.base.db.Partner;
import com.axelor.apps.project.db.Project;
import com.axelor.apps.sale.db.SaleOrder;
import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.EqualsInclude;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.VirtualColumn;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "PRODUCTION_PRODUCTION_ORDER", indexes = { @Index(columnList = "client_partner"), @Index(columnList = "sale_order"), @Index(columnList = "project") })
public class ProductionOrder extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCTION_PRODUCTION_ORDER_SEQ")
	@SequenceGenerator(name = "PRODUCTION_PRODUCTION_ORDER_SEQ", sequenceName = "PRODUCTION_PRODUCTION_ORDER_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Priority", selection = "production.order.priority.select")
	private Integer prioritySelect = 2;

	@EqualsInclude
	@Widget(title = "Name")
	@NameColumn
	@NotNull
	@Column(unique = true)
	private String productionOrderSeq;

	@Widget(title = "Customer")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Partner clientPartner;

	@Widget(title = "Sale order")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private SaleOrder saleOrder;

	@Widget(title = "Manufacturing orders")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@OrderBy("prioritySelect")
	private Set<ManufOrder> manufOrderSet;

	@Widget(title = "Status", selection = "production.order.status.select")
	private Integer statusSelect = 1;

	@VirtualColumn
	@Access(AccessType.PROPERTY)
	private Boolean isClosed = Boolean.FALSE;

	@Widget(title = "Project")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Project project;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public ProductionOrder() {
	}

	public ProductionOrder(String productionOrderSeq) {
		this.productionOrderSeq = productionOrderSeq;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPrioritySelect() {
		return prioritySelect == null ? 0 : prioritySelect;
	}

	public void setPrioritySelect(Integer prioritySelect) {
		this.prioritySelect = prioritySelect;
	}

	public String getProductionOrderSeq() {
		return productionOrderSeq;
	}

	public void setProductionOrderSeq(String productionOrderSeq) {
		this.productionOrderSeq = productionOrderSeq;
	}

	public Partner getClientPartner() {
		return clientPartner;
	}

	public void setClientPartner(Partner clientPartner) {
		this.clientPartner = clientPartner;
	}

	public SaleOrder getSaleOrder() {
		return saleOrder;
	}

	public void setSaleOrder(SaleOrder saleOrder) {
		this.saleOrder = saleOrder;
	}

	public Set<ManufOrder> getManufOrderSet() {
		return manufOrderSet;
	}

	public void setManufOrderSet(Set<ManufOrder> manufOrderSet) {
		this.manufOrderSet = manufOrderSet;
	}

	/**
	 * Add the given {@link ManufOrder} item to the {@code manufOrderSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addManufOrderSetItem(ManufOrder item) {
		if (getManufOrderSet() == null) {
			setManufOrderSet(new HashSet<>());
		}
		getManufOrderSet().add(item);
	}

	/**
	 * Remove the given {@link ManufOrder} item from the {@code manufOrderSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeManufOrderSetItem(ManufOrder item) {
		if (getManufOrderSet() == null) {
			return;
		}
		getManufOrderSet().remove(item);
	}

	/**
	 * Clear the {@code manufOrderSet} collection.
	 *
	 */
	public void clearManufOrderSet() {
		if (getManufOrderSet() != null) {
			getManufOrderSet().clear();
		}
	}

	public Integer getStatusSelect() {
		return statusSelect == null ? 0 : statusSelect;
	}

	public void setStatusSelect(Integer statusSelect) {
		this.statusSelect = statusSelect;
	}

	public Boolean getIsClosed() {
		try {
			isClosed = computeIsClosed();
		} catch (NullPointerException e) {
			Logger logger = LoggerFactory.getLogger(getClass());
			logger.error("NPE in function field: getIsClosed()", e);
		}
		return isClosed;
	}

	protected Boolean computeIsClosed() {
		if(manufOrderSet == null || manufOrderSet.isEmpty())
		  	return false;
		  	for(ManufOrder mf : manufOrderSet){
		  		if(mf.getStatusSelect() != 2 && mf.getStatusSelect() != 6 )
		  		   return false;
		}
		return true;
	}

	public void setIsClosed(Boolean isClosed) {
		this.isClosed = isClosed;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
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
		if (!(obj instanceof ProductionOrder)) return false;

		final ProductionOrder other = (ProductionOrder) obj;
		if (this.getId() != null || other.getId() != null) {
			return Objects.equals(this.getId(), other.getId());
		}

		return Objects.equals(getProductionOrderSeq(), other.getProductionOrderSeq())
			&& (getProductionOrderSeq() != null);
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("id", getId())
			.add("prioritySelect", getPrioritySelect())
			.add("productionOrderSeq", getProductionOrderSeq())
			.add("statusSelect", getStatusSelect())
			.omitNullValues()
			.toString();
	}
}
