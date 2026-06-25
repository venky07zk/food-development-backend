package foodapp.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Restaurent {
    @OneToMany(mappedBy="restaurent",fetch=FetchType.EAGER,cascade=CascadeType.ALL)
    @JsonIgnore
    private List<FoodItems> foodItems;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String name;
    private  String location;

    public List<FoodItems> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<FoodItems> foodItems) {
        this.foodItems = foodItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
