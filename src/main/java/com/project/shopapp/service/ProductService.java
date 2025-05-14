package com.project.shopapp.service;

import com.project.shopapp.dto.ProductDTO;
import com.project.shopapp.dto.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.model.Category;
import com.project.shopapp.model.ProductImage;
import com.project.shopapp.model.Products;
import com.project.shopapp.responses.ProductResponse;
import com.project.shopapp.respository.CategoryRespository;
import com.project.shopapp.respository.ProductImageRespository;
import com.project.shopapp.respository.ProductRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Optional;

@Service
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
                .description(productDTO.getDescription())
                .categoryId(existCategory)
                .build();
        return productRespository.save(products);
    }

    @Override
    public Products getProductsById(Long id) throws DataNotFoundException {
        return productRespository.findById(id).orElseThrow(() -> new DataNotFoundException("Khong tim thay"));
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRespository.findAll(pageable).map(ProductResponse::fromProduct);
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
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException {
        // Lấy entity Products theo productId
        Products existingProduct = productRespository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy Product Id " + productId));

        // Kiểm tra số lượng ảnh đã lưu


        // Tạo đối tượng ProductImage và gán lại trường product
        ProductImage productImage = ProductImage.builder()
                .product(existingProduct)
                .image_url(productImageDTO.getImage_url())
                .build();

        int size = productImageRespository.findByProductId(productId).size();
        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParameterException("Number of image must be <= 5");
        }

        return productImageRespository.save(productImage);
    }
}
