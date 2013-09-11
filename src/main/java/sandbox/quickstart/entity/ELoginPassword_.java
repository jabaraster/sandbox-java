package sandbox.quickstart.entity;

import jabara.jpa.entity.EntityBase_;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-06-23T09:58:41.123+0900")
@StaticMetamodel(ELoginPassword.class)
public class ELoginPassword_ extends EntityBase_ {
	public static volatile SingularAttribute<ELoginPassword, byte[]> password;
	public static volatile SingularAttribute<ELoginPassword, EUser> user;
}
