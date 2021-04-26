package net.cuiwei.xiangle;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import net.cuiwei.xiangle.fragment.MyFeedbackFragment;
import net.cuiwei.xiangle.model.FeedbackModel;
import net.cuiwei.xiangle.model.TokenModel;
import net.cuiwei.xiangle.model.UserModel;

import java.util.HashMap;

public class PersonInfoFieldActivity extends BaseActivity {
    private long user_id=0;
    private Integer type;
    private String content;
    EditText editText;
    Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TokenModel cache=new TokenModel(this);
        user_id=cache.getUserId();
        type=getIntent().getIntExtra("type", 0);
        content=getIntent().getStringExtra("content");


        String title="";
        if (type==1) title="设置昵称";
        if (type==2) title="设置个性签名";
        setTitle(title);

        editText=findViewById(R.id.content);
        if (!TextUtils.isEmpty(content)) editText.setText(content);
        editText.addTextChangedListener(new TextWatcher());
    }
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_person_info_field);
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
        return TransitionMode.LEFT;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.add(0, Menu.FIRST, Menu.FIRST, "完成").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);//always
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==1){
            Intent intent = new Intent();
            String content=editText.getText().toString().trim();
            intent.putExtra("content", content);
            setResult(RESULT_OK, intent);
            HashMap<String, String> map=new HashMap<String, String>();
            if (type==1) map.put("nickname", content);
            if (type==2) map.put("quotes", content);
            Log.e("map", map.toString());
            UserModel userModel=new UserModel();
            userModel.update(user_id, map, this);
            finish();
            //Toast.makeText(this, editText.getText(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    class TextWatcher implements android.text.TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //Log.e("textChange", "com in-beforeTextChanged");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Log.e("textChange", "com in-onTextChanged");
            String content=editText.getText().toString().trim();
            mMenu.setGroupEnabled(0, !content.equals(""));
        }

        @Override
        public void afterTextChanged(Editable s) {
            //Log.e("textChange", "com in-afterTextChanged");
        }
    }
}