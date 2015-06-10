package models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
public class Interview extends Model {

    public static Finder<String, Interview> find = new Finder<String, Interview>(String.class, Interview.class);

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="interview_seq")
    private Integer interviewId;

    @Constraints.Required
    private String interviewTitle; //Название опроса

    private Boolean reqUserName; // спрашивать ли имя у пользователя?

    @Column(columnDefinition="VARCHAR(4095)") //большое поле, можно писать много текста
    private String interviewText; //пояснение опроса

    @Constraints.Required
    @ManyToOne
    private User interviewOwner; //Хозяин опроса

    private Date createDate; //Дата создания опроса

    @OneToMany(cascade=CascadeType.ALL, mappedBy="questionParent")
    private List<Question> qestions;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="interview")
    private List<Answer> answers;

    public Interview() {
    }

    public Interview(String interviewTitle, String interviewText, Boolean reqUserName, User interviewOwner) {
        this.interviewTitle = interviewTitle;
        this.interviewText = interviewText;
        this.reqUserName = reqUserName;
        this.interviewOwner = interviewOwner;
        this.createDate = new java.util.Date();
    }

    public Integer getInterviewId() {
        return interviewId;
    }

    public String getInterviewTitle() {
        return interviewTitle;
    }

    public void setInterviewTitle(String interviewTitle) {
        this.interviewTitle = interviewTitle;
    }

    public User getInterviewOwner() {
        return interviewOwner;
    }

    public void setInterviewOwner(User interviewOwner) {
        this.interviewOwner = interviewOwner;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getInterviewText() {
        return interviewText;
    }

    public void setInterviewText(String interviewText) {
        this.interviewText = interviewText;
    }

    public Boolean getReqUserName() {
        return reqUserName;
    }

    public void setReqUserName(Boolean reqUserName) {
        this.reqUserName = reqUserName;
    }

    public List<Question> getQestions() {
        return qestions;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public ObjectNode getGeneralInfoJSON() {
        ObjectNode getInterviewInfoJSON = Json.newObject();
        DateFormat date = new SimpleDateFormat("dd.MM.yy HH:mm");

        getInterviewInfoJSON.put("interviewId", this.interviewId);
        getInterviewInfoJSON.put("interviewTitle", this.interviewTitle);
        getInterviewInfoJSON.put("interviewDate", date.format(this.createDate));
        getInterviewInfoJSON.put("interviewQcount", this.qestions.size());
        getInterviewInfoJSON.put("interviewAcount", this.answers.size());

        return getInterviewInfoJSON;
    }
}
