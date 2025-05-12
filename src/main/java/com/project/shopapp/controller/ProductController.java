package com.project.shopapp.controller;

import com.project.shopapp.dto.CategoryDTO;
import com.project.shopapp.dto.ProductDTO;
import com.project.shopapp.dto.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.model.ProductImage;
import com.project.shopapp.model.Products;
import com.project.shopapp.service.IProductService;
import com.project.shopapp.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @GetMapping()
    public ResponseEntity<String> getAllProducts(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return ResponseEntity.ok(String.format("Hello World, page = %d, limit = %d", page, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
        try {
            Products products = productService.getProductsById(id);
            return ResponseEntity.ok(products);
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    // Thêm request upload file ảnh
    @PostMapping(value = "") //, consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO,
                                           // @ModelAttribute("files") List<MultipartFile> files,
                                           BindingResult bindingResult
                                           //@RequestPart("file") MultipartFile file
    ) {
        try{
        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        Products newProduct = productService.addProduct(productDTO);
        return ResponseEntity.ok(newProduct);
    }catch (Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@PathVariable("id") Long productId,@ModelAttribute("files") List<MultipartFile> files) throws IOException {
        // Nếu không có hình thì null
        try {
            Products existingProduct = productService.getProductsById(productId);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if(files.size() >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
                return ResponseEntity.badRequest().body("You can only upload more than 5 images");
            }
            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.getSize() == 0){
                    continue;
                }
                // Kiểm tra kích thước & định dạng của file
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("file is too large! Maxium size is 10MB");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
                }
                // Lưu file và cập nhật thumbnail vào trong DTO
                String fileName = storeFile(file);
                // lưu vào đối tượng product trong DB -> Do it after
                // lưu vào bảng product_image
                ProductImage productImage = productService.createProductImage(existingProduct.getId(),
                        ProductImageDTO.builder()
                        .image_url(fileName)
                        .build());
                productImages.add(productImage);
            }
            return ResponseEntity.ok(productImages);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // Thêm UUID vào trước mỗi hình để tránh việc ghì đè bởi hình ảnh có tên giống nhau mà nội dung khác nhau
        String uniqueName = UUID.randomUUID().toString() + "_" + fileName;
        // Thêm đường dẫn đến thư mục muốn lưu file
        Path uploadDir = Paths.get("uploads");
        // Kiểm tra thư mục uploadDir đã tồn tại hay chưa nếu chưa thì tạo
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(), uniqueName);
        // Copy file vào thư mục
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueName;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id){
        return ResponseEntity.ok(String.format("Product with id = %d", id));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        return ResponseEntity.ok(String.format("Product with id = %d deleted", id));
    }
}

//{
//        "name": "MacBook pro m1 2025",
//        "price": 30000000,
//        "thumbnail": "",
//        "description": "The best laptop in 2025",
//        "category_id": 1
//        }