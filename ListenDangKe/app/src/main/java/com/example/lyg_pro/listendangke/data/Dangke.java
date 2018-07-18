package com.example.lyg_pro.listendangke.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LYG-Pro on 2018/5/16.
 */

public class Dangke {

    /**
     * code : 200
     * message : 成功
     * data : [{"0":"洛阳镇2016年党建纪实2","PeriodName":"洛阳镇2016年党建纪实2","1":"/UploadFiles/update/luoyang2016.mp4","Url":"/UploadFiles/update/luoyang2016.mp4","2":"2018-05-16 15:49:24","adddate":"2018-05-16"},{"0":"洛阳镇2016年党建纪实1","PeriodName":"洛阳镇2016年党建纪实1","1":"/UploadFiles/update/luoyang2016.mp4","Url":"/UploadFiles/update/luoyang2016.mp4","2":"2018-05-16 15:48:25","adddate":"2018-05-16"}]
     */

    private String code;
    private String message;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * 0 : 洛阳镇2016年党建纪实2
         * PeriodName : 洛阳镇2016年党建纪实2
         * 1 : /UploadFiles/update/luoyang2016.mp4
         * Url : /UploadFiles/update/luoyang2016.mp4
         * 2 : 2018-05-16 15:49:24
         * adddate : 2018-05-16
         */

        @SerializedName("0")
        private String _$0;
        private String PeriodName;
        @SerializedName("1")
        private String _$1;
        private String Url;
        @SerializedName("2")
        private String _$2;
        private String adddate;

        public String get_$0() {
            return _$0;
        }

        public void set_$0(String _$0) {
            this._$0 = _$0;
        }

        public String getPeriodName() {
            return PeriodName;
        }

        public void setPeriodName(String PeriodName) {
            this.PeriodName = PeriodName;
        }

        public String get_$1() {
            return _$1;
        }

        public void set_$1(String _$1) {
            this._$1 = _$1;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String Url) {
            this.Url = Url;
        }

        public String get_$2() {
            return _$2;
        }

        public void set_$2(String _$2) {
            this._$2 = _$2;
        }

        public String getAdddate() {
            return adddate;
        }

        public void setAdddate(String adddate) {
            this.adddate = adddate;
        }
    }
}
