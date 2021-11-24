package com.axelor.dms.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.axelor.apps.project.db.Project;
import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.EqualsInclude;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "DMS_FILE_TAG")
public class DMSFileTag extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DMS_FILE_TAG_SEQ")
	@SequenceGenerator(name = "DMS_FILE_TAG_SEQ", sequenceName = "DMS_FILE_TAG_SEQ", allocationSize = 1)
	private Long id;

	@EqualsInclude
	@NotNull
	@Size(min = 2)
	@Column(unique = true)
	private String code;

	@EqualsInclude
	@NameColumn
	@NotNull
	@Size(min = 2)
	@Column(unique = true)
	private String name;

	@Widget(selection = "dms.tag.style.selection")
	private String style;

	@Widget(title = "Tags")
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<DMSFileTag> tagList;

	@Widget(title = "Projects")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Project> projectSet;

	public DMSFileTag() {
	}

	public DMSFileTag(String code, String name) {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public List<DMSFileTag> getTagList() {
		return tagList;
	}

	public void setTagList(List<DMSFileTag> tagList) {
		this.tagList = tagList;
	}

	/**
	 * Add the given {@link DMSFileTag} item to the {@code tagList}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addTagListItem(DMSFileTag item) {
		if (getTagList() == null) {
			setTagList(new ArrayList<>());
		}
		getTagList().add(item);
	}

	/**
	 * Remove the given {@link DMSFileTag} item from the {@code tagList}.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeTagListItem(DMSFileTag item) {
		if (getTagList() == null) {
			return;
		}
		getTagList().remove(item);
	}

	/**
	 * Clear the {@code tagList} collection.
	 *
	 * <p>
	 * It sets {@code item.null = null} to break the relationship.
	 * </p>
	 */
	public void clearTagList() {
		if (getTagList() != null) {
			getTagList().clear();
		}
	}

	public Set<Project> getProjectSet() {
		return projectSet;
	}

	public void setProjectSet(Set<Project> projectSet) {
		this.projectSet = projectSet;
	}

	/**
	 * Add the given {@link Project} item to the {@code projectSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addProjectSetItem(Project item) {
		if (getProjectSet() == null) {
			setProjectSet(new HashSet<>());
		}
		getProjectSet().add(item);
	}

	/**
	 * Remove the given {@link Project} item from the {@code projectSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeProjectSetItem(Project item) {
		if (getProjectSet() == null) {
			return;
		}
		getProjectSet().remove(item);
	}

	/**
	 * Clear the {@code projectSet} collection.
	 *
	 */
	public void clearProjectSet() {
		if (getProjectSet() != null) {
			getProjectSet().clear();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof DMSFileTag)) return false;

		final DMSFileTag other = (DMSFileTag) obj;
		if (this.getId() != null || other.getId() != null) {
			return Objects.equals(this.getId(), other.getId());
		}

		return Objects.equals(getCode(), other.getCode())
			&& Objects.equals(getName(), other.getName())
			&& (getCode() != null
				|| getName() != null);
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
			.add("name", getName())
			.add("style", getStyle())
			.omitNullValues()
			.toString();
	}
}
