package sandbox.quickstart.web.ui.page;

import jabara.general.Empty;
import jabara.wicket.ErrorClassAppender;
import jabara.wicket.JavaScriptUtil;

import java.io.Serializable;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import sandbox.quickstart.entity.EUser;
import sandbox.quickstart.model.FailAuthentication;
import sandbox.quickstart.web.ui.AppSession;

/**
 * 
 */
@SuppressWarnings("synthetic-access")
public class LoginPage extends WebPageBase {
    private static final long serialVersionUID = 1925170327965147328L;

    private final Handler     handler          = new Handler();

    private Label             defaultAdministratorUserId;
    private Label             defaultAdministratorPassword;

    private FeedbackPanel     feedback;
    private StatelessForm<?>  form;
    private TextField<String> userId;
    private FeedbackPanel     userIdFeedback;
    private PasswordTextField password;
    private FeedbackPanel     passwordFeedback;
    private Button            submitter;

    /**
     * 
     */
    public LoginPage() {
        this.add(getDefaultAdministratorUserId());
        this.add(getDefaultAdministratorPassword());
        this.add(getFeedback());
        this.add(getForm());
    }

    /**
     * @see sandbox.quickstart.web.ui.page.WebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        addPageCssReference(pResponse, getPageClass());
        pResponse.render(OnDomReadyHeaderItem.forScript(JavaScriptUtil.getFocusScript(getUserId())));
    }

    /**
     * @see sandbox.quickstart.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Model.of(getString("pageTitle")); //$NON-NLS-1$
    }

    private Label getDefaultAdministratorPassword() {
        if (this.defaultAdministratorPassword == null) {
            this.defaultAdministratorPassword = new Label("defaultAdministratorPassword", EUser.DEFAULT_ADMINISTRATOR_PASSWORD); //$NON-NLS-1$
        }
        return this.defaultAdministratorPassword;
    }

    private Label getDefaultAdministratorUserId() {
        if (this.defaultAdministratorUserId == null) {
            this.defaultAdministratorUserId = new Label("defaultAdministratorUserId", EUser.DEFAULT_ADMINISTRATOR_USER_ID); //$NON-NLS-1$
        }
        return this.defaultAdministratorUserId;
    }

    private FeedbackPanel getFeedback() {
        if (this.feedback == null) {
            this.feedback = new ComponentFeedbackPanel("feedback", this); //$NON-NLS-1$
        }
        return this.feedback;
    }

    private StatelessForm<?> getForm() {
        if (this.form == null) {
            this.form = new StatelessForm<Object>("form"); //$NON-NLS-1$
            this.form.add(getUserId());
            this.form.add(getUserIdFeedback());
            this.form.add(getPassword());
            this.form.add(getPasswordFeedback());
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

    private FeedbackPanel getPasswordFeedback() {
        if (this.passwordFeedback == null) {
            this.passwordFeedback = new ComponentFeedbackPanel("passwordFeedback", getPassword()); //$NON-NLS-1$
        }
        return this.passwordFeedback;
    }

    @SuppressWarnings("serial")
    private Button getSubmitter() {
        if (this.submitter == null) {
            this.submitter = new Button("submitter") { //$NON-NLS-1$
                @Override
                public void onError() {
                    LoginPage.this.handler.onSubmitterError();
                }

                @Override
                public void onSubmit() {
                    LoginPage.this.handler.tryLogin();
                }
            };
        }
        return this.submitter;
    }

    private TextField<String> getUserId() {
        if (this.userId == null) {
            this.userId = new TextField<String>("userId", Model.of(Empty.STRING)); //$NON-NLS-1$
            this.userId.setRequired(true);
        }
        return this.userId;
    }

    private FeedbackPanel getUserIdFeedback() {
        if (this.userIdFeedback == null) {
            this.userIdFeedback = new ComponentFeedbackPanel("userIdFeedback", getUserId()); //$NON-NLS-1$
        }
        return this.userIdFeedback;
    }

    private class Handler implements Serializable {
        private static final long        serialVersionUID   = 6317461189636878176L;

        private final ErrorClassAppender errorClassAppender = new ErrorClassAppender(Model.of("error")); //$NON-NLS-1$

        private void onSubmitterError() {
            this.errorClassAppender.addErrorClass(getForm());
        }

        private void tryLogin() {
            try {
                AppSession.get().login(getUserId().getModelObject(), getPassword().getModelObject());
                setResponsePage(getApplication().getHomePage());
            } catch (final FailAuthentication e) {
                error(getString("message.failLogin")); //$NON-NLS-1$
                this.errorClassAppender.addErrorClass(getForm());
            }
        }
    }

}
