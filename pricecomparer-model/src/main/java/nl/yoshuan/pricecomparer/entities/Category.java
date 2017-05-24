package nl.yoshuan.pricecomparer.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id", nullable = false, insertable = false, updatable = false)
	private Long id;

	@Column(name = "category", nullable = false, updatable = false, unique = true)
	private String categoryName;

	@ManyToOne
	@JoinColumn(name = "parent_category_id", referencedColumnName = "category_id", updatable = false)
	private Category parentCategory;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentCategory")
    private List<Category> childCategories = new ArrayList<>();
	
	protected Category() {
    }
    
    public Category(String categoryName, Category parentCategory) {
        this.categoryName = categoryName;
        this.parentCategory = parentCategory;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public Category getParentCategory() {
        return parentCategory;
    }
    
    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }
    
    public List<Category> getChildCategories() {
        return childCategories;
    }
    
    public void setChildCategories(List<Category> childCategories) {
        this.childCategories = childCategories;
    }
    
}
