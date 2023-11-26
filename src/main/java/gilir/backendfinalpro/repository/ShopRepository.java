package gilir.backendfinalpro.repository;


import gilir.backendfinalpro.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Item, Long> {
    //derived query methods:
    Optional<Item> findByTitle(String title);

    List<Item> findByCategory(String category);
}
