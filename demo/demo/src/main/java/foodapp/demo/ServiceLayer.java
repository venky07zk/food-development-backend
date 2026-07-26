package foodapp.demo;

import java.io.IOException;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ServiceLayer {
    @Autowired
    private smsService smsservice;
    @Autowired
    private OtpRepository otpRepository;

    ServiceLayer(){

    }
    @Autowired
    OrderRepository orderrepo;
    @Autowired
    OrderItemRepository orderitemrepo;
    @Autowired
    cartRepository cartrepo;
    @Autowired
    RestaurentRepository restaurentRepository;
    @Autowired
    FoodRepository repo;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CartItemRepository cartitemrepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    PaymentService paymentservice;
    public ResponseEntity<String> verifyOtp(VerifyOtpDto dto)
    {
        String phoneNumber=dto.getPhoneNumber();
        Optional<OtpVerification> otpVerification=otpRepository.findByPhoneNumber(phoneNumber);
        if(otpVerification.isEmpty())
        {
            return ResponseEntity.badRequest().body("otp not found");
        }
        OtpVerification otpVerification1=otpVerification.get();
        System.out.println("Current time : " + LocalDateTime.now());
        System.out.println("Expires at   : " + otpVerification1.getExpiresAt());
        if(otpVerification1.getExpiresAt().isBefore(LocalDateTime.now()))
        {
            return ResponseEntity.badRequest().body("otp expired");
        }
        if (otpVerification1.getLockedUntil() != null &&
                LocalDateTime.now().isBefore(otpVerification1.getLockedUntil()))
        {
            return ResponseEntity.badRequest()
                    .body("Too many attempts. Try again after 10 minutes");
        }
        if(!otpVerification1.getOtp().equals(dto.getOtp()))
        {
            otpVerification1.setAttempts(otpVerification1.getAttempts() + 1);
            if(otpVerification1.getAttempts()>=5)
            {
                otpVerification1.setLockedUntil(LocalDateTime.now().plusMinutes(10));
            }
            otpRepository.save(otpVerification1);
            return ResponseEntity.badRequest().body("Invalid OTP");
        }
        User user=new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPhoneVerified(true);
        user.setRole("USER");
        User savedUser = userRepository.save(user);
        Cart cart=new Cart();
        cart.setUser(savedUser);
        cartrepo.save(cart);
        otpRepository.delete(otpVerification1);
        return ResponseEntity.ok("otp verified");
    }
    public ResponseEntity<String> sendOtp(String phoneNumber)
    {
       Optional<OtpVerification> otpoptional=otpRepository.findByPhoneNumber(phoneNumber);
       if(otpoptional.isPresent())
       {
         OtpVerification otp=otpoptional.get();
         if(LocalDateTime.now().isBefore(otp.getExpiresAt()))
         {
            return ResponseEntity.badRequest().body("otp sent alreday please wait");
         }
          Random r=new Random();
          int num=r.nextInt(900000);
          int otpvalue=100000+num;

          String generateOtp=String.valueOf(otpvalue);
           smsservice.sendOtp(phoneNumber,generateOtp);
          otp.setOtp(generateOtp);
          otp.setPhoneNumber(phoneNumber);
          otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
          otp.setAttempts(0);
          otp.setVerified(false);
          otp.setCreatedAt(LocalDateTime.now());
          otpRepository.save(otp);
       }
       else {
           OtpVerification otp=new OtpVerification();
           Random r=new Random();
           int num=r.nextInt(900000);
           int otpvalue=100000+num;
           String generateOtp=String.valueOf(otpvalue);
           smsservice.sendOtp(phoneNumber,generateOtp);
           otp.setOtp(generateOtp);
           otp.setPhoneNumber(phoneNumber);
           otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
           otp.setAttempts(0);
           otp.setVerified(false);
           otp.setCreatedAt(LocalDateTime.now());
           otpRepository.save(otp);
           return ResponseEntity.ok("otp sent");
       }
        return ResponseEntity.ok("otp sent");

    }
    public ResponseEntity<String>register(registerDTO r)
    {
        if(userRepository.findByUsername(r.getUsername())!=null)
        {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        User user=new User();
        user.setUsername(r.getUsername());
        user.setPassword(passwordEncoder.encode(r.getPassword()));
        user.setRole("USER");
        User saveduser=userRepository.save(user);
        Cart cart=new Cart();
        cart.setUser(saveduser);
        cartrepo.save(cart);
        return ResponseEntity.ok("Registered successfully");
    }
    public Page<FoodItems> getFoodsByPage(int page,int size)
    {
        Pageable pageable= PageRequest.of(page,size);
        return repo.findAll(pageable);
    }
    public ResponseEntity<List<FoodItems>> getFoodDetails()
    {
        List<FoodItems> list=repo.findAll();
        return ResponseEntity.ok(list);
    }
    public List<FoodItems> getFoodItemsByRestaurent(int restaurent_id)
    {
        Restaurent restaurent=restaurentRepository.findById(restaurent_id).orElseThrow(()->new RestaurentNotFoundException("restaurent not found"));
        return restaurent.getFoodItems();
    }
    public ResponseEntity<List<FoodItems>> getByName(String name)
    {
        if(!repo.existsByName(name)) {
        throw new FoodNotFound("Food not found");
    }
    return ResponseEntity.ok(repo.findByName(name));
    }
    public ResponseEntity<List<CartItem>> viewCart()
    {
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByUsername(username);
        Cart cart=cartrepo.findByUser(user);

        return ResponseEntity.ok(cart.getCartItems());
    }
    public ResponseEntity<List<FoodItems>> getBySort(String field)
    {
        return ResponseEntity.ok(repo.findBySort(field));
    }
    public ResponseEntity<List<Order>> adminorders()
    {
        return ResponseEntity.ok(orderrepo.findAll());
    }
    @CacheEvict(value="menu",key="#dto.restaurentId")
    public ResponseEntity<String> postdetails(foodDTO f) throws IOException
    {
       FoodItems food=new FoodItems();
       food.setName(f.getName());
       food.setCategory(f.getCategory());
       food.setPrice(f.getPrice());
       food.setRating(f.getRating());
        String fileName =
                f.getImage().getOriginalFilename();

        Path uploadPath =
                Paths.get("uploads");

        if (!Files.exists(uploadPath))
        {
            Files.createDirectories(uploadPath);
        }

        Path filePath =
                uploadPath.resolve(fileName);

        Files.copy(
                f.getImage().getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );
        food.setImagepath("uploads/"+fileName);
       Restaurent r=restaurentRepository.getById(f.getRestaurentId());
       food.setRestaurent(r);
        repo.save(food);
        return ResponseEntity.ok("Food Item Saved");
    }
    public ResponseEntity<String> addToCart(addToCartDTO c)
    {
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByUsername(username);
        if(!cartrepo.existsByUser(user))
        {
            throw new RuntimeException("cart not found for user"+user.getUsername());
        }
        Cart cart=cartrepo.findByUser(user);

        FoodItems food=repo.findById(c.getFood_id()).orElseThrow(()->new  FoodNotFound("food not found"));
        CartItem item=new CartItem();
        System.out.println("Username = " + username);
        System.out.println("User = " + user);
        System.out.println("Cart = " + cart);
        System.out.println("Cart ID = " + cart.getId());
        item.setCart(cart);
        item.setFoodItems(food);
        item.setQuantity(c.getQuantity());
        cartitemrepo.save(item);
        return ResponseEntity.ok("item added to cart");
    }
    public ResponseEntity<List<FoodItems>> getByCategoryAndRating(String name,int rating)
    {
        return ResponseEntity.ok(repo.findByCategoryAndRating(name,rating));
    }
    public ResponseEntity<String> putdetails(FoodItems f)
    {
        if(repo.existsById(f.getId()))
        {
            repo.save(f);
            return ResponseEntity.ok("Put complete");
        }
        throw new FoodNotFound("Food not found");
    }
    public ResponseEntity<String> deletedetails(int id)
    {
        if(repo.existsById(id))
        {
            repo.deleteById(id);
            return ResponseEntity.ok("delete complete");
        }
        throw new FoodNotFound("food not found");
    }
    public void testRelationship()
    {
       Restaurent r=restaurentRepository.findById(2).get();
       System.out.println(r.getName());
       System.out.println(r.getFoodItems().size());
       for(FoodItems item:r.getFoodItems())
       {
           System.out.print(item.getName());
       }
    }
    public void addRestaurent()
    {
        Restaurent r= new Restaurent();
        r.setName("paradise");
        r.setLocation("Hyderabad");
        restaurentRepository.save(r);
        FoodItems biryani = new FoodItems();

        biryani.setName("Biryani");
        biryani.setPrice(300);
        biryani.setRating(5);
        biryani.setCategory("Main Course");

        biryani.setRestaurent(r);
        System.out.println("saving biryani");
        repo.save(biryani);
    }
    @Cacheable(value="menu",key="#id")
    public List<FoodItems> menu(int id)
    {
        System.out.println("menu through mysql");
      Restaurent res=restaurentRepository.findById(id).orElseThrow(()->new RestaurentNotFoundException("not found"));

      return res.getFoodItems();
    }
    public ResponseEntity<String> removeFromCart(int id)
    {
         cartitemrepo.deleteById(id);
         return ResponseEntity.ok("item removed");
    }

    public ResponseEntity<paymentResponseDTO> placeorder(placeOrderDTO dto) throws RuntimeException
    {
        System.out.println("Place order eneterd");
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByUsername(username);
        Cart cart=cartrepo.findByUser(user);
        List<CartItem> cartItems=cart.getCartItems();
        Restaurent restaurent=cartItems.get(0).getFoodItems().getRestaurent();
        System.out.println("Restaurant ID: " + restaurent.getId());
        System.out.println("Customer Latitude  : " + dto.getCustomerlatitude());
        System.out.println("Customer Longitude : " + dto.getCustomerlongitude());
        System.out.println("Restaurant Latitude : " + restaurent.getLatitude());
        System.out.println("Restaurant Longitude: " + restaurent.getLongitude());

        System.out.println("Delivery Radius: " + restaurent.getDeliveryRadius());
        double distance=distanceCalculator.distance(dto.getCustomerlatitude(),dto.getCustomerlongitude(),restaurent.getLatitude(),restaurent.getLongitude());
        System.out.println("Distance = " + distance);
        if(distance> restaurent.getDeliveryRadius())
        {
            throw new RuntimeException("restaurent cannot deliver");
        }
        List<OrderItem> orderitemslist=new ArrayList<>();
        if(cartItems.isEmpty())
        {
        }
        Order order=new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setRazorpayOrderId("PENDING");
        order.setLatitude(dto.getCustomerlatitude());
        order.setLongitude(dto.getCustomerlongitude());
        order.setRestaurant(restaurent);
        double total=0;
        for(CartItem c:cartItems)
        {
            total+=c.getFoodItems().getPrice()*c.getQuantity();
        }
        order.setAmount(total);
        orderrepo.save(order);
        try {
            String razorPayOrderId = paymentservice.createOrder(total);

            order.setRazorpayOrderId(razorPayOrderId);
            orderrepo.save(order);
        }
        catch(Exception e){

        }
        for(CartItem c:cartItems)
        {

            OrderItem orderitem=new OrderItem();
            orderitem.setOrder(order);
            orderitem.setFoodItem(c.getFoodItems());
            orderitem.setQuantity(c.getQuantity());
            orderitemrepo.save(orderitem);
            orderitemslist.add(orderitem);
        }
        order.setOrderItems(orderitemslist);
        orderrepo.save(order);
        paymentResponseDTO p=new paymentResponseDTO();
        p.setOrderid(order.getId());
        p.setRazorpayorderid(order.getRazorpayOrderId());
        p.setAmount(order.getAmount());
        p.setCurrency("INR");
        p.setDistance(distance);
       // cartitemrepo.deleteAll(cartItems);
        return ResponseEntity.ok(p);    }

    public ResponseEntity<List<orderResponseDTO>> myorders()
    {
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByUsername(username);
        List<Order>orders =orderrepo.findByUser(user);

        List<orderResponseDTO> dtoList =
                new ArrayList<>();
        for(Order order:orders)
        {
            orderResponseDTO dto=new orderResponseDTO();
            dto.setId(order.getId());
            dto.setAmount(order.getAmount());
            dto.setStatus(order.getStatus());
            dtoList.add(dto);
        }
        return ResponseEntity.ok(dtoList);
    }
    public ResponseEntity<?> myorder(int id)
    {
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByUsername(username);
        Order order=orderrepo.findById(id).orElseThrow(()->new RuntimeException("not found"));
        if(order.getUser().getId() != user.getId())
        {
            return ResponseEntity.status(403)
                    .body("Access denied");
        }

        return ResponseEntity.ok(order);
    }
    public ResponseEntity<String> updatestatus(OrderStatus status,int id)
    {
        Order order=orderrepo.findById(id).orElseThrow(()->new RestaurentNotFoundException("not found"));
        if(order==null)
        {
            return ResponseEntity.badRequest().body("order not found");
        }
        if(order.getStatus()==OrderStatus.DELIVERED||order.getStatus()==OrderStatus.CANCELLED)
        {
            return ResponseEntity.badRequest()
                    .body("Cancelled order cannot be modified");
        }
        order.setStatus(status);
        orderrepo.save(order);
        return ResponseEntity.ok("status updated");
    }
    public ResponseEntity<String> cancelorder(int id)
    {
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByUsername(username);
        Order order=orderrepo.findById(id).orElseThrow(()->new RestaurentNotFoundException("not found"));
        if(order.getUser().getId() != user.getId())
        {
            return ResponseEntity.status(403)
                    .body("Access denied");
        }
        if(order.getStatus()!=OrderStatus.PENDING)
        {
            return ResponseEntity.badRequest().body("order cannot be calcelled");
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderrepo.save(order);
        return ResponseEntity.ok("order cancelled");
    }
}
