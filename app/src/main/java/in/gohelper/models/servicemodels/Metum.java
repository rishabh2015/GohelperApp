
package in.gohelper.models.servicemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metum {

    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("icon_title")
    @Expose
    private String iconTitle;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("meta_type")
    @Expose
    private String metaType;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconTitle() {
        return iconTitle;
    }

    public void setIconTitle(String iconTitle) {
        this.iconTitle = iconTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMetaType() {
        return metaType;
    }

    public void setMetaType(String metaType) {
        this.metaType = metaType;
    }

}
