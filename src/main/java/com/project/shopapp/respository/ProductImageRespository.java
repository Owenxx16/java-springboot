package com.project.shopapp.respository;

import com.project.shopapp.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRespository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(long productId);
}
