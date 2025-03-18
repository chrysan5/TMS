package com.tms.tms.service;

import com.tms.tms.dto.DeliveryRequestDto;
import com.tms.tms.dto.DeliveryResponseDto;
import com.tms.tms.exception.ErrorCode;
import com.tms.tms.exception.TmsCustomException;
import com.tms.tms.model.*;
import com.tms.tms.repository.DeliveryRepository;
import com.tms.tms.repository.HubRepository;
import com.tms.tms.repository.OrderRepository;
import com.tms.tms.repository.StoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DeliveryService {
    private final OrderRepository orderRepository;
    private DeliveryRepository deliveryRepository;
    private HubRepository hubRepository;
    private StoreRepository storeRepository;


    public DeliveryResponseDto getDelivery(Long deliveryId) {
        Delivery delivery = findByIdOrElseThrow(deliveryId);
        return new DeliveryResponseDto(delivery);
    }

    public List<DeliveryResponseDto> getDeliveriesByHub(Long hubId) {
        Hub hub = hubRepository.findById(hubId).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_HUB));

        List<Delivery> deliveries = new ArrayList<>();
        List<Store> stores = hub.getStoreList();
        for(Store store : stores){
            List<Order> orders = orderRepository.findAllByStore(store);
            deliveries = deliveryRepository.findAllByOrderIn(orders);

        }

        return deliveries.stream()
                .map(DeliveryResponseDto::new)
                .collect(Collectors.toList());
    }


    public List<DeliveryResponseDto> getDeliveriesByStore(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_STORE));

        List<Order> orders = orderRepository.findAllByStore(store);

        List<Delivery> deliveries = deliveryRepository.findAllByOrderIn(orders);

        return deliveries.stream()
                .map(DeliveryResponseDto::new)
                .collect(Collectors.toList());

    }


    @Transactional
    public DeliveryResponseDto updateOrderLocation(Long deliveryId, DeliveryRequestDto deliveryRequestDto) {
        Delivery delivery = findByIdOrElseThrow(deliveryId);
        delivery.setLocation(OrderLocation.valueOf(deliveryRequestDto.getLocation()));
        return new DeliveryResponseDto(delivery);
    }


    public Delivery findByIdOrElseThrow(Long deliveryId){
        return deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new TmsCustomException(ErrorCode.NOT_FOUND_DELIVERY)
        );
    }

}
