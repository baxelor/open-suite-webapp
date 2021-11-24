package com.axelor.apps.supplychain.db;

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

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "SUPPLYCHAIN_MRP_LINE_ORIGIN", indexes = { @Index(columnList = "mrp_line") })
public class MrpLineOrigin extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUPPLYCHAIN_MRP_LINE_ORIGIN_SEQ")
	@SequenceGenerator(name = "SUPPLYCHAIN_MRP_LINE_ORIGIN_SEQ", sequenceName = "SUPPLYCHAIN_MRP_LINE_ORIGIN_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Mrp line")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private MrpLine mrpLine;

	@Widget(title = "Related to", selection = "supplychain.mrp.line.related.to.select")
	private String relatedToSelect;

	private Long relatedToSelectId = 0L;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public MrpLineOrigin() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public MrpLine getMrpLine() {
		return mrpLine;
	}

	public void setMrpLine(MrpLine mrpLine) {
		this.mrpLine = mrpLine;
	}

	public String getRelatedToSelect() {
		return relatedToSelect;
	}

	public void setRelatedToSelect(String relatedToSelect) {
		this.relatedToSelect = relatedToSelect;
	}

	public Long getRelatedToSelectId() {
		return relatedToSelectId == null ? 0L : relatedToSelectId;
	}

	public void setRelatedToSelectId(Long relatedToSelectId) {
		this.relatedToSelectId = relatedToSelectId;
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
		if (!(obj instanceof MrpLineOrigin)) return false;

		final MrpLineOrigin other = (MrpLineOrigin) obj;
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
			.add("relatedToSelect", getRelatedToSelect())
			.add("relatedToSelectId", getRelatedToSelectId())
			.omitNullValues()
			.toString();
	}
}
