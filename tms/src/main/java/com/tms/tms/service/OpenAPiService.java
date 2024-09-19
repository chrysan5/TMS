package com.tms.tms.service;

import com.tms.tms.dto.ChatRequest;
import com.tms.tms.dto.ChatResponse;
import com.tms.tms.model.Hub;
import com.tms.tms.model.SlackMsg;
import com.tms.tms.repository.SlackMsgRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class OpenAPiService {

    @Value("${public.portal.url}")
    private String portalUrl;

    @Value("${public.portal.key}")
    private String serviceKey;


    @Value("${gemini.api.url}")
    private String geminiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;


    @Value("${slack.api.url}")
    private String slackApiUrl;

    @Value("${slack.api.token}")
    private String slackApitocken;



    private final RestTemplate restTemplate;
    private final HubService hubService;
    private final SlackMsgRepository slackMsgRepository;

    @Autowired
    public OpenAPiService(RestTemplateBuilder builder, HubService hubService, SlackMsgRepository slackMsgRepository) {
        this.restTemplate = builder.build();
        this.hubService = hubService;
        this.slackMsgRepository = slackMsgRepository;
    }


    public String getWeather(Long hubId) throws IOException {
        Hub hub = hubService.findByIdOrElseThrow(hubId);

        String nx = hub.getHubAddressX();
        String ny = hub.getHubAddressY();

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formatedNow = now.format(formatter);
        String baseDate = formatedNow;

        String baseTime = "0600";    //조회하고싶은 시간
        String type = "JSON";    //조회하고 싶은 type(json, xml 중 고름)


        StringBuilder urlBuilder = new StringBuilder(portalUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); //페이지번호
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("20", "UTF-8")); //한 페이지 결과 수
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); //경도
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); //위도
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); //조회하고싶은 날짜
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); //조회하고싶은 시간 AM 02시부터 3시간 단위
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));    //타입


        //GET방식으로 전송해서 파라미터 받아오기
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        log.debug("Response code: " + conn.getResponseCode());

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        rd.close();
        conn.disconnect();
        String result = sb.toString();
        log.debug(result);


        //=======json에서 데이터 파싱=============

        // response 키를 가지고 데이터를 파싱
        JSONObject resultObj = new JSONObject(result);
        JSONObject response = resultObj.getJSONObject("response");
        JSONObject body = response.getJSONObject("body");
        JSONObject items = body.getJSONObject("items");

        // "item" 배열을 추출
        JSONArray itemArray = items.getJSONArray("item");


        String observeResult = "오늘의 날씨입니다. ";
        for (int i = 0; i < itemArray.length(); i++) {
            JSONObject item = itemArray.getJSONObject(i);
            String category = item.getString("category");

            if (category.equals("REH")) {
                observeResult += "오늘의 습도는 " + item.getString("obsrValue") + " 이고, ";
            }

            if (category.equals("T1H")) {
                observeResult += "오늘의 기온은 " + item.getString("obsrValue") + " 이고, ";
            }

            if (category.equals("WSD")) {
                observeResult += "오늘의 풍속은 " + item.getString("obsrValue") + " 입니다.";
            }
        }

        log.debug("observeResult" + observeResult);
        return observeResult;
    }


    public String getGeminiChat(String question) {
        String requestUrl = geminiUrl + "?key=" + geminiApiKey;

        ChatRequest request = new ChatRequest(question);

        ChatResponse response = restTemplate.postForObject(requestUrl, request, ChatResponse.class);

        String message = response.getCandidates().get(0).getContent().getParts().get(0).getText().toString();

        return message;
    }

    public String combineWeatherAndGeminiAPi(Long hubId) throws IOException {
        String weatherResponse = getWeather(hubId);
        String geminiResponse = getGeminiChat(weatherResponse+"를 요약해서 정리해줘");

        slackMsgRepository.save(new SlackMsg(geminiResponse));
        return geminiResponse;
    }

    public void sendSlackMsg(String msg) throws IOException {
        String urlStr = slackApiUrl + "?";
        urlStr += "channel=C07N15NSWLD&"; //tms(채녈명)를 인코딩한 값
        urlStr += "text="+ URLEncoder.encode(msg, "UTF-8");


        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + slackApitocken);
        //System.out.println("Response code: " + conn.getResponseCode());

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        rd.close();
        conn.disconnect();
        String result = sb.toString();
        //System.out.println(result);
    }
}
