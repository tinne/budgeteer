package org.wickedsource.budgeteer.web.components.notificationlist;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.service.notification.*;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.pages.budgets.details.BudgetDetailsPage;
import org.wickedsource.budgeteer.web.pages.budgets.edit.EditBudgetPage;
import org.wickedsource.budgeteer.web.pages.imports.fileimport.ImportFilesPage;
import org.wickedsource.budgeteer.web.pages.person.edit.EditPersonPage;
import org.wickedsource.budgeteer.web.pages.user.edit.EditUserPage;
import org.wickedsource.budgeteer.web.pages.user.resettoken.ResetTokenPage;

import java.io.Serializable;

public class NotificationLinkFactory implements Serializable {

    public WebPage getLinkForNotification(Notification notification, Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        if (notification instanceof MissingDailyRateNotification) {
            MissingDailyRateNotification missingDailyRateNotification = (MissingDailyRateNotification) notification;
            return new EditPersonPage(EditPersonPage.createParameters(missingDailyRateNotification.getPersonId()), backlinkPage, backlinkParameters);
        } else if (notification instanceof MissingBudgetTotalNotification) {
            MissingBudgetTotalNotification missingBudgetTotalNotification = (MissingBudgetTotalNotification) notification;
            return new EditBudgetPage(EditBudgetPage.createParameters(missingBudgetTotalNotification.getBudgetId()), backlinkPage, backlinkParameters, false);
        } else if (notification instanceof MissingContractForBudgetNotification) {
            return new EditBudgetPage(EditBudgetPage.createParameters(((MissingContractForBudgetNotification) notification).getBudgetId()), backlinkPage, backlinkParameters, false);
        } else if (notification instanceof EmptyWorkRecordsNotification) {
            return new ImportFilesPage(backlinkPage, backlinkParameters);
        } else if (notification instanceof EmptyPlanRecordsNotification) {
            return new ImportFilesPage(backlinkPage, backlinkParameters);
        } else if (notification instanceof LimitReachedNotification) {
            return new BudgetDetailsPage(BudgetDetailsPage.createParameters(((LimitReachedNotification) notification).getBudgetId()));
        } else if (notification instanceof MailNotVerifiedNotification) {
            return new ResetTokenPage(new PageParameters().add("userId", BudgeteerSession.get().getLoggedInUser().getId()));
        } else if (notification instanceof MissingMailNotification) {
            return new EditUserPage(backlinkPage, backlinkParameters.add("userId", BudgeteerSession.get().getLoggedInUser().getId()));
        } else {
            throw new IllegalArgumentException(String.format("Notifications of type %s are not supported!", notification.getClass()));
        }
    }
}
