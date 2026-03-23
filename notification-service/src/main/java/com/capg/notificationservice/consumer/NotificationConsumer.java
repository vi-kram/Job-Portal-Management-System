package com.capg.notificationservice.consumer;

import com.capg.notificationservice.config.RabbitMQConfig;
import com.capg.notificationservice.dto.ApplicationEvent;
import com.capg.notificationservice.dto.ResumeEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    @RabbitListener(queues = RabbitMQConfig.APPLICATION_QUEUE)
    public void handleApplicationEvent(ApplicationEvent event) {

        System.out.println("Received Application Event:");
        System.out.println("User: " + event.getUserEmail());
        System.out.println("Job ID: " + event.getJobId());
        System.out.println("Status: " + event.getStatus());

        // simulate notification
        System.out.println("Notification sent to user for application");
    }

    @RabbitListener(queues = RabbitMQConfig.RESUME_QUEUE)
    public void handleResumeEvent(ResumeEvent event) {

        System.out.println("Received Resume Event:");
        System.out.println("User: " + event.getUserEmail());
        System.out.println("File URL: " + event.getFileUrl());

        System.out.println("Notification sent for resume upload");
    }
}