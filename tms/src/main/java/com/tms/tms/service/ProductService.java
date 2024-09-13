package com.tms.tms.service;

import com.tms.tms.dto.ProductRequestDto;
import com.tms.tms.dto.ProductResponseDto;
import com.tms.tms.exception.ErrorCode;
import com.tms.tms.exception.TmsCustomException;
import com.tms.tms.model.Product;
import com.tms.tms.model.Store;
import com.tms.tms.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final StoreService storeService;

    @Transactional
    public ProductResponseDto createProduct(@Valid ProductRequestDto productRequestDto) {
        Store store = storeService.findByIdOrElseThrow(productRequestDto.getStoreId());
        Long hubId = store.getHub().getHubId();

        Product product = productRepository.save(new Product(productRequestDto, store, hubId));
        return new ProductResponseDto(product);

    }

    @Transactional
    public ProductResponseDto updateProduct(Long productId, @Valid ProductRequestDto productRequestDto, String username, String role) {
        Product product = findByIdOrElseThrow(productId);

        if(role.equals("STORE")){
            if(!product.getCreatedBy().equals(username)){
                throw new TmsCustomException(ErrorCode.PERMISSION_DENIED);
            }
        }

        Store store = storeService.findByIdOrElseThrow(productRequestDto.getStoreId());
        Long hubId = store.getHub().getHubId();
        product.updateProduct(productRequestDto, store, hubId);
        return new ProductResponseDto(product);
    }

    @Transactional
    public void deleteProduct(Long productId, String username, String role) {
        Product product = findByIdOrElseThrow(productId);

        if(role.equals("STORE")){
            if(!product.getCreatedBy().equals(username)){
                throw new TmsCustomException(ErrorCode.PERMISSION_DENIED);
            }
        }

        product.setDelete(true);
        product.delete(username);
    }

    public ProductResponseDto getProduct(Long productId) {
        return new ProductResponseDto(findByIdOrElseThrow(productId));
    }

    public Page<ProductResponseDto> getProducts(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productList = productRepository.findAll(pageable);
        return productList.map(ProductResponseDto::new);
    }



    public Product findByIdOrElseThrow(Long productId){
        return productRepository.findById(productId).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_PRODUCT)
        );
    }

}
