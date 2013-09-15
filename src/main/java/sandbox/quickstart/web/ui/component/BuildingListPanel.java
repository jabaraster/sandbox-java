/**
 * 
 */
package sandbox.quickstart.web.ui.component;

import jabara.general.Sort;
import jabara.jpa.entity.EntityBase_;
import jabara.wicket.CssUtil;
import jabara.wicket.IAjaxCallback;
import jabara.wicket.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import sandbox.quickstart.entity.ECandidateBuilding;
import sandbox.quickstart.entity.ECandidateBuilding_;
import sandbox.quickstart.entity.EUser_;
import sandbox.quickstart.service.IBuildingService;
import sandbox.quickstart.service.IBuildingService.SearchCondition;

/**
 * @author jabaraster
 */
@SuppressWarnings("serial")
public class BuildingListPanel extends Panel {
    private static final long                                        serialVersionUID = 3519646252752890214L;

    private final Handler                                            handler          = new Handler();

    @Inject
    IBuildingService                                                 buildingService;

    private AjaxLink<?>                                              closer;
    private IAjaxCallback                                            onCloseClick;

    private Form<?>                                                  form;
    private TextField<String>                                        namePart;
    private TextField<String>                                        addressPart;
    private TextField<String>                                        registrationUserId;
    private AjaxButton                                               searcher;
    private AjaxFallbackDefaultDataTable<ECandidateBuilding, String> buildings;

    /**
     * @param pId -
     */
    public BuildingListPanel(final String pId) {
        super(pId);
        add(getCloser());
        add(getForm());
        add(getBuildings());
    }

    /**
     * @return -
     */
    public AjaxButton getSearcher() {
        if (this.searcher == null) {
            this.searcher = new IndicatingAjaxButton("searcher") { //$NON-NLS-1$
                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget, @SuppressWarnings("unused") final Form<?> pForm) {
                    BuildingListPanel.this.handler.search(pTarget);
                }
            };
        }
        return this.searcher;
    }

    /**
     * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
     */
    @Override
    public void renderHead(final IHeaderResponse pResponse) {
        super.renderHead(pResponse);
        CssUtil.addComponentCssReference(pResponse, BuildingListPanel.class);
    }

    /**
     * @param pCallback -
     */
    public void setOnCloseClick(final IAjaxCallback pCallback) {
        this.onCloseClick = pCallback;
    }

    private TextField<String> getAddressPart() {
        if (this.addressPart == null) {
            this.addressPart = new TextField<>("addressPart", Model.of("")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return this.addressPart;
    }

    private AjaxFallbackDefaultDataTable<ECandidateBuilding, String> getBuildings() {
        if (this.buildings == null) {
            final List<IColumn<ECandidateBuilding, String>> columns = new ArrayList<>();
            columns.add(new AttributeColumn<ECandidateBuilding>(ECandidateBuilding.getMeta(), EntityBase_.id));
            columns.add(new AttributeColumn<ECandidateBuilding>(ECandidateBuilding.getMeta(), ECandidateBuilding_.name));
            columns.add(new AttributeColumn<ECandidateBuilding>(ECandidateBuilding.getMeta(), ECandidateBuilding_.address));

            final String p = ECandidateBuilding_.registrationUser.getName() + "." + EUser_.userId.getName(); //$NON-NLS-1$
            columns.add(new PropertyColumn<ECandidateBuilding, String>( //
                    Models.readOnly("担当者"), //$NON-NLS-1$
                    p // sortProperty
                    , p // propertyExpression
            ));

            this.buildings = new AjaxFallbackDefaultDataTable<>("buildings", columns, new BuildingDataProvider(), 5); //$NON-NLS-1$
        }
        return this.buildings;
    }

    private AjaxLink<?> getCloser() {
        if (this.closer == null) {
            this.closer = new AjaxLink<Object>("closer") { //$NON-NLS-1$
                @Override
                public void onClick(final AjaxRequestTarget pTarget) {
                    BuildingListPanel.this.handler.onCloserClick(pTarget);
                }
            };
        }
        return this.closer;
    }

    private Form<?> getForm() {
        if (this.form == null) {
            this.form = new Form<>("form"); //$NON-NLS-1$
            this.form.add(getNamePart());
            this.form.add(getAddressPart());
            this.form.add(getRegistrationUserId());
            this.form.add(getSearcher());
        }
        return this.form;
    }

    private TextField<String> getNamePart() {
        if (this.namePart == null) {
            this.namePart = new TextField<>("namePart", Model.of("")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return this.namePart;
    }

    private TextField<String> getRegistrationUserId() {
        if (this.registrationUserId == null) {
            this.registrationUserId = new TextField<>("registrationUserId", Model.of("")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return this.registrationUserId;
    }

    private class BuildingDataProvider extends SortableDataProvider<ECandidateBuilding, String> {
        private static final long serialVersionUID = -1875936784894312670L;

        BuildingDataProvider() {
            this.setSort(EntityBase_.id.getName(), SortOrder.ASCENDING);
        }

        @Override
        public Iterator<? extends ECandidateBuilding> iterator(final long pFirst, final long pCount) {
            final SortParam<String> sort = getSort();
            final Sort s = sort.isAscending() ? Sort.asc(sort.getProperty()) : Sort.desc(sort.getProperty());

            final SearchCondition condition = createCondition();
            return BuildingListPanel.this.buildingService.search(condition, pFirst, pCount, s).iterator();
        }

        @Override
        public IModel<ECandidateBuilding> model(final ECandidateBuilding pObject) {
            return Models.of(pObject);
        }

        @Override
        public long size() {
            final SearchCondition condition = createCondition();
            return BuildingListPanel.this.buildingService.count(condition);
        }

        private SearchCondition createCondition() {
            final SearchCondition condition = new SearchCondition();
            condition.setAddressPart(getAddressPart().getModelObject());
            condition.setNamePart(getNamePart().getModelObject());
            condition.setRegistrationUserId(getRegistrationUserId().getModelObject());
            return condition;
        }
    }

    private class Handler implements Serializable {

        private void onCloserClick(final AjaxRequestTarget pTarget) {
            if (BuildingListPanel.this.onCloseClick != null) {
                BuildingListPanel.this.onCloseClick.call(pTarget);
            }
        }

        private void search(final AjaxRequestTarget pTarget) {
            pTarget.add(getBuildings());
        }
    }
}
