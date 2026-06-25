package foodapp.demo;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public class foodDTO {
    @NotBlank(message="name cannot be empty")
    private String name;
    private int price;
    private String category;
    private int rating;
    private Integer restaurentId;
    private MultipartFile image;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    foodDTO(){}

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Integer getRestaurentId() {
        return restaurentId;
    }

    public void setRestaurentId(Integer restaurentId) {
        this.restaurentId = restaurentId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
