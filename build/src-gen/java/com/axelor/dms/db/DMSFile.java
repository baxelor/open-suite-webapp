package com.axelor.dms.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.auth.db.User;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.Widget;
import com.axelor.meta.db.MetaFile;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "DMS_FILE", indexes = { @Index(columnList = "fileName"), @Index(columnList = "parent"), @Index(columnList = "meta_file"), @Index(columnList = "locked_by") })
public class DMSFile extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DMS_FILE_SEQ")
	@SequenceGenerator(name = "DMS_FILE_SEQ", sequenceName = "DMS_FILE_SEQ", allocationSize = 1)
	private Long id;

	@NameColumn
	@NotNull
	private String fileName;

	private Boolean isDirectory = Boolean.FALSE;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private DMSFile parent;

	@Widget(hidden = true)
	private Long relatedId = 0L;

	@Widget(title = "Related object", hidden = true)
	private String relatedModel;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private MetaFile metaFile;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DMSPermission> permissions;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@OrderBy("name")
	private Set<DMSFileTag> tags;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String content;

	private String contentType;

	@Widget(title = "Lock")
	private Boolean isLock = Boolean.FALSE;

	@Widget(title = "Locked by")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private User lockedBy;

	@Widget(title = "Favourite users")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<User> favouriteUserSet;

	public DMSFile() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Boolean getIsDirectory() {
		return isDirectory == null ? Boolean.FALSE : isDirectory;
	}

	public void setIsDirectory(Boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	public DMSFile getParent() {
		return parent;
	}

	public void setParent(DMSFile parent) {
		this.parent = parent;
	}

	public Long getRelatedId() {
		return relatedId == null ? 0L : relatedId;
	}

	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}

	public String getRelatedModel() {
		return relatedModel;
	}

	public void setRelatedModel(String relatedModel) {
		this.relatedModel = relatedModel;
	}

	public MetaFile getMetaFile() {
		return metaFile;
	}

	public void setMetaFile(MetaFile metaFile) {
		this.metaFile = metaFile;
	}

	public List<DMSPermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<DMSPermission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * Add the given {@link DMSPermission} item to the {@code permissions}.
	 *
	 * <p>
	 * It sets {@code item.file = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addPermission(DMSPermission item) {
		if (getPermissions() == null) {
			setPermissions(new ArrayList<>());
		}
		getPermissions().add(item);
		item.setFile(this);
	}

	/**
	 * Remove the given {@link DMSPermission} item from the {@code permissions}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removePermission(DMSPermission item) {
		if (getPermissions() == null) {
			return;
		}
		getPermissions().remove(item);
	}

	/**
	 * Clear the {@code permissions} collection.
	 *
	 * <p>
	 * If you have to query {@link DMSPermission} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearPermissions() {
		if (getPermissions() != null) {
			getPermissions().clear();
		}
	}

	public Set<DMSFileTag> getTags() {
		return tags;
	}

	public void setTags(Set<DMSFileTag> tags) {
		this.tags = tags;
	}

	/**
	 * Add the given {@link DMSFileTag} item to the {@code tags}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addTag(DMSFileTag item) {
		if (getTags() == null) {
			setTags(new HashSet<>());
		}
		getTags().add(item);
	}

	/**
	 * Remove the given {@link DMSFileTag} item from the {@code tags}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeTag(DMSFileTag item) {
		if (getTags() == null) {
			return;
		}
		getTags().remove(item);
	}

	/**
	 * Clear the {@code tags} collection.
	 *
	 */
	public void clearTags() {
		if (getTags() != null) {
			getTags().clear();
		}
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Boolean getIsLock() {
		return isLock == null ? Boolean.FALSE : isLock;
	}

	public void setIsLock(Boolean isLock) {
		this.isLock = isLock;
	}

	public User getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(User lockedBy) {
		this.lockedBy = lockedBy;
	}

	public Set<User> getFavouriteUserSet() {
		return favouriteUserSet;
	}

	public void setFavouriteUserSet(Set<User> favouriteUserSet) {
		this.favouriteUserSet = favouriteUserSet;
	}

	/**
	 * Add the given {@link User} item to the {@code favouriteUserSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addFavouriteUserSetItem(User item) {
		if (getFavouriteUserSet() == null) {
			setFavouriteUserSet(new HashSet<>());
		}
		getFavouriteUserSet().add(item);
	}

	/**
	 * Remove the given {@link User} item from the {@code favouriteUserSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeFavouriteUserSetItem(User item) {
		if (getFavouriteUserSet() == null) {
			return;
		}
		getFavouriteUserSet().remove(item);
	}

	/**
	 * Clear the {@code favouriteUserSet} collection.
	 *
	 */
	public void clearFavouriteUserSet() {
		if (getFavouriteUserSet() != null) {
			getFavouriteUserSet().clear();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof DMSFile)) return false;

		final DMSFile other = (DMSFile) obj;
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
			.add("fileName", getFileName())
			.add("isDirectory", getIsDirectory())
			.add("relatedId", getRelatedId())
			.add("relatedModel", getRelatedModel())
			.add("contentType", getContentType())
			.add("isLock", getIsLock())
			.omitNullValues()
			.toString();
	}
}
