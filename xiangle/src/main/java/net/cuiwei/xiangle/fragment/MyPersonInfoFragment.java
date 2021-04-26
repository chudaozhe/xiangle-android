package net.cuiwei.xiangle.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import net.cuiwei.xiangle.PersonInfoFieldActivity;
import net.cuiwei.xiangle.R;
import net.cuiwei.xiangle.adapter.MyPersonInfoAdapter;
import net.cuiwei.xiangle.bean.Division;
import net.cuiwei.xiangle.bean.User;
import net.cuiwei.xiangle.listener.OnDetailListener;
import net.cuiwei.xiangle.model.TokenModel;
import net.cuiwei.xiangle.model.UserModel;
import net.cuiwei.xiangle.service.DivisionService;
import net.cuiwei.xiangle.utility.Http;
import net.cuiwei.xiangle.view.dialog.BaseDialog;
import net.cuiwei.xiangle.view.dialog.CommonDialog;
import net.cuiwei.xiangle.view.dialog.ViewConvertListener;
import net.cuiwei.xiangle.view.dialog.ViewHolder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class MyPersonInfoFragment extends Fragment implements OnDetailListener<User> {
    private long user_id=0;
    public ImageView avatarView;
    public ListView listView;

    MyPersonInfoAdapter personInfoAdapter;
    private ArrayList<HashMap<String, String>> fields=new ArrayList<>();
    public User field;
    public ArrayList<Division> divisions;
    public String[] provinces;
    public String[] citys;
    public ArrayAdapter<String> cityAdapter;
    //gender，选中的
    public RadioButton radioButton;
    //birthday
    public DatePicker datePicker;
    public Spinner provinceSpinner;
    public Spinner citySpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("编辑个人资料");
        TokenModel cache=new TokenModel(getContext());
        user_id=cache.getUserId();
        if (user_id>0){
            UserModel model=new UserModel();
            model.get(user_id, getContext(),this);
        }
        View view= inflater.inflate(R.layout.fragment_my_person_info, container, false);
        avatarView=view.findViewById(R.id.avatar);
        listView=view.findViewById(R.id.listView);

        personInfoAdapter=new MyPersonInfoAdapter(fields, getContext());
        listView.setAdapter(personInfoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("index", position+"");
                if (position==0){
                    Intent intent = new Intent(getContext(), PersonInfoFieldActivity.class);
                    intent.putExtra("type", 1);//昵称
                    intent.putExtra("content", field.getNickname());
                    startActivityForResult(intent, 1);
                }else if (position==4){
                    Intent intent = new Intent(getContext(), PersonInfoFieldActivity.class);
                    intent.putExtra("type", 2);//签名
                    intent.putExtra("content", field.getQuotes());
                    startActivityForResult(intent, 2);
                }else if (position==1){//性别
                    CommonDialog.newInstance()
                            .setLayoutId(R.layout.dialog_person_info_gender)
                            .setConvertListener(new ViewConvertListener() {
                                @Override
                                public void convertView(ViewHolder holder, final BaseDialog dialog) {
                                    RadioGroup radioGroup=holder.getView(R.id.radioGroup);
                                    RadioButton maleRadio=holder.getView(R.id.male);
                                    RadioButton femaleRadio=holder.getView(R.id.female);

                                    if (field.getGender()==1) maleRadio.setChecked(true);
                                    if (field.getGender()==2) femaleRadio.setChecked(true);
                                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                                             RadioButton rb = (RadioButton)holder.getView(radioGroup.getCheckedRadioButtonId());
                                             Log.e("map", "选择了："+rb.getText());
                                        }
                                    });

                                    holder.getView(R.id.close).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    holder.getView(R.id.choose).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            //获取选择的radioButton
                                            radioButton = (RadioButton)holder.getView(radioGroup.getCheckedRadioButtonId());
                                            genderAction();
                                        }
                                    });
                                }
                            })
                            .setShowBottom(true)
                            .setSize(0, 160)
                            .show(getFragmentManager());
                }else if (position==2){//生日
                    CommonDialog.newInstance()
                            .setLayoutId(R.layout.dialog_person_info_birthday)
                            .setConvertListener(new ViewConvertListener() {
                                @Override
                                public void convertView(ViewHolder holder, final BaseDialog dialog) {
                                    datePicker=holder.getView(R.id.datePicker);
                                    holder.getView(R.id.close).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);//禁止编辑
                                    //默认值
                                    String birth=field.getBirthday();
                                    String[] births=birth.split("-");
                                    datePicker.init(Integer.parseInt(births[0]), Integer.parseInt(births[1])-1, Integer.parseInt(births[2]), new DatePicker.OnDateChangedListener() {
                                        @Override
                                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                            //Toast.makeText(getContext(), "year=" + year+" month=" + (monthOfYear+1)+" day=" + dayOfMonth, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    setDatePickerDividerColor(datePicker);//设置日期选择器的分割线颜色
                                    holder.getView(R.id.choose).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            birthdayAction();
                                        }
                                    });
                                }
                            })
                            .setShowBottom(true)
                            .setSize(0, 160)
                            .show(getFragmentManager());
                }else if (position==3){//所在地
                    CommonDialog.newInstance()
                            .setLayoutId(R.layout.dialog_person_info_city)
                            .setConvertListener(new ViewConvertListener() {
                                @Override
                                public void convertView(ViewHolder holder, final BaseDialog dialog) {
                                    provinceSpinner = holder.getView(R.id.province);
                                    citySpinner = holder.getView(R.id.city);

                                    provinceSpinner();
                                    citySpinner();
                                    //默认省份
                                    int index=getDivisionIndex(provinces, field.getProvince());
                                    //provinceSpinner.setSelected(true);
                                    provinceSpinner.setSelection(index, true);//会自动触发setOnItemSelectedListener，设置默认城市

                                    holder.getView(R.id.close).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    holder.getView(R.id.choose).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            divisionAction();
                                        }
                                    });
                                }
                            })
                            .setShowBottom(true)
                            .setSize(0, 160)
                            .show(getFragmentManager());
                }

            }
        });
        //行政区规划
        DivisionService service = Http.getInstance(getContext()).create(DivisionService.class);
        Call<ArrayList<Division>> call = service.list();
        call.enqueue(new Callback<ArrayList<Division>>() {
            @Override
            public void onResponse(Call<ArrayList<Division>> call, Response<ArrayList<Division>> response) {
                ArrayList<Division> result=response.body();
                //Log.e("divisions", result.toString());
                divisions=result;
            }

            @Override
            public void onFailure(Call<ArrayList<Division>> call, Throwable t) {
                Log.e("error", "com in");
            }
        });
        return view;
    }
    public void genderAction(){
        int gender=buildGender((String) radioButton.getText());
        field.setGender(gender);

        //更新到listView
        initData();

        //http
        HashMap<String, String> map=new HashMap<String, String>();
        map.put("gender", String.valueOf(gender));
        Log.e("map", map.toString());
        UserModel userModel=new UserModel();
        userModel.update(user_id, map, getContext());
    }
    public void divisionAction(){
        //取值
        Object province=provinceSpinner.getSelectedItem();
        Object city=citySpinner.getSelectedItem();

        //http
        HashMap<String, String> map=new HashMap<String, String>();
        if (province!=null){
            field.setProvince(province.toString());
            map.put("province", province.toString());
        }
        String city_str="";
        if (city!=null){
            city_str=city.toString();
        }
        field.setCity(city_str);
        map.put("city", city_str);
        Log.e("map", map.toString());
        UserModel userModel=new UserModel();
        userModel.update(user_id, map, getContext());

        //更新到listView
        initData();
    }
    public void birthdayAction(){
        String year=String.valueOf(datePicker.getYear());
        String month=String.valueOf(datePicker.getMonth());
        //前导零
        DecimalFormat df=new DecimalFormat("00");
        String day = df.format(datePicker.getDayOfMonth());

        String birthday=year+"-"+(Integer.parseInt(month)+1)+"-"+day;
        //获取选择的 年月日
        field.setBirthday(birthday);

        //更新到listView
        initData();

        //http
        HashMap<String, String> map=new HashMap<String, String>();
        map.put("birthday", birthday);
        Log.e("map", map.toString());
        UserModel userModel=new UserModel();
        userModel.update(user_id, map, getContext());
    }
    public String[] buildDivision(ArrayList<Division> list){
        String str2[] = new String[list.size()];
        for(int i=0; i<list.size(); i++){
            Division item=list.get(i);
            str2[i] = item.getName();
        }
        return str2;
    }
    public String[] buildDivisionChilden(ArrayList<Division.child> list){
        String str2[] = new String[list.size()];
            for(int i=0; i<list.size(); i++){
                Division.child item=(Division.child)list.get(i);
                str2[i] = (String) item.getName();
            }
        return str2;
    }
    public int buildGender(String text){
        Log.e("map-取到到", text);
        int gender=0;
        if (text.equals("男")){
            gender=1;
        }else if (text.equals("女")) gender=2;
        return gender;
    }
    /**
     * 设置日期选择器的分割线颜色
     *
     * @param datePicker
     */
    private void setDatePickerDividerColor(DatePicker datePicker){
        // Divider changing:

        // 获取 mSpinners
        LinearLayout llFirst       = (LinearLayout) datePicker.getChildAt(0);

        // 获取 NumberPicker
        LinearLayout mSpinners      = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        pf.set(picker, new ColorDrawable(0xffff8100));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
    public int getDivisionIndex(String[] list, String keyword){
        for (int i = 0; i < list.length; i++){
            if (list[i].equals(keyword)){
                return i;
            }
        }
        return 0;
    }
    public void provinceSpinner(){
        provinces=buildDivision(divisions);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, provinces);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceSpinner.setAdapter(adapter);
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("map-province", "position=" + position);
                cityAdapter.clear();
                ArrayList<Division.child> childens=divisions.get(position).getChildren();
                citys=buildDivisionChilden(childens);
                cityAdapter.addAll(citys);
                cityAdapter.notifyDataSetChanged();
                //city的默认选择
                int index2=getDivisionIndex(citys, field.getCity());
                citySpinner.setSelection(index2, true);
                //Log.e("map-citys", Arrays.toString(citys));
                //Log.e("map-city", field.getCity());
                //Log.e("map-index2", index2+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void citySpinner(){
        cityAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("map-city", "position=" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void initData(){
        ArrayList<HashMap<String, String>> data=new ArrayList<>();
        HashMap<String, String> d1=new HashMap<>();
        d1.put("label", "昵称");
        d1.put("value", field.getNickname());
        data.add(d1);
        HashMap<String, String> d2=new HashMap<>();
        d2.put("label", "性别");
        String genderText="";
        if (field.getGender()==1) genderText="男";
        if (field.getGender()==2) genderText="女";
        d2.put("value", genderText);
        data.add(d2);
        HashMap<String, String> d3=new HashMap<>();
        d3.put("label", "生日");
        d3.put("value", field.getBirthday());
        data.add(d3);
        HashMap<String, String> d4=new HashMap<>();
        d4.put("label", "所在地");
        d4.put("value", field.getProvince()+"-"+field.getCity());
        data.add(d4);
        HashMap<String, String> d5=new HashMap<>();
        d5.put("label", "个性签名");
        d5.put("value", field.getQuotes());
        data.add(d5);

        this.fields.clear();
        this.fields.addAll(data);
        personInfoAdapter.notifyDataSetChanged();
    }
    @Override
    public void onSuccess(User field) {
        this.field=field;
        initData();
        Glide.with(getContext()).load("https://cw-test.oss-cn-hangzhou.aliyuncs.com/"+field.getAvatar()+"?x-oss-process=image/circle,r_100/format,png").into(avatarView);
    }
    @Override
    public void onError() {
        Toast.makeText(getContext(), "出错来哦！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data!=null) {
            if (requestCode==1){
                String content=(String) data.getStringExtra("content");
                //Log.e("map", "收到昵称："+content);
                field.setNickname(content);
            }else if (requestCode==2){
                String content=(String) data.getStringExtra("content");
                field.setQuotes(content);
                //Log.e("map", "收到签名："+content);
            }
            initData();
        }
    }
}