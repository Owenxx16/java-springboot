package com.project.shopapp.service;

import com.project.shopapp.dto.ProductDTO;
import com.project.shopapp.dto.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.model.Category;
import com.project.shopapp.model.ProductImage;
import com.project.shopapp.model.Products;
import com.project.shopapp.respository.CategoryRespository;
import com.project.shopapp.respository.ProductImageRespository;
import com.project.shopapp.respository.ProductRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.InvalidParameterException;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRespository productRespository;
    private final CategoryRespository categoryRespository;
    private final ProductImageRespository productImageRespository;
    @Override
    public Products addProduct(ProductDTO productDTO) throws DataNotFoundException {
       Category existCategory = categoryRespository.findById( productDTO.getCategoryId())
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
        return productRespository.findAll(pageable);
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Products> productOptional = productRespository.findById(id);
        productOptional.ifPresent(productRespository::delete);
    }

    @Override
    public Products updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException {
        Products existingProduct = productRespository.findById(id).orElseThrow(() -> new DataNotFoundException("Khong co san pham"));
        if(existingProduct != null){
            Category category = categoryRespository.findById(productDTO.getCategoryId()).orElseThrow(() -> new DataNotFoundException("Category id not found"));
            existingProduct.setName(productDTO.getName());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setCategoryId(category);
            return productRespository.save(existingProduct);
        }
        return null;
    }

    @Override
    public boolean existsByName(String name) {
        return productRespository.existsByName(name);
    }
    @Override
    public ProductImage createProductImage(Long id, ProductImageDTO productImageDTO) throws DataNotFoundException {
        Products existingProducts = productRespository.findById(productImageDTO.getProducts_id()).orElseThrow(
                () -> new DataNotFoundException("khong tim thay Product Id" + productImageDTO.getProducts_id())
        );
        ProductImage productImage = ProductImage.builder()
                .products_id(existingProducts)
                .image_url(productImageDTO.getImage_url())
                .build();
        // Khong cho insert qua 5 anh trong 1 product
        int size = productImageRespository.findByProductId(id).size();
        if (size >= 5) {
            throw new InvalidParameterException("Number of image must be <= 5");
        }
        return productImageRespository.save(productImage);
    }
}
