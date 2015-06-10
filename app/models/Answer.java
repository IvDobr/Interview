package models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Answer extends Model {

    public static Finder<String, Answer> find = new Finder<String, Answer>(String.class, Answer.class);

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="answer_seq")
    private Integer answerId;

    private Date createDate;

    private String userName;

    @Constraints.Required
    @ManyToOne
    private Interview interview;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="answer")
    private List<Check_variant> check_variants;

    public Answer() {
    }

    public Answer(String userName, Interview interview) {
        this.createDate = new java.util.Date();
        this.userName = userName;
        this.interview = interview;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Interview getInterview() {
        return interview;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }

    public List<Check_variant> getCheck_variants() {
        return check_variants;
    }

    public ObjectNode getAnswerInfoJSON() {
        ObjectNode getAnswerInfoJSON = Json.newObject();
        DateFormat date = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

        getAnswerInfoJSON.put("answerId", this.answerId);
        getAnswerInfoJSON.put("interview", this.interview.getInterviewTitle()); //кладем заголовок опроса
        getAnswerInfoJSON.put("createDate", date.format(this.createDate)); //кладем дату
        getAnswerInfoJSON.put("userName", this.userName); //кладем имя ответевшего юзера

        List<ObjectNode> cv_s = new LinkedList<>();
        for(Check_variant i : this.check_variants) {
            cv_s.add(i.getCheck_variantInfoJSON());
        }
        getAnswerInfoJSON.put("check_variants", Json.toJson(cv_s));

        return getAnswerInfoJSON;
    }
}
