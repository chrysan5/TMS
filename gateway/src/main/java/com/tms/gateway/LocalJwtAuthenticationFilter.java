package com.tms.gateway;

import com.tms.gateway.application.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import javax.crypto.SecretKey;


@Slf4j
@Component
public class LocalJwtAuthenticationFilter implements GlobalFilter {

    private final String secretKey;

    private final UserService userService;

    // FeignClient 와 Global Filter 의 순환참조 문제가 발생하여 Bean 초기 로딩 시 순환을 막기 위해 @Lazy 어노테이션을 추가
    public LocalJwtAuthenticationFilter(@Value("${jwt.secret.key}") String secretKey, @Lazy UserService userSerivce) {
        this.secretKey = secretKey;
        this.userService = userSerivce;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (path.equals("/users/signup") || path.equals("/auth/login") || path.equals("/users/verify")) {
            return chain.filter(exchange);
        }
        String token = extractToken(exchange);
        if (token == null || !validateToken(token, exchange)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED); //401, unauthorized
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private boolean validateToken(String token, ServerWebExchange exchange) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);

            log.info("#####payload :: " + claimsJws.getPayload().toString());

            Claims claims = claimsJws.getBody();
            exchange.getRequest().mutate()
                    .header("X-username", claims.get("username").toString())
                    .header("X-role", claims.get("role").toString())
                    .build();

            return true;

            //생성된 토큰이 최신정보 담고있지 않으므로 jwt 값을 검증함 -이부분이 순환참조를 3번 일으킴
            /*if (claimsJws.getPayload().get("username") != null) {
                String username = claimsJws.getPayload().get("username").toString();
                return userService.verifyUser(username);
            } else {
                return false;
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}