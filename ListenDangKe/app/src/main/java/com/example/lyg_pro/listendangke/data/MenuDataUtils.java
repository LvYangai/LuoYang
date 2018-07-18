package com.example.lyg_pro.listendangke.data;



import com.example.lyg_pro.listendangke.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前类注释:左侧菜单Item数据构造
 * ProjectName：App36Kr
 * Author:<a href="http://www.cniao5.com">菜鸟窝</a>
 * Description：
 * 菜鸟窝是一个只专注做Android开发技能的在线学习平台，课程以实战项目为主，对课程与服务”吹毛求疵”般的要求，
 * 打造极致课程，是菜鸟窝不变的承诺
 */
public class MenuDataUtils {
    public static List<LeftItemMenu> getItemMenus(){
        List<LeftItemMenu> menus=new ArrayList<LeftItemMenu>();
        menus.add(new LeftItemMenu(R.mipmap.icon_zhanghaoxinxi,"修改密码"));
        menus.add(new LeftItemMenu(R.mipmap.icon_wodeguanzhu,"修改手机号"));
        menus.add(new LeftItemMenu(R.mipmap.icon_shoucang,"退出登录"));
        return  menus;
    }

}
