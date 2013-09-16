/**
 * 
 */
package sandbox.quickstart.web.ui.page;

import jabara.wicket.CssUtil;
import jabara.wicket.IAjaxCallback;
import jabara.wicket.JavaScriptUtil;
import jabara.wicket.Models;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.resource.TextTemplateResourceReference;

import sandbox.quickstart.web.ui.component.BuildingListPanel;

/**
 * @author jabaraster
 */
public class MapPage extends RestrictedPageBase {
    private static final long serialVersionUID = 6625851280961344375L;

    private BuildingListPanel buildings;

    private Link<?>           goLogout;
    private Link<?>           reloader;

    /**
     * 
     */
    public MapPage() {
        this.add(getBuildings());
        this.add(getGoLogout());
        this.add(getReloader());
    }

    /**
     * @see sandbox.quickstart.web.ui.page.WebPageBase#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        CssUtil.addComponentCssReference(pResponse, MapPage.class);

        addContextPathJavaScriptReference(pResponse);
        pResponse.render(JavaScriptHeaderItem.forReference(buildVarsJavaScriptReference()));

        addAppJavascriptReference(pResponse);
        pResponse.render(JavaScriptHeaderItem.forUrl("http://maps.google.com/maps/api/js?v=3&sensor=false")); //$NON-NLS-1$
        JavaScriptUtil.addComponentJavaScriptReference(pResponse, MapPage.class);
    }

    /**
     * @see sandbox.quickstart.web.ui.page.WebPageBase#getTitleLabelModel()
     */
    @Override
    protected IModel<String> getTitleLabelModel() {
        return Models.readOnly("地図情報システム"); //$NON-NLS-1$
    }

    private ResourceReference buildVarsJavaScriptReference() {
        final Map<String, Object> params = new HashMap<>();
        params.put("buildingsSearcherId", getBuildings().getSearcher().getMarkupId()); //$NON-NLS-1$
        return new TextTemplateResourceReference( //
                WebPageBase.class //
                , MapPage.class.getSimpleName() + "_vars.js" // //$NON-NLS-1$
                , "text/javascript" // //$NON-NLS-1$
                , Models.readOnly(params));
    }

    @SuppressWarnings("serial")
    private BuildingListPanel getBuildings() {
        if (this.buildings == null) {
            this.buildings = new BuildingListPanel("buildings"); //$NON-NLS-1$
            this.buildings.setOnCloseClick(new IAjaxCallback() {
                @Override
                public void call(final AjaxRequestTarget pTarget) {
                    pTarget.appendJavaScript("markers.hideBuildingsPanel();"); //$NON-NLS-1$
                }
            });
            this.buildings.setOutputMarkupPlaceholderTag(true);
        }
        return this.buildings;
    }

    private Link<?> getGoLogout() {
        if (this.goLogout == null) {
            this.goLogout = new BookmarkablePageLink<>("goLogout", LogoutPage.class); //$NON-NLS-1$
        }
        return this.goLogout;
    }

    private Link<?> getReloader() {
        if (this.reloader == null) {
            this.reloader = new BookmarkablePageLink<>("reloader", MapPage.class); //$NON-NLS-1$
        }
        return this.reloader;
    }
}
