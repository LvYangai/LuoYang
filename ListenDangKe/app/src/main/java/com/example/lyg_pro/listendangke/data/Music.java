package com.example.lyg_pro.listendangke.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LYG-Pro on 2018/5/16.
 */

public class Music {

    /**
     * code : 200
     * message : 成功
     * data : [{"0":"ohbkb","PeriodName":"ohbkb","1":"/UploadFiles/update/几米 - 拥有 - Masbfca 广告配乐完整版 (1).mp3","Url":"/UploadFiles/update/几米 - 拥有 - Masbfca 广告配乐完整版 (1).mp3","2":"2018-05-10 15:47:25","adddate":"2018-05-10"},{"0":"poou yoybi","PeriodName":"poou yoybi","1":"/UploadFiles/update/music4.mp3","Url":"/UploadFiles/update/music4.mp3","2":"2018-05-10 15:47:04","adddate":"2018-05-10"},{"0":"ibyhuhnljk","PeriodName":"ibyhuhnljk","1":"/UploadFiles/update/music3.mp3","Url":"/UploadFiles/update/music3.mp3","2":"2018-05-10 15:46:42","adddate":"2018-05-10"},{"0":"iubykugjh","PeriodName":"iubykugjh","1":"/UploadFiles/update/music2.mp3","Url":"/UploadFiles/update/music2.mp3","2":"2018-05-10 15:46:07","adddate":"2018-05-10"},{"0":"agvadta","PeriodName":"agvadta","1":"/UploadFiles/update/music1.mp3","Url":"/UploadFiles/update/music1.mp3","2":"2018-05-10 15:45:14","adddate":"2018-05-10"}]
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
         * 0 : ohbkb
         * PeriodName : ohbkb
         * 1 : /UploadFiles/update/几米 - 拥有 - Masbfca 广告配乐完整版 (1).mp3
         * Url : /UploadFiles/update/几米 - 拥有 - Masbfca 广告配乐完整版 (1).mp3
         * 2 : 2018-05-10 15:47:25
         * adddate : 2018-05-10
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
