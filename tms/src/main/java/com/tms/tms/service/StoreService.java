package com.tms.tms.service;

import com.tms.tms.application.UserService;
import com.tms.tms.dto.ProductResponseDto;
import com.tms.tms.dto.StoreRequestDto;
import com.tms.tms.dto.StoreResponseDto;
import com.tms.tms.exception.ErrorCode;
import com.tms.tms.exception.TmsCustomException;
import com.tms.tms.model.Hub;
import com.tms.tms.model.Product;
import com.tms.tms.model.Store;
import com.tms.tms.repository.ProductRepository;
import com.tms.tms.repository.StoreRepository;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final HubService hubService;
    private final ProductRepository productRepository;
    private final UserService userService;

    public StoreService(StoreRepository storeRepository, HubService hubService, ProductRepository productRepository, @Lazy UserService userService){
        this.storeRepository = storeRepository;
        this.hubService = hubService;
        this.productRepository = productRepository;
        this.userService = userService;
    }


    @Transactional
    public StoreResponseDto createStore(@Valid StoreRequestDto storeRequestDto, String username) {
        //유저 한번더 확인
        if(!userService.verifyUser(username)) {
            throw new TmsCustomException(ErrorCode.NOT_FOUND_USER);
        }

        Hub hub = hubService.findByIdOrElseThrow(storeRequestDto.getHubId());
        Store store =  storeRepository.save(new Store(storeRequestDto, username, hub));
        return new StoreResponseDto(store);
    }

    @Transactional
    public StoreResponseDto updateStore(Long storeId, @Valid StoreRequestDto storeRequestDto, String username, String role) {
        if(!userService.verifyUser(username)) {
            throw new TmsCustomException(ErrorCode.NOT_FOUND_USER);
        }

        Store store = findByIdOrElseThrow(storeId);

        if(role.equals("STORE")){
            if(!store.getCreatedBy().equals(username)){
                throw new TmsCustomException(ErrorCode.PERMISSION_DENIED);
            }
        }

        Hub hub = hubService.findByIdOrElseThrow(storeRequestDto.getHubId());
        store.updateStore(storeRequestDto, hub);
        return new StoreResponseDto(store);
    }

    @Transactional
    public void deleteStore(Long storeId, String username, String role) {
        if(!userService.verifyUser(username)) {
            throw new TmsCustomException(ErrorCode.NOT_FOUND_USER);
        }

        Store store = findByIdOrElseThrow(storeId);

        if(role.equals("STORE")){
            if(!store.getCreatedBy().equals(username)){
                throw new TmsCustomException(ErrorCode.PERMISSION_DENIED);
            }
        }

        List<Product> productList = productRepository.findAllByStore(store);
        for(Product product : productList){
            product.setDelete(true);
            product.delete(username);
        }

        store.setDelete(true);
        store.delete(username);
    }


    public StoreResponseDto getStore(Long storeId) {
        return new StoreResponseDto(findByIdOrElseThrow(storeId));
    }


    public Page<StoreResponseDto> getStores(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Store> storeList = storeRepository.findAll(pageable);
        return storeList.map(StoreResponseDto::new);
    }



    public List<StoreResponseDto> getStoreSearch(String keyword) {
        List<Store> storeList = storeRepository.findAllByStoreNameContaining(keyword);
        return storeList.stream()
                .map(StoreResponseDto::new)
                .collect(Collectors.toList());
    }


    public List<ProductResponseDto> getProductByStore(Long storeId) {
        Store store = findByIdOrElseThrow(storeId);
        List<Product> productList = productRepository.findAllByStore(store);

        return productList.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDto> getProductSearchByStore(Long storeId, String keyword) {
        Store store = findByIdOrElseThrow(storeId);
        List<Product> productList = productRepository.findAllByStoreAndProductNameContaining(store, keyword);

        return productList.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }

    public Store findByIdOrElseThrow(Long storeId){
        return storeRepository.findById(storeId).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_STORE)
        );
    }
}
