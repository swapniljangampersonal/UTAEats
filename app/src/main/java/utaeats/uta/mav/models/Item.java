package utaeats.uta.mav.models;

public class Item {
    private String itemName;
    private float cost;
    private int numberOfServings;
    private String location;
    private Boolean available;
    private String image;

    public Item(String itemName, float cost, int numberOfServings, String location, Boolean available, String image) {
        this.itemName = itemName;
        this.cost = cost;
        this.numberOfServings = numberOfServings;
        this.location = location;
        this.available = available;
        this.image = image;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public int getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(int numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
