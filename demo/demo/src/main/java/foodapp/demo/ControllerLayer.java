package foodapp.demo;

import io.swagger.v3.oas.annotations.Operation;
import java.nio.file.Path;

import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
public class ControllerLayer {
    @Autowired
    ServiceLayer s;
    @Autowired
    private RestaurentRepository restaurentRepository;
    @GetMapping("/orders/myorders/{id}")
    public ResponseEntity<?> myorder(@PathVariable int id)
    {
        return s.myorder(id);
    }
    @GetMapping("/orders/myorders")
    public ResponseEntity<List<orderResponseDTO>> myorders()
    {
        return s.myorders();
    }
    @Operation(summary="Get food items by pages")
    @GetMapping("FoodItems/page")
    public Page<FoodItems> getdetailsbypage(@RequestParam int page,@RequestParam int size)
    {
        return s.getFoodsByPage(page,size);
    }
    @GetMapping("/lazyload/{id}")
    public String lazySet(@PathVariable int id)
    {
        Restaurent r=restaurentRepository.findById(id).get();
        System.out.println("loaded");
        return "done";
    }
    @GetMapping("/restaurent/{id}/foods")
    public List<FoodItems>getFoodsByRestaurent(@PathVariable int id)
    {
        return s.getFoodItemsByRestaurent(id);
    }
    @Operation(summary="get all food items")
    @GetMapping("/FoodItems")
    public ResponseEntity<List<FoodItems>> getdetails()
    {
        return s.getFoodDetails();
    }
    @Operation(summary="get food items by name")
    @GetMapping("/FoodItems/{name}")
    public ResponseEntity<List<FoodItems>> getbyname(@PathVariable String name)
    {
        return s.getByName(name);
    }
    @Operation(summary="get food items by sorted by field")
    @GetMapping("/FoodItems/sort/{field}")
    public ResponseEntity<List<FoodItems>> getBySort(@PathVariable String field)
    {
        return s.getBySort(field);
    }
    @Operation(summary="get food items by serach of category and rating")
    @GetMapping("/FoodItems/search")
    public ResponseEntity<List<FoodItems>> getbycaterory(@RequestParam String category,@RequestParam int rating)
    {
        return s.getByCategoryAndRating(category,rating);
    }
    @GetMapping("/viewCart")
    public ResponseEntity<List<CartItem>> viewcart()
    {
        System.out.println("view cart reached");
        return s.viewCart();
    }
    @GetMapping("/admin/orders")
    public ResponseEntity<List<Order>> getadminorders()
    {
        return s.adminorders();
    }
    @GetMapping("/searchmenu")
    public ResponseEntity<List<FoodItems>> search(@RequestParam int id)
    {
        return s.menu(id);
    }
    @Operation(summary="post food items")
    @PostMapping("/FoodItems")
    public ResponseEntity<String> postdetails( @ModelAttribute foodDTO f) throws IOException
    {
        System.out.println("controller reached");
       return s.postdetails(f);
    }
    @PutMapping("/admin/{id}/status")
    public ResponseEntity<String> updatestatus(@RequestParam OrderStatus status,@PathVariable int id)
    {
        return s.updatestatus(status,id);
    }
    @PostMapping("/addtocart")
    public ResponseEntity<String> addToCart(@RequestBody addToCartDTO c)
    {
        return s.addToCart(c);
    }
    @PutMapping("/cancelorder/{id}")
    public ResponseEntity<String> cancelorder(@PathVariable int id)
    {
        return s.cancelorder(id);
    }
    @Operation(summary="put food items")
    @PutMapping("/FoodItems/put")
    public ResponseEntity<String> putdetails(@RequestBody FoodItems f)
    {
        return s.putdetails(f);
    }
    @Operation(summary="delete fooditems by id")
    @DeleteMapping("/FoodItems/{id}")
    public ResponseEntity<String> deletedetails(@PathVariable int id)
    {
        return s.deletedetails(id);
    }
    @DeleteMapping("/removeFromcart/{id}")
    public ResponseEntity<String> removeFromCart(@PathVariable int id)
    {

        return s.removeFromCart(id);
    }
    @Transactional
    @PostMapping("/placeorder")
    public ResponseEntity<String> placeorder()throws RuntimeException
    {
        return s.placeorder();
    }

}
