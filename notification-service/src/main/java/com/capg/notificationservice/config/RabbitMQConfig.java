package com.capg.notificationservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

@Configuration
public class RabbitMQConfig {

    public static final String APPLICATION_QUEUE = "application.queue";
    public static final String RESUME_QUEUE = "resume.queue";

    @Bean
    public Queue applicationQueue() {
        return new Queue(APPLICATION_QUEUE);
    }

    @Bean
    public Queue resumeQueue() {
        return new Queue(RESUME_QUEUE);
    }

    // 🔥 ADD THIS
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}