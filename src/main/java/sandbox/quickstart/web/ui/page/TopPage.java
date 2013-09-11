package sandbox.quickstart.web.ui.page;

import java.io.Serializable;

import org.apache.wicket.markup.html.WebPage;

/**
 *
 */
@SuppressWarnings("synthetic-access")
public class TopPage extends WebPage {
    private static final long serialVersionUID = -4965903336608758671L;

    private final Handler     handler          = new Handler();

    /**
     * 
     */
    public TopPage() {
    }

    private class Handler implements Serializable {
        private static final long serialVersionUID = 8826180320287426527L;

    }
}
