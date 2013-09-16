/**
 * 
 */
package sandbox.quickstart.entity;

import jabara.bean.BeanProperties;
import jabara.bean.annotation.Localized;
import jabara.jpa.entity.EntityBase;

import java.nio.charset.Charset;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

/**
 * @author jabaraster
 */
@Entity
public class ECandidateBuilding extends EntityBase<ECandidateBuilding> {
    private static final long     serialVersionUID       = 54653879111484362L;

    private static BeanProperties _meta                  = BeanProperties.getInstance(ECandidateBuilding.class);

    private static final Charset  ENC                    = Charset.forName("utf-8");                            //$NON-NLS-1$

    private static final int      MAX_CHAR_COUNT_NAME    = 50;
    private static final int      MAX_CHAR_COUNT_ADDRESS = 200;

    /**
     * 
     */
    @Column(nullable = false, length = MAX_CHAR_COUNT_NAME * 3)
    protected String              name;

    /**
     * 
     */
    @Column(nullable = false, length = MAX_CHAR_COUNT_ADDRESS * 3)
    protected String              address;

    /**
     * 
     */
    @Embedded
    protected LatLng              position               = new LatLng();

    /**
     * 
     */
    @Lob
    protected byte[]              freeText;

    /**
     * 
     */
    @ManyToOne
    protected EUser               registrationUser;

    /**
     * @return addressを返す.
     */
    @Localized
    public String getAddress() {
        return this.address;
    }

    /**
     * @return -
     */
    public String getFreeText() {
        if (this.freeText == null) {
            return null;
        }
        return new String(this.freeText, ENC);
    }

    /**
     * @see jabara.jpa.entity.EntityBase#getId()
     */
    @Override
    @Localized
    public Long getId() {
        return super.getId();
    }

    /**
     * @return nameを返す.
     */
    @Localized
    public String getName() {
        return this.name;
    }

    /**
     * @return positionを返す.
     */
    public LatLng getPosition() {
        return this.position;
    }

    /**
     * @return registrationUserを返す.
     */
    public EUser getRegistrationUser() {
        return this.registrationUser;
    }

    /**
     * @param pAddress addressを設定.
     */
    public void setAddress(final String pAddress) {
        this.address = pAddress;
    }

    /**
     * @param pFreeText -
     */
    public void setFreeText(final String pFreeText) {
        this.freeText = pFreeText.getBytes(ENC);
    }

    /**
     * @param pName nameを設定.
     */
    public void setName(final String pName) {
        this.name = pName;
    }

    /**
     * @param pPosition positionを設定.
     */
    public void setPosition(final LatLng pPosition) {
        this.position = pPosition;
    }

    /**
     * @param pRegistrationUser registrationUserを設定.
     */
    public void setRegistrationUser(final EUser pRegistrationUser) {
        this.registrationUser = pRegistrationUser;
    }

    /**
     * @return -
     */
    public static BeanProperties getMeta() {
        return _meta;
    }
}
