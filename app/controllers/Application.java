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
    
    private static User user() {
        return User.find.byId(Crypto.decryptAES(session("current_user")));
    } 

    public static Result index() {
        return ok(index.render(user(), 1, null));
    }

    public static Result seeAnswers(Integer id) {
        if (0 == id) return ok(index.render(user(), 2, null));
        else return ok(index.render(user(), 0, Ebean.find(Interview.class, id)));
    }

    public static Result createNew() {
        return ok(index.render(user(), 3, null));
    }

    public static Result users() {
        if (user().getIsAdmin()) return ok(index.render(user(), 4, null));
        else return ok(index.render(user(), 1, null));
    }
}