package de.adesso.budgeteer.web.pages.user.login;

import de.adesso.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import de.adesso.budgeteer.web.pages.administration.BudgeteerAdministrationOverview;
import de.adesso.budgeteer.web.pages.base.dialogpage.DialogPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.InvalidLoginCredentialsException;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.settings.BudgeteerSettings;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount("/login")
public class LoginPage extends DialogPage {

    @SpringBean
    private UserService userService;

    @SpringBean
    private BudgeteerSettings settings;

    public LoginPage() {
        Form<LoginCredentials> form = new Form<LoginCredentials>("loginForm", model(from(new LoginCredentials()))) {
            @Override
            protected void onSubmit() {
                try {
                    User user = userService.login(getModelObject().getUsername(), getModelObject().getPassword());
                    if(userService.getAllAdmins().contains(user)){
                        BudgeteerSession.get().login(user);
                        setResponsePage(BudgeteerAdministrationOverview.class);
                    }else if(userService.getAllAdmins().isEmpty()){ //The first user to log into this tool gets the admin role
                        userService.setGlobalRoleForUser(user.getId(), "admin");
                        BudgeteerSession.get().login(user);
                        setResponsePage(BudgeteerAdministrationOverview.class);
                    } else{
                        error("You are not authorized to access the administration menu!");
                    }
                } catch (InvalidLoginCredentialsException e) {
                    error(getString("message.invalidLogin"));
                }
            }
        };
        add(form);

        form.add(new CustomFeedbackPanel("feedback"));
        form.add(new RequiredTextField<String>("username", model(from(form.getModel()).getUsername())));
        form.add(new PasswordTextField("password", model(from(form.getModel()).getPassword())));
    }
}
