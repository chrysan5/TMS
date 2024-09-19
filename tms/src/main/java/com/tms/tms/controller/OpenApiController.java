package com.tms.tms.controller;

import com.tms.tms.service.OpenAPiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

@RequestMapping("openApi")
@RequiredArgsConstructor
@RestController
public class OpenApiController {

    private final OpenAPiService openAPiService;

    @GetMapping("/getWeather/hubs/{hubId}")
    public ResponseEntity<String> getWeather(@PathVariable("hubId") Long hubId) throws IOException {
        try {
            return ResponseEntity.ok(openAPiService.getWeather(hubId));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/getGeminiChat")
    public ResponseEntity<String> getGeminiChat(@RequestParam("question") String question) {
        try {
            return ResponseEntity.ok(openAPiService.getGeminiChat(question));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //날씨 정보를 gemini에 보낸 후 받은 메시지를 저장
    @GetMapping("/getWeather/hubs/{hubId}/getGeminiChat")
    public ResponseEntity<String> combineWeatherAndGeminiAPi(@PathVariable("hubId") Long hubId) throws IOException {
        return ResponseEntity.ok(openAPiService.combineWeatherAndGeminiAPi(hubId));
    }


    @GetMapping("/sendMsg")
    public ResponseEntity<String> sendSlackMsg(@RequestParam String msg) throws IOException {
        openAPiService.sendSlackMsg(msg);
        return ResponseEntity.ok().build();
    }

}
