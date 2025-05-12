package com.project.shopapp.service;

import com.project.shopapp.dto.ProductDTO;
import com.project.shopapp.dto.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.model.ProductImage;
import com.project.shopapp.model.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface IProductService {
    Products addProduct(ProductDTO productDTO) throws Exception;
    Products getProductsById(Long id) throws DataNotFoundException;
    Page<Products> getAllProducts(Pageable pageable);
    void deleteProduct(Long id);
    public Products updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException;
    boolean existsByName(String name);
    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException;
}
