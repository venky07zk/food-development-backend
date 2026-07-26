package foodapp.demo;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceLayerTest {
    @Mock
    private FoodRepository repo;
    @Mock
    private UserRepository userrepo;
    @Mock
    private OrderRepository orderrepo;
    @Mock
    private CartItemRepository cartitemrepo;
    @Mock
    private cartRepository cartrepo;
    @Mock
    private OrderItemRepository orderitemrepo;
    @InjectMocks
    private ServiceLayer service;
    @Test
    void testGetFoodDetails(){
        List<FoodItems> foods=new ArrayList<>();
        FoodItems food=new FoodItems();
        food.setName("Biryani");
        foods.add(food);
        when(repo.findAll()).thenReturn(foods);
        ResponseEntity<List<FoodItems>> response=service.getFoodDetails();
        assertNotNull(response);
        assertEquals(1,response.getBody().size());
        assertEquals("Biryani",response.getBody().get(0).getName());
        verify(repo).findAll();
    }
    @Test
    void testGetByName()
    {
        //arrange
        List<FoodItems> foods=new ArrayList<>();
        FoodItems food =new FoodItems();
        food.setName("Biryani");
        foods.add(food);
        when(repo.existsByName("Biryani")).thenReturn(true);
        when(repo.findByName("Biryani")).thenReturn(foods);
        //act
        ResponseEntity<List<FoodItems>> response=service.getByName("Biryani");
        //assert
        assertNotNull(response);
        assertEquals(1,response.getBody().size());
        assertEquals("Biryani",response.getBody().get(0).getName());
        //verify
        verify(repo).existsByName("Biryani");
        verify(repo).findByName("Biryani");
    }
    @Test
    void testGetByNameException(){
        //arrrange
        when(repo.existsByName("Pizza")).thenReturn(false);
        //act&assert
        assertThrows(FoodNotFound.class,()->service.getByName("Pizza"));
        //verify
        verify(repo).existsByName("Pizza");
        verify(repo,never()).findByName(anyString());
    }
    @Test
    void testPlaceOrder()
    {
        Authentication authentication=mock(Authentication.class);
        SecurityContext context=mock(SecurityContext.class);
        when(authentication.getName()).thenReturn("Venky");
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);
        User user=new User();
        user.setUsername("Venky");
         when(userrepo.findByUsername("Venky")).thenReturn(user);
         Cart cart=new Cart();
         cart.setUser(user);
         when(cartrepo.findByUser(user)).thenReturn(cart);
         FoodItems food=new FoodItems();
         food.setPrice(299);
         CartItem cartItem=new CartItem();
         cartItem.setFoodItems(food);
         cartItem.setQuantity(2);
         List<CartItem> cartItems=new ArrayList<>();
         cartItems.add(cartItem);
         cart.setCartItems(cartItems);
         ResponseEntity<String> response =service.placeorder();
         assertNotNull(response);
         assertEquals("Order placed successfully",response.getBody());
         verify(userrepo).findByUsername("Venky");
         verify(cartrepo).findByUser(user);
         verify(orderrepo,times(2)).save(any(Order.class));
         verify(orderitemrepo).save(any(OrderItem.class));
    }
    @Test
    void testaddtocart()
    {
        Authentication authentication=mock(Authentication.class);
        SecurityContext context=mock(SecurityContext.class);
        when(authentication.getName()).thenReturn("venky");
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);
        User user=new User();
        user.setUsername("venky");

        when(userrepo.findByUsername("venky")).thenReturn(user);
        when(cartrepo.existsByUser(user)).thenReturn(true);
        Cart cart=new Cart();
        cart.setUser(user);
        when(cartrepo.findByUser(user)).thenReturn(cart);
        FoodItems food=new FoodItems();
        food.setId(1);
        when(repo.findById(1)).thenReturn(Optional.of(food));
        addToCartDTO addtocart=new addToCartDTO();
        addtocart.setQuantity(3);
        addtocart.setFood_id(1);
        //act

        ResponseEntity<String> response=service.addToCart(addtocart);
       assertNotNull(response);
       assertEquals("item added to cart",response.getBody());
       //verify
        verify(userrepo).findByUsername("venky");
        verify(cartrepo).findByUser(user);
        verify(repo).findById(1);
        verify(cartitemrepo).save(any(CartItem.class));

    }
   @Test
    void testcancelorderaccess()
   {
       Authentication authentication=mock(Authentication.class);
       SecurityContext context=mock(SecurityContext.class);
       when(authentication.getName()).thenReturn("venky");
       when(context.getAuthentication()).thenReturn(authentication);
       SecurityContextHolder.setContext(context);
       User user=new User();
       user.setUsername("venky");
       user.setId(1);
       User user2=new User();
       user2.setId(2);
       when(userrepo.findByUsername("venky")).thenReturn(user);
       Order order=new Order();
       order.setUser(user2);
       when(orderrepo.findById(1)).thenReturn(order);
       ResponseEntity<String> response=service.cancelorder(1);
       assertNotNull(response);
       assertEquals("Access denied",response.getBody());
       verify(userrepo).findByUsername("venky");
       verify(orderrepo).findById(1);

   }



}
