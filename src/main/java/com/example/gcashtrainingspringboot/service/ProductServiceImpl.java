package com.example.gcashtrainingspringboot.service;

import com.example.gcashtrainingspringboot.model.Product;
import com.example.gcashtrainingspringboot.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    //LAB 1 - Implement pageable
    public Page<Product> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> findProductByID(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> update(Long id, Product newProduct) {
        Optional<Product> existing = productRepository.findById(id);
        if (existing.isPresent()){
            newProduct.setId(id);
            return Optional.of(productRepository.save(newProduct));
        }
        return Optional.empty();
    }

    //fixed bugs present in patch method body
    @Override
    public Optional<Product> patch(Long id, Product patchProduct) {
        Optional<Product> existing = productRepository.findById(id);
        if ((existing.isPresent())){
            Product exist = existing.get();

            if(patchProduct.getName() != null){
                exist.setName(patchProduct.getName());
            }

            if(patchProduct.getPrice() != null){
                exist.setPrice(patchProduct.getPrice());
            }

            return Optional.of(productRepository.save(exist));
        }


        return Optional.empty();
    }

    @Override
    public boolean deleteProduct(Long id) {
        if(productRepository.existsById(id)){
            productRepository.deleteById(id);
            return true;
        }
        return false;

    }

    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

}
