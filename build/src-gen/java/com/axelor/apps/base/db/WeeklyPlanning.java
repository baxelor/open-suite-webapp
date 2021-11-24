package com.axelor.apps.base.db;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Cacheable
@Table(name = "BASE_WEEKLY_PLANNING", indexes = { @Index(columnList = "name") })
public class WeeklyPlanning extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BASE_WEEKLY_PLANNING_SEQ")
	@SequenceGenerator(name = "BASE_WEEKLY_PLANNING_SEQ", sequenceName = "BASE_WEEKLY_PLANNING_SEQ", allocationSize = 1)
	private Long id;

	@NameColumn
	@NotNull
	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "weeklyPlann", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DayPlanning> weekDays;

	@Widget(title = "Coefficient for bonus")
	private BigDecimal bonusCoef = BigDecimal.ZERO;

	@Widget(title = "Coefficient for leave")
	private BigDecimal leaveCoef = new BigDecimal("1");

	@Widget(title = "Type", selection = "production.weekly.planning.type.select")
	private Integer typeSelect = 1;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public WeeklyPlanning() {
	}

	public WeeklyPlanning(String name) {
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

	public List<DayPlanning> getWeekDays() {
		return weekDays;
	}

	public void setWeekDays(List<DayPlanning> weekDays) {
		this.weekDays = weekDays;
	}

	/**
	 * Add the given {@link DayPlanning} item to the {@code weekDays}.
	 *
	 * <p>
	 * It sets {@code item.weeklyPlann = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addWeekDay(DayPlanning item) {
		if (getWeekDays() == null) {
			setWeekDays(new ArrayList<>());
		}
		getWeekDays().add(item);
		item.setWeeklyPlann(this);
	}

	/**
	 * Remove the given {@link DayPlanning} item from the {@code weekDays}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeWeekDay(DayPlanning item) {
		if (getWeekDays() == null) {
			return;
		}
		getWeekDays().remove(item);
	}

	/**
	 * Clear the {@code weekDays} collection.
	 *
	 * <p>
	 * If you have to query {@link DayPlanning} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearWeekDays() {
		if (getWeekDays() != null) {
			getWeekDays().clear();
		}
	}

	public BigDecimal getBonusCoef() {
		return bonusCoef == null ? BigDecimal.ZERO : bonusCoef;
	}

	public void setBonusCoef(BigDecimal bonusCoef) {
		this.bonusCoef = bonusCoef;
	}

	public BigDecimal getLeaveCoef() {
		return leaveCoef == null ? BigDecimal.ZERO : leaveCoef;
	}

	public void setLeaveCoef(BigDecimal leaveCoef) {
		this.leaveCoef = leaveCoef;
	}

	public Integer getTypeSelect() {
		return typeSelect == null ? 0 : typeSelect;
	}

	public void setTypeSelect(Integer typeSelect) {
		this.typeSelect = typeSelect;
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
		if (!(obj instanceof WeeklyPlanning)) return false;

		final WeeklyPlanning other = (WeeklyPlanning) obj;
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
			.add("bonusCoef", getBonusCoef())
			.add("leaveCoef", getLeaveCoef())
			.add("typeSelect", getTypeSelect())
			.omitNullValues()
			.toString();
	}
}
