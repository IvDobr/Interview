package controllers;

import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Crypto;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.auth;

import java.util.List;

public class Secure extends Controller {

    public static Result index() {
        if(session("current_user") == null){
            return ok(auth.render(""));
        } else {
            return redirect(routes.Application.index());
        }
    }

    public static Result logInProc() {
        DynamicForm requestData = Form.form().bindFromRequest();

        List<User> userList = User.find.where()
                .ilike("userLogin", requestData.get("userLogin"))
                .findList();

        User current_user;
        try{
            current_user = userList.get(0);
        } catch (Exception e){
            System.out.println("INFO: Неверный логин или пароль!");
            return badRequest(auth.render("Неверный логин или пароль!"));
        }

        if (current_user != null) {
            if (requestData.get("userPass").equals(current_user.getUserPass())) {
                session("current_user", Crypto.encryptAES(current_user.getUserId().toString()));
                return redirect(routes.Application.index());
            } else {
                System.out.println("INFO: Неверный логин или пароль!");
                return badRequest(auth.render("Неверный логин или пароль!"));
            }
        } else {
            System.out.println("INFO: Неверный логин или пароль!");
            return badRequest(auth.render("Неверный логин или пароль!"));
        }
    }

    public static Result logOutProc() {
        session().clear();
        System.out.println("INFO: Выход из сессии");
        return redirect(routes.Secure.index());
    }
}