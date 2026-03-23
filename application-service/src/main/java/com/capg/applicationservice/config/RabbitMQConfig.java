//package com.capg.applicationservice.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitMQConfig {
//
//    public static final String EXCHANGE = "jobportal.exchange";
//    public static final String QUEUE = "job.applied.queue";
//    public static final String ROUTING_KEY = "job.applied";
//
//    // 🔥 FORCE connection
//    @Bean
//    public CachingConnectionFactory connectionFactory() {
//        CachingConnectionFactory factory = new CachingConnectionFactory();
//
//        factory.setHost("localhost");
//        factory.setPort(5672);
//        factory.setUsername("guest");
//        factory.setPassword("guest");
//
//        System.out.println("🔥 RabbitMQ connection configured");
//
//        return factory;
//    }
//
//    // 🔥 RabbitTemplate
//    @Bean
//    public RabbitTemplate rabbitTemplate(CachingConnectionFactory factory) {
//        return new RabbitTemplate(factory);
//    }
//
//    // 🔥 Exchange
//    @Bean
//    public DirectExchange exchange() {
//        System.out.println("🔥 EXCHANGE BEAN CREATED");
//        return new DirectExchange(EXCHANGE);
//    }
//
//    //  Queue
//    @Bean
//    public Queue queue() {
//        return new Queue(QUEUE);
//    }
//
//    //  Binding
//    @Bean
//    public Binding binding() {
//        return BindingBuilder
//                .bind(queue())
//                .to(exchange())
//                .with(ROUTING_KEY);
//    }
//
//    //  TEST CONNECTION (VERY IMPORTANT)
//    @Bean
//    public CommandLineRunner testConnection(RabbitTemplate rabbitTemplate) {
//        return args -> {
//            try {
//                System.out.println("🔥 Sending TEST message...");
//                rabbitTemplate.convertAndSend("test.exchange", "test.key", "HELLO");
//            } catch (Exception e) {
//                System.out.println("❌ RabbitMQ connection FAILED: " + e.getMessage());
//            }
//        };
//    }
//    
//}


package com.capg.applicationservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

@Configuration
public class RabbitMQConfig {

    public static final String APPLICATION_QUEUE = "application.queue";

    @Bean
    public Queue applicationQueue() {
        return new Queue(APPLICATION_QUEUE);
    }

    //  VERY IMPORTANT (FIXES YOUR ERROR)
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}