package com.example.gcashtrainingspringboot.controller;

import com.example.gcashtrainingspringboot.dto.ProductResponse;
import com.example.gcashtrainingspringboot.model.Product;
import com.example.gcashtrainingspringboot.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

//    @GetMapping
//    public List<Product> getAll(){
//        return productService.findAllProducts();
//    }

    //Modify GET to accept Pageable
    //Change default size to 2 just to test pageability
    @GetMapping
    public Page<ProductResponse> getAllProductsPageable(@PageableDefault(size = 2) Pageable pageable) {
        return productService.findAllProducts(pageable).map(this::toResponse);
    }

    @GetMapping("/search")
    public Page<Product> getProducts(@RequestParam(required = false) String searchKeyword, Pageable pageable) {
        if (searchKeyword != null && !searchKeyword.isEmpty() && !searchKeyword.isBlank()) return productService.searchProducts(searchKeyword.trim(), pageable);
        else return productService.findAllProducts(pageable);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductByID(@PathVariable Long id) {
        return productService.findProductByID(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@Valid @RequestBody Product newProduct) {
        return productService.saveProduct(newProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);

        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return notFound().build(); // 404 Not Found
        }
    }

    // @PatchMapping
    // Removed @Valid annotation to skip validation ss
    @PatchMapping("/{id}")
    public ResponseEntity<Product> patchProduct(@PathVariable Long id, @RequestBody Product patchData) {
        return productService.patch(id, patchData).map(ResponseEntity::ok).orElseGet(() -> notFound().build());
    }

    // @PutMapping
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product updatedProduct) {
        return productService.update(id, updatedProduct).map(ResponseEntity::ok).orElseGet(() -> notFound().build());
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }

}
