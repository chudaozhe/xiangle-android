package net.cuiwei.xiangle;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import net.cuiwei.xiangle.fragment.*;

public class SettingActivity extends BaseActivity{
    public FragmentManager mManager;
    String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fragment管理器
        mManager=getSupportFragmentManager();
        //初始化fragment
        Intent intent=getIntent();
        action=intent.getAction();
        if (action==null) return;
        switch (action){
            case "my_favorite":
                mManager.beginTransaction().replace(R.id.container,new MyFavoriteFragment()).commit();//.addToBackStack(null)
                break;
            case "my_comment":
                mManager.beginTransaction().replace(R.id.container,new MyCommentFragment()).commit();
                break;
            case "my_like":
                mManager.beginTransaction().replace(R.id.container,new MyLikeFragment()).commit();
                break;
            case "my_setting":
                mManager.beginTransaction().replace(R.id.container,new MySettingFragment()).commit();
                break;
            case "my_feedback":
                mManager.beginTransaction().replace(R.id.container,new MyFeedbackFragment()).commit();
                break;
            case "discover_topic":
                mManager.beginTransaction().replace(R.id.container, new TopicFragment()).commit();
                break;
            case "my_login":
                mManager.beginTransaction().replace(R.id.container, new LoginFragment()).commit();
                break;
            case "my_register":
                mManager.beginTransaction().replace(R.id.container, new RegisterFragment()).commit();
                break;
            case "search":
                mManager.beginTransaction().replace(R.id.container, new SearchFragment()).commit();
                break;
            case "author":
                long author_id=intent.getLongExtra("author_id", 0);
                mManager.beginTransaction().replace(R.id.container, AuthorFragment.newInstance(author_id)).commit();
                break;
            case "my_follow":
                mManager.beginTransaction().replace(R.id.container, new MyFollowFragment()).commit();
                break;
            case "my_fans":
                mManager.beginTransaction().replace(R.id.container, new MyFansFragment()).commit();
                break;
            case "my_recommend":
                mManager.beginTransaction().replace(R.id.container, new MyRecommendFragment()).commit();
                break;
            case "my_person_info":
                mManager.beginTransaction().replace(R.id.container, new MyPersonInfoFragment()).commit();
                break;
        }
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void initializeData(Bundle saveInstance) {
        setTitle("...");
    }
    @Override
    protected boolean isCenter() {
        return false;
    }
    @Override
    protected boolean hasBackIcon() {
        return true;
    }
    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        if (action!=null && action.equals("my_login")) return TransitionMode.SCALE;
        return TransitionMode.LEFT;
    }
}