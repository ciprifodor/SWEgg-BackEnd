package io.github.cristaling.swegg.backend.core.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.utils.enums.JobStatus;
import io.github.cristaling.swegg.backend.utils.enums.JobType;
import io.github.cristaling.swegg.backend.web.requests.JobAddRequest;
import io.github.cristaling.swegg.backend.web.responses.JobWithAbilities;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "jobs")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Job {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_uuid", nullable = false)
    @JsonIgnore
    private Member owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_uuid")
    @JsonIgnore
    private Member employee;

	@Enumerated(EnumType.STRING)
	private JobType jobType;

    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;

    @NotNull
    private String title;

    @NotNull
    private String description;

    private Date doneDate;

    public Job() {
    }

    public Job(Member owner, JobType jobType, JobStatus jobStatus, String title, String description) {
        this.owner = owner;
        this.jobType = jobType;
        this.jobStatus = jobStatus;
        this.title = title;
        this.description = description;
    }

    public Job(JobAddRequest jobAddRequest) {
        this.jobType = jobAddRequest.getJobType();
        this.jobStatus = jobAddRequest.getJobStatus();
        this.title = jobAddRequest.getTitle();
        this.description = jobAddRequest.getDescription();
    }

    public Job(JobWithAbilities jobWithAbilities){
        this.description = jobWithAbilities.getDescription();
        this.employee = jobWithAbilities.getEmployee();
        this.jobStatus = jobWithAbilities.getJobStatus();
        this.jobType = jobWithAbilities.getJobType();
        this.owner = jobWithAbilities.getOwner();
        this.title = jobWithAbilities.getTitle();
    }

    public Member getOther(Member member) {
        if (member.equals(this.owner)) {
            return this.employee;
        }
        if (member.equals(this.employee)) {
            return this.owner;
        }
        return null;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Member getEmployee() { return employee; }

    public void setEmployee(Member employee) { this.employee = employee; }

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(Date doneDate) {
        this.doneDate = doneDate;
    }
}
