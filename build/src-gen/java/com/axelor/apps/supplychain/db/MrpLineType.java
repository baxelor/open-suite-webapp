package com.axelor.apps.supplychain.db;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "SUPPLYCHAIN_MRP_LINE_TYPE", indexes = { @Index(columnList = "code"), @Index(columnList = "name") })
public class MrpLineType extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUPPLYCHAIN_MRP_LINE_TYPE_SEQ")
	@SequenceGenerator(name = "SUPPLYCHAIN_MRP_LINE_TYPE_SEQ", sequenceName = "SUPPLYCHAIN_MRP_LINE_TYPE_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Code")
	private String code;

	@Widget(title = "Label")
	private String label;

	@Widget(title = "Name")
	private String name;

	@Widget(title = "Element", selection = "supplychain.mrp.line.element.select")
	private Integer elementSelect = 0;

	@Widget(title = "Type", selection = "supplychain.mrp.line.type.select")
	private Integer typeSelect = 0;

	@Widget(title = "Sequence to order MRP results")
	private Integer sequence = 0;

	@Widget(title = "Statuses to take into account")
	private String statusSelect;

	@Widget(title = "Include element without date", help = "Purchase order or sale order without estimated delivery date and manufacturing order without planned date. In this case, we use the MRP start date (today)")
	private Boolean includeElementWithoutDate = Boolean.FALSE;

	@Widget(title = "Ignore end date", help = "Include elements with a date later than MRP end date.")
	private Boolean ignoreEndDate = Boolean.FALSE;

	@Widget(title = "Field of application", selection = "supplychain.mrp.line.type.application.field.select")
	private String applicationFieldSelect;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public MrpLineType() {
	}

	public MrpLineType(String code, String name) {
		this.code = code;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getElementSelect() {
		return elementSelect == null ? 0 : elementSelect;
	}

	public void setElementSelect(Integer elementSelect) {
		this.elementSelect = elementSelect;
	}

	public Integer getTypeSelect() {
		return typeSelect == null ? 0 : typeSelect;
	}

	public void setTypeSelect(Integer typeSelect) {
		this.typeSelect = typeSelect;
	}

	public Integer getSequence() {
		return sequence == null ? 0 : sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getStatusSelect() {
		return statusSelect;
	}

	public void setStatusSelect(String statusSelect) {
		this.statusSelect = statusSelect;
	}

	/**
	 * Purchase order or sale order without estimated delivery date and manufacturing order without planned date. In this case, we use the MRP start date (today)
	 *
	 * @return the property value
	 */
	public Boolean getIncludeElementWithoutDate() {
		return includeElementWithoutDate == null ? Boolean.FALSE : includeElementWithoutDate;
	}

	public void setIncludeElementWithoutDate(Boolean includeElementWithoutDate) {
		this.includeElementWithoutDate = includeElementWithoutDate;
	}

	/**
	 * Include elements with a date later than MRP end date.
	 *
	 * @return the property value
	 */
	public Boolean getIgnoreEndDate() {
		return ignoreEndDate == null ? Boolean.FALSE : ignoreEndDate;
	}

	public void setIgnoreEndDate(Boolean ignoreEndDate) {
		this.ignoreEndDate = ignoreEndDate;
	}

	public String getApplicationFieldSelect() {
		return applicationFieldSelect;
	}

	public void setApplicationFieldSelect(String applicationFieldSelect) {
		this.applicationFieldSelect = applicationFieldSelect;
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
		if (!(obj instanceof MrpLineType)) return false;

		final MrpLineType other = (MrpLineType) obj;
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
			.add("code", getCode())
			.add("label", getLabel())
			.add("name", getName())
			.add("elementSelect", getElementSelect())
			.add("typeSelect", getTypeSelect())
			.add("sequence", getSequence())
			.add("statusSelect", getStatusSelect())
			.add("includeElementWithoutDate", getIncludeElementWithoutDate())
			.add("ignoreEndDate", getIgnoreEndDate())
			.add("applicationFieldSelect", getApplicationFieldSelect())
			.omitNullValues()
			.toString();
	}
}
