package com.sai.Product.Controller.Customer_Interest;


import com.sai.Product.Request.ProductRequestDTO;
import com.sai.Product.Request.ProductSalesDTO;
import com.sai.Product.Request.ProductSearchStatDTO;
import com.sai.Product.Request.ProductSoldDTO;
import com.sai.Product.Entity.Product;
import com.sai.Product.Service.Customer_Interest_Products.Customer_Product_Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/Products")

public class Customer_Interest_Controller {

         private final Customer_Product_Service customerProductService;

    public Customer_Interest_Controller(Customer_Product_Service customerProductService) {
        this.customerProductService = customerProductService;
    }


    @GetMapping("/mostSearched")
    // http://localhost:8080/Products/mostSearched
    public ResponseEntity<List<ProductSearchStatDTO>> getMostSearchedProducts() {
        List<ProductSearchStatDTO> mostSearched = customerProductService.getMostSearchedProducts();
        if (mostSearched.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(mostSearched);
    }

    @GetMapping("/search-stat/{productName}")
    //  http://localhost:8080/Products//search-stat/dell laptop
    public ResponseEntity<ProductSearchStatDTO> getProductSearchStat(@PathVariable String productName) {
        ProductSearchStatDTO searchStat = customerProductService.getSearchStatForProductName(productName);
        if (searchStat == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(searchStat);
    }


    @GetMapping("/mostSoldProducts")
    // http://localhost:8080/Products/mostSoldProducts?limit=3
    public ResponseEntity<List<ProductSoldDTO>> getMostSoldProducts(@RequestParam(defaultValue = "10") int limit) {
        List<Product> mostSoldProducts = customerProductService.findTopMostSoldProducts(limit);
        List<ProductSoldDTO> soldDTOList = mostSoldProducts.stream()
                .map(product -> new ProductSoldDTO(product.getName(), product.getSoldQuantity()))
                .collect(Collectors.toList());

        if (soldDTOList.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content if list is empty
        }
        return ResponseEntity.ok(soldDTOList);
    }


    @PostMapping("/No_Of_Sales/product")
    // http://localhost:8080/Products/No_Of_Sales/product
    public ResponseEntity<ProductSalesDTO> getQuantitySoldByProductName(@RequestBody ProductRequestDTO productRequest) {
        ProductSalesDTO productSales = customerProductService.getQuantitySoldByProductName(productRequest.getProductName());
        if (productSales == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productSales);
    }


//    {
//        "productName": "Example Product"
//    }

}
