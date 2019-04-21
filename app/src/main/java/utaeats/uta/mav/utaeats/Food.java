

//Model class. It has same name as that of table storing menu items in the database

package UTAEats.com;
public class Food {

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }



    public Food() {
    }

   public Food(String name, String cost) {
        Name = name;
        Cost = cost;
    }
    private  String Name;
    private String Cost;



   /* public String getServings() {
        return Servings;
    }

    public void setServings(String servings) {
        Servings = servings;
    }

    public String getPickup_Location() {
        return Pickup_Location;
    }

    public void setPickup_Location(String pickup_Location) {
        Pickup_Location = pickup_Location;
    }
*/
   /*private String Servings;
private String Pickup_Location;*/
}
