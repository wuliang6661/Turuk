package com.haoyigou.hyg.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.base.BaseActivity;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.entity.DailyBean;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.utils.StringUtils;
import com.haoyigou.hyg.view.widget.SharePopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * Created by Witness on 2020-03-23
 * Describe:
 */
public class EverydayDetail extends BaseActivity {

    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.right_image01)
    ImageView rightImage01;
    @BindView(R.id.rlTitleBg)
    RelativeLayout rlTitleBg;
    @BindView(R.id.productName)
    TextView productName;
    @BindView(R.id.productImg)
    ImageView productImg;
    @BindView(R.id.buy)
    ImageView buy;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.sell)
    TextView sell;
    @BindView(R.id.nowPrice)
    TextView nowPrice;
    @BindView(R.id.oldPrice)
    TextView oldPrice;
    @BindView(R.id.webView)
    JocellWebView webView;
    @BindView(R.id.videoplayer)
    JzvdStd videoplayer;
    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;

    private DailyBean dailyBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.everyday_detail_layout);
        // android6.0+系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        ButterKnife.bind(this);
        dailyBean = (DailyBean) getIntent().getSerializableExtra("data");
        initView();
    }

    @OnClick({R.id.back, R.id.right_image01, R.id.buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right_image01://分享
                showWindow(dailyBean.getTitle(),dailyBean.getPicture(),dailyBean.getSecondTitle(),dailyBean.getShareUrl());
                break;
            case R.id.buy:
                break;
        }
    }

    private void initView() {
        Glide.with(EverydayDetail.this).load(dailyBean.getLogo()).asBitmap().into(productImg);
        productName.setText(dailyBean.getSecondTitle());
        title.setText(dailyBean.getTitle());
        name.setText(dailyBean.getName());
        sell.setText(dailyBean.getMemo());
        nowPrice.setText(dailyBean.getDisprice());
        oldPrice.setText(String.format("￥ %s", dailyBean.getPrice()));
        oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        webView.loadDataWithBaseURL(null, getHtmlData(dailyBean.getTweet()), "text/html", "utf-8", null);//加载html数据
        buy.setOnClickListener(v -> {
            Intent intent = new Intent(EverydayDetail.this, HomeWebViewAct.class);
            intent.putExtra("url", dailyBean.getJumpUrl());
            intent.putExtra("all", true);
            startActivity(intent);
        });

        videoplayer.setUpEveryDay(
                dailyBean.getVideo(),
                dailyBean.getName(), 0, "yes");
        Glide.with(EverydayDetail.this)
                .load(dailyBean.getPicture())
                .into(videoplayer.thumbImageView);
    }


    private String getHtmlData(String bodyHTML) {
        String head = "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + "<style>img{max-width: 100%; width:100%; height:auto;}*{margin:0px;}</style>"
                + "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (Jzvd.backPress()) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /***
     * 显示分享弹窗
     */
    private void showWindow(String sharetitle,String imgurl,String tabdesc,String shareurl) {
        SharePopupWindow share = new SharePopupWindow(this);
        share.setShareMessage(sharetitle, imgurl, tabdesc, shareurl);
        share.showAtLocation(findViewById(R.id.mainLayout), Gravity.BOTTOM, 0, 0);
    }


}
