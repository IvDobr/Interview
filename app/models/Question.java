package models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Question extends Model {

    public static Finder<String, Question> find = new Finder<String, Question>(String.class, Question.class);

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="question_seq")
    private Integer questionId;

    @Constraints.Required
    private String questionTitle; //собственно сам вопрос

    private Boolean required; //обязателен ли вопрос

    @Constraints.Required
    @ManyToOne
    private Interview questionParent; //в каком опросе находится вопрос

    @OneToMany(mappedBy="variantParent")
    private List<Variant> variants;

    public Question() {
    }

    public Question(String questionTitle, Interview questionParent) {
        this.questionTitle = questionTitle;
        this.questionParent = questionParent;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public Interview getQuestionParent() {
        return questionParent;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public void setQuestionParent(Interview questionParent) {
        this.questionParent = questionParent;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public ObjectNode getQuestionInfoJSON() {
        ObjectNode getQuestionInfoJSON = Json.newObject();

        getQuestionInfoJSON.put("questionId", this.questionId);
        getQuestionInfoJSON.put("questionTitle", this.questionTitle);
        getQuestionInfoJSON.put("required", this.required);

        List<ObjectNode> v_s = new LinkedList<>();
        for(Variant i : this.variants) {
            v_s.add(i.getVariantInfoJSON());
        }
        getQuestionInfoJSON.put("variants", Json.toJson(v_s));
        //TODO ^^^ проверить ^^^

        return getQuestionInfoJSON;
    }
}
