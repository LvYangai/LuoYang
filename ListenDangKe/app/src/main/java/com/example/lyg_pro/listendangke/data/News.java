package com.example.lyg_pro.listendangke.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LYG-Pro on 2018/5/8.
 */

public class News {

    /**
     * code : 200
     * message : 成功
     * data : [{"0":"4505","1":"1","2":"1290","3":"新时代，习近平要求外事工作这么干！","4":"/UploadFiles/2016-5/75/b11310672788555577490I9.jpg","5":"新时代，习近平要求外事工作这么干！","6":"2018-05-16 19:22:18","InfoID":"4505","ChannelID":"1","ClassID":"1290","Title":"新时代，习近平要求外事工作这么干！","DefaultPic":"/UploadFiles/2016-5/75/b11310672788555577490I9.jpg","Intro":"新时代，习近平要求外事工作这么干！","AddDate":"2018-05-16"},{"0":"4504","1":"1","2":"1290","3":"习近平主持召开中央外事工作委员会第一次会议","4":"/UploadFiles/75/2016-11/B131243541375984.jpg","5":"习近平主持召开中央外事工作委员会第一次会议强调\r\n加强党中央对外事工作的集中统一领导\r\n努力开创中国特色大国外交新局面\r\n李克强王沪宁韩正王岐山参加","6":"2018-05-16 19:03:12","InfoID":"4504","ChannelID":"1","ClassID":"1290","Title":"习近平主持召开中央外事工作委员会第一次会议","DefaultPic":"/UploadFiles/75/2016-11/B131243541375984.jpg","Intro":"习近平主持召开中央外事工作委员会第一次会议强调\r\n加强党中央对外事工作的集中统一领导\r\n努力开创中国特色大国外交新局面\r\n李克强王沪宁韩正王岐山参加","AddDate":"2018-05-16"}]
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
         * 0 : 4505
         * 1 : 1
         * 2 : 1290
         * 3 : 新时代，习近平要求外事工作这么干！
         * 4 : /UploadFiles/2016-5/75/b11310672788555577490I9.jpg
         * 5 : 新时代，习近平要求外事工作这么干！
         * 6 : 2018-05-16 19:22:18
         * InfoID : 4505
         * ChannelID : 1
         * ClassID : 1290
         * Title : 新时代，习近平要求外事工作这么干！
         * DefaultPic : /UploadFiles/2016-5/75/b11310672788555577490I9.jpg
         * Intro : 新时代，习近平要求外事工作这么干！
         * AddDate : 2018-05-16
         */

        @SerializedName("0")
        private String _$0;
        @SerializedName("1")
        private String _$1;
        @SerializedName("2")
        private String _$2;
        @SerializedName("3")
        private String _$3;
        @SerializedName("4")
        private String _$4;
        @SerializedName("5")
        private String _$5;
        @SerializedName("6")
        private String _$6;
        private String InfoID;
        private String ChannelID;
        private String ClassID;
        private String Title;
        private String DefaultPic;
        private String Intro;
        private String AddDate;

        public String get_$0() {
            return _$0;
        }

        public void set_$0(String _$0) {
            this._$0 = _$0;
        }

        public String get_$1() {
            return _$1;
        }

        public void set_$1(String _$1) {
            this._$1 = _$1;
        }

        public String get_$2() {
            return _$2;
        }

        public void set_$2(String _$2) {
            this._$2 = _$2;
        }

        public String get_$3() {
            return _$3;
        }

        public void set_$3(String _$3) {
            this._$3 = _$3;
        }

        public String get_$4() {
            return _$4;
        }

        public void set_$4(String _$4) {
            this._$4 = _$4;
        }

        public String get_$5() {
            return _$5;
        }

        public void set_$5(String _$5) {
            this._$5 = _$5;
        }

        public String get_$6() {
            return _$6;
        }

        public void set_$6(String _$6) {
            this._$6 = _$6;
        }

        public String getInfoID() {
            return InfoID;
        }

        public void setInfoID(String InfoID) {
            this.InfoID = InfoID;
        }

        public String getChannelID() {
            return ChannelID;
        }

        public void setChannelID(String ChannelID) {
            this.ChannelID = ChannelID;
        }

        public String getClassID() {
            return ClassID;
        }

        public void setClassID(String ClassID) {
            this.ClassID = ClassID;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getDefaultPic() {
            return DefaultPic;
        }

        public void setDefaultPic(String DefaultPic) {
            this.DefaultPic = DefaultPic;
        }

        public String getIntro() {
            return Intro;
        }

        public void setIntro(String Intro) {
            this.Intro = Intro;
        }

        public String getAddDate() {
            return AddDate;
        }

        public void setAddDate(String AddDate) {
            this.AddDate = AddDate;
        }
    }
}
