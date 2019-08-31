
package in.gohelper.models.testimonialsmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestimonialsData {

    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("reviewer")
    @Expose
    private Reviewer reviewer;

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Reviewer getReviewer() {
        return reviewer;
    }

    public void setReviewer(Reviewer reviewer) {
        this.reviewer = reviewer;
    }

}
