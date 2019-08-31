
package in.gohelper.models.testimonialsmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestimonialsModel {

    @SerializedName("data")
    @Expose
    private List<TestimonialsData> data = null;

    public List<TestimonialsData> getData() {
        return data;
    }

    public void setData(List<TestimonialsData> data) {
        this.data = data;
    }

}
