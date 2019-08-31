
package in.gohelper.models.servicemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("menu_title")
    @Expose
    private String menuTitle;
    @SerializedName("base_price")
    @Expose
    private String basePrice;
    @SerializedName("night_charges")
    @Expose
    private String nightCharges;
    @SerializedName("price_type_label")
    @Expose
    private String priceTypeLabel;
    @SerializedName("price_type_key")
    @Expose
    private String priceTypeKey;
    @SerializedName("childs")
    @Expose
    private List<ServiceData> childs = null;
    @SerializedName("meta")
    @Expose
    private List<Metum> meta = null;
    @SerializedName("questions")
    @Expose
    private List<Question> questions = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public String getNightCharges() {
        return nightCharges;
    }

    public void setNightCharges(String nightCharges) {
        this.nightCharges = nightCharges;
    }

    public String getPriceTypeLabel() {
        return priceTypeLabel;
    }

    public void setPriceTypeLabel(String priceTypeLabel) {
        this.priceTypeLabel = priceTypeLabel;
    }

    public String getPriceTypeKey() {
        return priceTypeKey;
    }

    public void setPriceTypeKey(String priceTypeKey) {
        this.priceTypeKey = priceTypeKey;
    }

    public List<ServiceData> getChilds() {
        return childs;
    }

    public void setChilds(List<ServiceData> childs) {
        this.childs = childs;
    }

    public List<Metum> getMeta() {
        return meta;
    }

    public void setMeta(List<Metum> meta) {
        this.meta = meta;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

}
