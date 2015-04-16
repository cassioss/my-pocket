package edu.illinois.dscs.mypocket.model;

/**
 * @author Dennis
 * @version 1.0
 */
public class Category {

    private int categoryID;
    private String name;

    /**
     * Creates a new Category object.
     *
     * @param name the name of the Category.
     */
    public Category(int categoryID, String name) {
        this.categoryID = categoryID;
        this.name = name;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}