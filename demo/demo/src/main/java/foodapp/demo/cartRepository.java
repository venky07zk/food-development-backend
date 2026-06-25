package foodapp.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface cartRepository extends JpaRepository<Cart,Integer> {

    Cart findByUser(User user);
}
