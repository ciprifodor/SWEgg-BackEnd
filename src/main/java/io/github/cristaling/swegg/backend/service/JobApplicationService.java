package io.github.cristaling.swegg.backend.service;


import io.github.cristaling.swegg.backend.core.job.Job;
import io.github.cristaling.swegg.backend.core.job.JobApplication;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.repositories.JobApplicationRepository;
import io.github.cristaling.swegg.backend.repositories.JobRepository;
import io.github.cristaling.swegg.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
public class JobApplicationService {

    private JobApplicationRepository jobApplicationRepository;
    private UserRepository userRepository;
    private JobRepository jobRepository;
    private EmailSenderService emailSenderService;

    @Autowired
    public JobApplicationService(JobApplicationRepository jobApplicationRepository, UserRepository userRepository, JobRepository jobRepository, EmailSenderService emailSenderService) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.emailSenderService = emailSenderService;
    }


    public boolean addJobApplication(Member member, UUID jobUUID) {

        Job job;

        try {
            job = this.jobRepository.getOne(jobUUID);
        } catch (EntityNotFoundException e) {
            return false;
        }

        if (jobApplicationRepository.getJobApplicationByApplicantAndJob(member, job) != null) {
            return false;
        }

        JobApplication jobApplication = new JobApplication();

        jobApplication.setApplicant(member);
        jobApplication.setJob(job);

        jobApplicationRepository.save(jobApplication);

        emailSenderService.sendJobApplicationNotificationToMember(jobApplication);

        return true;
    }

    public List<JobApplication> getAll() {
        return jobApplicationRepository.findAll();
    }

    public List<JobApplication> getApplicationsForApplicant(String email){
        Member applicator = userRepository.getMemberByEmail(email);
        if(applicator==null){
            return null;
        }
        return jobApplicationRepository.getJobApplicationsByApplicant(applicator);
    }

    public List<JobApplication> getApplicationsForJob(UUID uuid){
        try {
            Job job = jobRepository.getOne(uuid);
            return jobApplicationRepository.getJobApplicationsByJob(job);
        } catch (EntityNotFoundException ex) {
            return null;
        }
    }
}
