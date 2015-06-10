package controllers;

import com.avaje.ebean.Ebean;
import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.interview;
import views.html.supportPage;

import java.util.Map;

public class InterviewCtrl extends Controller {

    public static Result send(Integer id) {
        Interview interviewObj = Ebean.find(Interview.class, id);
        if (null==interviewObj) {
            return badRequest(supportPage.render(
                    "К сожалению, опрос не найден :(",
                    "Обратитесь к создателю опроса или зарегистрируйтесь и сделайте свой опрос!"));
        }
        return ok(interview.render(interviewObj));
    }

    public static Result get(Integer id) {
        Map<String, String> requestData = Form.form().bindFromRequest().data();
        Interview interviewObj = Ebean.find(Interview.class, id);

        if (null==interviewObj) {
            return badRequest(supportPage.render(
                    "К сожалению, опрос не найден :(",
                    "Обратитесь к создателю опроса или зарегистрируйтесь и сделайте свой опрос!"));
        }

        if (null == requestData.get("userName")) requestData.put("userName", "-");
        Answer answer = new Answer(requestData.get("userName"), interviewObj);
        Ebean.save(answer);

        for (String key: requestData.keySet()) {
            if (requestData.get(key).equals("userVar")) {
                Integer qId = Integer.parseInt(key);
                Question question  = Ebean.find(Question.class, qId);
                Ebean.save(new Check_variant(answer, requestData.get("var_" + qId), question));
            } else if (requestData.get(key).matches("[0-9]+")){
                Integer varId = Integer.parseInt(requestData.get(key));
                Variant variant = Ebean.find(Variant.class, varId);
                Ebean.save(new Check_variant(answer, variant));
            }
        }

        return ok(supportPage.render("Спасибо за Ваш ответ!", ""));
    }
}
