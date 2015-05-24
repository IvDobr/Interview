package controllers;

import models.User;
import play.api.libs.Crypto;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    public static Result index() {
        User current_user = User.find.byId(Crypto.decryptAES(session("current_user")));
        return ok(index.render(current_user.getUserFirstName() + " " + current_user.getUserLastName()));
    }

}
