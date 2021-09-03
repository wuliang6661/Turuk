package com.haoyigou.hyg.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.BoolRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.DragListener;
import com.haoyigou.hyg.adapter.FullyGridLayoutManager;
import com.haoyigou.hyg.adapter.GridImageAdapter;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.OnDeleteListener;
import com.haoyigou.hyg.entity.SavePersonalInfomationEntity;
import com.haoyigou.hyg.entity.SendCircleBean;
import com.haoyigou.hyg.utils.GlideCacheEngine;
import com.haoyigou.hyg.utils.GlideEngine;
import com.haoyigou.hyg.utils.MediaUtils;
import com.haoyigou.hyg.utils.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.broadcast.BroadcastAction;
import com.luck.picture.lib.broadcast.BroadcastManager;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnPictureSelectorInterfaceListener;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.listener.OnVideoSelectedPlayCallback;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.ScreenUtils;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Witness on 2020-04-18
 * Describe: 发布社区动态
 */
public class SendCircleActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private static final int PRC_PHOTO_PICKER = 1;


    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.send)
    LinearLayout send;
    @BindView(R.id.rlTitleBg)
    RelativeLayout rlTitleBg;
    @BindView(R.id.inputEdit)
    EditText inputEdit;
    @BindView(R.id.picList)
    RecyclerView mRecyclerView;
    @BindView(R.id.addProduct)
    RelativeLayout addProduct;
    @BindView(R.id.productName)
    TextView productName;

    private GridImageAdapter mAdapter;
    private int themeId;
    private int chooseMode = PictureMimeType.ofAll();
    private int language = -1;
    private PictureParameterStyle mPictureParameterStyle;
    private PictureCropParameterStyle mCropParameterStyle;
    private PictureWindowAnimationStyle mWindowAnimationStyle;
    private ItemTouchHelper mItemTouchHelper;
    private DragListener mDragListener;
    private String productId = "";//关联商品id
    private String sendType = "1";//圈子类型  1 推荐  2  买家秀


    private boolean canClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            // 被回收
        } else {
            clearCache();
        }
        setContentView(R.layout.send_circle_layout);
        ButterKnife.bind(this);
        initView(savedInstanceState);

    }

    private void initView(Bundle savedInstanceState) {
        //修改字体颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        sendType = getIntent().getStringExtra("sendType");

        themeId = R.style.picture_default_style;
        getWhiteStyle();
        mWindowAnimationStyle = new PictureWindowAnimationStyle();
        mWindowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out);


        FullyGridLayoutManager manager = new FullyGridLayoutManager(this,
                3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);

        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3,
                ScreenUtils.dip2px(this, 8), false));
        mAdapter = new GridImageAdapter(getContext(), onAddPicClickListener);
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("selectorList") != null) {
            mAdapter.setList(savedInstanceState.getParcelableArrayList("selectorList"));
        }
        mAdapter.setSelectMax(9);
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener((v, position) -> {
            List<LocalMedia> selectList = mAdapter.getData();
            if (selectList.size() > 0) {
                LocalMedia media = selectList.get(position);
                String mimeType = media.getMimeType();
                int mediaType = PictureMimeType.getMimeType(mimeType);
                switch (mediaType) {
                    case PictureConfig.TYPE_VIDEO:
                        // 预览视频
                        PictureSelector.create(SendCircleActivity.this)
                                .themeStyle(R.style.picture_default_style)
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                .externalPictureVideo(TextUtils.isEmpty(media.getAndroidQToPath()) ? media.getPath() : media.getAndroidQToPath());
                        break;
                    case PictureConfig.TYPE_AUDIO:
                        // 预览音频
                        PictureSelector.create(SendCircleActivity.this)
                                .externalPictureAudio(PictureMimeType.isContent(media.getPath()) ? media.getAndroidQToPath() : media.getPath());
                        break;
                    default:
                        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE};
                        if (EasyPermissions.hasPermissions(this, perms)) {
                            PictureSelector.create(SendCircleActivity.this)
                                    .themeStyle(R.style.picture_default_style) // xml设置主题
                                    .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
//                                .setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
                                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)// 设置相册Activity方向，不设置默认使用系统
                                    .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                                    //.bindCustomPlayVideoCallback(callback)// 自定义播放回调控制，用户可以使用自己的视频播放界面
                                    .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                                    .openExternalPreview(position, selectList);
                        } else {
                            EasyPermissions.requestPermissions(this, "选择上传图片需要获取以下权限:\n\n1.访问设备上的照片\n\n2.拍照", PRC_PHOTO_PICKER, perms);
                        }

                        break;
                }
            }
        });

        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int itemViewType = viewHolder.getItemViewType();
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    viewHolder.itemView.setAlpha(0.7f);
                }
                return makeMovementFlags(ItemTouchHelper.DOWN | ItemTouchHelper.UP
                        | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //得到item原来的position
                try {
                    int fromPosition = viewHolder.getAdapterPosition();
                    //得到目标position
                    int toPosition = target.getAdapterPosition();
                    int itemViewType = target.getItemViewType();
                    if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                        if (fromPosition < toPosition) {
                            for (int i = fromPosition; i < toPosition; i++) {
                                Collections.swap(mAdapter.getData(), i, i + 1);
                            }
                        } else {
                            for (int i = fromPosition; i > toPosition; i--) {
                                Collections.swap(mAdapter.getData(), i, i - 1);
                            }
                        }
                        mAdapter.notifyItemMoved(fromPosition, toPosition);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                int itemViewType = viewHolder != null ? viewHolder.getItemViewType() : GridImageAdapter.TYPE_CAMERA;
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    if (ItemTouchHelper.ACTION_STATE_DRAG == actionState && mDragListener != null) {
                        mDragListener.dragState(true);
                    }
                    super.onSelectedChanged(viewHolder, actionState);
                }
            }

            @Override
            public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
                return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int itemViewType = viewHolder.getItemViewType();
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    viewHolder.itemView.setAlpha(1.0f);
                    super.clearView(recyclerView, viewHolder);
                    mAdapter.notifyDataSetChanged();
                    resetState();
                }
            }
        });

        // 绑定拖拽事件
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        // 注册外部预览图片删除按钮回调
        BroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,
                BroadcastAction.ACTION_DELETE_PREVIEW_POSITION);



    }

    /**
     * 重置
     */
    private void resetState() {
        if (mDragListener != null) {
            mDragListener.deleteState(false);
            mDragListener.dragState(false);
        }
    }

    /**
     * 清空缓存包括裁剪、压缩、AndroidQToPath所生成的文件，注意调用时机必须是处理完本身的业务逻辑后调用；非强制性
     */
    private void clearCache() {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //PictureFileUtils.deleteCacheDirFile(this, PictureMimeType.ofImage());
            PictureFileUtils.deleteAllCacheDirFile(getContext());
        } else {
            PermissionChecker.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE);
        }
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(SendCircleActivity.this)
                    .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                    .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                    .isUseCustomCamera(true)// 是否使用自定义相机
                    .setLanguage(language)// 设置语言，默认中文
                    .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                    .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                    .setPictureWindowAnimationStyle(mWindowAnimationStyle)// 自定义相册启动退出动画
                    .isWithVideoImage(false)// 图片和视频是否可以同选,只在ofAll模式下有效
                    .loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
                    .maxSelectNum(9)// 最大图片选择数量
                    .minSelectNum(0)// 最小选择数量
                    .maxVideoSelectNum(1) // 视频最大选择数量，如果没有单独设置的需求则可以不设置，同用maxSelectNum字段
                    .imageSpanCount(4)// 每行显示个数
                    .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)// 设置相册Activity方向，不设置默认使用系统
                    .isOriginalImageControl(true)// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                    .previewImage(true)// 是否可预览图片
                    .previewVideo(true)// 是否可预览视频
                    .enablePreviewAudio(true) // 是否可播放音频
                    .isCamera(true)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .enableCrop(false)// 是否裁剪
                    .compress(true)// 是否压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
                    .withAspectRatio(16, 9)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                    .isGif(true)// 是否显示gif图片
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .queryMaxFileSize(100)// 只查多少M以内的图片、视频、音频  单位M
                    .openClickSound(false)// 是否开启点击声音
                    .selectionMedia(mAdapter.getData())// 是否传入已选图片
                    .cutOutQuality(90)// 裁剪输出质量 默认100
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .forResult(new OnResultCallbackListener<LocalMedia>() {
                        @Override
                        public void onResult(List<LocalMedia> result) {
                            mAdapter.setList(result);
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancel() {
//                            Log.i(TAG, "PictureSelector Cancel");
                        }
                    });

        }
    };

    /**
     * List转换String
     *
     * @param list
     *            :需要转换的List
     * @return String转换后的字符串
     */
    public String ListToString(List<?> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i) == "") {
                    continue;
                }
                // 如果值是list类型则调用自己
                if (list.get(i) instanceof List) {
                    sb.append(ListToString((List<?>) list.get(i)));
                    sb.append(",");
                } else if (list.get(i) instanceof Map) {
                    sb.append(MapToString((Map<?, ?>) list.get(i)));
                    sb.append(",");
                } else {
                    sb.append(list.get(i));
                    sb.append(",");
                }
            }
        }
        return sb.toString();
    }

    /**
     * Map转换String
     *
     * @param map
     *            :需要转换的Map
     * @return String转换后的字符串
     */
    public String MapToString(Map<?, ?> map) {
        StringBuffer sb = new StringBuffer();
        // 遍历map
        for (Object obj : map.keySet()) {
            if (obj == null) {
                continue;
            }
            Object key = obj;
            Object value = map.get(key);
            if (value instanceof List<?>) {
                sb.append(key.toString() + "," + ListToString((List<?>) value));
                sb.append(",");
            } else if (value instanceof Map<?, ?>) {
                sb.append(key.toString() + ","
                        + MapToString((Map<?, ?>) value));
                sb.append(",");
            } else {
                sb.append(key.toString() + "," + value.toString());
                sb.append(",");
            }
        }
        return sb.toString();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回五种path
                    // 1.media.getPath(); 原图path
                    // 2.media.getCutPath();裁剪后path，需判断media.isCut();切勿直接使用
                    // 3.media.getCompressPath();压缩后path，需判断media.isCompressed();切勿直接使用
                    // 4.media.getOriginalPath()); media.isOriginal());为true时此字段才有值
                    // 5.media.getAndroidQToPath();Android Q版本特有返回的字段，但如果开启了压缩或裁剪还是取裁剪或压缩路径；注意：.isAndroidQTransform 为false 此字段将返回空
                    // 如果同时开启裁剪和压缩，则取压缩路径为准因为是先裁剪后压缩
                    for (LocalMedia media : selectList) {
//                        Log.i(TAG, "是否压缩:" + media.isCompressed());
//                        Log.i(TAG, "压缩:" + media.getCompressPath());
//                        Log.i(TAG, "原图:" + media.getPath());
//                        Log.i(TAG, "是否裁剪:" + media.isCut());
//                        Log.i(TAG, "裁剪:" + media.getCutPath());
//                        Log.i(TAG, "是否开启原图:" + media.isOriginal());
//                        Log.i(TAG, "原图路径:" + media.getOriginalPath());
//                        Log.i(TAG, "Android Q 特有Path:" + media.getAndroidQToPath());
//                        Log.i(TAG, "宽高: " + media.getWidth() + "x" + media.getHeight());
//                        Log.i(TAG, "Size: " + media.getSize());

                        // TODO 可以通过PictureSelectorExternalUtils.getExifInterface();方法获取一些额外的资源信息，如旋转角度、经纬度等信息
                    }
                    mAdapter.setList(selectList);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }else if (requestCode == 666){//选择关联商品
            if (data != null) {
                productId = data.getStringExtra("id");
                productName.setText(data.getStringExtra("name"));
            }
        }
    }


    private void getWhiteStyle() {
        // 相册主题
        mPictureParameterStyle = new PictureParameterStyle();
        // 是否改变状态栏字体颜色(黑白切换)
        mPictureParameterStyle.isChangeStatusBarFontColor = true;
        // 是否开启右下角已完成(0/9)风格
        mPictureParameterStyle.isOpenCompletedNumStyle = false;
        // 是否开启类似QQ相册带数字选择风格
        mPictureParameterStyle.isOpenCheckNumStyle = false;
        // 相册状态栏背景色
        mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#FFFFFF");
        // 相册列表标题栏背景色
        mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#FFFFFF");
        // 相册列表标题栏右侧上拉箭头
        mPictureParameterStyle.pictureTitleUpResId = R.drawable.ic_orange_arrow_up;
        // 相册列表标题栏右侧下拉箭头
        mPictureParameterStyle.pictureTitleDownResId = R.drawable.ic_orange_arrow_down;
        // 相册文件夹列表选中圆点
        mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval;
        // 相册返回箭头
        mPictureParameterStyle.pictureLeftBackIcon = R.drawable.ic_back_arrow;
        // 标题栏字体颜色
        mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(getContext(), R.color.app_color_black);
        // 相册右侧取消按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(getContext(), R.color.app_color_black);
        // 相册列表勾选图片样式
        mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_checkbox_selector;
        // 相册列表底部背景色
        mPictureParameterStyle.pictureBottomBgColor = ContextCompat.getColor(getContext(), R.color.picture_color_fa);
        // 已选数量圆点背景样式
        mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.picture_num_oval;
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        mPictureParameterStyle.picturePreviewTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_fa632d);
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_9b);
        // 相册列表已完成色值(已完成 可点击色值)
        mPictureParameterStyle.pictureCompleteTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_fa632d);
        // 相册列表未完成色值(请选择 不可点击色值)
        mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_9b);
        // 预览界面底部背景色
        mPictureParameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(getContext(), R.color.picture_color_white);
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalControlStyle = R.drawable.picture_original_checkbox;
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalFontColor = ContextCompat.getColor(getContext(), R.color.app_color_53575e);
        // 外部预览界面删除按钮样式
        mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_black_delete;
        // 外部预览界面是否显示删除按钮
        mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true;
        // 裁剪主题
        mCropParameterStyle = new PictureCropParameterStyle(
                ContextCompat.getColor(getContext(), R.color.app_color_white),
                ContextCompat.getColor(getContext(), R.color.app_color_white),
                ContextCompat.getColor(getContext(), R.color.app_color_black),
                mPictureParameterStyle.isChangeStatusBarFontColor);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE:
                // 存储权限
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        PictureFileUtils.deleteCacheDirFile(getContext(), PictureMimeType.ofImage());
                    } else {
                        Toast.makeText(SendCircleActivity.this,
                                getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAdapter != null && mAdapter.getData() != null && mAdapter.getData().size() > 0) {
            outState.putParcelableArrayList("selectorList",
                    (ArrayList<? extends Parcelable>) mAdapter.getData());
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle extras;
            switch (action) {
                case BroadcastAction.ACTION_DELETE_PREVIEW_POSITION:
                    // 外部预览删除按钮回调
                    extras = intent.getExtras();
                    int position = extras.getInt(PictureConfig.EXTRA_PREVIEW_DELETE_POSITION);
                    if (position < mAdapter.getData().size()) {
                        mAdapter.remove(position);
                        mAdapter.notifyItemRemoved(position);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            BroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver,
                    BroadcastAction.ACTION_DELETE_PREVIEW_POSITION);
        }
    }

    public Context getContext() {
        return this;
    }



    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == PRC_PHOTO_PICKER) {
            Toast.makeText(this, "您拒绝了「图片选择」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.back, R.id.send,R.id.addProduct})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.send:
                if (canClick) {
                    if (TextUtils.isEmpty(inputEdit.getText())){
                        ToastUtils.showToast(SendCircleActivity.this,"你的心得还没有写下来呢");
                        return;
                    }
                    if (mAdapter.getData().size() == 0){
                        ToastUtils.showToast(SendCircleActivity.this,"要分享的照片或视频还没选择呢");
                        return;
                    }
                    if (sendType.equals("2")) {//发布买家秀必须关联商品
                        if (productId.equals("")) {
                            ToastUtils.showToast(SendCircleActivity.this, "请选择关联商品");
                            return;
                        }
                    }
                    startProgressDialog("",SendCircleActivity.this);
                    uploadUserImage(mAdapter.getData());
                }else {
                    ToastUtils.showToast(SendCircleActivity.this,"您还有正在发送的消息,请等待上一条发送成功再尝试");
                }
                break;
            case R.id.addProduct://添加关联商品
                Intent intent = new Intent(SendCircleActivity.this,AboutProductActivity.class);
                intent.putExtra("sendType",sendType);
                startActivityForResult(intent,666);
                break;
        }
    }

    /**
     * 发布消息
     * @param localPath
     */
    private void uploadUserImage(final List<LocalMedia> localPath) {
        canClick = false;
        SendCircleBean param = new SendCircleBean();
        param.setFileupload(localPath);
        if (localPath.size()>0){
            if (PictureMimeType.eqVideo(localPath.get(0).getMimeType())){
                param.setType("2");
                MediaUtils.getImageForVideo(SendCircleActivity.this,localPath.get(0).getPath(), new MediaUtils.OnLoadVideoImageListener() {
                    @Override
                    public void onLoadImage(File file) {
                        param.setVideoImg(file);
                        param.setText(inputEdit.getText().toString());
                        param.setProductId(productId);
                        param.setSendType(sendType);
                        HttpClient.sendImage(param, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(String body) {
                                stopProgressDialog();
                                JSONObject object = JSON.parseObject(body);
                                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                                    finish();
                                } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {
                                    ToastUtils.showToast(SendCircleActivity.this,object.getString("message"));
                                }
                                canClick = true;
                            }

                            @Override
                            public void onFailure(Request request, IOException e) {
                                stopProgressDialog();
                                canClick = true;
                                Toast.makeText(SendCircleActivity.this, request.toString(), Toast.LENGTH_LONG).show();
                            }
                        }, SendCircleActivity.this);
                    }
                });
            }else {
                param.setType("1");
                param.setText(inputEdit.getText().toString());
                param.setProductId(productId);
                param.setSendType(sendType);
                HttpClient.sendImage(param, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String body) {
                        stopProgressDialog();
                        canClick = true;
                        JSONObject object = JSON.parseObject(body);
                        if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                            finish();
                        } else if (HttpClient.UNLOGIN_CODE.equals(object.getString("status"))) {
                            ToastUtils.showToast(SendCircleActivity.this,object.getString("message"));
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {
                        stopProgressDialog();
                        canClick = true;
                        Toast.makeText(SendCircleActivity.this, request.toString(), Toast.LENGTH_LONG).show();
                    }
                }, SendCircleActivity.this);
            }
        }
    }

}
