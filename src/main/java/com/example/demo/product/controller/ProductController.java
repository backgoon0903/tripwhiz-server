package com.example.demo.product.controller;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.product.dto.ProductListDTO;
import com.example.demo.product.dto.ProductReadDTO;
import com.example.demo.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@Log4j2
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 생성
    @PostMapping("/add")
    public ResponseEntity<Long> createProduct(@RequestBody ProductListDTO productListDTO) {
        log.info("Creating new product");
        Long pno = productService.createProduct(productListDTO);
        log.info("Product created with ID: {}", pno);
        return ResponseEntity.ok(pno);
    }

    // 상품 수정
    @PutMapping("/update/{pno}")
    public ResponseEntity<Long> updateProduct(@PathVariable Long pno, @RequestBody ProductListDTO productListDTO) {
        log.info("Updating product with ID: {}", pno);
        productService.updateProduct(pno, productListDTO);
        log.info("Product updated with ID: {}", pno);
        return ResponseEntity.ok(pno);
    }

    // 상품 삭제
    @DeleteMapping("/delete/{pno}")
    public ResponseEntity<Long> deleteProduct(@PathVariable Long pno) {
        log.info("Deleting product with ID: {}", pno);
        productService.deleteProduct(pno);
        log.info("Product deleted with ID: {}", pno);
        return ResponseEntity.ok(pno);
    }

    // 기본 상품 목록 조회
    @GetMapping("/list")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> list(@Validated PageRequestDTO requestDTO) {
        log.info("Fetching product list");
        PageResponseDTO<ProductListDTO> response = productService.list(requestDTO);
        log.info("Product list response: {}", response);
        return ResponseEntity.ok(response);
    }

    // 특정 상품 ID로 조회
    @GetMapping("/read/{pno}")
    public ResponseEntity<ProductReadDTO> getProduct(@PathVariable Long pno) {
        log.info("Fetching product with ID: {}", pno);
        Optional<ProductReadDTO> productObj = productService.getProductById(pno);
        return productObj.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 상위 카테고리(cno)로 상품 목록 조회
    @GetMapping("/list/category")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> listByCategory(@RequestParam Long cno, @Validated PageRequestDTO requestDTO) {
        log.info("Fetching product list by category ID (cno): {}", cno);
        requestDTO.setCategoryCno(cno);
        PageResponseDTO<ProductListDTO> response = productService.listByCategory(cno, requestDTO);
        log.info("Product list by category response: {}", response);
        return ResponseEntity.ok(response);
    }

    // 하위 카테고리(scno)로 상품 목록 조회
    @GetMapping("/list/subcategory")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> listBySubCategory(@RequestParam Long scno, @Validated PageRequestDTO requestDTO) {
        log.info("Fetching product list by sub-category ID (scno): {}", scno);
        PageResponseDTO<ProductListDTO> response = productService.listBySubCategory(scno, requestDTO);
        log.info("Product list by sub-category response: {}", response);
        return ResponseEntity.ok(response);
    }

    // 테마 카테고리로 상품 목록 조회
    @GetMapping("/list/theme")
    public ResponseEntity<PageResponseDTO<ProductListDTO>> listByTheme(@RequestParam String themeCategory, @Validated PageRequestDTO requestDTO) {
        log.info("Fetching product list by theme category: {}", themeCategory);
        PageResponseDTO<ProductListDTO> response = productService.listByTheme(themeCategory, requestDTO);
        log.info("Product list by theme response: {}", response);
        return ResponseEntity.ok(response);
    }
}
