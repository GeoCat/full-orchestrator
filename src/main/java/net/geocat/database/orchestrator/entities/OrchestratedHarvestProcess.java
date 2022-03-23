package net.geocat.database.orchestrator.entities;

import javax.persistence.*;


@Entity
public class OrchestratedHarvestProcess {


    @Id
    @Column(columnDefinition = "varchar(40)")
    private String jobId;


    @Column(columnDefinition = "varchar(40)")
    private String harvesterJobId;

    @Column(columnDefinition = "varchar(40)")
    private String linkCheckJobId;

    @Column(columnDefinition = "varchar(40)")
    private String injectJobId;


    //state of the overall process
    @Enumerated(EnumType.STRING)
    private OrchestratedHarvestProcessState state;

    @Column
    private Boolean executeLinkChecker;

    @Column
    private Boolean skipHarvesting;

    //---

    public String getJobId() {
        return jobId;
    }

    public String getHarvesterJobId() {
        return harvesterJobId;
    }

    public void setHarvesterJobId(String harvesterJobId) {
        this.harvesterJobId = harvesterJobId;
    }

    public String getLinkCheckJobId() {
        return linkCheckJobId;
    }

    public void setLinkCheckJobId(String linkCheckJobId) {
        this.linkCheckJobId = linkCheckJobId;
    }

    public String getInjectJobId() {
        return injectJobId;
    }

    public void setInjectJobId(String injectJobId) {
        this.injectJobId = injectJobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public OrchestratedHarvestProcessState getState() {
        return state;
    }

    public void setState(OrchestratedHarvestProcessState state) {
        this.state = state;
    }

    public Boolean getExecuteLinkChecker() {
        return executeLinkChecker;
    }

    public void setExecuteLinkChecker(Boolean executeLinkChecker) {
        this.executeLinkChecker = executeLinkChecker;
    }

    public Boolean getSkipHarvesting() {
        return skipHarvesting;
    }

    public void setSkipHarvesting(Boolean skipHarvesting) {
        if (skipHarvesting == null) {
            this.skipHarvesting = Boolean.FALSE;
        } else {
            this.skipHarvesting = skipHarvesting;
        }
    }

    //---

}
