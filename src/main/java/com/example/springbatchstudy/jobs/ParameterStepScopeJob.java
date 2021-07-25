package com.example.springbatchstudy.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@EnableBatchProcessing
@SpringBootApplication
public class ParameterStepScopeJob {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    public ParameterStepScopeJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job job3() {
        return this.jobBuilderFactory.get("job3")
                .start(step3())
                .build();
    }

    @Bean
    public Step step3() {
        return this.stepBuilderFactory.get("step3")
                .tasklet(parameterStepScopeTasklet(null))
                .build();
    }

    @StepScope
    @Bean
    public Tasklet parameterStepScopeTasklet(@Value("#{jobParameters['name']}") String name) {
        return (contribution, chunkContext) -> {
            log.debug(String.format("Hello, %s!", name));
            return RepeatStatus.FINISHED;
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(ParameterStepScopeJob.class, args);
    }
}
