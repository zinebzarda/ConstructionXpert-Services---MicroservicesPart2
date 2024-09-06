package com.Gateway.filter;

import com.Gateway.Feign.authInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;


    @Autowired
    private authInterface authInterface;

//    @Autowired
//    private RestTemplate restTemplate;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecred.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return onError(exchange, "Missing authorization header", HttpStatus.UNAUTHORIZED);
                }

                String AuthHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                if (AuthHeader != null && AuthHeader.startsWith("Bearer")) {
                    AuthHeader = AuthHeader.substring(7);
                }
                try {
                    System.out.println(AuthHeader);
//                    restTemplate.getForObject("http://localhost:8084/api/v1/auth/validate?token="+ AuthHeader, String.class);
                    authInterface.validateToken(AuthHeader);


                } catch (Exception e) {
                    throw new RuntimeException("ERROR:"+e.getMessage());
                }
            }

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            }));
        };
    }

    public static class Config {
    }
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

}
