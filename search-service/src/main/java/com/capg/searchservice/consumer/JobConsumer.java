package com.capg.searchservice.consumer;

import com.capg.searchservice.dto.JobEvent;
import com.capg.searchservice.entity.Job;
import com.capg.searchservice.repository.JobRepository;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class JobConsumer {

    private final JobRepository repository;

    public JobConsumer(JobRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "job.queue")
    public void consume(JobEvent event) {

        System.out.println("🔥 Received job: " + event.getTitle());

        Job job = new Job();
        job.setTitle(event.getTitle());
        job.setCompany(event.getCompany());
        job.setLocation(event.getLocation());
        job.setSalary(event.getSalary());
        job.setDescription(event.getDescription());

        repository.save(job);
    }
}