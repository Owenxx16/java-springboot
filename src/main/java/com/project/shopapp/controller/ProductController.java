package com.project.shopapp.controller;

import com.github.javafaker.Faker;
import com.project.shopapp.dto.CategoryDTO;
import com.project.shopapp.dto.ProductDTO;
import com.project.shopapp.dto.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.model.ProductImage;
import com.project.shopapp.model.Products;
import com.project.shopapp.responses.ProductListResponse;
import com.project.shopapp.responses.ProductResponse;
import com.project.shopapp.service.IProductService;
import com.project.shopapp.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @GetMapping()
    public ResponseEntity<ProductListResponse> getAllProducts(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        // Tạo Pageable từ thông tin trang & giới hạn
        // Page bắt đâu` tu` 0
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> products = productService.getAllProducts(pageRequest);
        // Lấy tổng số trang
        int totalPages = products.getTotalPages();
        List<ProductResponse> products1 = products.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                        .products(products1)
                        .totalPage(totalPages)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
        try {
            Products products = productService.getProductsById(id);
            return ResponseEntity.ok(ProductResponse.fromProduct(products));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
    public ResponseEntity<?> uploadImage(@PathVariable("id") Long productId,@RequestParam("files") List<MultipartFile> files) throws IOException {
        // Nếu không có hình thì null
        try {
            Products existingProduct = productService.getProductsById(productId);
            //files = files == null ? new ArrayList<MultipartFile>() : files;
            files = files == null ? new ArrayList<>() : new ArrayList<>(files);
            int numberOfFiles = files == null ? 0 : files.size();
            if(numberOfFiles >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
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
        if(!isImageFile(file) && file.getOriginalFilename() == null ){
            throw new IOException("Invalid image file");
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
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

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType == null || !contentType.startsWith("image/");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable("id") Long id, @RequestBody ProductDTO productDTO) {
        try {
            Products products = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(products.toString());
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id){
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(String.format("Product with id = %d has been deleted", id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/generateFakeProducts")
    public ResponseEntity<String> generateFakeProducts(){
        Faker faker = new Faker();
        for(int i = 0; i < 1_000_000; i++){
            String productName = faker.commerce().productName();
            if(productService.existsByName(productName)){
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(1_000_000,90_000_000))
                    .thumbnail("")
                    .description(faker.lorem().word())
                    .categoryId((long) faker.number().numberBetween(2,5))
                    .build();
            try {

                productService.addProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok(String.format("Successfully generated product"));
    }
}

//{
//        "name": "MacBook pro m1 2025",
//        "price": 30000000,
//        "thumbnail": "",
//        "description": "The best laptop in 2025",
//        "category_id": 1
//        }