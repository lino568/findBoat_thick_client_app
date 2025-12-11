package fr.cda.findboat.model;

public class Boat {

    private Integer id;
    private String title;
    private String description;
    private double length;
    private int year;
    private int capacity;
    private String pictureUrl;
    private double weekPrice;
    private String url;
    private Type type;

    public Boat(String title, String description, double length, int year, int capacity, String pictureUrl, double weekPrice, String url, Type type) {
        this.id = null;
        this.title = title;
        this.description = description;
        this.length = length;
        this.year = year;
        this.capacity = capacity;
        this.pictureUrl = pictureUrl;
        this.weekPrice = weekPrice;
        this.url = url;
        this.type = type;
    }

    public Boat(int id, String title, String description, double length, int year, int capacity, String pictureUrl, double weekPrice, String url, Type type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.length = length;
        this.year = year;
        this.capacity = capacity;
        this.pictureUrl = pictureUrl;
        this.weekPrice = weekPrice;
        this.url = url;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public double getWeekPrice() {
        return weekPrice;
    }

    public void setWeekPrice(double weekPrice) {
        this.weekPrice = weekPrice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Type getType() {
        return type;
    }


    public void setType(Type type) {
        this.type = type;
    }
}
