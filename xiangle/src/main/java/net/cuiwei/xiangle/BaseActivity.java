package net.cuiwei.xiangle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BaseActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    protected TextView mToolBarTitleLabel;

    public enum TransitionMode{ LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE};
    protected abstract TransitionMode getOverridePendingTransitionMode();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getOverridePendingTransitionMode() != null) {

            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;

            }

        }
        super.onCreate(savedInstanceState);
        setContentView();
        initializeView();
        initializeData(savedInstanceState);
    }
    /**
     * 1. 设置布局
     */
    protected abstract void setContentView();

    /**
     * 2. 初始化布局
     */
    protected void initializeView() {
        if (findViewById(R.id.toolbar) != null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (hasBackIcon()) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            if (isCenter()) {
                mToolBarTitleLabel = (TextView) findViewById(R.id.mToolBarTitleLabel);
            }
        }
    }
    public Toolbar getToolbar(){
        return toolbar;
    }
    /**
     * 3. 初始化ui数据
     */
    protected abstract void initializeData(Bundle saveInstance);
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finishActivity();
        }
        return super.onOptionsItemSelected(item);
    }
    public void finishActivity() {
        finish();
    }
    /**
     * 是否有回退功能
     *
     * @return
     */
    protected boolean hasBackIcon() {
        return false;
    }
    protected boolean isCenter() {
        return false;
    }
    /**
     * 重写父类的setTitle方法根据当前标题显示是否居中做相应处理
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        if (mToolBarTitleLabel != null && isCenter()) {
            mToolBarTitleLabel.setText(title);
            super.setTitle("");
        } else {
            super.setTitle(title);
        }
    }

    public void setContentView(int layoutId) {
        setContentView(layoutId, true);
    }

    /**
     * 容器模版
     * @param layoutId 内容视图
     * @param isContainerTitle true 带有toolbar的布局容器 false无toolbar 你可以自定义标题实现复杂的title
     */
    protected void setContentView(int layoutId, boolean isContainerTitle) {
        if (isContainerTitle) {
            LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_content, null);
            LayoutInflater.from(this).inflate(layoutId, root);
            super.setContentView(root);
        } else {
            super.setContentView(layoutId);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (getOverridePendingTransitionMode()!=null) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.right_in,R.anim.right_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.left_in,R.anim.left_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.top_in,R.anim.top_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in_disappear,R.anim.scale_out_disappear);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in_disappear,R.anim.fade_out_disappear);
                    break;
            }
        }
    }
}
