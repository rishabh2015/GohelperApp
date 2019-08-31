
package in.gohelper.models.servicemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Question {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("question_text")
    @Expose
    private String questionText;
    @SerializedName("q_type")
    @Expose
    private String qType;
    @SerializedName("options")
    @Expose
    private List<Option> options = null;

    @SerializedName("selected_options")
    @Expose
    private List<Option> selectedOptions = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQType() {
        return qType;
    }

    public void setQType(String qType) {
        this.qType = qType;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public List<Option> getSelectedOptions() { return selectedOptions; }

    public void setSelectedOptions(List<Option> selectedOptions) { this.selectedOptions = selectedOptions; }
}
