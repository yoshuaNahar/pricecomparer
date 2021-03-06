package nl.yoshuan.pricecomparer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@JsonIgnoreProperties({"parentCategory"})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false, insertable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY) // Default is CascadeType.NONE
    @JoinColumn(name = "parent_category_id", referencedColumnName = "category_id")
    private Category parentCategory;

    // This also will be CascadeType.NONE, because in my data-parser I will have
    // urls with the same parent/root category and different child/subcategories
    // When using my persistIfNotExist on the parent (that already exists) I wont be able to
    // persist the children. So I will do this manually.
    @OneToMany(mappedBy = "parentCategory")
    private List<Category> childCategories = new ArrayList<>();

    protected Category() {}

    public Category(String name, Category parentCategory) {
        this.name = name;
        this.parentCategory = parentCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
