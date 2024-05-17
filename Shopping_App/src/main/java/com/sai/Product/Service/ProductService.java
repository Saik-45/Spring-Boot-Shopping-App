package com.sai.Product.Service;

import com.sai.Product.Entity.Product;
import com.sai.Product.Entity.SearchStat;
import com.sai.Product.ImageSize.ImageUtils;
import com.sai.Product.Repository.ProductRepository;
import com.sai.Product.Repository.SearchStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;

@Service
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final SearchStatsRepository  searchStatsRepository;


    @Autowired
    public ProductService(ProductRepository productRepository, SearchStatsRepository searchStatsRepository) {
        this.productRepository = productRepository;
        this.searchStatsRepository = searchStatsRepository;
    }

    @Transactional
    public Product addProduct(Product product, byte[] originalPhotoBytes) throws IOException {
        Assert.notNull(product, "Product must not be null");

        byte[] resizedImageBytes = ImageUtils.resizeAndStoreImage(originalPhotoBytes);
        product.setPhoto(resizedImageBytes);

        return productRepository.save(product);
    }

    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name.trim());
    }

    public Optional<SearchStat> findByProduct(Product product) {
        return searchStatsRepository.findByProductName(product);
    }

    public SearchStat saveOrUpdate(SearchStat searchStat) {
        return searchStatsRepository.save(searchStat);
    }


    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public boolean deleteProductByName(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        if (!products.isEmpty()) {
            productRepository.deleteAll(products);
            return true;
        } else {
            return false;
        }
    }


    @Transactional(readOnly = true)
    public List<Product> getProductsByCategoryContaining(String keyword) {
        Assert.notNull(keyword, "Keyword must not be null");
        return productRepository.findByCategoryContainingIgnoreCase(keyword.trim());
    }


    @Transactional
    public Product updateProductFieldsByName(String name, Map<String, Object> updates) {
        Optional<Product> productOptional = productRepository.findByName(name);
        if (!productOptional.isPresent()) {
            return null;
        }

        Product product = productOptional.get();
        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    product.setName((String) value);
                    break;
                case "description":
                    product.setDescription((String) value);
                    break;
                case "category":
                    product.setCategory((String) value);
                    break;
//                case "quantity":
//                    product.setQuantity((Integer) value);
//                    break;
//                case "soldQuantity":
//                    product.setSoldQuantity((Integer) value);
//                    break;
//                case "avgRating":
//                    product.setAvgRating((Double) value);
//                    break;
//                case "noOfVoted":
//                    product.setNoOfVoted((Integer) value);
//                    break;
                case "productPhoto": // Ensure proper handling for byte arrays (like decoding Base64 if necessary)
                    product.setPhoto((byte[]) value);
                    break;
                case "productCode":
                    throw new IllegalArgumentException("Updating product code is not allowed.");
            }
        });
        return productRepository.save(product);
    }


}
