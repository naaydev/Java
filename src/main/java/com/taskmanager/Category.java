package com.taskmanager;

public class Category {
    private long id;
    private String name;
    private String description;
    private String color;

    public Category(String name) {
        this(name, "", "#4a90d9");
    }

    public Category(String name, String description, String color) {
        this.id = System.currentTimeMillis();
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getColor() { return color; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setColor(String color) { this.color = color; }
}