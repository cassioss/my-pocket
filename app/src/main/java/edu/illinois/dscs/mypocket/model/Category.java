package edu.illinois.dscs.mypocket.model;

/**
 * @author Dennis
 * @version 1.0
 */
public class Category {

    private String name;

    /**
     * Creates a new Category object.
     *
     * @param name the name of the Category.
     */
    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
