package com.project.shopapp.service;

import com.project.shopapp.dto.ProductDTO;
import com.project.shopapp.dto.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.model.ProductImage;
import com.project.shopapp.model.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface IProductService {
    public Products addProduct(ProductDTO productDTO) throws DataNotFoundException;
    Products getProductsById(Long id) throws DataNotFoundException;
    Page<Products> getAllProducts(Pageable pageable);
    void deleteProduct(Long id);
    public Products updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException;
    boolean existsByName(String name);
    ProductImage createProductImage(Long id, ProductImageDTO productImageDTO) throws DataNotFoundException;
}
