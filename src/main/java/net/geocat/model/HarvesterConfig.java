package net.geocat.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import net.geocat.xml.XMLTools;
import org.w3c.dom.Document;

import java.net.URL;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HarvesterConfig {

    // tag so you can refer to "previous run results"
    //ie. country name
    private String longTermTag;

    // looks for nested discovery service
    //true for poland, otherwise false
    private boolean lookForNestedDiscoveryService;

    //CSW <ogc:Filter>
    private String filter;

    // endpoint for GetCapabilities
    private String url;

    // GUID for the harvest (used as JMS Correlation ID).  Provided by server (do not specify)
    private String processID;

    // how many records to retrieve in a single GetRecords request
    // defaults to 20 records in a GetRecords request (see DEFAULT_NRECORDS)
    private int numberOfRecordsPerRequest;


    // which queue set to use.  blank=auto determined.  Otherwise "PARALLEL#" #=2,3,4
    // usually, you want to make this blank.  Except for large servers
    //   (i.e. lots of records and can handle multiple simutaneous requests)
    private String getRecordQueueHint;

    //what to do in the case an error condition occurs.
    // See the ProblematicResultsConfiguration class for details
    private ProblematicResultsConfiguration problematicResultsConfiguration;


    //if true then do NOT add the SORT BY to the individual getRecord requests
    // if false (or null), then DO SORT/
    public Boolean doNotSort;

    // If true, executes the link checker process, otherwise skips it.
    public Boolean executeLinkChecker;

    // If true, skips the harvesting process. This flag is used to execute the link checker again
    // with the harvesting results, setting also executeLinkChecker=true if skipHarvesting=true.
    public Boolean skipHarvesting;


    // if numberOfRecordsPerRequest is not specified, use this
    public static int DEFAULT_NRECORDS = 20;


    public Boolean getDoNotSort() {
        return doNotSort;
    }

    public void setDoNotSort(Boolean doNotSort) {
        this.doNotSort = doNotSort;
    }

    public String getGetRecordQueueHint() {
        return getRecordQueueHint;
    }

    public void setGetRecordQueueHint(String getRecordQueueHint) {
        this.getRecordQueueHint = getRecordQueueHint;
    }

    public ProblematicResultsConfiguration getProblematicResultsConfiguration() {
        return problematicResultsConfiguration;
    }

    public void setProblematicResultsConfiguration(ProblematicResultsConfiguration problematicResultsConfiguration) {
        this.problematicResultsConfiguration = problematicResultsConfiguration;
    }

    public boolean isLookForNestedDiscoveryService() {
        return lookForNestedDiscoveryService;
    }

    public void setLookForNestedDiscoveryService(boolean lookForNestedDiscoveryService) {
        this.lookForNestedDiscoveryService = lookForNestedDiscoveryService;
    }

    public String getLongTermTag() {
        return longTermTag;
    }

    public void setLongTermTag(String longTermTag) {
        this.longTermTag = longTermTag;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProcessID() {
        return processID;
    }

    public void setProcessID(String processID) {
        this.processID = processID;
    }

    public void validate() throws Exception {
        if ((url == null) || (url.isEmpty()))
            throw new Exception("No URL given for remote CSW");
        URL _url = new URL(url);
        if (!_url.getProtocol().equalsIgnoreCase("HTTP") && !_url.getProtocol().equalsIgnoreCase("HTTPS"))
            throw new Exception("URL isn't http or https");

        if ((filter != null) && (!filter.isEmpty())) { //filter present
            Document filterDoc = XMLTools.parseXML(filter);
            String rootNodeName = filterDoc.getFirstChild().getNodeName();
            if (rootNodeName != "ogc:Filter")
                throw new Exception("filter doesn't start with <ogc:Filter>");
        }
        if (problematicResultsConfiguration == null)
            problematicResultsConfiguration = new ProblematicResultsConfiguration();
        problematicResultsConfiguration.validate();

        if (numberOfRecordsPerRequest <= 0)
            numberOfRecordsPerRequest = DEFAULT_NRECORDS;

        if (numberOfRecordsPerRequest > 500) // unreasonable
            numberOfRecordsPerRequest = 500;
    }

    @Override
    public String toString() {
        return "{processID=" + processID + ", urls=" + url + "}";
    }

    public int getNumberOfRecordsPerRequest() {
        return numberOfRecordsPerRequest;
    }

    public void setNumberOfRecordsPerRequest(int numberOfRecordsPerRequest) {
        this.numberOfRecordsPerRequest = numberOfRecordsPerRequest;
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
        this.skipHarvesting = skipHarvesting;

        if ((this.skipHarvesting != null) && (this.skipHarvesting == Boolean.TRUE)) {
            setExecuteLinkChecker(true);
        }
    }
}
