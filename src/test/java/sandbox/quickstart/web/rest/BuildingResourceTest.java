/**
 * 
 */
package sandbox.quickstart.web.rest;

import net.arnx.jsonic.JSON;

import org.junit.Test;

import sandbox.quickstart.entity.ECandidateBuilding;
import sandbox.quickstart.entity.EUser;
import sandbox.quickstart.entity.LatLng;
import sandbox.quickstart.model.CandidateBuildingModel;

/**
 * @author jabaraster
 */
public class BuildingResourceTest {

    /**
     * 
     */
    @SuppressWarnings({ "nls", "serial" })
    @Test
    public void _JAXB() {
        final ECandidateBuilding cb = new ECandidateBuilding() {
            {
                this.id = Long.valueOf(1);
            }
        };
        cb.setAddress("日本");
        cb.setName("物件名");
        cb.setPosition(new LatLng(30.111, 120.394));
        cb.setRegistrationUser(new EUser());

        System.out.println(JSON.encode(new CandidateBuildingModel(cb), true));
    }

}
