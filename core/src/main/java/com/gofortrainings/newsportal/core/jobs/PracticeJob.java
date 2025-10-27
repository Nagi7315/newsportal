package com.gofortrainings.newsportal.core.jobs;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = JobConsumer.class,
        immediate = true,
        property = {JobConsumer.PROPERTY_TOPICS + "=practice/job"}
)
public class PracticeJob implements JobConsumer {
    private final Logger log = LoggerFactory.getLogger(PracticeJob.class);

    @Override
    public JobResult process(Job job) {
        try {
            log.info("Practice JOB Called *******");
            return JobConsumer.JobResult.OK;
        } catch (Exception e) {
            log.error("Exception ", e);
            return JobResult.FAILED;
        }
    }
}
