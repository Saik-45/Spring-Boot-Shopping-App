package com.sai.Product.Controller.UserController;

import com.sai.Product.Request.BuyRequest;
import com.sai.Product.Service.UserService.ByProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/Products")
public class ByProductController {

    private final ByProductService byProductService;

    public ByProductController(ByProductService byProductService) {
        this.byProductService = byProductService;
    }

    @PostMapping("/product/buyProduct")
    // http://localhost:8080/Products/product/buyProduct
    public ResponseEntity<String> buyProductByName(@RequestBody BuyRequest buyRequest) {
        try {
            boolean isBought = byProductService.buyProductByName(buyRequest.getProductName(), buyRequest.getQuantity());
            if (isBought) {
                return ResponseEntity.ok("Successfully bought " + buyRequest.getProductName() +
                        " - " + buyRequest.getQuantity());
            } else {
                return ResponseEntity.badRequest().body("Failed to buy product. Insufficient quantity available or product not found.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while processing the request.");
        }
    }

//    {
//        "productName": "Product Name",
//            "quantity": 1
//    }





}
