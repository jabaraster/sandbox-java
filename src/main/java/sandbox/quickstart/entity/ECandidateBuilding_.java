package sandbox.quickstart.entity;

import jabara.jpa.entity.EntityBase_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-09-13T18:30:34.412+0900")
@StaticMetamodel(ECandidateBuilding.class)
public class ECandidateBuilding_ extends EntityBase_ {
	public static volatile SingularAttribute<ECandidateBuilding, String> name;
	public static volatile SingularAttribute<ECandidateBuilding, String> address;
	public static volatile SingularAttribute<ECandidateBuilding, LatLng> position;
	public static volatile SingularAttribute<ECandidateBuilding, EUser> registrationUser;
}
