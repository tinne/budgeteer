package org.wickedsource.budgeteer.web.components.breadcrumb;

import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.resource.loader.IStringResourceLoader;
import org.wickedsource.budgeteer.web.base.BasePage;

import java.util.*;

public class BreadcrumbsModel implements IModel<List<Breadcrumb>> {

    /**
     * Name of the property key for the breadcrumb title of a page.
     */
    private static final String BREADCRUMB_TITLE_KEY = "breadcrumb.title";

    private List<Class<? extends BasePage>> crumbs;

    public BreadcrumbsModel(Class<? extends BasePage>... crumbs) {
        this.crumbs = Arrays.asList(crumbs);
    }

    @Override
    public List<Breadcrumb> getObject() {
        List<Breadcrumb> breadcrumbsList = new ArrayList<Breadcrumb>();
        for (Class<? extends BasePage> crumb : crumbs) {
            breadcrumbsList.add(new Breadcrumb(crumb, getBreadcrumbTitle(crumb)));
        }
        return breadcrumbsList;
    }

    @Override
    public void setObject(List<Breadcrumb> object) {

    }

    @Override
    public void detach() {

    }

    /**
     * Reads the breadcrumb title from the properties file of the given page class.
     */
    private String getBreadcrumbTitle(Class<? extends BasePage> clazz) {
        String resourceValue = null;
        List<IStringResourceLoader> resourceLoaders = Application.get()
                .getResourceSettings()
                .getStringResourceLoaders();
        Locale locale = Session.get().getLocale();
        String style = Session.get().getStyle();

        for (IStringResourceLoader stringResourceLoader : resourceLoaders) {
            resourceValue = stringResourceLoader.loadStringResource(clazz, BREADCRUMB_TITLE_KEY, locale, style, null);
            if (resourceValue != null) {
                break;
            }
        }

        if (resourceValue == null) {
            throw new RuntimeException(String.format("Property %s could not be found for class %s!", BREADCRUMB_TITLE_KEY, clazz.getName()));
        } else {
            return resourceValue.toString();
        }
    }
}