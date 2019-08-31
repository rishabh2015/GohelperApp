package in.gohelper.models.cartmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import in.gohelper.models.SuccessModel;

    public class SuccessCartModel {

        @SerializedName("data")
        @Expose
        private SuccessModel data = null;

        public SuccessModel getData() {
            return data;
        }

        public void setData(SuccessModel data) {
            this.data = data;
        }

    }


