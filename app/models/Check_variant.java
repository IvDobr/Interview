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

    @Constraints.Required
    @ManyToOne
    private Variant variant;

    public Check_variant() {
    }

    public Check_variant(Answer answer, Variant variant) {
        this.answer = answer;
        this.variant = variant;
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

    public ObjectNode getCheck_variantInfoJSON() {
        ObjectNode getCheck_variantInfoJSON = Json.newObject();
        getCheck_variantInfoJSON.put("question", this.variant.getVariantParent().getQuestionTitle()); //Кладем вопрос
        getCheck_variantInfoJSON.put("question", this.variant.getVariant()); //Кладем ответ на вопрос
        return getCheck_variantInfoJSON;
    }
}
