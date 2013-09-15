package sandbox.quickstart.web.ui.page;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebPage;

import sandbox.quickstart.web.ui.AppSession;

/**
 * 
 */
public class LogoutPage extends WebPage {
    private static final long serialVersionUID = -3810270407936165942L;

    /**
     * 
     */
    public LogoutPage() {
        AppSession.get().invalidate();
        throw new RestartResponseAtInterceptPageException(LoginPage.class);
    }
}
