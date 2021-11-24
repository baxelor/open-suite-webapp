package com.axelor.apps.project.db;

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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.axelor.apps.base.db.Product;
import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "PROJECT_PROJECT_TASK_CATEGORY", indexes = { @Index(columnList = "name"), @Index(columnList = "default_product") })
public class ProjectTaskCategory extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_PROJECT_TASK_CATEGORY_SEQ")
	@SequenceGenerator(name = "PROJECT_PROJECT_TASK_CATEGORY_SEQ", sequenceName = "PROJECT_PROJECT_TASK_CATEGORY_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Name")
	@NotNull
	private String name;

	@Widget(title = "Default product")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Product defaultProduct;

	@Widget(title = "Default value invoicing type", selection = "business.project.task.invoicing.type.select")
	private Integer defaultInvoicingType = 0;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public ProjectTaskCategory() {
	}

	public ProjectTaskCategory(String name) {
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

	public Product getDefaultProduct() {
		return defaultProduct;
	}

	public void setDefaultProduct(Product defaultProduct) {
		this.defaultProduct = defaultProduct;
	}

	public Integer getDefaultInvoicingType() {
		return defaultInvoicingType == null ? 0 : defaultInvoicingType;
	}

	public void setDefaultInvoicingType(Integer defaultInvoicingType) {
		this.defaultInvoicingType = defaultInvoicingType;
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
		if (!(obj instanceof ProjectTaskCategory)) return false;

		final ProjectTaskCategory other = (ProjectTaskCategory) obj;
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
			.add("defaultInvoicingType", getDefaultInvoicingType())
			.omitNullValues()
			.toString();
	}
}
