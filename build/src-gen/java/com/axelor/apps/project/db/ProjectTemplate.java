package com.axelor.apps.project.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.axelor.apps.base.db.Company;
import com.axelor.apps.base.db.Product;
import com.axelor.auth.db.AuditableModel;
import com.axelor.auth.db.User;
import com.axelor.db.annotations.EqualsInclude;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.Widget;
import com.axelor.team.db.Team;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "PROJECT_PROJECT_TEMPLATE", indexes = { @Index(columnList = "name"), @Index(columnList = "team"), @Index(columnList = "assigned_to"), @Index(columnList = "company"), @Index(columnList = "fullName") })
public class ProjectTemplate extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_PROJECT_TEMPLATE_SEQ")
	@SequenceGenerator(name = "PROJECT_PROJECT_TEMPLATE_SEQ", sequenceName = "PROJECT_PROJECT_TEMPLATE_SEQ", allocationSize = 1)
	private Long id;

	@NotNull
	private String name;

	@Widget(multiline = true)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String description;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Team team;

	@Widget(title = "Project Folders")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<ProjectFolder> projectFolderSet;

	@Widget(title = "Assigned to")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private User assignedTo;

	@Widget(title = "Type of authorized categories")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<ProjectTaskCategory> projectTaskCategorySet;

	private Boolean synchronize = Boolean.FALSE;

	@Widget(title = "Wiki")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Wiki> wikiList;

	@EqualsInclude
	@Widget(title = "Code")
	@Column(unique = true)
	private String code;

	@Widget(title = "Membres")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<User> membersUserSet;

	@Widget(massUpdate = true)
	private Boolean imputable = Boolean.TRUE;

	@Widget(title = "Company")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Company company;

	@Widget(title = "Type of authorized activities")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Product> productSet;

	@Widget(title = "Name")
	@NameColumn
	private String fullName;

	@Widget(title = "Exclude planning")
	private Boolean excludePlanning = Boolean.FALSE;

	@Widget(title = "Business project")
	private Boolean isBusinessProject = Boolean.FALSE;

	@Widget(title = "Task templates")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<TaskTemplate> taskTemplateSet;

	@Widget(title = "Invoicing Expenses")
	private Boolean isInvoicingExpenses = Boolean.FALSE;

	@Widget(title = "Invoicing Purchases")
	private Boolean isInvoicingPurchases = Boolean.FALSE;

	@Widget(title = "Invoicing Type", selection = "project.invoicing.type.select")
	private Integer invoicingTypeSelect = 0;

	@Widget(title = "Invoicing comment")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String invoicingComment;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public ProjectTemplate() {
	}

	public ProjectTemplate(String name, String code) {
		this.name = name;
		this.code = code;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Set<ProjectFolder> getProjectFolderSet() {
		return projectFolderSet;
	}

	public void setProjectFolderSet(Set<ProjectFolder> projectFolderSet) {
		this.projectFolderSet = projectFolderSet;
	}

	/**
	 * Add the given {@link ProjectFolder} item to the {@code projectFolderSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addProjectFolderSetItem(ProjectFolder item) {
		if (getProjectFolderSet() == null) {
			setProjectFolderSet(new HashSet<>());
		}
		getProjectFolderSet().add(item);
	}

	/**
	 * Remove the given {@link ProjectFolder} item from the {@code projectFolderSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeProjectFolderSetItem(ProjectFolder item) {
		if (getProjectFolderSet() == null) {
			return;
		}
		getProjectFolderSet().remove(item);
	}

	/**
	 * Clear the {@code projectFolderSet} collection.
	 *
	 */
	public void clearProjectFolderSet() {
		if (getProjectFolderSet() != null) {
			getProjectFolderSet().clear();
		}
	}

	public User getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(User assignedTo) {
		this.assignedTo = assignedTo;
	}

	public Set<ProjectTaskCategory> getProjectTaskCategorySet() {
		return projectTaskCategorySet;
	}

	public void setProjectTaskCategorySet(Set<ProjectTaskCategory> projectTaskCategorySet) {
		this.projectTaskCategorySet = projectTaskCategorySet;
	}

	/**
	 * Add the given {@link ProjectTaskCategory} item to the {@code projectTaskCategorySet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addProjectTaskCategorySetItem(ProjectTaskCategory item) {
		if (getProjectTaskCategorySet() == null) {
			setProjectTaskCategorySet(new HashSet<>());
		}
		getProjectTaskCategorySet().add(item);
	}

	/**
	 * Remove the given {@link ProjectTaskCategory} item from the {@code projectTaskCategorySet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeProjectTaskCategorySetItem(ProjectTaskCategory item) {
		if (getProjectTaskCategorySet() == null) {
			return;
		}
		getProjectTaskCategorySet().remove(item);
	}

	/**
	 * Clear the {@code projectTaskCategorySet} collection.
	 *
	 */
	public void clearProjectTaskCategorySet() {
		if (getProjectTaskCategorySet() != null) {
			getProjectTaskCategorySet().clear();
		}
	}

	public Boolean getSynchronize() {
		return synchronize == null ? Boolean.FALSE : synchronize;
	}

	public void setSynchronize(Boolean synchronize) {
		this.synchronize = synchronize;
	}

	public List<Wiki> getWikiList() {
		return wikiList;
	}

	public void setWikiList(List<Wiki> wikiList) {
		this.wikiList = wikiList;
	}

	/**
	 * Add the given {@link Wiki} item to the {@code wikiList}.
	 *
	 * <p>
	 * It sets {@code item.projectTemplate = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addWikiListItem(Wiki item) {
		if (getWikiList() == null) {
			setWikiList(new ArrayList<>());
		}
		getWikiList().add(item);
		item.setProjectTemplate(this);
	}

	/**
	 * Remove the given {@link Wiki} item from the {@code wikiList}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeWikiListItem(Wiki item) {
		if (getWikiList() == null) {
			return;
		}
		getWikiList().remove(item);
	}

	/**
	 * Clear the {@code wikiList} collection.
	 *
	 * <p>
	 * If you have to query {@link Wiki} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearWikiList() {
		if (getWikiList() != null) {
			getWikiList().clear();
		}
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Set<User> getMembersUserSet() {
		return membersUserSet;
	}

	public void setMembersUserSet(Set<User> membersUserSet) {
		this.membersUserSet = membersUserSet;
	}

	/**
	 * Add the given {@link User} item to the {@code membersUserSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addMembersUserSetItem(User item) {
		if (getMembersUserSet() == null) {
			setMembersUserSet(new HashSet<>());
		}
		getMembersUserSet().add(item);
	}

	/**
	 * Remove the given {@link User} item from the {@code membersUserSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeMembersUserSetItem(User item) {
		if (getMembersUserSet() == null) {
			return;
		}
		getMembersUserSet().remove(item);
	}

	/**
	 * Clear the {@code membersUserSet} collection.
	 *
	 */
	public void clearMembersUserSet() {
		if (getMembersUserSet() != null) {
			getMembersUserSet().clear();
		}
	}

	public Boolean getImputable() {
		return imputable == null ? Boolean.FALSE : imputable;
	}

	public void setImputable(Boolean imputable) {
		this.imputable = imputable;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Set<Product> getProductSet() {
		return productSet;
	}

	public void setProductSet(Set<Product> productSet) {
		this.productSet = productSet;
	}

	/**
	 * Add the given {@link Product} item to the {@code productSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addProductSetItem(Product item) {
		if (getProductSet() == null) {
			setProductSet(new HashSet<>());
		}
		getProductSet().add(item);
	}

	/**
	 * Remove the given {@link Product} item from the {@code productSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeProductSetItem(Product item) {
		if (getProductSet() == null) {
			return;
		}
		getProductSet().remove(item);
	}

	/**
	 * Clear the {@code productSet} collection.
	 *
	 */
	public void clearProductSet() {
		if (getProductSet() != null) {
			getProductSet().clear();
		}
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Boolean getExcludePlanning() {
		return excludePlanning == null ? Boolean.FALSE : excludePlanning;
	}

	public void setExcludePlanning(Boolean excludePlanning) {
		this.excludePlanning = excludePlanning;
	}

	public Boolean getIsBusinessProject() {
		return isBusinessProject == null ? Boolean.FALSE : isBusinessProject;
	}

	public void setIsBusinessProject(Boolean isBusinessProject) {
		this.isBusinessProject = isBusinessProject;
	}

	public Set<TaskTemplate> getTaskTemplateSet() {
		return taskTemplateSet;
	}

	public void setTaskTemplateSet(Set<TaskTemplate> taskTemplateSet) {
		this.taskTemplateSet = taskTemplateSet;
	}

	/**
	 * Add the given {@link TaskTemplate} item to the {@code taskTemplateSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addTaskTemplateSetItem(TaskTemplate item) {
		if (getTaskTemplateSet() == null) {
			setTaskTemplateSet(new HashSet<>());
		}
		getTaskTemplateSet().add(item);
	}

	/**
	 * Remove the given {@link TaskTemplate} item from the {@code taskTemplateSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeTaskTemplateSetItem(TaskTemplate item) {
		if (getTaskTemplateSet() == null) {
			return;
		}
		getTaskTemplateSet().remove(item);
	}

	/**
	 * Clear the {@code taskTemplateSet} collection.
	 *
	 */
	public void clearTaskTemplateSet() {
		if (getTaskTemplateSet() != null) {
			getTaskTemplateSet().clear();
		}
	}

	public Boolean getIsInvoicingExpenses() {
		return isInvoicingExpenses == null ? Boolean.FALSE : isInvoicingExpenses;
	}

	public void setIsInvoicingExpenses(Boolean isInvoicingExpenses) {
		this.isInvoicingExpenses = isInvoicingExpenses;
	}

	public Boolean getIsInvoicingPurchases() {
		return isInvoicingPurchases == null ? Boolean.FALSE : isInvoicingPurchases;
	}

	public void setIsInvoicingPurchases(Boolean isInvoicingPurchases) {
		this.isInvoicingPurchases = isInvoicingPurchases;
	}

	public Integer getInvoicingTypeSelect() {
		return invoicingTypeSelect == null ? 0 : invoicingTypeSelect;
	}

	public void setInvoicingTypeSelect(Integer invoicingTypeSelect) {
		this.invoicingTypeSelect = invoicingTypeSelect;
	}

	public String getInvoicingComment() {
		return invoicingComment;
	}

	public void setInvoicingComment(String invoicingComment) {
		this.invoicingComment = invoicingComment;
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
		if (!(obj instanceof ProjectTemplate)) return false;

		final ProjectTemplate other = (ProjectTemplate) obj;
		if (this.getId() != null || other.getId() != null) {
			return Objects.equals(this.getId(), other.getId());
		}

		return Objects.equals(getCode(), other.getCode())
			&& (getCode() != null);
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
			.add("synchronize", getSynchronize())
			.add("code", getCode())
			.add("imputable", getImputable())
			.add("fullName", getFullName())
			.add("excludePlanning", getExcludePlanning())
			.add("isBusinessProject", getIsBusinessProject())
			.add("isInvoicingExpenses", getIsInvoicingExpenses())
			.add("isInvoicingPurchases", getIsInvoicingPurchases())
			.add("invoicingTypeSelect", getInvoicingTypeSelect())
			.omitNullValues()
			.toString();
	}
}
