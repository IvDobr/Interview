package controllers;

import com.avaje.ebean.Ebean;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Crypto;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.auth;

import play.Logger;

public class Secure extends Controller {

    public static Result index() {
        if(session("current_user") == null){
            return ok(auth.render(""));
        } else {
            return redirect(routes.Application.index());
        }
    }

    public static Result reg() {
        DynamicForm requestData = Form.form().bindFromRequest();
        try{
            Ebean.save(new User(
                    requestData.get("userLoginReg"),
                    requestData.get("userFirstNameReg"),
                    requestData.get("userLastNameReg"),
                    requestData.get("userPassReg"),
                    false));
            return badRequest(auth.render("Регистрация успешна! Входите :)"));
        } catch (Exception e){
            Logger.error("Неверные данные: " + e.getLocalizedMessage());
            return badRequest(auth.render("Регистрация не успешна :("));
        }
    }

    public static Result logInProc() {
        DynamicForm requestData = Form.form().bindFromRequest();

        User current_user;
        try{
            current_user = User.find.where()
                    .like("userLogin", requestData.get("userLogin")).findUnique();
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