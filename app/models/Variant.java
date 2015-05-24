package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

import javax.persistence.*;

@Entity
public class Variant extends Model {

    public static Finder<String, Variant> find = new Finder<String, Variant>(String.class, Variant.class);

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="variant_seq")
    private Integer variantId;

    private String variant;

    @Constraints.Required
    @ManyToOne
    private Question variantParent;

    public Variant(String variant, Question variantParent) {
        this.variant = variant;
        this.variantParent = variantParent;
    }

    public Question getVariantParent() {
        return variantParent;
    }

    public void setVariantParent(Question variantParent) {
        this.variantParent = variantParent;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public ObjectNode getVariantInfoJSON() {
        ObjectNode getVariantInfoJSON = Json.newObject();

        getVariantInfoJSON.put("variantId", this.variantId);
        getVariantInfoJSON.put("variant", this.variant);

        return getVariantInfoJSON;
    }

}
