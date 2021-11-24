package com.axelor.apps.base.db;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
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
@Table(name = "BASE_APP_PROJECT", indexes = { @Index(columnList = "app") })
@Track(fields = { @TrackField(name = "projectLabel", on = TrackEvent.UPDATE), @TrackField(name = "resourceManagement", on = TrackEvent.UPDATE) })
public class AppProject extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BASE_APP_PROJECT_SEQ")
	@SequenceGenerator(name = "BASE_APP_PROJECT_SEQ", sequenceName = "BASE_APP_PROJECT_SEQ", allocationSize = 1)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private App app;

	@Widget(title = "Project Label")
	private String projectLabel;

	@Widget(title = "Resource management")
	private Boolean resourceManagement = Boolean.FALSE;

	@Widget(title = "Check availability of resources")
	private Boolean checkResourceAvailibility = Boolean.FALSE;

	@Widget(title = "Enable task signature")
	private Boolean isEnableSignature = Boolean.FALSE;

	@Widget(title = "Generate sequence for project")
	private Boolean generateProjectSequence = Boolean.FALSE;

	@Widget(title = "Default increment")
	private BigDecimal defaultIncrement = BigDecimal.ZERO;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public AppProject() {
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

	public String getProjectLabel() {
		return projectLabel;
	}

	public void setProjectLabel(String projectLabel) {
		this.projectLabel = projectLabel;
	}

	public Boolean getResourceManagement() {
		return resourceManagement == null ? Boolean.FALSE : resourceManagement;
	}

	public void setResourceManagement(Boolean resourceManagement) {
		this.resourceManagement = resourceManagement;
	}

	public Boolean getCheckResourceAvailibility() {
		return checkResourceAvailibility == null ? Boolean.FALSE : checkResourceAvailibility;
	}

	public void setCheckResourceAvailibility(Boolean checkResourceAvailibility) {
		this.checkResourceAvailibility = checkResourceAvailibility;
	}

	public Boolean getIsEnableSignature() {
		return isEnableSignature == null ? Boolean.FALSE : isEnableSignature;
	}

	public void setIsEnableSignature(Boolean isEnableSignature) {
		this.isEnableSignature = isEnableSignature;
	}

	public Boolean getGenerateProjectSequence() {
		return generateProjectSequence == null ? Boolean.FALSE : generateProjectSequence;
	}

	public void setGenerateProjectSequence(Boolean generateProjectSequence) {
		this.generateProjectSequence = generateProjectSequence;
	}

	public BigDecimal getDefaultIncrement() {
		return defaultIncrement == null ? BigDecimal.ZERO : defaultIncrement;
	}

	public void setDefaultIncrement(BigDecimal defaultIncrement) {
		this.defaultIncrement = defaultIncrement;
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
		if (!(obj instanceof AppProject)) return false;

		final AppProject other = (AppProject) obj;
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
			.add("projectLabel", getProjectLabel())
			.add("resourceManagement", getResourceManagement())
			.add("checkResourceAvailibility", getCheckResourceAvailibility())
			.add("isEnableSignature", getIsEnableSignature())
			.add("generateProjectSequence", getGenerateProjectSequence())
			.add("defaultIncrement", getDefaultIncrement())
			.omitNullValues()
			.toString();
	}
}
