package fr.cda.findboat.model;

public class Type {

    private Integer id;
    private String name;

    public Type(String name) {
        this.id = null;
        this.name = name;
    }

    public Type(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
