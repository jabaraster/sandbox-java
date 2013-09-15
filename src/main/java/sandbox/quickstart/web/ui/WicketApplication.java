package sandbox.quickstart.web.ui;

import jabara.general.ArgUtil;
import jabara.wicket.LoginPageInstantiationAuthorizer;
import jabara.wicket.MarkupIdForceOutputer;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.util.IProvider;
import org.apache.wicket.util.time.Duration;

import sandbox.quickstart.web.ui.page.LoginPage;
import sandbox.quickstart.web.ui.page.LogoutPage;
import sandbox.quickstart.web.ui.page.MapPage;
import sandbox.quickstart.web.ui.page.RestrictedPageBase;

import com.google.inject.Injector;

/**
 *
 */
public class WicketApplication extends WebApplication {

    private static final String       ENC = "UTF-8";   //$NON-NLS-1$

    private final IProvider<Injector> injectorProvider;

    /**
     * @param pInjectorProvider Guiceの{@link Injector}を供給するオブジェクト. DI設定に使用します.
     */
    public WicketApplication(final IProvider<Injector> pInjectorProvider) {
        ArgUtil.checkNull(pInjectorProvider, "pInjectorProvider"); //$NON-NLS-1$
        this.injectorProvider = pInjectorProvider;
    }

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends Page> getHomePage() {
        return MapPage.class;
    }

    /**
     * @return -
     */
    public Injector getInjector() {
        return this.injectorProvider.get();
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.wicket.request.Request, org.apache.wicket.request.Response)
     */
    @Override
    public Session newSession(final Request pRequest, @SuppressWarnings("unused") final Response pResponse) {
        return new AppSession(pRequest);
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#init()
     */
    @Override
    protected void init() {
        super.init();

        mountResources();
        mountPages();
        initializeEncoding();
        initializeInjection();
        initializeSecurity();
        initializeOther();
    }

    private void initializeEncoding() {
        getMarkupSettings().setDefaultMarkupEncoding(ENC);
        getRequestCycleSettings().setResponseRequestEncoding(getMarkupSettings().getDefaultMarkupEncoding());
    }

    private void initializeInjection() {
        getComponentInstantiationListeners().add(new GuiceComponentInjector(this, this.injectorProvider.get()));
    }

    private void initializeOther() {
        getComponentInstantiationListeners().add(new MarkupIdForceOutputer());
    }

    private void initializeSecurity() {
        final SecurePackageResourceGuard guard = new SecurePackageResourceGuard();
        guard.addPattern("+*.ttf"); //$NON-NLS-1$
        guard.addPattern("+*.woff"); //$NON-NLS-1$
        getResourceSettings().setPackageResourceGuard(guard);

        getSecuritySettings().setAuthorizationStrategy(new LoginPageInstantiationAuthorizer() {

            @Override
            protected Class<? extends Page> getFirstPageType() {
                return MapPage.class;
            }

            @Override
            protected Class<? extends Page> getLoginPageType() {
                return LoginPage.class;
            }

            @Override
            protected Class<? extends Page> getRestictedPageType() {
                return RestrictedPageBase.class;
            }

            @Override
            protected boolean isAuthenticated() {
                final AppSession session = AppSession.get();
                return session.isAuthenticated();
            }

            @Override
            protected boolean isPermittedPage(@SuppressWarnings("unused") final Class<? extends WebPage> pPageType) {
                return true;
            }
        });
    }

    private void mountPages() {
        this.mountPage("login", LoginPage.class); //$NON-NLS-1$
        this.mountPage("logout", LogoutPage.class); //$NON-NLS-1$
        this.mountPage("map", MapPage.class); //$NON-NLS-1$
    }

    @SuppressWarnings({ "nls", "serial" })
    private void mountResources() {
        mountResource("back", new ResourceReference("back") {
            @SuppressWarnings("resource")
            @Override
            public IResource getResource() {
                return new ResourceStreamResource(new UrlResourceStream(WicketApplication.class.getResource("brickwall.png"))) //
                        .setCacheDuration(Duration.days(10)) //
                ;
            }
        });
    }

    /**
     * @return -
     */
    public static WicketApplication get() {
        return (WicketApplication) WebApplication.get();
    }
}
