package com.tms.tms.scheduler;

import com.tms.tms.model.Order;
import com.tms.tms.repository.OrderRepository;
import com.tms.tms.service.OpenAPiService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class SlackMsgScheduler {
    private final OrderRepository orderRepository;
    private final OpenAPiService openAPiService;


    @Scheduled(cron = "0 5 6 * * *")
    //@Scheduled(cron = "*/10 * * * * *") //프로젝트 run하자마자 테스트용
    public void sendSlackMsgScheduler() throws IOException {
        List<Order> orderList = orderRepository.findAllByOrderState();
        
        for(Order order : orderList){
            Long hubId = order.getStartHubId();
            String geminiResponse = openAPiService.combineWeatherAndGeminiAPi(hubId);
            openAPiService.sendSlackMsg(geminiResponse);
        }
    }
}
