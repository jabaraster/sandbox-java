package sandbox.quickstart.web.ui.page;

import jabara.general.Empty;
import jabara.wicket.CssUtil;
import jabara.wicket.ErrorClassAppender;
import jabara.wicket.JavaScriptUtil;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import sandbox.quickstart.model.FailAuthentication;
import sandbox.quickstart.web.ui.AppSession;

/**
 * 
 */
@SuppressWarnings("synthetic-access")
public class LoginPage extends WebPageBase {
    private static final long serialVersionUID = 1925170327965147328L;

    private final Handler     handler          = new Handler();

    private StatelessForm<?>  form;
    private TextField<String> userId;
    private PasswordTextField password;
    private AjaxButton        submitter;

    /**
     * 
     */
    public LoginPage() {
        this.add(getForm());
    }

    /**
     * @see sandbox.quickstart.web.ui.page.WebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        CssUtil.addComponentCssReference(pResponse, LoginPage.class);
        pResponse.render(JavaScriptHeaderItem.forUrl("http://maps.google.com/maps/api/js?v=3&sensor=false")); //$NON-NLS-1$
        addAppJavascriptReference(pResponse);

        JavaScriptUtil.addComponentJavaScriptReference(pResponse, LoginPage.class);
        pResponse.render(OnDomReadyHeaderItem.forScript(JavaScriptUtil.getFocusScript(getUserId())));
    }

    /**
     * @see sandbox.quickstart.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Model.of(getString("pageTitle")); //$NON-NLS-1$
    }

    private StatelessForm<?> getForm() {
        if (this.form == null) {
            this.form = new StatelessForm<>("form"); //$NON-NLS-1$
            this.form.add(getUserId());
            this.form.add(getPassword());
            this.form.add(getSubmitter());
        }
        return this.form;
    }

    private PasswordTextField getPassword() {
        if (this.password == null) {
            this.password = new PasswordTextField("password", Model.of(Empty.STRING)); //$NON-NLS-1$
        }
        return this.password;
    }

    @SuppressWarnings("serial")
    private AjaxButton getSubmitter() {
        if (this.submitter == null) {
            this.submitter = new IndicatingAjaxButton("submitter") { //$NON-NLS-1$
                @Override
                protected void onError(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    LoginPage.this.handler.onSubmitterError(pTarget);
                }

                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    LoginPage.this.handler.tryLogin(pTarget);
                }
            };
        }
        return this.submitter;
    }

    private TextField<String> getUserId() {
        if (this.userId == null) {
            this.userId = new TextField<>("userId", Model.of(Empty.STRING)); //$NON-NLS-1$
            this.userId.setRequired(true);
        }
        return this.userId;
    }

    private class Handler implements Serializable {
        private static final long        serialVersionUID   = 6317461189636878176L;

        private final ErrorClassAppender errorClassAppender = new ErrorClassAppender(Model.of("error")); //$NON-NLS-1$

        private void onSubmitterError(final AjaxRequestTarget pTarget) {
            this.errorClassAppender.addErrorClass(getForm());
            pTarget.add(getUserId());
            pTarget.add(getPassword());
        }

        private void tryLogin(final AjaxRequestTarget pTarget) {
            try {
                AppSession.get().login(getUserId().getModelObject(), getPassword().getModelObject());
                setResponsePage(getApplication().getHomePage());
            } catch (final FailAuthentication e) {
                error(getString("message.failLogin")); //$NON-NLS-1$
                this.errorClassAppender.addErrorClass(getForm());
            }
            pTarget.add(getUserId());
            pTarget.add(getPassword());
        }
    }

}
