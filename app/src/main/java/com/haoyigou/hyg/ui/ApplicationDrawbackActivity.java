package com.haoyigou.hyg.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.ImageAddGridViewAdapter;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.APPReFoudEntity;
import com.haoyigou.hyg.utils.AppManager;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.view.widget.NiceSpinner;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 申请退款
 */

public class ApplicationDrawbackActivity extends BaseActivity {
    /**
     * 取消选择图片类型按钮
     */
    private TextView cancel;
    /**
     * 拍照按钮
     */
    private TextView take_picture;
    /**
     * 选区本地图片
     */
    private TextView local_picture;
    /**
     * 显示单个图片删除按钮
     */
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 2;
    private ImageView delete_image;
    private RelativeLayout edit_photo_fullscreen_layout, edit_photo_outer_layout, display_big_image_layout;
    private Animation get_photo_layout_out_from_up, get_photo_layout_in_from_down;
    private Intent intent;
    private final int NONE = 0, TAKE_PICTURE = 1, LOCAL_PICTURE = 2;
    private final int SHOW_TAKE_PICTURE = 9;
    private final int SHOW_LOCAL_PICTURE = 10;
    private int addTakePicCount = 1, viewpagerPosition;
    /**
     * 上传本地图片url
     */
    private List<String> uploadImgUrlList;
    private ImageAddGridViewAdapter imageAddGridViewAdapter;
    private GridView add_image_gridview;

    /**
     * 拍摄图片保存地址
     */
    private String takePictureUrl;
    File sdcardDir = Environment.getExternalStorageDirectory();

    //点击添加图片
    private ImageView add_image;
    private Button upload_button;
    private EditText et_message;
    private NiceSpinner niceSpinner;
    private String url;
    private TextView drawbacknum;
    private TextView drawbackname;
    private TextView drawbackmoney;
    private ImageView drawbackpiclogo;
    private TextView tvHearder;
    String ordernum;    //需要查询退款的订单
    String source;
    private String images = "";
    private ImageView setting_backto;
    private Button cancle_uploadbutton;

    //申请退款理由
    ArrayList<String> dataset = new ArrayList<>(Arrays.asList("无理由退款", "多拍/错拍/不想要", "物流问题", "未按规定时间发货", "商品描述不符", "质量问题", "其他"));

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_back_shop);
        /**
         * 得到后台传来的订单信息需要截取一下
         */
        Intent intent = getIntent();
        boolean isWeb = intent.getBooleanExtra("isweb", true);
        if (isWeb) {
            url = intent.getStringExtra("url");
//            Log.e("URL", url);
            cutMessage();
        } else {
            ordernum = intent.getStringExtra("orderNum");
            source = "1";
        }
        uploadImgUrlList = new ArrayList<>();
//        Log.e("URL", ordernum + "   " + source);
        initView();
        loadreason();
        initEvent();
    }

    /***
     * 截取JS中传回的数据
     */
    private void cutMessage() {
        if (url != null && !url.equals("")) {
            String parameter = url.substring(url.indexOf("?"), url.length());
//            Log.e("parameter", parameter);
            String[] parameters = parameter.split("&");
            ordernum = parameters[0].substring(parameters[0].indexOf("=") + 1, parameters[0].length());
            String aa = parameters[1].substring(parameters[1].indexOf("+"), parameters[1].length());
            source = aa.substring(aa.indexOf("=") + 1, aa.length());
        }
    }


    /**
     * 加载理由 或得传来的订单信息
     */

    private void loadreason() {
        APPReFoudEntity param = new APPReFoudEntity();
        param.setDistributorId(SharedPreferencesUtils.getInstance().getString("distributorId", null));
        param.setOrdernum(ordernum);
        HttpClient.apprefundresaon(param, new AsyncHttpResponseHandler() {
            public void onSuccess(String body) {
//                Log.e("getReason", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                String piclogo = object.getString("piclogo");
                String name = object.getString("name");
                String totalprice = object.getString("totalprice");
                String memo = object.getString("memo");
                String productnum = object.getString("productnum");
                et_message.setText(memo);
                Picasso.with(ApplicationDrawbackActivity.this).load(piclogo).into(drawbackpiclogo);
                drawbackmoney.setText(totalprice);
                drawbackname.setText(name);
                drawbacknum.setText("共" + productnum + "件商品");
            }

            public void onFailure(Request request, IOException e) {
            }
        }, this);
    }

    /**
     * 初始化
     */
    private void initView() {
        drawbackpiclogo = (ImageView) findViewById(R.id.drawback_pic);
        drawbackname = (TextView) findViewById(R.id.drawback_name);
        drawbackmoney = (TextView) findViewById(R.id.drawback_money);
        drawbacknum = (TextView) findViewById(R.id.drawback_ordernum);
        niceSpinner = (NiceSpinner) findViewById(R.id.drawback_spinner);
        niceSpinner.attachDataSource(dataset);
        et_message = (EditText) findViewById(R.id.et_message);
        et_message.setText(niceSpinner.getText().toString());
        upload_button = (Button) findViewById(R.id.upload_button);
        add_image = (ImageView) findViewById(R.id.add_image);
        cancel = (TextView) findViewById(R.id.cancel);
        take_picture = (TextView) findViewById(R.id.take_picture);
        local_picture = (TextView) findViewById(R.id.local_picture);
        edit_photo_fullscreen_layout = (RelativeLayout) findViewById(R.id.edit_photo_fullscreen_layout);
        edit_photo_outer_layout = (RelativeLayout) findViewById(R.id.edit_photo_outer_layout);
        display_big_image_layout = (RelativeLayout) findViewById(R.id.display_big_image_layout);
        add_image_gridview = (GridView) findViewById(R.id.add_image_gridview);
        delete_image = (ImageView) findViewById(R.id.delete_image);
        imageAddGridViewAdapter = new ImageAddGridViewAdapter(this, uploadImgUrlList);
        add_image_gridview.setAdapter(imageAddGridViewAdapter);
        setting_backto = (ImageView) findViewById(R.id.setting_backto);
        cancle_uploadbutton = (Button) findViewById(R.id.cancle_uploadbutton);
        tvHearder = (TextView) findViewById(R.id.tvHearder);
        tvHearder.setText("退款");
    }


    private void initEvent() {
        /**
         * 下拉列表的选中事件
         */
        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                et_message.setText(dataset.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        /**
         * 返回后退点击事件
         */
        setting_backto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //取消按钮
        cancle_uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /**
         * 点击添加图片，弹出选择图片类型对话框
         */
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(ApplicationDrawbackActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    //申请WRITE_EXTERNAL_STORAGE权限
//                    ActivityCompat.requestPermissions(ApplicationDrawbackActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
//                            1);
//                }
                if (ContextCompat.checkSelfPermission(ApplicationDrawbackActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions((Activity) ApplicationDrawbackActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                }
                if (uploadImgUrlList.size() >= 3) {
                    Toast.makeText(ApplicationDrawbackActivity.this, "上传图片不能超过3张", Toast.LENGTH_SHORT).show();
                    return;
                }
                edit_photo_fullscreen_layout.setVisibility(View.VISIBLE);
                get_photo_layout_in_from_down = AnimationUtils.loadAnimation(
                        ApplicationDrawbackActivity.this, R.anim.layout_in_from_down);
                edit_photo_outer_layout.startAnimation(get_photo_layout_in_from_down);
            }
        });

        /**
         * 点击取消，选择图片类型对话框
         */
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenSelectImageDialog();
            }
        });

        /**
         * 隐藏选择图片类型对话框
         */
        edit_photo_fullscreen_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenSelectImageDialog();
            }
        });
        /**
         * 点击拍照按钮
         */
        take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "点击上传图片take_picture");
                //看是否需要申请权限
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
                File destDir = new File(sdcardDir.getPath() + "/haoyigou/cache/photoes/");
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
                takePictureUrl = sdcardDir.getPath() + "/haoyigou/cache/photoes/" + "take_pic" + addTakePicCount + ".png";
                File file = new File(takePictureUrl);
                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0及以上com.haoyigou.hyg.fileProvider",  file);
                        Uri uriForFile = FileProvider.getUriForFile(ApplicationDrawbackActivity.this, "com.haoyigou.hyg.fileProvider", file);
                        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
                        intentFromCapture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intentFromCapture.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    } else {
                        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    }

                startActivityForResult(intentFromCapture, TAKE_PICTURE);


//                File destDir = new File(sdcardDir.getPath() + "/haoyigou/cache/photoes/");
//                if (!destDir.exists()) {
//                    destDir.mkdirs();
//                }
//                takePictureUrl = sdcardDir.getPath() + "/haoyigou/cache/photoes/" + "take_pic" + addTakePicCount + ".png";
//
//                File file = new File(takePictureUrl);
//                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//                startActivityForResult(intent, TAKE_PICTURE);
                addTakePicCount++;
            }
        });
        /**
         * 点击选择本地图片
         */
        local_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
                intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, LOCAL_PICTURE);
            }
        });

        /**
         * 点击提交
         */
        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(et_message.getText().toString())) {
                    Toast.makeText(ApplicationDrawbackActivity.this, "请填写问题反馈", Toast.LENGTH_LONG).show();
                    return;
                }
                if (uploadImgUrlList.size() > 3) {
                    return;
                }
                if (uploadImgUrlList != null && uploadImgUrlList.size() > 0) {
                    startProgressDialog("", ApplicationDrawbackActivity.this);
                    for (int i = 0; i < uploadImgUrlList.size(); i++) {
//                        Log.e("imgepath---->", uploadImgUrlList.get(i).toString());
                        uploadImage(uploadImgUrlList.get(i).toString());
                    }
                } else {
                    savereason(images);
                }

            }
        });
        /**
         * 点击查看大图
         */
        add_image_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击图片查看大图
//                showImageViewPager(position,
//                        uploadImgUrlList, "local", "upload");
//                viewpagerPosition = position - 1;
            }
        });
        imageAddGridViewAdapter.setOnDeleteListener(new ImageAddGridViewAdapter.onDeleteOnClice() {
            @Override
            public void onDelele(int position) {
                if (uploadImgUrlList.size() == 0) {
                    return;
                }
                uploadImgUrlList.remove(viewpagerPosition);
                imageAddGridViewAdapter.changeList(uploadImgUrlList);
                imageAddGridViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void hiddenSelectImageDialog() {
        get_photo_layout_out_from_up = AnimationUtils.loadAnimation(
                ApplicationDrawbackActivity.this, R.anim.layout_out_from_up);
        edit_photo_outer_layout.startAnimation(get_photo_layout_out_from_up);
        get_photo_layout_out_from_up.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {

                edit_photo_fullscreen_layout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationStart(Animation arg0) {

            }
        });
    }

    // @Override
    public void onDismissBigPhoto() {
        display_big_image_layout.setVisibility(View.GONE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (ContextCompat.checkSelfPermission(ApplicationDrawbackActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions((Activity) ApplicationDrawbackActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
        super.onActivityResult(requestCode, resultCode, intent);
//        Log.e("TAG", "onActivityResult" + requestCode + ":" + resultCode);


        if (resultCode == NONE)
            return;
        //为什么不在这处理图片呢？因为处理图片比较耗时，如果在这里处理图片，从图库或者拍照Activity时会不流畅，很卡卡卡，试试就知道了
        if (requestCode == TAKE_PICTURE) {
            handler.sendEmptyMessage(SHOW_TAKE_PICTURE);
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            this.intent = intent;
            handler.sendEmptyMessage(SHOW_LOCAL_PICTURE);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_TAKE_PICTURE:
                    Log.e("TAG", "SHOW_TAKE_PICTURE");
                    uploadImgUrlList.add(takePictureUrl);
                    imageAddGridViewAdapter.changeList(uploadImgUrlList);
                    imageAddGridViewAdapter.notifyDataSetChanged();
                    break;
                case SHOW_LOCAL_PICTURE:
                    Uri uri = intent.getData();
                    String[] pojo = {MediaStore.Images.Media.DATA};
                    CursorLoader cursorLoader = new CursorLoader(ApplicationDrawbackActivity.this, uri, pojo, null, null, null);
                    Cursor cursor = cursorLoader.loadInBackground();
                    cursor.moveToFirst();
                    String photo_local_file_path = cursor.getString(cursor.getColumnIndex(pojo[0]));
                    uploadImgUrlList.add(photo_local_file_path);
                    imageAddGridViewAdapter.changeList(uploadImgUrlList);
                    imageAddGridViewAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    private int count = 0;  // 负责记录上传的图片数量

    private void uploadImage(String localPath) {
        APPReFoudEntity param = new APPReFoudEntity();
        param.setLocalPath(localPath);
        HttpClient.uploadImage(param, new AsyncHttpResponseHandler() {
            public void onSuccess(String body) {
//                Log.e("getImage", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                int status = object.getInteger("status");
                if (status == 1) {
                    images += object.getString("nowpic") + ",";
                    count++;
                    if (count == uploadImgUrlList.size()) {
                        if (!StringUtils.isEmpty(images.toString())) {
                            images = images.substring(0, images.length() - 1);
                        }
                        savereason(images);
                    }
                } else {
                    showToast("图片上传失败！");
                }
            }

            public void onFailure(Request request, IOException e) {
            }
        }, ApplicationDrawbackActivity.this);
    }

    private void savereason(String images) {
//        Log.e("img", images);
        APPReFoudEntity param = new APPReFoudEntity();
        param.setOrdernum(ordernum);
        param.setMemo(et_message.getText().toString());
        param.setPicurl(images);
        HttpClient.savedrawbackreson(param, new AsyncHttpResponseHandler() {
            public void onSuccess(String body) {
                stopProgressDialog();
//                Log.e("getReason1", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                Toast.makeText(ApplicationDrawbackActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(ApplicationDrawbackActivity.this, PersonWebViewAct.class);
//                String disid = SharedPreferencesUtils.getInstance().getString("distributorId", "");
//                intent.putExtra("url", HttpClient.ALLORDERLINK + "?distributorId=" + disid);
//                intent.putExtra("souce", source);
//                startActivity(intent);
                AppManager.getAppManager().finishActivity(OrderMessageAct.getInstance());
                finish();
            }

            public void onFailure(Request request, IOException e) {
                Toast.makeText(ApplicationDrawbackActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        }, ApplicationDrawbackActivity.this);
    }
}
