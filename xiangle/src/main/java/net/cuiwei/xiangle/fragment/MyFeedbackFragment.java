package net.cuiwei.xiangle.fragment;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.cuiwei.xiangle.R;
import net.cuiwei.xiangle.listener.OnDetailListener;
import net.cuiwei.xiangle.model.FeedbackModel;
import net.cuiwei.xiangle.model.TokenModel;

import java.util.HashMap;

public class MyFeedbackFragment extends Fragment implements OnDetailListener<HashMap<String, Object>> {
    private long user_id=0;
    Button sub_btn;
    EditText content;
    EditText contact;
    OnDetailListener<HashMap<String, Object>> t;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TokenModel cache=new TokenModel(getContext());
        user_id=cache.getUserId();
        View view=inflater.inflate(R.layout.fragment_my_feedback, container, false);
        getActivity().setTitle("意见反馈");
        content=view.findViewById(R.id.content);
        contact=view.findViewById(R.id.contact);
        sub_btn=view.findViewById(R.id.submit_btn);

        sub_btn.setEnabled(false);
        sub_btn.setBackgroundResource(R.color.orange2);

        t=this;

        content.addTextChangedListener(new TextWatcher());
        contact.addTextChangedListener(new TextWatcher());

        return view;
    }

    @Override
    public void onSuccess(HashMap<String, Object> field) {
        Toast.makeText(getContext(), "谢谢反馈！", Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    @Override
    public void onError() {
        Toast.makeText(getContext(), "err", Toast.LENGTH_SHORT).show();
    }
    class TextWatcher implements android.text.TextWatcher {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Log.e("textChange", "com in-beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.e("textChange", "com in-onTextChanged");
                String content1=content.getText().toString().trim();
                String contact1=contact.getText().toString().trim();
                if (!content1.equals("") || !contact1.equals("")) {
                    sub_btn.setEnabled(true);
                    sub_btn.setBackgroundResource(R.color.orange);
                    sub_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FeedbackModel feedbackModel = new FeedbackModel();
                            feedbackModel.create(user_id, content1, contact1, getContext(), t);
                        }
                    });
                }else {
                    sub_btn.setEnabled(false);
                    sub_btn.setBackgroundResource(R.color.orange2);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Log.e("textChange", "com in-afterTextChanged");
            }
    }
}