package sandbox.quickstart.web.ui;

import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;

import sandbox.quickstart.model.FailAuthentication;
import sandbox.quickstart.model.LoginUser;
import sandbox.quickstart.service.IAuthenticationService;
import sandbox.quickstart.web.LoginUserHolder;

/**
 * 
 */
public class AppSession extends WebSession {
    private static final long                serialVersionUID = -5522467353190211133L;

    private final AtomicReference<LoginUser> authenticated    = new AtomicReference<>();

    /**
     * @param pRequest -
     */
    public AppSession(final Request pRequest) {
        super(pRequest);
    }

    /**
     * @return 管理者ユーザとしてログイン済みならtrue.
     */
    public boolean currentUserIsAdministrator() {
        if (!isAuthenticatedCore()) {
            return false;
        }
        return this.authenticated.get().isAdministrator();
    }

    /**
     * @see org.apache.wicket.protocol.http.WebSession#invalidate()
     */
    @Override
    public void invalidate() {
        super.invalidate();
        invalidateHttpSession();
    }

    /**
     * @see org.apache.wicket.Session#invalidateNow()
     */
    @Override
    public void invalidateNow() {
        super.invalidateNow();
        invalidateHttpSession();
    }

    /**
     * @return 認証済みあればtrue.
     */
    public boolean isAuthenticated() {
        return isAuthenticatedCore();
    }

    /**
     * @param pUserId -
     * @param pPassword -
     * @throws FailAuthentication 認証NGの場合にスローして下さい.
     */
    public void login(final String pUserId, final String pPassword) throws FailAuthentication {
        final LoginUser loginUser = getAuthenticationService().login(pUserId, pPassword);
        this.authenticated.set(loginUser);

        final HttpSession session = ((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest()).getSession();
        LoginUserHolder.set(session, loginUser);
    }

    private boolean isAuthenticatedCore() {
        return this.authenticated.get() != null;
    }

    /**
     * @return 現在のコンテキスト中の{@link AppSession}.
     */
    public static AppSession get() {
        return (AppSession) Session.get();
    }

    private static IAuthenticationService getAuthenticationService() {
        return WicketApplication.get().getInjector().getInstance(IAuthenticationService.class);
    }

    private static void invalidateHttpSession() {
        // Memcahcedによるセッション管理を行なっていると、Session.get()ではセッションが破棄されない.
        ((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest()).getSession().invalidate();
    }
}
