package com.project.shopapp.service;

import com.project.shopapp.dto.ProductDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.model.Category;
import com.project.shopapp.model.Products;
import com.project.shopapp.respository.CategoryRespository;
import com.project.shopapp.respository.ProductRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRespository productRespository;
    private final CategoryRespository categoryRespository;
    @Override
    public Products addProduct(ProductDTO productDTO) throws DataNotFoundException {
       Category existCategory = categoryRespository.findById((long) productDTO.getCategoryId())
               .orElseThrow(() -> new DataNotFoundException("Category id not found"));
        Products products = Products.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .categoryId(existCategory)
                .build();
        return productRespository.save(products);
    }

    @Override
    public Products getProductsById(Long id) throws DataNotFoundException {
        return productRespository.findById(id).orElseThrow(() -> new DataNotFoundException("Khong tim thay"));
    }

    @Override
    public Page<Products> getAllProducts(Pageable pageable) {
        return null;
    }

    @Override
    public void deleteProduct(Long id) {

    }

    @Override
    public Products updateProduct(Long id, ProductDTO productDTO) {
        return null;
    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }
}
