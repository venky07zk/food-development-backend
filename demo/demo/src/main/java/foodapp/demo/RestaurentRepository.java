package foodapp.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurentRepository extends JpaRepository<Restaurent,Integer> {

}
