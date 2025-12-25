package vn.van.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import vn.van.api_gateway.repository.IdentityClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:9001/identity")
                .build();
    }

    @Bean
    IdentityClient identityClient(WebClient webClient) {
        HttpServiceProxyFactory httpServiceProxyFactory =
                HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();
        return httpServiceProxyFactory.createClient(IdentityClient.class);
    }
}
