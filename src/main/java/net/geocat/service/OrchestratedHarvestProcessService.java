package net.geocat.service;


import net.geocat.database.orchestrator.entities.OrchestratedHarvestProcess;
import net.geocat.database.orchestrator.entities.OrchestratedHarvestProcessState;
import net.geocat.database.orchestrator.repos.OrchestratedHarvestProcessRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("prototype")
public class OrchestratedHarvestProcessService {

    @Autowired
    OrchestratedHarvestProcessRepo orchestratedHarvestProcessRepo;

    public OrchestratedHarvestProcess updateLinkCheckJobStateInDBToError(String guid) throws Exception {
        return updateLinkCheckJobStateInDB(guid, OrchestratedHarvestProcessState.ERROR);
    }

    public OrchestratedHarvestProcess updateLinkCheckJobStateInDB(String guid, OrchestratedHarvestProcessState state) {
        Optional<OrchestratedHarvestProcess> jobOptional = orchestratedHarvestProcessRepo.findById(guid);

        if (jobOptional.isPresent()) {
            OrchestratedHarvestProcess job = jobOptional.get();
            job.setState(state);

            return orchestratedHarvestProcessRepo.save(job);
        } else {
            return null;
        }
    }


}
