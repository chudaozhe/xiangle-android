package net.cuiwei.xiangle.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;


public class TokenModel {
//    private String token="token";//token名称
//    private String filename="user_token";

    SharedPreferences sp;
    public TokenModel(Context context){
        sp=context.getSharedPreferences("user_cookie", Context.MODE_PRIVATE);
    }

    /**
     * 设置
     * @param user_id
     * @param nickname
     * @param avatar
     * @param token
     * @return
     */
    public boolean setCache(long user_id, String nickname, String avatar, String token){
        SharedPreferences.Editor editor=sp.edit();
        editor.putLong("user_id", user_id);
        editor.putString("nickname", nickname);
        editor.putString("avatar", avatar);
        editor.putString("token", token);
        return editor.commit();
    }

    /**
     * 获取所有
     * @return
     */
    public Map<String, ?> getCache(){
        return sp.getAll();
    }

    /**
     * 删除token，好像没用
     * @return
     */
    public boolean delToken(){
        SharedPreferences.Editor editor=sp.edit();
        editor.remove("user_id");
        editor.remove("nickname");
        editor.remove("avatar");
        editor.remove("token");
        return editor.commit();
    }
    public long getUserId(){
       return sp.getLong("user_id", 0);
    }
    public String getAvatar(){
        return sp.getString("avatar", "");
    }
    public String getNickname(){
        return sp.getString("nickname", "");
    }
    public String getToken(){
        return sp.getString("token", "");
    }
}
