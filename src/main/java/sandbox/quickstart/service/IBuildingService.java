/**
 * 
 */
package sandbox.quickstart.service;

import jabara.general.NotFound;
import jabara.general.Sort;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import sandbox.quickstart.entity.ECandidateBuilding;
import sandbox.quickstart.model.LoginUser;
import sandbox.quickstart.service.impl.BuildingServiceImpl;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(BuildingServiceImpl.class)
public interface IBuildingService {

    /**
     * @param pCondition -
     * @return -
     */
    long count(SearchCondition pCondition);

    /**
     * @param pId -
     */
    void deleteById(long pId);

    /**
     * @param pLoginUser -
     * @param pNewBuilding -
     * @param pImageData -
     */
    void insert(LoginUser pLoginUser, ECandidateBuilding pNewBuilding, InputStream pImageData);

    /**
     * @param pId -
     * @return -
     * @throws NotFound -
     */
    ECandidateBuilding search(long pId) throws NotFound;

    /**
     * @param pCondition -
     * @param pFirst -
     * @param pCount -
     * @param pSort -
     * @return -
     */
    List<ECandidateBuilding> search(SearchCondition pCondition, long pFirst, long pCount, Sort pSort);

    /**
     * @param pCondition -
     * @param pSort -
     * @return -
     */
    List<ECandidateBuilding> search(SearchCondition pCondition, Sort pSort);

    /**
     * @param pLoginUser -
     * @param pId -
     * @param pUpdateValues -
     * @param pImageData -
     */
    void update(LoginUser pLoginUser, long pId, ECandidateBuilding pUpdateValues, InputStream pImageData);

    /**
     * @see IBuildingService#search(SearchCondition, long, long, Sort)
     */
    public static class SearchCondition implements Serializable {
        private static final long serialVersionUID = -3262731369907134211L;

        private String            namePart;
        private String            addressPart;
        private String            registrationUserId;

        /**
         * @return addressPartを返す.
         */
        public String getAddressPart() {
            return this.addressPart;
        }

        /**
         * @return namePartを返す.
         */
        public String getNamePart() {
            return this.namePart;
        }

        /**
         * @return registrationUserIdを返す.
         */
        public String getRegistrationUserId() {
            return this.registrationUserId;
        }

        /**
         * @param pAddressPart addressPartを設定.
         */
        public void setAddressPart(final String pAddressPart) {
            this.addressPart = pAddressPart;
        }

        /**
         * @param pNamePart namePartを設定.
         */
        public void setNamePart(final String pNamePart) {
            this.namePart = pNamePart;
        }

        /**
         * @param pRegistrationUserId registrationUserIdを設定.
         */
        public void setRegistrationUserId(final String pRegistrationUserId) {
            this.registrationUserId = pRegistrationUserId;
        }
    }
}
