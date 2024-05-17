package com.sai.Product.Repository;

import com.sai.Product.Entity.SearchStat;
import com.sai.Product.Entity.Product;
import com.sai.Product.Request.ProductSearchStatDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SearchStatsRepository extends JpaRepository<SearchStat, Long> {


    Optional<SearchStat> findByProductName(Product product);

    @Query("SELECT s FROM SearchStat s WHERE s.product.name = :productName")
    Optional<SearchStat> findByProductName(@Param("productName") String productName);

//    @Query("SELECT s.product FROM SearchStat s ORDER BY s.count DESC")
//    List<Product> findMostSearchedProducts();

    @Query("SELECT new com.sai.Product.Request.ProductSearchStatDTO(p.name, s.count) FROM SearchStat s JOIN s.product p ORDER BY s.count DESC")
    List<ProductSearchStatDTO> findMostSearchedProducts();


    @Query("SELECT new com.sai.Product.Request.ProductSearchStatDTO(p.name, s.count) FROM SearchStat s JOIN s.product p WHERE p.name = :productName")
    ProductSearchStatDTO findSearchStatByProductName(String productName);

}
