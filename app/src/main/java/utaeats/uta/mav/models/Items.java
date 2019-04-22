package utaeats.uta.mav.models;

public class Items {

    private String itemID;
    private String ItemName;
    private String No_of_servings;
    private String cost;
    private String pickupAdd;
    private String image;

    public Items(String itemID, String itemName, String no_of_servings, String cost, String pickupAdd, String image) {
        this.itemID = itemID;
        this.ItemName = itemName;
        this.No_of_servings = no_of_servings;
        this.cost = cost;
        this.pickupAdd = pickupAdd;
        this.image = image;
    }

    public Items(){

    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getNo_of_servings() {
        return No_of_servings;
    }

    public void setNo_of_servings(String no_of_servings) {
        No_of_servings = no_of_servings;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getPickupAdd() {
        return pickupAdd;
    }

    public void setPickupAdd(String pickupAdd) {
        this.pickupAdd = pickupAdd;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Items{" +
                "itemID='" + itemID + '\'' +
                ", ItemName='" + ItemName + '\'' +
                ", No_of_servings='" + No_of_servings + '\'' +
                ", cost='" + cost + '\'' +
                ", pickupAdd='" + pickupAdd + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
