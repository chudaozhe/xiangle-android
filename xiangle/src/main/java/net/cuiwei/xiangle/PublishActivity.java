package net.cuiwei.xiangle;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.Nullable;
import android.os.Bundle;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bumptech.glide.Glide;
import net.cuiwei.xiangle.bean.Publish;
import net.cuiwei.xiangle.bean.Topic;
import net.cuiwei.xiangle.listener.OnDetailListener;
import net.cuiwei.xiangle.model.JokeModel;
import net.cuiwei.xiangle.model.TokenModel;
import net.cuiwei.xiangle.utility.DisplayUtil;
import net.cuiwei.xiangle.utility.OssServiceUtil;
import net.cuiwei.xiangle.view.Grid9Layout;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PublishActivity extends BaseActivity implements View.OnClickListener, OnDetailListener<HashMap<String, Object>>, OSSCompletedCallback<PutObjectRequest, PutObjectResult> {
    private long user_id=0;
    private String tag;
    private String[] imgs={};//{"flash/1.jpg", "joke/2020-05-24/1590328017.jpg", "joke/2020-5-22/1590105814032x.jpg", "joke/2020-5-22/1590103879301.jpg"}
    private ArrayList<String> images=new ArrayList<String>();
    private Grid9Layout grid9Layout;
    private Publish publish=new Publish();
    //文本内容
    private EditText contentView;
    private Menu mMenu;
    private TextView topicNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TokenModel cache=new TokenModel(this);
        user_id=cache.getUserId();
        tag=getIntent().getAction();

        grid9Layout=findViewById(R.id.images);
        //如果是纯文本，隐藏上传图片按钮
        if (tag.equals("text")) grid9Layout.setVisibility(View.GONE);
        grid9Layout.setGridAdapter(new Grid9Layout.GridAdatper() {
            @Override
            public View getView(int index) {
                View view = getLayoutInflater().inflate(R.layout.publish_grid_item, null);
                ImageView imageView = view.findViewById(R.id.iv);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                //imageView.setBackgroundResource(R.mipmap.ic_launcher);//srcs[index]
                Glide.with(PublishActivity.this).load("https://cw-test.oss-cn-hangzhou.aliyuncs.com/"+imgs[index]).into(imageView);
                return view;
            }
            @Override
            public int getCount() {
                return imgs.length;
            }
        });
        grid9Layout.setOnItemClickListener(new Grid9Layout.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int index) {
                System.out.println("item="+index);
                //Toast.makeText(getContext(), "item="+index, 0).show();
            }
        });
        //Grid9Layout创建成功后 设置
        findViewById(R.id.choose).setOnClickListener(this);
        contentView=findViewById(R.id.content);
        contentView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                publish.setContent(contentView.getText().toString());
                if (!TextUtils.isEmpty(publish.getContent()) || publish.getImages() != null || publish.getVideo() != null) {
                    mMenu.setGroupEnabled(1, true);//可用
                } else {
                    mMenu.setGroupEnabled(1, false);//禁用
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //选择话题
        LinearLayout topicView=findViewById(R.id.topic);
        ((RelativeLayout.LayoutParams) topicView.getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.content);//.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);//沉于父容器底部
        if (!tag.equals("text")) {
            ((RelativeLayout.LayoutParams) topicView.getLayoutParams()).removeRule(RelativeLayout.BELOW);
            ((RelativeLayout.LayoutParams) topicView.getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.images);
        }
        topicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PublishActivity.this, ChooseTopicActivity.class);
                startActivityForResult(intent, 3);
            }
        });
        topicNameView=findViewById(R.id.topic_name);
    }
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_publish);
    }

    @Override
    protected void initializeData(Bundle saveInstance) {
        setTitle("发布");
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.choose://拉起相册
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                Uri originalUri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                String type="image/*";
                if (tag.equals("video")) {
                    originalUri=MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    type="video/*";
                }
                intent.setDataAndType(originalUri, type);
                startActivityForResult(intent, 2);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode", String.valueOf(requestCode));
        Log.e("resultCode", String.valueOf(resultCode));

        // Log.e("data", data.getDataString());
        //if (resultCode == RESULT_OK) {}
        if (requestCode == 2) {
            // 从相册返回的数据
            if (data != null) {
                // path = data.getStringArrayListExtra("");
                //content://com.google.android.apps.photos.contentprovider/-1/2/content://media/external/video/media/36/ORIGINAL/NONE/600240573
                //content://com.google.android.apps.photos.contentprovider/-1/1/content://media/external/images/media/24/ORIGINAL/NONE/2011217170
                //真机
                //content://com.google.android.apps.photos.contentprovider/1/2/content://media/external/video/media/751958/ORIGINAL/NONE/video/mp4/708595299
                Uri uri = data.getData();
                Log.e("uri", uri.toString());
                //uploadOSS
                //真机：/storage/emulated/0/DCIM/Camera/cb41e60de96b2862873d96ac5c031563.mp4
                ///storage/emulated/0/Download/1-2.jpg
                String realPath=getRealPathFromURI(uri);
                Log.e("RealPath", realPath);
                long timestamp=System.currentTimeMillis() / 1000;
                Date date = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

                String suffix=".jpg";
                if (tag.equals("video")) suffix=".mp4";
                String objectName="joke/"+ft.format(date)+"/"+timestamp+suffix;
                String objectName2="joke/"+ft.format(date)+"/"+timestamp+"_thumbnail.jpg";
                OssServiceUtil.putObject(this, objectName, realPath, this);
                //imageView.setImageURI(uri);

                if (!tag.equals("video")){
                    images.add(objectName);
                    publish.setType(2);
                    publish.setImages(images);
                }else {
                    Bitmap bitmap=getVideoThumbnail(realPath);
                    byte[] bytes=bitmap2byte(bitmap);
                    OssServiceUtil.putObject(this, objectName2, bytes);

                    publish.setType(3);
                    publish.setVideo(objectName);
                    publish.setImage(objectName2);
                    objectName=objectName2;
                }
                mMenu.setGroupEnabled(1, true);//可用

                //添加
                ImageView imageView=new ImageView(this);
                Glide.with(this).load("https://cw-test.oss-cn-hangzhou.aliyuncs.com/"+objectName).into(imageView);
                //imageView.setImageBitmap(bitmap);
                int count=grid9Layout.getChildCount();
                grid9Layout.addView(imageView, count-1);
                //超过9张隐藏 选择按钮
                int max=9;
                if (tag.equals("video")) max=1;
                if (count>=max) grid9Layout.removeViewAt(count);

                //动态改变grid9Layout的高度
                RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams) grid9Layout.getLayoutParams();
                params.width=RelativeLayout.LayoutParams.MATCH_PARENT;
                int rows= (int) Math.ceil((float)grid9Layout.getChildCount()/3);//行数, 向上取整(注：其中一个转为浮点数
                int gridH = (DisplayUtil.getScreenWidth(this) - 10 * (3 - 1)) / 3;// 格子宽度, 20:左右两边各10dp
                params.height=rows*gridH+(rows-1)*10;//单位px,非dp
                grid9Layout.setLayoutParams(params);//动态改变grid9Layout的高度
            }
        }else if(requestCode==3){
            if (data!=null && data.getExtras()!=null) {
                Topic topic = (Topic) data.getExtras().get("topic");//话题内容
                if (topic!=null){
                    topicNameView.setText(topic.getName());
                    publish.setTopic_id(topic.getId());
                    Log.e("received", topic.toString());
                }
            }
        }
    }

    /**
     * 获取绝对路径
     * @param contentUri
     * @return
     */
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String column = MediaStore.Images.Media.DATA;
        if (tag.equals("video")) column=MediaStore.Video.Media.DATA;
        String[] proj = { column };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(column);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    /**
     * 视频缩略图
     * @param filePath
     * @return
     */
    public Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        }catch(IllegalArgumentException e) {
            e.printStackTrace();
        }catch (RuntimeException e) {
            e.printStackTrace();
        }
        finally {
            try {
                retriever.release();
            }catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * bitmap->byte[]
     * @param bitmap
     * @return
     */
    public byte[] bitmap2byte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
    @Override
    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
        Log.e("oss", request.getObjectKey());
        Log.e("oss", request.getUploadFilePath());
    }

    @Override
    public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
        if (clientException != null) {
            Log.e("oss", clientException.getMessage());
        }
        if (serviceException != null) {
            Log.e("oss", serviceException.getRawMessage());
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==1){
            if(user_id==0){
                Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            }else {
                doPublish();
            }
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //toolbar 右侧'发布'按钮
        menu.add(1, Menu.FIRST, Menu.FIRST, "提交").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);//always
        menu.setGroupVisible(1, true);
        menu.setGroupEnabled(1, false);//默认禁用
        mMenu=menu;
        return super.onCreateOptionsMenu(menu);
    }
    public void doPublish(){
        //发表
        int type=publish.getType();
        ArrayList<String> images=publish.getImages();
        String content=publish.getContent();
        String image=publish.getImage();
        String video=publish.getVideo();
        JokeModel model= new JokeModel();
        //,type,topic_id,content,image,images,video,
        HashMap<String, String> map=new HashMap<String, String>();
        if (type==0){
            type=1;
        }
        map.put("type", String.valueOf(type));
        if (publish.getTopic_id()>0) map.put("topic_id", String.valueOf(publish.getTopic_id()));
        if (!TextUtils.isEmpty(content)) map.put("content", content);
        if (!TextUtils.isEmpty(video)){
            map.put("image", image);
            map.put("video", video);
        }
        if (images!=null) map.put("images", StringUtils.join(images, ","));
        Log.e("value-map", map.toString());
        model.create(user_id, map, this, this);
    }
    /**
     * 点击空白区域隐藏键盘
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("onTouchEvent", event.getAction()+"");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (this.getCurrentFocus() != null) {
                if (this.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.onTouchEvent(event);
    }
    @Override
    public void onSuccess(HashMap<String, Object> field) {
        Toast.makeText(this, "发表成功", Toast.LENGTH_SHORT).show();
        finish();
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onError() {
        Toast.makeText(this, "服务器错误！", Toast.LENGTH_SHORT).show();
    }
}