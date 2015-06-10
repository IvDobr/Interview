package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.PagingList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.Logger;
import play.Routes;
import play.libs.Crypto;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class API extends Controller {

    private static User getUser() {
        return User.find.byId(Crypto.decryptAES(session("current_user")));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result newInterviewJSON() {
        ObjectNode result = Json.newObject();
        JsonNode request = request().body().asJson();
        try {
            Interview interview = new Interview(
                    request.findPath("title").textValue(),
                    request.findPath("description").textValue(),
                    request.findPath("reqUserName").booleanValue(),
                    getUser());

            Ebean.save(interview);

            Iterator<JsonNode> questions = request.findPath("questions").elements();

            while (questions.hasNext()) {
                JsonNode q = questions.next();
                try {
                    Question question = new Question(
                            q.findPath("question").textValue(),
                            q.findPath("required").booleanValue(),
                            q.findPath("manyVars").booleanValue(),
                            q.findPath("userVar").booleanValue(),
                            interview
                    );
                    Ebean.save(question);
                    Iterator<JsonNode> variants = q.findPath("variants").elements();
                    while (variants.hasNext()){
                        JsonNode v = variants.next();
                        try {
                            Variant variant = new Variant(
                                    v.findPath("variant").textValue(),
                                    question
                            );
                            Ebean.save(variant);
                        } catch (Exception e) {
                            Logger.error(e.getMessage());
                            result.put("status", "error");
                            return badRequest(result);
                        }
                    }
                } catch (Exception e) {
                    Logger.error(e.getMessage());
                    result.put("status", "error");
                    return badRequest(result);
                }
            }
            result.put("id", interview.getInterviewId());
            result.put("status","OK");
            return ok(result);
        } catch (Exception e) {
            Logger.error(e.getMessage());
            result.put("status", "error");
            return badRequest(result);
        }
    }

    public static Result getInterviewJSON(String sortby, String search) {
        ObjectNode result = Json.newObject();

        List<String> sorters = Arrays.asList(
                "interviewTitle",
                "interviewTitle desc",
                "interviewId",
                "interviewId desc");
        if (!sorters.contains(sortby)) sortby = "interviewId desc";

        List<ObjectNode> interviewList = Ebean
                .find(Interview.class)
                .where()
                .eq("interviewOwner", getUser())
                .icontains("interviewTitle", search)
                .orderBy(sortby)
                .findList()
                .stream()
                .map(Interview::getGeneralInfoJSON)
                .collect(Collectors.toList());

        result.set("interviewList", Json.toJson(interviewList));
        result.put("status","OK");
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result removeInterviewJSON(){
        JsonNode request = request().body().asJson();
        ObjectNode result = Json.newObject();
        try {
            Ebean.find(Interview.class, request.findPath("id").intValue()).delete();
            result.put("status", "OK");
            return ok(result);
        } catch (Exception e) {
            Logger.error("Невозможно удалить опрос: " + e.getMessage());
            result.put("status", "error");
            return badRequest(result);
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result editInterviewJSON() {
        ObjectNode result = Json.newObject();
        JsonNode request = request().body().asJson();
        try {
            Interview interview = new Interview(
                    request.findPath("title").textValue(),
                    request.findPath("description").textValue(),
                    request.findPath("reqUserName").booleanValue(),
                    getUser());

            Ebean.save(interview);

            Iterator<JsonNode> questions = request.findPath("questions").elements();

            while (questions.hasNext()) {
                JsonNode q = questions.next();
                try {
                    Question question = new Question(
                            q.findPath("question").textValue(),
                            q.findPath("required").booleanValue(),
                            q.findPath("manyVars").booleanValue(),
                            q.findPath("userVar").booleanValue(),
                            interview
                    );
                    Ebean.save(question);
                    Iterator<JsonNode> variants = q.findPath("variants").elements();
                    while (variants.hasNext()){
                        JsonNode v = variants.next();
                        try {
                            Variant variant = new Variant(
                                    v.findPath("variant").textValue(),
                                    question
                            );
                            Ebean.save(variant);
                        } catch (Exception e) {
                            Logger.error(e.getMessage());
                            result.put("status", "error");
                            return badRequest(result);
                        }
                    }
                } catch (Exception e) {
                    Logger.error(e.getMessage());
                    result.put("status", "error");
                    return badRequest(result);
                }
            }
            result.put("id", interview.getInterviewId());
            result.put("status","OK");
            return ok(result);
        } catch (Exception e) {
            Logger.error(e.getMessage());
            result.put("status", "error");
            return badRequest(result);
        }
    }

    public static Result getAnswersJSON(Integer intId, String sortby, String search, int pageSize, int currentPage) {
        ObjectNode result = Json.newObject();

        List<String> sorters = Arrays.asList(
                "answerId",
                "answerId desc");
        if (!sorters.contains(sortby)) sortby = "answerId desc";

        try {
            List<Interview> interviewList = new ArrayList<>();
            if (0 == intId) interviewList = Ebean
                    .find(Interview.class)
                    .where()
                    .eq("interviewOwner", getUser())
                    .findList();
            else interviewList.add(Ebean.find(Interview.class, intId));

            PagingList<Answer> answerPagedList = Ebean
                    .find(Answer.class)
                    .where()
                    .in("interview", interviewList)
                    .icontains("userName", search)
                    .orderBy(sortby).findPagingList(pageSize);

            List<ObjectNode> answersList = answerPagedList
                    .getPage(currentPage - 1)
                    .getList()
                    .stream()
                    .map(Answer::getAnswerInfoJSON)
                    .collect(Collectors.toList());

            result.set("answersList", Json.toJson(answersList));
            result.put("pages", answerPagedList.getTotalPageCount());
            result.put("countAnswers", answerPagedList.getTotalRowCount());
            result.put("status", "OK");
            return ok(result);
        } catch (Exception e) {
            Logger.error("Ошибка вывода: " + e.getLocalizedMessage());
            result.put("status", "error");
            return badRequest(result);
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result removeAnswerJSON(){
        JsonNode request = request().body().asJson();
        ObjectNode result = Json.newObject();
        try {
            Ebean.find(Answer.class, request.findPath("id").intValue()).delete();
            result.put("status", "OK");
            return ok(result);
        } catch (Exception e) {
            Logger.error("Невозможно удалить ответ: " + e.getMessage());
            result.put("status", "error");
            return badRequest(result);
        }
    }

    public static Result getAllUsersJSON(int pageSize, int currentPage, String sortby, String search){
        if (!getUser().getIsAdmin()) return badRequest("Доступ запрещен!");
        ObjectNode result = Json.newObject();

        List<String> str = Arrays.asList(
                "userLogin",
                "userLogin desc",
                "userLastName",
                "userLastName desc",
                "userFirstName desc",
                "userFirstName",
                "isAdmin desc",
                "isAdmin",
                "userId desc",
                "userId"
        );

        if (!str.contains(sortby)) sortby = "userId desc";

        PagingList<User> users = Ebean
                .find(User.class)
                .where()
                .or(
                        Expr.icontains("userLogin", search),
                        Expr.or(
                                Expr.icontains("userLastName", search),
                                Expr.icontains("userFirstName", search)
                        )
                )
                .orderBy(sortby)
                .findPagingList(pageSize);

        List<ObjectNode> usersListJSON = users
                .getPage(currentPage - 1)
                .getList()
                .stream()
                .map(User::getUserInfoJSON)
                .collect(Collectors.toList());

        result.put("pages", users.getTotalPageCount());
        result.put("countUsers", users.getTotalRowCount());
        result.put("status","OK");
        result.set("users", Json.toJson(usersListJSON));
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result editUserJSON() {
        if (!getUser().getIsAdmin()) return badRequest("Доступ запрещен!");
        JsonNode request = request().body().asJson();
        ObjectNode result = Json.newObject();

        try {
            User u = Ebean.find(User.class, request.findPath("userId").intValue());

            u.setUserPass(request.findPath("editUserPass").textValue());
            u.setUserFirstName(request.findPath("editUserFirstName").textValue());
            u.setUserLastName(request.findPath("editUserLastName").textValue());
            u.setIsAdmin(request.findPath("editUserStatus").booleanValue());
            u.setUserLogin(request.findPath("editUserLogin").textValue());

            Ebean.save(u);
            result.put("status","OK");
            return ok(result);
        } catch (Exception e) {
            Logger.error("Невозможно изменить аккаунт! " + e.getMessage());
            result.put("status", "error");
            return badRequest(result);
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result removeUserJSON() {
        if (!getUser().getIsAdmin()) return badRequest("Доступ запрещен!");
        JsonNode request = request().body().asJson();
        ObjectNode result = Json.newObject();
        User user = User.find.byId(request.findPath("userId").toString());

        if (!Crypto.decryptAES(session("current_user")).equals(request.findPath("userId").toString())) {
            try {
                user.delete();
                result.put("status", "OK");
                return ok(result);
            } catch (Exception e) {
                Logger.error("Невозможно удалить аккаунт!");
                result.put("status", "error");
                return badRequest(result);
            }
        } else {
            result.put("status", "error");
            return badRequest(result);
        }
    }
    
    public static Result jsRoutes() {
        response().setContentType("text/javascript");
        return ok(
                Routes.javascriptRouter("jsRoutes",
                        controllers.routes.javascript.API.newInterviewJSON(),
                        controllers.routes.javascript.API.getInterviewJSON(),
                        controllers.routes.javascript.API.removeInterviewJSON(),
                        controllers.routes.javascript.API.editInterviewJSON(),
                        controllers.routes.javascript.API.getAnswersJSON(),
                        controllers.routes.javascript.API.removeAnswerJSON(),
                        controllers.routes.javascript.API.getAllUsersJSON(),
                        controllers.routes.javascript.API.editUserJSON(),
                        controllers.routes.javascript.API.removeUserJSON()
                )
        );
    }
}
