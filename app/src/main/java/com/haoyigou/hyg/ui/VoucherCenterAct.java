package com.haoyigou.hyg.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.ABaseTransformer;
import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.adapter.FragmentPagerAdapter;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.BannerBO;
import com.haoyigou.hyg.entity.BaseResult;
import com.haoyigou.hyg.entity.LunBoBO;
import com.haoyigou.hyg.entity.PayActivityBO;
import com.haoyigou.hyg.entity.PayReult01;
import com.haoyigou.hyg.entity.StoreInfoEntity;
import com.haoyigou.hyg.entity.VoucherEntry;
import com.haoyigou.hyg.fragment.VoucherChangeFragment;
import com.haoyigou.hyg.fragment.VoucherFlowFragment;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.ui.personweb.PersonWebViewAct;
import com.haoyigou.hyg.utils.DisplayUtil;
import com.haoyigou.hyg.utils.RegexUtils;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.view.widget.PayPupWindow;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by wuliang on 2017/1/13.
 * <p>
 * 充值中心
 */

public class VoucherCenterAct extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.payActivity)
    TextView payActivity;
    private String LINK = "中国联通";
    private String MOVE = "中国移动";
    private String TELECOM = "中国电信";
    @BindView(R.id.right_image01)
    ImageView rightImage01;
    @BindView(R.id.right_image)
    ImageView rightImage;
    @BindView(R.id.phone_edit)
    EditText phoneEdit;
    @BindView(R.id.phone_address)
    TextView phoneAddress;
    @BindView(R.id.person_book)
    ImageView personBook;
    @BindView(R.id.changes_text)
    TextView changesText;
    @BindView(R.id.changes_img)
    ImageView changesImg;
    @BindView(R.id.voucher_changes)
    LinearLayout voucherChanges;
    @BindView(R.id.flow_text)
    TextView flowText;
    @BindView(R.id.flow_img)
    ImageView flowImg;
    @BindView(R.id.voucher_flow)
    LinearLayout voucherFlow;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.voucher_price)
    TextView voucherPrice;
    @BindView(R.id.voucher_button)
    TextView voucherButton;
    @BindView(R.id.phone_coupon)
    LinearLayout phonecoupon;
    @BindView(R.id.aaa)
    LinearLayout aaa;
    @BindView(R.id.aa)
    LinearLayout aa;
    @BindView(R.id.quanguoxianshi)
    RelativeLayout quanguoxianshi;
    @BindView(R.id.quanguojigete)
    TextView quanguojigete;
    @BindView(R.id.quanguoliuliang)
    TextView quanguoliuliang;
    @BindView(R.id.shengneixianshi)
    RelativeLayout shengneixianshi;
    @BindView(R.id.shengneijiagete)
    TextView shengneijiagete;
    @BindView(R.id.shengneiliuliang)
    TextView shengneiliuliang;
    @BindView(R.id.banner)
    ConvenientBanner banner;
    @BindView(R.id.coupon)

    TextView coupon;
    @BindView(R.id.sc)

    ScrollView sc;
    boolean isEdit = false;
    boolean hfisshow = false;
    boolean llisshow = false;
    /**
     * 号码归属地
     **/
    private String Adress;
    private List<VoucherEntry> priceList;

    private int TYPE = 1;    //1则在冲花费界面，2则冲流量

    VoucherChangeFragment changeFragment;
    VoucherFlowFragment flowFragment;

    private VoucherEntry huafei;
    private VoucherEntry liuliang;

    private PayPupWindow payPupWindow;

    IWXAPI msgApi;
    private int tzid;
    private List<VoucherEntry.SkulistBo> liulianglist;
    private String liuliangpri;
    private int hftzid;
    private boolean ishfid = false;
    private int iddd;
    private final static int REQUEST_CODE = 1;
    private int mycouponid;
    private double hfxianshijine;
    private double huafeiqueren;
    private String resultjiage;
    private String llresult;
    private double liuliangqueren;
    private LunBoBO lunBO;
    private JSONObject object;

    private String parentLocation = "";//后台日志标志


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_voucher_center);
        ButterKnife.bind(this);
        msgApi = WXAPIFactory.createWXAPI(this, null);
        goBack();
        setTitleText("充值中心");
        setListener();
        loadlunbo();
        if (getIntent().getStringExtra("parentLocation") != null) {
            parentLocation = getIntent().getStringExtra("parentLocation");
        } else {
            parentLocation = "";
        }
        changeFragment = new VoucherChangeFragment();
        flowFragment = new VoucherFlowFragment();
        changeFragment.setCallBack(new VoucherChangeFragment.onCallBack() {
            @Override
            public void onClick(VoucherEntry entry) {
//                closeInputMethod();
                if (object.getString("hasMoneyCoupon").equals("0")) {//没有花费
                    coupon.setText("暂无可用优惠券");
                    coupon.setTextColor(Color.parseColor("#000000"));
                }
                if (object.getString("hasMoneyCoupon").equals("1")) {//有花费
                    coupon.setText("请选择优惠券");
                    coupon.setTextColor(Color.parseColor("#e72e2d"));
                }
                huafei = entry;
                hfxianshijine = huafei.getPricetemp();
                mycouponid = 0;
                DecimalFormat df = new DecimalFormat("0.##");
                double de = hfxianshijine;
                resultjiage = df.format(de);
                ishfid = true;
                hftzid = huafei.getId();
                showPrice();
                getPayActivity(String.valueOf(entry.getParprice()));
            }
        });
        flowFragment.setCallBack(new VoucherFlowFragment.onCallBack() {
            @Override
            public void onClick(VoucherEntry entry) {
//                closeInputMethod();
                sc.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        sc.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                if (object.getString("hasflowCoupon").equals("0")) {//没有花费
                    coupon.setText("暂无可用优惠券");
                    coupon.setTextColor(Color.parseColor("#000000"));
                }
                if (object.getString("hasflowCoupon").equals("1")) {//有花费
                    coupon.setText("请选择优惠券");
                    coupon.setTextColor(Color.parseColor("#e72e2d"));
                }
                liuliang = entry;
                liulianglist = entry.getSkulist();
                mycouponid = 0;
                if (liulianglist.size() == 2) {
                    shengneixianshi.setVisibility(View.VISIBLE);
                    quanguoxianshi.setVisibility(View.VISIBLE);
                    for (int i = 0; i < liulianglist.size(); i++) {
                        if (liulianglist.get(i).getRegion() == 0) {
                            quanguojigete.setText("¥" + String.valueOf(liulianglist.get(i).getDisprice()));
                            aaa.setBackgroundResource(R.drawable.item_voucher_nocheck);
                        }
                        if (liulianglist.get(i).getRegion() == 1) {
                            shengneijiagete.setText("¥" + String.valueOf(liulianglist.get(i).getDisprice()));
                            liuliangpri = String.valueOf(liulianglist.get(i).getDisprice());
                            llresult = liuliangpri;
                            aa.setBackgroundResource(R.drawable.item_voucher_checkd);
                            shengneijiagete.setTextColor(Color.parseColor("#ef2e2d"));

                            ishfid = false;
                            tzid = liulianglist.get(i).getId();
                        }
                    }
                }
                if (liulianglist.size() == 1) {
                    shengneixianshi.setVisibility(View.VISIBLE);
                    quanguoxianshi.setVisibility(View.VISIBLE);
                    for (int i = 0; i < liulianglist.size(); i++) {
                        if (liulianglist.get(i).getRegion() == 0) {
//                            Log.e("log--aa", "全国的价格"+liulianglist.get(i).getDisprice());
                            quanguojigete.setText("¥" + String.valueOf(liulianglist.get(i).getDisprice()));
                            aaa.setBackgroundResource(R.drawable.item_voucher_checkd);
                            quanguojigete.setTextColor(Color.parseColor("#ef2e2d"));
                            liuliangpri = String.valueOf(liulianglist.get(i).getDisprice());
                            llresult = liuliangpri;
                            shengneixianshi.setVisibility(View.GONE);
                            ishfid = false;
                            tzid = liulianglist.get(i).getId();
                        }
                        if (liulianglist.get(i).getRegion() == 1) {
//                            Log.e("log--aa", "省内的价格"+liulianglist.get(i).getDisprice());
                            shengneijiagete.setText("¥" + String.valueOf(liulianglist.get(i).getDisprice()));
                            aa.setBackgroundResource(R.drawable.item_voucher_checkd);
                            shengneijiagete.setTextColor(Color.parseColor("#ef2e2d"));
                            quanguoxianshi.setVisibility(View.GONE);
                            liuliangpri = String.valueOf(liulianglist.get(i).getDisprice());
                            llresult = liuliangpri;
                            ishfid = false;
                            tzid = liulianglist.get(i).getId();
                        }
                    }
                }
                showPrice();
            }
        });
        setPagerData();
        if (getIntent().getIntExtra("type", 0) == 1) {
            viewPager.setCurrentItem(1);
        }
    }

    private void loadlunbo() {
        Map<String, Object> params = new HashMap<>();
        if (!parentLocation.equals("")) {
            params.put("parentLocation", parentLocation);
        }
        HttpClient.post(HttpClient.CHARGELUN, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                BaseResult result = JSONObject.parseObject(content, BaseResult.class);
                if (result.surcess()) {
                    lunBO = JSONObject.parseObject(result.getData(), LunBoBO.class);
                    setBanner();
                } else {
                    showToast(result.getMessage());
                }
            }
        }, this);
    }

    private void setBanner() {
        banner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
            @Override
            public LocalImageHolderView createHolder() {
                return new LocalImageHolderView();
            }
        }, lunBO.getLunbopics())
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.lunbo_press, R.drawable.lunbo_normal})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        banner.setManualPageable(true);//设置不能手动影响
        banner.setScrollDuration(lunBO.getLunbotime());
        try {
            Class cls = Class.forName("com.ToxicBakery.viewpager.transforms." + AccordionTransformer.class.getSimpleName());
            ABaseTransformer transforemer = (ABaseTransformer) cls.newInstance();
            banner.getViewPager().setPageTransformer(true, transforemer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        banner.startTurning(lunBO.getLunbotime() * 1000);
        banner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String url = lunBO.getLunbopics().get(position).getAddress();
                if (url == null || url.length() < 10) return;
                Intent intent = new Intent(VoucherCenterAct.this, HomeWebViewAct.class);
                intent.putExtra("url", url);
                intent.putExtra("all", true);
                intent.putExtra("isTitle", true);
                startActivity(intent);
            }
        });

    }

    public class LocalImageHolderView implements Holder<BannerBO> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, BannerBO data) {
            Glide.with(context).load(data.getPictureroot())
                    .error(R.drawable.default_image)
                    .into(imageView);
        }
    }

    /**
     * 设置监听
     */
    private void setListener() {
        setEditListener();
        rightImage.setVisibility(View.VISIBLE);
        rightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(VoucherRecordAct.class, false);
            }
        });
        rightImage01.setVisibility(View.VISIBLE);
        rightImage01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VoucherCenterAct.this, PersonWebViewAct.class);
                intent.putExtra("url", "/jz/recharge/question");
                intent.putExtra("isTitle", true);
                startActivity(intent);
            }
        });
        voucherChanges.setOnClickListener(this);
        voucherFlow.setOnClickListener(this);
        personBook.setOnClickListener(this);
        voucherButton.setOnClickListener(this);
        phonecoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((TYPE == 1 && huafei != null) || (TYPE == 2 && liuliang != null)) {
                    if (ishfid == true) {
                        if (object.getString("hasMoneyCoupon").equals("0")) {//没有花费
                            coupon.setText("暂无可用优惠券");
                            coupon.setTextColor(Color.parseColor("#000000"));
                        }
                        if (object.getString("hasMoneyCoupon").equals("1")) {//有花费
                            iddd = hftzid;
                            String sty = "hf";
                            Intent two = new Intent(VoucherCenterAct.this, VoucherListact.class);
                            two.putExtra("VoucherListactid", iddd + "");
                            two.putExtra("sty", sty);
                            startActivityForResult(two, REQUEST_CODE);

                        }


                    } else {
                        if (object.getString("hasflowCoupon").equals("0")) {//没有花费
                            coupon.setText("暂无可用优惠券");
                            coupon.setTextColor(Color.parseColor("#000000"));
                        }
                        if (object.getString("hasflowCoupon").equals("1")) {//有流量
                            iddd = tzid;
                            String sty = "ll";
                            Intent two = new Intent(VoucherCenterAct.this, VoucherListact.class);
                            two.putExtra("VoucherListactid", iddd + "");
                            two.putExtra("sty", sty);
                            startActivityForResult(two, REQUEST_CODE);
                        }


                    }


                } else {
                    showToast("请选择充值项目");
                }


            }

        });
        quanguoxianshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liulianglist.size() == 2) {
                    if (object.getString("hasflowCoupon").equals("0")) {//没有花费
                        coupon.setText("暂无可用优惠券");
                    }
                    if (object.getString("hasflowCoupon").equals("1")) {//有花费
                        coupon.setText("请选择优惠券");
                        coupon.setTextColor(Color.parseColor("#e72e2d"));
                        mycouponid = 0;
                    }
                    shengneixianshi.setVisibility(View.VISIBLE);
                    quanguoxianshi.setVisibility(View.VISIBLE);
                    for (int i = 0; i < liulianglist.size(); i++) {
                        if (liulianglist.get(i).getRegion() == 0) {
                            aaa.setBackgroundResource(R.drawable.item_voucher_checkd);
                            quanguojigete.setTextColor(Color.parseColor("#ef2e2d"));
                            shengneijiagete.setTextColor(Color.parseColor("#2b2b2b"));
                            aa.setBackgroundResource(R.drawable.item_voucher_nocheck);
                            liuliangpri = String.valueOf(liulianglist.get(i).getDisprice());
                            llresult = liuliangpri;
                            ishfid = false;
                            tzid = liulianglist.get(i).getId();
                            showPrice();
                        }
                    }
                }

            }
        });
        shengneixianshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liulianglist.size() == 2) {
                    if (object.getString("hasflowCoupon").equals("0")) {//没有花费
                        coupon.setText("暂无可用优惠券");
                    }
                    if (object.getString("hasflowCoupon").equals("1")) {//有花费
                        coupon.setText("请选择优惠券");
                        coupon.setTextColor(Color.parseColor("#e72e2d"));
                        mycouponid = 0;
                    }
                    shengneixianshi.setVisibility(View.VISIBLE);
                    quanguoxianshi.setVisibility(View.VISIBLE);
                    for (int i = 0; i < liulianglist.size(); i++) {
                        if (liulianglist.get(i).getRegion() == 1) {
                            aa.setBackgroundResource(R.drawable.item_voucher_checkd);
                            shengneijiagete.setTextColor(Color.parseColor("#ef2e2d"));
                            quanguojigete.setTextColor(Color.parseColor("#2b2b2b"));
                            aaa.setBackgroundResource(R.drawable.item_voucher_nocheck);
                            liuliangpri = String.valueOf(liulianglist.get(i).getDisprice());
                            llresult = liuliangpri;
                            ishfid = false;
                            tzid = liulianglist.get(i).getId();
                            showPrice();
                        }
                    }
                }

            }
        });
    }


    /**
     * 申请权限
     */
    private void getPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS
                    }, 1);
        } else {
            startActivityForResult(new Intent(
                    Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 0);
        }
    }

    /**
     * 对Edittext的输入做处理
     */

//    private void closeInputMethod() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        boolean isOpen = imm.isActive();
//        if (isOpen) {
//            // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
//            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void setEditListener() {
        phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                isEdit = false;
                huafei = null;
                liuliang = null;
                showPrice();
                if (priceList != null) {
                    changeFragment.setGridAdapter(priceList);
                    flowFragment.setGridAdapter(priceList);
                }
                if (editable.toString().length() >= 11) {
                    getPhoneData(editable.toString());
                } else {
                    phoneAddress.setVisibility(View.GONE);
                }
            }
        });
        if (SharedPreferencesUtils.getInstance().getString("userPhone", "").equals("")) {
            loaduserinfo();
        } else {
            phoneEdit.setText(SharedPreferencesUtils.getInstance().getString("userPhone", ""));
        }

    }

    private void loaduserinfo() {
        StoreInfoEntity param = new StoreInfoEntity();
        HttpClient.userinfo(param, new AsyncHttpResponseHandler() {
            public void onSuccess(String body) {
//                Log.e("getMobileCodeuser", "返回数据:" + body);
                JSONObject object = JSON.parseObject(body);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    phoneEdit.setText(object.getString("phone"));
                } else if ("0".equals(object.getString("status"))) {
                }
            }

            public void onFailure(Request request, IOException e) {
            }
        }, this);
    }

    /**
     * 设置viewpager显示
     */
    private void setPagerData() {
        List<Fragment> list = new ArrayList<>();
        list.add(changeFragment);
        list.add(flowFragment);
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    changesImg.setVisibility(View.VISIBLE);
                    changesText.setTextColor(Color.parseColor("#e72e2d"));
                    flowImg.setVisibility(View.GONE);
                    flowText.setTextColor(Color.parseColor("#2b2b2b"));
                    TYPE = 1;
                    showPrice();
                } else {
                    changesImg.setVisibility(View.GONE);
                    changesText.setTextColor(Color.parseColor("#2b2b2b"));
                    flowImg.setVisibility(View.VISIBLE);
                    flowText.setTextColor(Color.parseColor("#e72e2d"));
                    TYPE = 2;
                    showPrice();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 设置售价显示
     */
    private void showPrice() {
        switch (TYPE) {
            case 1:    //话费
                if (huafei != null) {
                    voucherPrice.setText("特惠价¥ " + resultjiage);
                } else {
                    voucherPrice.setText("特惠价¥ 0.0");
                }
//                text2.setVisibility(View.GONE);
                break;
            case 2:    //流量
                if (liuliang != null) {
                    voucherPrice.setText("特惠价¥ " + llresult);
                } else {
                    voucherPrice.setText("特惠价¥ 0.0");
                }
//                text2.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 充值送券活动
     */
    PayActivityBO payActivityBO;
    private void getPayActivity(String faceMoney) {
        Map<String, Object> params = new HashMap<>();
        params.put("faceMoney", faceMoney);
        HttpClient.post(HttpClient.PAY_ACTIVITY, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    String result = object.getString("data");
                    payActivityBO = JSONObject.parseObject(result,PayActivityBO.class);
                    if (payActivityBO.getId() != null && !payActivityBO.getId().equals("")){
                        payActivity.setVisibility(View.VISIBLE);
                        payActivity.setText(payActivityBO.getTitle());
                        StateMessage.activityTitle = payActivityBO.getTitle();
                    }else {
                        payActivity.setVisibility(View.GONE);
                        StateMessage.activityTitle = "";
                    }

                }
            }
        }, VoucherCenterAct.this);
    }

    /***
     * 查询手机号归属地
     */
    private void getPhoneData(String num) {
        String url = "http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=" + num.replaceAll(" ", "");
        HttpClient.get(url, new HashMap<String, Object>(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
//                Log.e("log--aa", content);
                JSONObject object = JSON.parseObject(content);
                Message message = new Message();
                Adress = object.getString("catName");
                String carrier = object.getString("carrier");
                if (StringUtils.isEmpty(carrier)) {
                    carrier = "未知";
                }
                message.what = 0x11;
                message.obj = "(" + carrier + ")";
                handler.sendMessage(message);
            }
        }, this);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x11:
                    phoneAddress.setVisibility(View.VISIBLE);
                    phoneAddress.setText((CharSequence) msg.obj);
                    getChangePriceData();
                    break;
                case 0x22:   //支付宝支付完成
                    PayReult01 payResult = new PayReult01((Map<String, String>) msg.obj);
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        if (TYPE == 1) {
                            Toast.makeText(VoucherCenterAct.this, "话费充值成功，将在5-10分钟内到账，请您耐心等待。", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(VoucherCenterAct.this, "流量充值成功，将在5-10分钟内到账，请您耐心等待。", Toast.LENGTH_LONG).show();
                        }
                        goToActivity(VoucherRecordAct.class, false);
                    } else {
//                        goToActivity(VoucherRecordAct.class, false);
                        //清空活动
                        StateMessage.payOrderNum = "";
                    }
                    break;
            }
        }
    };


    /***
     * 根据手机运营商获取不同的价格
     */
    private void getChangePriceData() {
        Map<String, Object> params = new HashMap<>();
        if (MOVE.equals(Adress)) {
            params.put("accounttype", "1");
        } else if (LINK.equals(Adress)) {
            params.put("accounttype", "3");
        } else if (TELECOM.equals(Adress)) {
            params.put("accounttype", "2");
        }
        if (!parentLocation.equals("")) {
            params.put("parentLocation", parentLocation);
        }
        HttpClient.post(HttpClient.VOUCHER, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
//                Log.e("log--充值", content);
                object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    String result = object.getString("result");
                    priceList = JSONArray.parseArray(result, VoucherEntry.class);
                    if (object.getString("hasMoneyCoupon").equals("0")) {//没有花费
                        coupon.setText("暂无可用优惠券");
                        coupon.setTextColor(Color.parseColor("#000000"));
                    }
                    if (object.getString("hasMoneyCoupon").equals("1")) {//有花费
                        coupon.setText("请选择优惠券");
                        coupon.setTextColor(Color.parseColor("#e72e2d"));
                    }
                    changeFragment.setGridAdapter(priceList);
                    flowFragment.setGridAdapter(priceList);
                }
            }
        }, VoucherCenterAct.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voucher_changes:
                if (object.getString("hasMoneyCoupon").equals("0")) {//没有花费
                    coupon.setText("暂无可用优惠券");
                    coupon.setTextColor(Color.parseColor("#000000"));
                }
                if (object.getString("hasMoneyCoupon").equals("1")) {//有花费
                    coupon.setText("请选择优惠券");
                    coupon.setTextColor(Color.parseColor("#e72e2d"));
                }
                mycouponid = 0;
                changesImg.setVisibility(View.VISIBLE);
                changesText.setTextColor(Color.parseColor("#e72e2d"));
                flowImg.setVisibility(View.GONE);
                flowText.setTextColor(Color.parseColor("#2b2b2b"));
                viewPager.setCurrentItem(0);
                TYPE = 1;
                ishfid = true;
                shengneixianshi.setVisibility(View.GONE);
                quanguoxianshi.setVisibility(View.GONE);
                showPrice();
                break;
            case R.id.voucher_flow:
                if (object.getString("hasflowCoupon").equals("0")) {//没有花费
                    coupon.setText("暂无可用优惠券");
                    coupon.setTextColor(Color.parseColor("#000000"));
                }
                if (object.getString("hasflowCoupon").equals("1")) {//有花费
                    coupon.setText("请选择优惠券");
                    coupon.setTextColor(Color.parseColor("#e72e2d"));
                }
                mycouponid = 0;
                changesImg.setVisibility(View.GONE);
                changesText.setTextColor(Color.parseColor("#2b2b2b"));
                flowImg.setVisibility(View.VISIBLE);
                flowText.setTextColor(Color.parseColor("#e72e2d"));
                viewPager.setCurrentItem(1);
                TYPE = 2;
                ishfid = false;
                if (liuliang != null) {

                    if (liulianglist.size() == 2) {
                        shengneixianshi.setVisibility(View.VISIBLE);
                        quanguoxianshi.setVisibility(View.VISIBLE);
                        for (int i = 0; i < liulianglist.size(); i++) {
                            if (liulianglist.get(i).getRegion() == 0) {
//                                Log.e("log--aa", "全国的价格"+liulianglist.get(i).getDisprice());
                            }
                            if (liulianglist.get(i).getRegion() == 1) {
//                                Log.e("log--aa", "省内的价格"+liulianglist.get(i).getDisprice());
                            }
                        }
                    }
                    if (liulianglist.size() == 1) {
                        shengneixianshi.setVisibility(View.VISIBLE);
                        quanguoxianshi.setVisibility(View.VISIBLE);
                        for (int i = 0; i < liulianglist.size(); i++) {
                            if (liulianglist.get(i).getRegion() == 0) {
//                                Log.e("log--aa", "全国的价格"+liulianglist.get(i).getDisprice());
                                shengneixianshi.setVisibility(View.GONE);
                            }
                            if (liulianglist.get(i).getRegion() == 1) {
//                                Log.e("log--aa", "省内的价格"+liulianglist.get(i).getDisprice());
                                quanguoxianshi.setVisibility(View.GONE);
                            }
                        }
                    }
                }
                showPrice();
                break;
            case R.id.person_book:
                getPermission();
                break;
            case R.id.voucher_button:   //立即充值
                if (StringUtils.isEmpty(phoneEdit.getText().toString().trim())) {
                    showToast("请输入手机号！");
                    return;
                }
                if (!RegexUtils.isMobileExact(phoneEdit.getText().toString().trim())) {
                    showToast("该手机号码暂不支持充值！");
                    return;
                }
                if ((TYPE == 1 && huafei != null) || (TYPE == 2 && liuliang != null)) {
                    showUpdateDialog();
                } else {
                    showToast("请选择一个金额");
                }
                break;
        }
    }


    /**
     * 充值 LogUtils.showToast("亲，付款未成功，请稍后重试！");
     */
    private boolean isShowPrizaDialog = false;//是否显示充值送礼包的弹窗
    private void pay(final int payType) {
//        Log.e("DDDFFF", String.valueOf(iddd));
        Map<String, Object> params = new HashMap<>();
        if (TYPE == 1) {
            params.put("id", hftzid);
        } else {
            params.put("id", tzid);
        }
        if (mycouponid != 0) {
            params.put("mycouponid", mycouponid);
        }
        params.put("phone", phoneEdit.getText().toString().trim());
        params.put("paytype", payType);
        if (!parentLocation.equals("")) {
            params.put("parentLocation", parentLocation);
        }
        startProgressDialog("加载中...", this);
        HttpClient.post(HttpClient.VOUCHERPAY, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                JSONObject object = JSON.parseObject(content);
                stopProgressDialog();
                if ("2".equals(object.getString("status"))) {
                    if (TYPE == 1) {
                        Toast.makeText(VoucherCenterAct.this, "话费充值成功，将在5-10分钟内到账，请您耐心等待。", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(VoucherCenterAct.this, "流量充值成功，将在5-10分钟内到账，请您耐心等待。", Toast.LENGTH_LONG).show();
                    }
                    goToActivity(VoucherRecordAct.class, false);
                }
                if ("0".equals(object.getString("status"))) {
                    Toast.makeText(VoucherCenterAct.this, object.getString("message"), Toast.LENGTH_LONG).show();
                    stopProgressDialog();
                    return;
//                    LogUtils.showToast("jkl;j");
//                    Log.e("log-123-", object.getString("message"));
                }
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    switch (payType) {
                        case 1:    //微信支付
                            PayReq req = new PayReq();
                            req.appId = object.getString("appid");
                            req.partnerId = object.getString("partnerid");
                            req.prepayId = object.getString("prepayid");
                            req.nonceStr = object.getString("noncestr");
                            req.timeStamp = object.getString("timestamp");
                            req.packageValue = object.getString("package");
                            req.sign = object.getString("sign");
                            StateMessage.payOrderNum = object.getString("ordernum");
                            StateMessage.isVoucher = TYPE;
                            try {
                                Thread.sleep(1000);
                                msgApi.registerApp(req.appId);
                                msgApi.sendReq(req);
                                stopProgressDialog();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 2:     //支付宝支付
                            String data = object.getString("data");
                            StateMessage.payOrderNum = object.getString("ordernum");
                            new AlipayThread(data).start();
                            break;
                    }
                }
            }
        }, this);
    }

    /**
     * 调用系统通讯录
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == VoucherListact.RESULT_CODE) {
//            Log.e("DDDFFF", liuliangpri+"");
            Bundle bundle = data.getExtras();
            String strResult = bundle.getString("shopBOsresult");
            String ji = bundle.getString("jine");
            String style = bundle.getString("style");
//            Log.e("DDDFFF", ji);
            Double youhuijuanjine = Double.parseDouble(ji);
            String zt = bundle.getString("youhui");
            int fhid = Integer.parseInt(strResult);
            String text = bundle.getString("text");
            coupon.setText(text);
            if (zt.equals("1")) {
                if (style.equals("hf")) {
                    mycouponid = fhid;
                    huafeiqueren = hfxianshijine - youhuijuanjine;
                    DecimalFormat df = new DecimalFormat("######0.00");
                    double d = huafeiqueren;
                    if (d <= 0) {
                        resultjiage = "0";
                    } else {
                        resultjiage = df.format(d);
                    }
                    showPrice();
                }
                if (style.equals("ll")) {
                    mycouponid = fhid;
                    Double liuliangpria = Double.parseDouble(liuliangpri);
                    liuliangqueren = liuliangpria - youhuijuanjine;
                    DecimalFormat df = new DecimalFormat("######0.00");
                    double d = liuliangqueren;
                    if (d <= 0) {
                        llresult = "0";
                    } else {
                        llresult = df.format(d);
                    }
                    showPrice();
                }


            }

//            Log.e("DDDFFF", liuliangpri+"");


        }
        if (requestCode == REQUEST_CODE && resultCode == VoucherListact.RESULT_CODE_NO) {
            Bundle bundle = data.getExtras();
            String strResult = bundle.getString("no");
            if (strResult.equals("no")) {
                mycouponid = 0;
                coupon.setText("不使用优惠券");
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            //ContentProvider展示数据类似一个单个数据库表
            //ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
            ContentResolver reContentResolverol = getContentResolver();
            //URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
            Uri contactData = data.getData();
            //查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
            Cursor cursor = managedQuery(contactData, null, null, null, null);
            cursor.moveToFirst();
            //获得DATA表中的名字
            String username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            //条件为联系人ID
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
            Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null,
                    null);
            while (phone.moveToNext()) {
                String usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneEdit.setText(usernumber.replaceAll(" ", "").replaceAll("-", ""));
                huafei = null;
                liuliang = null;
                showPrice();
            }
        }
    }

    class AlipayThread extends Thread {
        String itemdata;

        AlipayThread(String itemdata) {
            this.itemdata = itemdata;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            PayTask alipay = new PayTask(VoucherCenterAct.this);
            Map<String, String> result = alipay.payV2(itemdata, true);
            Message msg = new Message();
            msg.what = 0x22;
            msg.obj = result;
            handler.sendMessage(msg);
            stopProgressDialog();
            super.run();
        }
    }


    private void showUpdateDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        final LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.voucher_dialog, null);
        ImageView ivClose = (ImageView) v.findViewById(R.id.ivClose);
        final TextView number = (TextView) v.findViewById(R.id.number);//电话号码
        final TextView payNum = (TextView) v.findViewById(R.id.payNum);//充值金额
        final TextView payType = (TextView) v.findViewById(R.id.payType);//充值类型
        final TextView txtHuiCount = (TextView) v.findViewById(R.id.txtHuiCount);//优惠金额
        final TextView txtRealPay = (TextView) v.findViewById(R.id.txtRealPay);//实付
        final TextView payRealNum = (TextView) v.findViewById(R.id.payRealNum);
        final RelativeLayout rlSure = (RelativeLayout) v.findViewById(R.id.rlSure);
        number.setText(phoneEdit.getText().toString().trim());
        if (TYPE == 1) {
            payType.setText("话费");
            if (huafei.getRechargeName() != null) {
                payNum.setText(String.format("%s", huafei.getRechargeName()));
                txtRealPay.setText(String.format("%s元", resultjiage));
                payRealNum.setText(String.format("售价：%s元", huafei.getPrice()));
                if (compare(String.valueOf(huafei.getPrice()), resultjiage)) {
                    txtHuiCount.setText(String.format("-%s元",
                            new BigDecimal(huafei.getPrice())
                                    .subtract(new BigDecimal(resultjiage)).intValue()));
                } else {
                    txtHuiCount.setText("无");
                }
            }
        } else {
            payType.setText("流量");
            if (liuliang.getRechargeName() != null) {
                payNum.setText(String.format("%s", liuliang.getRechargeName()));
                txtRealPay.setText(String.format("%s元", llresult));
                payRealNum.setText(String.format("售价：%s元", liuliang.getPrice()));
                if (compare(String.valueOf(liuliang.getPrice()), llresult)) {
                    txtHuiCount.setText(String.format("-%s元",
                            new BigDecimal(liuliang.getPrice())
                                    .subtract(new BigDecimal(llresult)).intValue()));
                } else {
                    txtHuiCount.setText("无");
                }
            }
        }
        builder.setView(v);
        builder.setCancelable(true);
        final Dialog noticeDialog = builder.create();
        noticeDialog.getWindow().setGravity(Gravity.CENTER);
        noticeDialog.show();
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeDialog.dismiss();
            }
        });
        rlSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeDialog.dismiss();
                payPupWindow = new PayPupWindow(VoucherCenterAct.this);
                payPupWindow.setOnPayButton(new PayPupWindow.onPayButton() {
                    @Override
                    public void onClick(int type) {
                        pay(type);
                        payPupWindow.dismiss();
                    }
                });
                payPupWindow.showAtLocation(findViewById(R.id.voucher_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        noticeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        WindowManager.LayoutParams layoutParams = noticeDialog.getWindow().getAttributes();
        layoutParams.width = (int) (DisplayUtil.getMobileWidth(this) * 0.78);
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        noticeDialog.getWindow().setAttributes(layoutParams);
    }

    public static boolean compare(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        int bj = b1.compareTo(b2);
        if (bj > 0)
            return true;
        else
            return false;
    }
}