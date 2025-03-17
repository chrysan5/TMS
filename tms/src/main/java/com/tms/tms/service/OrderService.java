package com.tms.tms.service;

import com.tms.tms.dto.OrderRequestDto;
import com.tms.tms.dto.OrderResponseDto;
import com.tms.tms.exception.ErrorCode;
import com.tms.tms.exception.TmsCustomException;
import com.tms.tms.model.*;
import com.tms.tms.repository.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final OrderProductRepository orderProductRepository;


    @Transactional
    public void createOrder(OrderRequestDto orderRequestDto, String username) {
        Store requestStore = storeRepository.findByUsername(username).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_STORE));

        Cart cart = cartRepository.findByUsername(username).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_ORDER)
        );

        List<CartProduct> cartProducts = cartProductRepository.findAllByCart(cart);
        if (cartProducts.isEmpty()) {
            throw new TmsCustomException(ErrorCode.NOT_FOUND_CART_PRODUCT);
        }

        Order order = new Order(username, requestStore, orderRequestDto.getSellerStoreId());
        orderRepository.saveAndFlush(order);

        List<OrderProduct> orderProducts = new ArrayList<>();
        int totalQuantity = 0;
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartProduct cartProduct : cartProducts) {
            OrderProduct orderProduct = new OrderProduct(order, cartProduct.getProduct(), cartProduct.getQuantity(), cartProduct.getProduct().getPrice());
            orderProducts.add(orderProduct);

            totalQuantity += cartProduct.getQuantity();
            totalPrice = totalPrice.add(cartProduct.getProduct().getPrice().multiply(BigDecimal.valueOf(cartProduct.getQuantity())));
        }

        orderProductRepository.saveAll(orderProducts);
        cartProductRepository.deleteAll(cartProducts); //장바구니에 주문한 상품 비우기

        //order에 세부 값 추가
        order.updateOrder(orderProducts, totalQuantity, totalPrice);

        orderRepository.save(order);
    }

    //배송 상태가 pending인 경우만, 주문 후 12시간 이내만 수정 가능, 주문 업체만 변경 가능
    @Transactional
    public OrderResponseDto updateOrder(@Valid OrderRequestDto orderRequestDto, Long orderId) {
        Order order = findByIdOrElseThrow(orderId);

        /*if(!order.getState().equals("PENDING")){
            throw new TmsCustomException(ErrorCode.CANT_MODIFY_ORDER);
        }*/

        LocalDateTime createdTime = order.getCreatedAt();
        if(createdTime.isBefore(LocalDateTime.now().minus(12, ChronoUnit.HOURS))){
            throw new TmsCustomException(ErrorCode.CANT_MODIFY_ORDER);
        }

        order.setSellerStoreId(orderRequestDto.getSellerStoreId());
        return new OrderResponseDto(order);
    }


    public OrderResponseDto getOrder(Long orderId, String username) {
        Order order = findByIdOrElseThrow(orderId);

        if(!order.getStore().getUsername().equals(username)){
            throw new TmsCustomException(ErrorCode.PERMISSION_DENIED);
        }

        return new OrderResponseDto(order);
    }


    public List<OrderResponseDto> getOrdersByStore(String username) {
        Store store = storeRepository.findByUsername(username).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_STORE));

        List<Order> orderList = orderRepository.findAllByStore(store);

        return orderList.stream()
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());
    }

    /*@Transactional
    public OrderResponseDto updateOrderLocation(String location, Long orderId) {
        Order order = findByIdOrElseThrow(orderId);
        order.setLocation(OrderLocation.valueOf(location));
        return new OrderResponseDto(order);
    }*/

    //배송 상태가 pending인 경우만, 주문 후 3시간 이내만 취소 가능, 관리자의 경우 취소 가능
    @Transactional
    public OrderResponseDto cancelOrder(Long orderId, String role) {
        /*if(!order.getState().equals("PENDING")){
            throw new TmsCustomException(ErrorCode.CANT_MODIFY_ORDER);
        }*/

        Order order = findByIdOrElseThrow(orderId);

        if(!role.equals("MASTER")) {
            LocalDateTime createdTime = order.getCreatedAt();
            if (createdTime.isBefore(LocalDateTime.now().minus(3, ChronoUnit.HOURS))) {
                throw new TmsCustomException(ErrorCode.CANT_MODIFY_ORDER);
            }
        }

        order.setState(OrderState.CANCELED);

        //orderProduct를 모두 cartProduct로 되돌린 뒤 제거
        Cart cart = cartRepository.findByUsername(order.getUsername())
                .orElseGet(() -> cartRepository.save(new Cart(order.getUsername())));

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder(order);

        List<CartProduct> cartProducts = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            CartProduct cartProduct = new CartProduct(cart, orderProduct.getProduct(), orderProduct.getQuantity());
            cartProducts.add(cartProduct);
        }

        cartProductRepository.saveAll(cartProducts);
        orderProductRepository.deleteAll(orderProducts);
        orderProductRepository.flush(); //윗줄 위해 추가

        return new OrderResponseDto(order);
    }


    public Order findByIdOrElseThrow(Long orderId){
        return orderRepository.findById(orderId).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_ORDER)
        );
    }

}
