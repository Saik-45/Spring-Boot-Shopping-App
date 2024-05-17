package com.sai.Product.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SearchStat searchStat;


    @NotBlank
    @Size(max = 255)
    @Column(name = "product_name")
    private String name;

    @NotBlank
    @Size(max = 1000)
    @Column(name = "product_description")
    private String description;

    @NotBlank
    @Size(max = 255)
    @Column(name = "product_category")
    private String category;

    @NotNull
    @Column(name = "product_quantity")
    private Integer quantity;

    @Lob
    @Column(name = "product_photo")
    private byte[] productPhoto;

    @Column(name = "product_code", unique = true, nullable = false, updatable = false)
    private String productCode;

    @NotNull
    @Column(name = "sold_quantity")
    private Integer soldQuantity = 0;

    @Column(name = "avg_rating")
    private Double avgRating;

    @Column(name = "no_of_voted")
    private Integer noOfVoted;


    public Product() {
    }

    public Product(Long id, SearchStat searchStat,  String name, String description, String category, Integer quantity, byte[] photo, String productCode, Integer soldQuantity) {
        this.id = id;
        this.searchStat = searchStat;
        this.name = name;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
        this.productPhoto = productPhoto;
        this.productCode = productCode;
        this.soldQuantity = soldQuantity;

        this.avgRating = avgRating;
        this.noOfVoted = noOfVoted;
    }

    @PrePersist
    private void generateProductCode() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String nameInitial = name.contains(" ") ? name.substring(0, name.indexOf(" ")) : name;
        this.productCode = date + nameInitial + System.nanoTime(); // Added nanoTime for uniqueness
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SearchStat getSearchStat() {
        return searchStat;
    }

    public void setSearchStat(SearchStat searchStat) {
        this.searchStat = searchStat;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public byte[] getPhoto() {
        return productPhoto;
    }

    public void setPhoto(byte[] photo) {
        this.productPhoto = photo;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Integer getNoOfVoted() {
        return noOfVoted;
    }

    public void setNoOfVoted(Integer noOfVoted) {
        this.noOfVoted = noOfVoted;
    }
}