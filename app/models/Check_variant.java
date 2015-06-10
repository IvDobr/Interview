package models;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;

@Entity
public class Check_variant extends Model {

    public static Finder<String, Check_variant> find = new Finder<String, Check_variant>(String.class, Check_variant.class);

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="check_variant_seq")
    private Integer check_variantId;

    @Constraints.Required
    @ManyToOne
    private Answer answer;
    
    private String userVar;

    @ManyToOne
    private Question parentQuestion;

    @ManyToOne
    private Variant variant;

    public Check_variant() {
    }

    public Check_variant(Answer answer, Variant variant) {
        this.answer = answer;
        this.variant = variant;
    }

    public Check_variant(Answer answer, String userVar, Question parentQuestion) {
        this.answer = answer;
        this.userVar = userVar;
        this.parentQuestion = parentQuestion;
    }

    public Integer getCheck_variantId() {
        return check_variantId;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public String getUserVar() {
        return userVar;
    }

    public void setUserVar(String userVar) {
        this.userVar = userVar;
    }

    public Question getParentQuestion() {
        return parentQuestion;
    }

    public void setParentQuestion(Question parent) {
        this.parentQuestion = parent;
    }

    public ObjectNode getCheck_variantInfoJSON() {
        ObjectNode getCheck_variantInfoJSON = Json.newObject();

        if (null == variant){
            getCheck_variantInfoJSON.put("question", this.parentQuestion.getQuestionTitle()); //Кладем вопрос
            getCheck_variantInfoJSON.put("answer", userVar); //Кладем ответ на вопрос
        } else {
            getCheck_variantInfoJSON.put("question", this.variant.getVariantParent().getQuestionTitle()); //Кладем вопрос
            getCheck_variantInfoJSON.put("answer", this.variant.getVariant()); //Кладем ответ на вопрос
        }
        return getCheck_variantInfoJSON;
    }
}
