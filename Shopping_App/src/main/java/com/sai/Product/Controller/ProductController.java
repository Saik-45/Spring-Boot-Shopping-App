package com.sai.Product.Controller;


import com.sai.Product.Entity.Product;
import com.sai.Product.Entity.SearchStat;
import com.sai.Product.Repository.SearchStatsRepository;
import com.sai.Product.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/Products")
public class ProductController {

    @Autowired
    private final ProductService productService;

    @Autowired
    private SearchStatsRepository searchStatsRepository;


    @Autowired
    public ProductController(ProductService productService, SearchStatsRepository searchStatsRepository, SearchStatsRepository searchStatsRepository1) {
        this.productService = productService;

        this.searchStatsRepository = searchStatsRepository1;
    }

    @GetMapping("/home")
    // http://localhost:8080/Products/home
    public String home() {
        return "\n\t\t ____ __ _________ _____________ \n\t\t|  Home Of Shopping Application  |\n\t\t ____ __ _________ _____________ ";
    }

    @PostMapping("/add")
//   http://localhost:8080/Products/add
//   name category  description quantity photo
    public ResponseEntity<?> addProduct(
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("photo") MultipartFile photo) {

        try {
            Product product = new Product();
            product.setName(name);
            product.setCategory(category);
            product.setDescription(description);
            product.setQuantity(quantity);

            byte[] originalPhotoBytes = photo.getBytes();
            Product savedProduct = productService.addProduct(product, originalPhotoBytes);

            return ResponseEntity.ok("Product added successfully: " + savedProduct.getName());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing image: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/product/{name}")
    // http://localhost:8080/Products/product/laptop
    public ResponseEntity<String> getProductsByName(@PathVariable String name) {
        List<Product> products = productService.getProductsByName(name);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Increment the search count for each product since they are being accessed here
        products.forEach(this::incrementSearchCount);

        StringBuilder response = new StringBuilder("<h1 style=\"text-align: center;\">").append(name).append(" Products</h1>\n");
        for (Product product : products) {
            // Add product photo with center alignment
            response.append("<div style=\"text-align: center; margin-bottom: 20px;\">")
                    .append("<img src=\"data:image/jpeg;base64,")
                    .append(getBase64EncodedPhoto(product.getPhoto()))
                    .append("\" alt=\"").append(product.getName()).append(" Photo\" style=\"max-width:100%; height:auto; display:block; margin:auto;\">")
                    .append("</div>");

            response.append("<div style=\"text-align: center; margin-bottom: 20px;\">")
                    .append("<h2>").append(product.getName()).append("</h2>\n")
                    .append("<p>")
                    .append(product.getDescription()).append("<br>")
                    .append("<strong>Category: </strong>").append(product.getCategory()).append("<br>")
                    .append("<strong>Stock:</strong> ").append(product.getQuantity()).append("<br>")
                    .append("<strong>Rating:</strong> ")
                    .append(product.getAvgRating() != null ? String.format("%.2f / 5", product.getAvgRating()) : "N/A").append("<br>")
                    .append("<strong>No Of Reviews Given:</strong> ").append(product.getNoOfVoted() != null ? product.getNoOfVoted() : "N/A")
                    .append("</p>")
                    .append("</div>");
        }

        return ResponseEntity.ok(response.toString());
    }

    @Transactional
    public void incrementSearchCount(Product product) {
        // Find SearchStat by product name
        Optional<SearchStat> optionalSearchStat = searchStatsRepository.findByProductName(product.getName());
        SearchStat searchStat;

        if (optionalSearchStat.isPresent()) {
            // If SearchStat exists, increment its count
            searchStat = optionalSearchStat.get();
            searchStat.incrementCount();
        } else {
            // If SearchStat doesn't exist, create a new one
            searchStat = new SearchStat(product, 1);
        }
        // Save or update the SearchStat object in the database
        searchStatsRepository.save(searchStat);
    }

    private String getBase64EncodedPhoto(byte[] photoBytes) {
        if (photoBytes == null || photoBytes.length == 0) {
            return "";
        }
        return Base64.getEncoder().encodeToString(photoBytes);
    }


    @GetMapping(value = "/get/all", produces = MediaType.TEXT_HTML_VALUE)
     // http://localhost:8080/Products/get/all
    public ResponseEntity<String> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        StringBuilder response = new StringBuilder("<!DOC TYPE html>\n");
        response.append("<html><head><title>All Products</title></head><body>");
        response.append("<h1 style=\"text-align: center;\">All Products</h1>\n");

        int count = 0;
        response.append("<div style=\"display: flex; flex-wrap: wrap; justify-content: center;\">"); // Ensure products are displayed in a single row

        for (Product product : products) {
            // Add product details with center alignment within a single block
            response.append("<div style=\"text-align: center; margin: 20px; width: 30%;\">")
                    .append("<img src=\"data:image/jpeg;base64,")
                    .append(getBase64EncodedPhoto(product.getPhoto()))
                    .append("\" alt=\"Product Photo\" style=\"max-width:100%; height:auto; display:block; margin:auto;\">")
                    .append("<h2>").append(product.getName()).append("</h2>\n")
                    .append("<p>")
                    .append(product.getDescription()).append("<br>")
                    .append("<strong>Stock:</strong> ").append(product.getQuantity()).append("<br>")
                    .append("<strong>Rating:</strong> ")
                    .append(product.getAvgRating() != null ? String.format("%.2f / 5", product.getAvgRating()) : "N/A").append("<br>")
                    .append("<strong>No Of Reviews Given:</strong> ").append(product.getNoOfVoted() != null ? product.getNoOfVoted() : "N/A")
                    .append("</p>")
                    .append("</div>");
            count++;
        }

        response.append("</div>");
        response.append("</body></html>");

        return ResponseEntity.ok(response.toString());
    }


    @GetMapping("/**")
    public ResponseEntity<String> handleUnmappedRequests() {
        String response = "<h1>Error 404 : Page Not Found</h1>\n"
                + "<p>This End Point Url Not Mapped.</p>";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @PatchMapping(value = "/update/{name}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProductPartiallyByName(@PathVariable String name,
                                                          @RequestPart("update") Map<String, Object> updates,
                                                          @RequestPart(value = "productPhoto", required = false) MultipartFile productPhoto) {
        try {
            if (productPhoto != null && !productPhoto.isEmpty()) {
                updates.put("productPhoto", productPhoto.getBytes());
            }
            Product updatedProduct = productService.updateProductFieldsByName(name, updates);
            if (updatedProduct != null) {
                return ResponseEntity.ok("Product '" + updatedProduct.getName() + "' successfully updated.");
            } else {
                return ResponseEntity.status(404).body("Product with name '" + name + "' not found.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }


    @DeleteMapping("/delete/{name}")
    // http://localhost:8080/Products/delete/
    public ResponseEntity<String> deleteProductByName(@PathVariable String name) {
        try {
            boolean isDeleted = productService.deleteProductByName(name);
            if (isDeleted) {
                return ResponseEntity.ok("Product named '" + name + "' was deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No product found with the name '" + name + "'.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/byNameContaining/{keyword}")
    // http://localhost:8080/Products/byNameContaining/
    public ResponseEntity<String> getProductsByNameContaining(@PathVariable String keyword) {
        try {
            List<Product> products = productService.getProductsByCategoryContaining(keyword);
            if (products.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            StringBuilder response = new StringBuilder("<h1 style=\"text-align: center;\">Products Matching Name: ")
                    .append(keyword)
                    .append("</h1>\n");

            for (Product product : products) {
                // Add product photo with center alignment
                response.append("<div style=\"text-align: center; margin-bottom: 20px;\">")
                        .append("<img src=\"data:image/jpeg;base64,")
                        .append(getBase64EncodedPhoto(product.getPhoto()))
                        .append("\" alt=\"Product Photo\" style=\"max-width:100%; height:auto; display:block; margin:auto;\">")
                        .append("</div>");

                // Add product details with center alignment
                response.append("<div style=\"text-align: center; margin-bottom: 20px;\">")
                        .append("<h2>").append(product.getName()).append("</h2>\n")

                        .append("<p>")
                        .append(product.getDescription()).append("<br>")
                        .append("<strong>Stock :</strong> ").append(product.getQuantity()).append("<br>")
                        .append("<strong>Rating :</strong> ")
                        .append(product.getAvgRating() != null ? String.format("%.2f / 5", product.getAvgRating()) : "N/A").append("<br>")
                        .append("<strong>No Of Reviews Given :</strong> ").append(product.getNoOfVoted() != null ? product.getNoOfVoted() : "N/A")
                        .append("</p>")
                        .append("</div>");
            }

            return ResponseEntity.ok(response.toString());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }


}