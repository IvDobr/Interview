package controllers;

import com.avaje.ebean.Ebean;
import models.Interview;
import models.User;
import play.api.libs.Crypto;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.index;

@Security.Authenticated(Secured.class)
public class Application extends Controller {
    
    private static String getName() {
        User current_user = User.find.byId(Crypto.decryptAES(session("current_user")));
        return current_user.getUserFirstName() + " " + current_user.getUserLastName();
    }

    public static Result index() {
        return ok(index.render(getName(), 1, null));
    }

    public static Result seeAnswers(Integer id) {
        if (0 == id) return ok(index.render(getName(), 2, null));
        else return ok(index.render(getName(), 0, Ebean.find(Interview.class, id)));
    }

    public static Result createNew() {
        return ok(index.render(getName(), 3, null));
    }
}
