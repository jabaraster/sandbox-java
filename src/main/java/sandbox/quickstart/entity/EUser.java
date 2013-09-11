/**
 * 
 */
package sandbox.quickstart.entity;

import jabara.jpa.entity.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jabaraster
 */
@Entity
public class EUser extends EntityBase<EUser> {
    private static final long  serialVersionUID               = 5322511553248558567L;

    /**
     * 
     */
    public static final String DEFAULT_ADMINISTRATOR_USER_ID  = "admin";             //$NON-NLS-1$
    /**
     * 
     */
    public static final String DEFAULT_ADMINISTRATOR_PASSWORD = "admin";             //$NON-NLS-1$

    /**
     * 
     */
    public static final int    MAX_CHAR_COUNT_USER_ID         = 50;

    /**
     * 
     */
    @Column(nullable = false, unique = true, length = MAX_CHAR_COUNT_USER_ID)
    @NotNull
    @Size(min = 1, max = MAX_CHAR_COUNT_USER_ID)
    protected String           userId;

    /**
     * 
     */
    @Column(nullable = false)
    protected boolean          administrator;

    /**
     * @return userIdを返す.
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * @return administratorを返す.
     */
    public boolean isAdministrator() {
        return this.administrator;
    }

    /**
     * @param pAdministrator administratorを設定.
     */
    public void setAdministrator(final boolean pAdministrator) {
        this.administrator = pAdministrator;
    }

    /**
     * @param pUserId userIdを設定.
     */
    public void setUserId(final String pUserId) {
        this.userId = pUserId;
    }
}
