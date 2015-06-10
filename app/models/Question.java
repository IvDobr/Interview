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

    private Boolean manyVariants; //множественный выбор

    private Boolean userVariant; //вариант пользователя

    @Constraints.Required
    @ManyToOne
    private Interview questionParent; //в каком опросе находится вопрос

    @OneToMany(cascade=CascadeType.ALL, mappedBy="variantParent")
    private List<Variant> variants;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="parentQuestion")
    private List<Check_variant> user_variants;

    public Question() {
    }

    public Question(String questionTitle, Boolean required, Boolean manyVariants, Boolean userVariant, Interview questionParent) {
        this.questionTitle = questionTitle;
        this.required = required;
        this.manyVariants = manyVariants;
        this.userVariant = userVariant;
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

    public List<Check_variant> getUser_variants() {
        return user_variants;
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

    public Boolean getManyVariants() {
        return manyVariants;
    }

    public void setManyVariants(Boolean manyVariants) {
        this.manyVariants = manyVariants;
    }

    public Boolean getUserVariant() {
        return userVariant;
    }

    public void setUserVariant(Boolean userVariant) {
        this.userVariant = userVariant;
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

        return getQuestionInfoJSON;
    }
}
