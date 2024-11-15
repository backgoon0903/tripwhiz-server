package com.example.demo.product.service;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.product.domain.Product;
import com.example.demo.product.dto.ProductListDTO;
import com.example.demo.product.dto.ProductReadDTO;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.category.domain.Category;
import com.example.demo.category.domain.SubCategory;
import com.example.demo.category.repository.CategoryRepository;
import com.example.demo.category.repository.SubCategoryRepository;
import com.example.demo.util.CustomFileUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final CustomFileUtil customFileUtil;

    // 기본 상품 목록 조회
    public PageResponseDTO<ProductListDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("Fetching product list with pagination");
        return productRepository.listByCno(pageRequestDTO);
    }

    // 상품 ID로 단일 상품 조회
    public Optional<ProductReadDTO> getProductById(Long pno) {
        return productRepository.read(pno);
    }

    // 상위 카테고리(cno)로 상품 목록 조회
    public PageResponseDTO<ProductListDTO> listByCategory(Long cno, PageRequestDTO pageRequestDTO) {
        log.info("Fetching product list by category ID (cno): {}", cno);
        return productRepository.listByCategory(cno, pageRequestDTO);
    }

    // 하위 카테고리(scno)로 상품 목록 조회
    public PageResponseDTO<ProductListDTO> listBySubCategory(Long scno, PageRequestDTO pageRequestDTO) {
        log.info("Fetching product list by sub-category ID (scno): {}", scno);
        return productRepository.listBySubCategory(scno, pageRequestDTO);
    }

    // 테마 카테고리로 상품 목록 조회
    public PageResponseDTO<ProductListDTO> listByTheme(String themeCategory, PageRequestDTO pageRequestDTO) {
        log.info("Fetching product list by theme category: {}", themeCategory);
        return productRepository.listByTheme(themeCategory, pageRequestDTO);
    }

    // 상품 정보와 이미지를 함께 조회
    public Optional<ProductReadDTO> getProductWithImage(Long pno) {
        Optional<ProductReadDTO> productOptional = productRepository.read(pno);

        return productOptional.map(product -> {
            if (product.getImages() != null && !product.getImages().isEmpty()) {
                String firstImageFileName = product.getImages().get(0).getFileName();
                customFileUtil.getFile(firstImageFileName);
                log.info("Image retrieved for product: {}", firstImageFileName);
            }
            return product;
        });
    }

    // 상품 생성
    public Long createProduct(ProductListDTO productListDTO) {
        Category category = categoryRepository.findById(productListDTO.getCategoryCno())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        SubCategory subCategory = subCategoryRepository.findById(productListDTO.getSubCategoryScno())
                .orElseThrow(() -> new RuntimeException("SubCategory not found"));

        Product product = productListDTO.toEntity(category, subCategory);
        Product savedProduct = productRepository.save(product);

        log.info("Product created with ID: {}", savedProduct.getPno());
        return savedProduct.getPno();
    }

    // 상품 수정
    public Long updateProduct(Long pno, ProductListDTO productListDTO) {
        Product product = productRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // DTO의 데이터를 이용하여 Product 엔티티 업데이트
        product.updateFromDTO(productListDTO);
        productRepository.save(product);

        log.info("Product updated with ID: {}", pno);
        return pno;
    }

    // 상품 삭제
    public Long deleteProduct(Long pno) {
        productRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.deleteById(pno);
        log.info("Product deleted with ID: {}", pno);
        return pno;
    }
}
