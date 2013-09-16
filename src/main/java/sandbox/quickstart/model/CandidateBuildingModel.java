package sandbox.quickstart.model;

import java.io.Serializable;
import java.util.Date;

import sandbox.quickstart.entity.ECandidateBuilding;
import sandbox.quickstart.entity.LatLng;

/**
 * @author jabaraster
 */
public class CandidateBuildingModel implements Serializable {
    private static final long        serialVersionUID = -2905618821809011333L;

    private final ECandidateBuilding candidateBuilding;

    /**
     * 
     */
    public CandidateBuildingModel() {
        this(new ECandidateBuilding());
    }

    /**
     * @param pCandidateBuilding -
     */
    public CandidateBuildingModel(final ECandidateBuilding pCandidateBuilding) {
        this.candidateBuilding = pCandidateBuilding;
    }

    /**
     * @return -
     */
    public String getAddress() {
        return this.candidateBuilding.getAddress();
    }

    /**
     * @return -
     */
    public Date getCreated() {
        return this.candidateBuilding.getCreated();
    }

    /**
     * @return -
     */
    public long getId() {
        return this.candidateBuilding.getId().longValue();
    }

    /**
     * @return -
     */
    public String getName() {
        return this.candidateBuilding.getName();
    }

    /**
     * @return -
     */
    public LatLng getPosition() {
        return this.candidateBuilding.getPosition();
    }

    /**
     * @return -
     */
    public Date getUpdated() {
        return this.candidateBuilding.getUpdated();
    }
}