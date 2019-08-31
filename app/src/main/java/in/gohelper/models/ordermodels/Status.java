
package in.gohelper.models.ordermodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("status_label")
    @Expose
    private String statusLabel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

}
