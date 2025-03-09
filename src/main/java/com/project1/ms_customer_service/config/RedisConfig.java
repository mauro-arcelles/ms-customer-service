package com.project1.ms_customer_service.config;

import com.project1.ms_customer_service.model.CustomerResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public ReactiveRedisTemplate<String, CustomerResponse> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<CustomerResponse> valueSerializer = new Jackson2JsonRedisSerializer<>(CustomerResponse.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, CustomerResponse> builder =
            RedisSerializationContext.newSerializationContext(keySerializer);

        RedisSerializationContext<String, CustomerResponse> context =
            builder.value(valueSerializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
