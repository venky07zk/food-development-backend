package foodapp.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodRepository extends JpaRepository<FoodItems,Integer> {
    @Query("from FoodItems where name=?1")
   List<FoodItems> findByName(String name);

    boolean existsByName(String name);
    @Query("from FoodItems where category=?1 and rating=?2")
    List<FoodItems> findByCategoryAndRating(String name,int rating);
    @Query("from FoodItems order by name asc")
    List<FoodItems> findBySort(String name);
}
