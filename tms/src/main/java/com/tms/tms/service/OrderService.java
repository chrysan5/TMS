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
    private final DeliveryRepository deliveryRepository;


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

        //배송 관련 로직
        Long startHubId = requestStore.getHub().getHubId();

        Store sellerStore = storeRepository.findById(orderRequestDto.getSellerStoreId()).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_STORE));
        Long endHubId = sellerStore.getHub().getHubId();

        Delivery delivery = new Delivery(orderRequestDto.getAddress(), startHubId, endHubId, order);
        deliveryRepository.save(delivery);

        order.setDelivery(delivery);
    }

    //배송 상태가 pending인 경우만, 주문 후 12시간 이내만 수정 가능, 주문 업체만 변경 가능
    @Transactional
    public OrderResponseDto updateOrder(@Valid OrderRequestDto orderRequestDto, Long orderId) {
        Order order = findByIdOrElseThrow(orderId);

        if(order.getState().equals("CANCELED")){
            throw new TmsCustomException(ErrorCode.ALREADY_CANCELED_ORDER);
        }else if(order.getState().equals("COMPLETED)")) {
            throw new TmsCustomException(ErrorCode.ALREADY_COMPLETED_ORDER);
        }

        LocalDateTime createdTime = order.getCreatedAt();
        if(createdTime.isBefore(LocalDateTime.now().minus(12, ChronoUnit.HOURS))){
            throw new TmsCustomException(ErrorCode.CANT_MODIFY_ORDER);
        }

        order.setSellerStoreId(orderRequestDto.getSellerStoreId());


        //배송 관련 로직
        if(!OrderLocation.PENDING.equals(order.getDelivery().getLocation())){
            throw new TmsCustomException(ErrorCode.ALREADY_IN_DELIVERY);
        }

        Store sellerStore = storeRepository.findById(orderRequestDto.getSellerStoreId()).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_STORE));

        order.getDelivery().setEndHubId(sellerStore.getHub().getHubId());
        order.getDelivery().setAddress(orderRequestDto.getAddress());

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


    //배송 상태가 pending인 경우만, 주문 후 3시간 이내만 취소 가능, 관리자의 경우 취소 가능
    @Transactional
    public OrderResponseDto cancelOrder(Long orderId, String role) {
        Order order = findByIdOrElseThrow(orderId);

        if(OrderState.CANCELED.equals(order.getState())){
            throw new TmsCustomException(ErrorCode.ALREADY_CANCELED_ORDER);
        }else if(OrderState.COMPLETED.equals(order.getState())) {
            throw new TmsCustomException(ErrorCode.ALREADY_COMPLETED_ORDER);
        }

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

        //배송 관련 로직
        Delivery delivery = deliveryRepository.findByOrder(order).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_DELIVERY));

        if(!OrderLocation.PENDING.equals(order.getDelivery().getLocation())){
            throw new TmsCustomException(ErrorCode.ALREADY_IN_DELIVERY);
        }

        delivery.setDelete(true);

        return new OrderResponseDto(order);
    }


    public Order findByIdOrElseThrow(Long orderId){
        return orderRepository.findById(orderId).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_ORDER)
        );
    }

}
