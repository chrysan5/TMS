package com.tms.tms.service;

import com.tms.tms.dto.OrderRequestDto;
import com.tms.tms.dto.OrderResponseDto;
import com.tms.tms.exception.ErrorCode;
import com.tms.tms.exception.TmsCustomException;
import com.tms.tms.model.*;
import com.tms.tms.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final StoreService storeService;
    private final ProductService productService;


    @Transactional
    public OrderResponseDto createOrder(@Valid OrderRequestDto requestDto, Long storeId) {
        Store requestStore = storeService.findByIdOrElseThrow(storeId);

        Long receiveStoreId = requestDto.getReceiveStoreId();
        Store responseStore = storeService.findByIdOrElseThrow(receiveStoreId);
        Long endHubId = responseStore.getHub().getHubId();

        Product product = productService.findByIdOrElseThrow(requestDto.getProductId());

        Order order = new Order(requestDto, endHubId, requestStore, product);
        return new OrderResponseDto(orderRepository.save(order));
    }


    @Transactional
    public OrderResponseDto updateOrder(@Valid OrderRequestDto requestDto, Long orderId, String username, String role) {
        Order order = findByIdOrElseThrow(orderId);

        if(role.equals("STORE")){
            if(!order.getStore().getUsername().equals(username)){
                throw new TmsCustomException(ErrorCode.PERMISSION_DENIED);
            }
        }

        if(order.getState().equals("CANCELED") || order.getState().equals("COMPLETED")){
            throw new TmsCustomException(ErrorCode.CANT_MODIFY);
        }

        Long receiveStoreId = requestDto.getReceiveStoreId();
        Store responseStore = storeService.findByIdOrElseThrow(receiveStoreId);
        Long endHubId = responseStore.getHub().getHubId();

        Product product = productService.findByIdOrElseThrow(requestDto.getProductId());

        order.updateOrder(requestDto, endHubId, product);
        return new OrderResponseDto(order);
    }

    @Transactional
    public void cancelOrder(Long orderId, String username, String role) {
        Order order = findByIdOrElseThrow(orderId);

        if(role.equals("STORE")){
            if(!order.getStore().getUsername().equals(username)){
                throw new TmsCustomException(ErrorCode.PERMISSION_DENIED);
            }
        }

        order.setState(OrderState.CANCELED);
    }


    public OrderResponseDto getOrder(Long orderId, String username) {
        Order order = findByIdOrElseThrow(orderId);

        if(!order.getStore().getUsername().equals(username)){
            throw new TmsCustomException(ErrorCode.PERMISSION_DENIED);
        }

        return new OrderResponseDto(order);
    }


    public List<OrderResponseDto> getOrdersByStore(Long storeId) {
        Store store = storeService.findByIdOrElseThrow(storeId);
        List<Order> orderList = orderRepository.findAllByStore(store);

        return orderList.stream()
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDto updateOrderLocation(String location, Long orderId) {
        Order order = findByIdOrElseThrow(orderId);
        order.setLocation(OrderLocation.valueOf(location));
        return new OrderResponseDto(order);
    }

    @Transactional
    public OrderResponseDto updateOrderState(String state, Long orderId) {
        Order order = findByIdOrElseThrow(orderId);
        order.setState(OrderState.valueOf(state));
        return new OrderResponseDto(order);
    }


    public Order findByIdOrElseThrow(Long orderId){
        return orderRepository.findById(orderId).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_ORDER)
        );
    }

}
