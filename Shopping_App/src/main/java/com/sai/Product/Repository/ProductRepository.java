package com.sai.Product.Repository;

import com.sai.Product.Entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    List<Product> findByNameContainingIgnoreCase(String name);

    Optional<Product> findByName(String name);

    void deleteByNameIgnoreCase(String name);

    List<Product> findByCategoryContainingIgnoreCase(String keyword);

    @Query("SELECT p FROM Product p ORDER BY p.soldQuantity DESC")
    List<Product> findTopMostSoldProducts(Pageable pageable);

    @Query("SELECT SUM(p.soldQuantity) FROM Product p")
    Integer getTotalSoldQuantity();

    @Query("SELECT p FROM Product p ORDER BY p.soldQuantity DESC")
    Product findTopByOrderBySoldQuantityDesc();


    @Query("SELECT p.soldQuantity FROM Product p WHERE p.name = :productName")
    Long findQuantitySoldByProductName(String productName);

    @Query("SELECT p FROM Product p WHERE p.avgRating = (SELECT MAX(p.avgRating) FROM Product p)")
    List<Product> findProductsWithHighestRating();

    @Query("SELECT p FROM Product p WHERE p.avgRating = (SELECT MIN(p.avgRating) FROM Product p)")
    List<Product> findProductsWithLowestRating();
}
