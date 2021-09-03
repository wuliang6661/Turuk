package com.haoyigou.hyg.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.tabs.TabLayout;
import com.haoyigou.hyg.R;
import com.haoyigou.hyg.application.MApplication;
import com.haoyigou.hyg.base.BaseFragment;
import com.haoyigou.hyg.common.http.AsyncHttpResponseHandler;
import com.haoyigou.hyg.common.http.HttpClient;
import com.haoyigou.hyg.config.StateMessage;
import com.haoyigou.hyg.entity.CommentBean;
import com.haoyigou.hyg.entity.CommuBannerBean;
import com.haoyigou.hyg.entity.CommunBean;
import com.haoyigou.hyg.ui.LoginActivity;
import com.haoyigou.hyg.ui.SendCircleActivity;
import com.haoyigou.hyg.ui.SmartFooter;
import com.haoyigou.hyg.ui.homeweb.HomeWebViewAct;
import com.haoyigou.hyg.utils.DisplayUtils;
import com.haoyigou.hyg.utils.SharedPreferencesUtils;
import com.haoyigou.hyg.utils.ToastUtils;
import com.haoyigou.hyg.view.FixedRecyclerView;
import com.haoyigou.hyg.view.SmartHeader;
import com.haoyigou.hyg.view.ViewPagerScroller;
import com.haoyigou.hyg.view.circlephoto.RoundImageView;
import com.haoyigou.hyg.view.ninepic.GlideSimpleTarget;
import com.haoyigou.hyg.view.ninepic.NineGridView;
import com.haoyigou.hyg.view.ninepic.NineImageAdapter;
import com.haoyigou.hyg.view.viewpager.DepthPageTransformer;
import com.haoyigou.hyg.view.viewpager.ViewPagerSlide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.shehuan.niv.NiceImageView;
import com.squareup.okhttp.Request;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.autolayout.utils.ScreenUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.ielse.view.imagewatcher.ImageWatcher;
import cn.dreamtobe.kpswitch.widget.KPSwitchFSPanelLinearLayout;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.MessageEvent;
import xbanner.XXBanner;

/**
 * Created by Witness on 2020-04-17
 * Describe: 社区
 */
public class CommunityFragment extends BaseFragment implements ImageWatcher.OnPictureLongPressListener, ImageWatcher.Loader {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.image_watcher)
    ImageWatcher imageWatcher;
    @BindView(R.id.panel_root)
    KPSwitchFSPanelLinearLayout panelRoot;
    @BindView(R.id.sendBtn)
    RelativeLayout sendBtn;
    @BindView(R.id.viewPager)
    ViewPagerSlide viewPager;


    private List<CommuBannerBean> bannerData = new ArrayList<>();

    private String sendType = "1";//圈子类型  1 推荐  2  买家秀
    private int pageNo = 1;
    private boolean canShow = true;//是否可以发布圈子

    private int onePageNo = 1;//推荐
    /**
     * 二个页面
     */
    private View oneView;//推荐
    private SmartRefreshLayout oneRefresh;
    private RecyclerView oneList;
    private List<CommunBean> data = new ArrayList<>();
    private View twoView;//买家秀
    private SmartRefreshLayout twoRefresh;
    private RecyclerView twoList;
    private List<CommunBean> twoData = new ArrayList<>();
    private com.zhy.adapter.recyclerview.CommonAdapter<CommunBean> twoAdapter;
    /**
     * viewPager相关
     */
    private List<View> viewListData = new ArrayList<>();//view数组
    private PagerAdapter pagerAdapter;

    private int showPosOne;
    private int showPosTwo;
    private OneAdapter oneAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community_layout, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        startProgressDialog("", getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        getOneListBanner();
        getTwoList();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        JzvdStd.releaseAllVideos();//在销毁活动时，关闭饺子视频
    }

    private void initView() {
        imageWatcher.setTranslucentStatus(ScreenUtils.getStatusBarHeight(getActivity()));
        imageWatcher.setErrorImageRes(R.mipmap.error_picture);
        imageWatcher.setOnPictureLongPressListener(this);
        imageWatcher.setLoader(this);

        viewPager.setScanScroll(false);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        LayoutInflater inflater = getLayoutInflater();
        oneView = inflater.inflate(R.layout.refresh_layout, null);
        twoView = inflater.inflate(R.layout.refresh_two_layout, null);
        initOneView(oneView);
        initTwoView(twoView);
        viewListData.add(oneView);
        viewListData.add(twoView);
        setViewPagerAdapter();

        tablayout.addTab(tablayout.newTab().setText("推荐"));
        tablayout.addTab(tablayout.newTab().setText("买家秀"));
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.getTabAt(0).select();
        tablayout.setTabIndicatorFullWidth(false);
        TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.textview_layout, null);
        textView.setTextSize(20);
        textView.setText("推荐");
        tablayout.getTabAt(0).setCustomView(textView);

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.textview_layout, null);
                textView.setTextSize(20);
                textView.setText(tab.getText());
                tab.setCustomView(textView);
                if (tab.getPosition() == 0) {
                    sendType = "1";
                    canShow = true;//推荐不需要一定关联商品
                    viewPager.setCurrentItem(0, true);
                } else {
                    sendType = "2";
                    viewPager.setCurrentItem(1, true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setViewPagerAdapter() {
        pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewListData.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewListData.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewListData.get(position));


                return viewListData.get(position);
            }
        };
        ViewPagerScroller scroller = new ViewPagerScroller(getActivity());
        scroller.setScrollDuration(1000);//时间越长，速度越慢。
        scroller.initViewPagerScroll(viewPager);
        viewPager.setAdapter(pagerAdapter);
    }


    /**
     * 推荐
     *
     * @param view
     */
    private void initOneView(View view) {
        oneList = view.findViewById(R.id.grid_view);
        oneRefresh = view.findViewById(R.id.refresh);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        oneList.setLayoutManager(manager);

        oneList.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                Jzvd jzvd = view.findViewById(R.id.videoplayer);
                if (jzvd != null && Jzvd.CURRENT_JZVD != null) {
                    if (Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                        Jzvd.releaseAllVideos();
                        videoNum(data.get(showPosOne).getId());
                    }
                }
            }
        });

        oneRefresh.setEnableLoadMore(true);
        oneRefresh.setRefreshHeader(new SmartHeader(getActivity()));
        oneRefresh.setHeaderHeight(60);
        oneRefresh.setRefreshFooter(new SmartFooter(getActivity()));
        oneRefresh.setFooterHeight(60);
        oneRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                onePageNo++;
                getOneListBanner();
                refreshLayout.finishLoadMore(1000);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                onePageNo = 1;
                getOneListBanner();
                refreshLayout.finishRefresh(1000);
            }
        });
    }


    /**
     * 推荐适配器
     */
    class OneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context context;

        private static final int ITEM_TYPE_ONE = 1;
        private static final int ITEM_TYPE_TWO = 2;

        public OneAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == ITEM_TYPE_ONE) {
                View convertView = LayoutInflater.from(context).inflate(R.layout.community_head_layout, parent, false);
                HeadViewoHolder headViewoHolder = new HeadViewoHolder(convertView);
                return headViewoHolder;
            } else {
                View convertView = LayoutInflater.from(context).inflate(R.layout.community_item_layout, parent, false);
                ViewHolder viewHolder = new ViewHolder(convertView);
                return viewHolder;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof HeadViewoHolder) {
                bindViewHolderOne((HeadViewoHolder) holder);
            } else if (holder instanceof ViewHolder) {
                bindViewHolderTwo((ViewHolder) holder, position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            if (bannerData.size() > 0) {
                return data.size() + 1;
            } else {
                return data.size();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (bannerData.size() > 0) {
                if (position == 0) {
                    return ITEM_TYPE_ONE;
                } else {
                    return ITEM_TYPE_TWO;
                }
            } else {
                return ITEM_TYPE_TWO;
            }
        }

        class HeadViewoHolder extends RecyclerView.ViewHolder {//头布局
            XXBanner headBanner;

            public HeadViewoHolder(@NonNull View itemView) {
                super(itemView);
                headBanner = itemView.findViewById(R.id.banner);
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            NineGridView picGridView;
            JzvdStd videoplayer;
            LinearLayout videoLayout;
            RoundImageView header;
            TextView content;
            TextView seeNum;
            ImageView downImage;
            TextView like;
            TextView comment;
            TextView time;
            TextView productLink;
            TextView name;
            ImageView likeImg;
            LinearLayout showComment;
            LinearLayout commentBtn;
            LinearLayout likeBtn;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                picGridView = itemView.findViewById(R.id.picGrid);
                videoplayer = itemView.findViewById(R.id.videoplayer);
                videoLayout = itemView.findViewById(R.id.videoLayout);
                header = itemView.findViewById(R.id.header);
                content = itemView.findViewById(R.id.content);
                seeNum = itemView.findViewById(R.id.seeNum);
                like = itemView.findViewById(R.id.like);
                comment = itemView.findViewById(R.id.comment);
                productLink = itemView.findViewById(R.id.productLink);
                name = itemView.findViewById(R.id.name);
                likeImg = itemView.findViewById(R.id.likeImg);
                showComment = itemView.findViewById(R.id.showComment);
                commentBtn = itemView.findViewById(R.id.commentBtn);
                likeBtn = itemView.findViewById(R.id.likeBtn);
                time = itemView.findViewById(R.id.time);
                downImage = itemView.findViewById(R.id.downImage);
            }
        }

        private void bindViewHolderOne(HeadViewoHolder headViewoHolder) {
            /** 设置模糊背景图片高度 */
            ViewGroup.LayoutParams param = headViewoHolder.headBanner.getLayoutParams();
            param.height = (int) (DisplayUtils.getScreenWidth(getActivity()) * 0.4);
            headViewoHolder.headBanner.setLayoutParams(param);
            initBanner(headViewoHolder.headBanner);
            headViewoHolder.headBanner.setAutoPlayAble(bannerData.size() > 1);
            headViewoHolder.headBanner.setIsClipChildrenMode(true);
            headViewoHolder.headBanner.setData(R.layout.banner_layout, bannerData, null);
        }

        private void bindViewHolderTwo(ViewHolder holder, int position) {
            CommunBean item;
            int mposition;
            if (bannerData.size() > 0) {
                mposition = position - 1;
                item = data.get(position - 1);
            } else {
                item = data.get(position);
                mposition = position;
            }
            Glide.with(getActivity()).load(item.getHeader()).asBitmap().placeholder(R.drawable.headpic)
                    .into(holder.header);
            holder.content.setText(item.getText());
            holder.content.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    //这个回调会调用多次，获取完行数记得注销监听
                    holder.content.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (holder.content.getLineCount() < 3) {
                        holder.downImage.setVisibility(View.GONE);
                    } else {
                        holder.downImage.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
            });
            holder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.content.getMaxLines() == 50) {
                        holder.content.setMaxLines(3);
                        Glide.with(getActivity()).load(R.mipmap.down_image).into(holder.downImage);
                    } else {
                        holder.content.setMaxLines(50);
                        Glide.with(getActivity()).load(R.mipmap.up_image).into(holder.downImage);
                    }
                }
            });
            holder.downImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.content.getMaxLines() == 50) {
                        holder.content.setMaxLines(3);
                        Glide.with(getActivity()).load(R.mipmap.down_image).into(holder.downImage);
                    } else {
                        holder.content.setMaxLines(50);
                        Glide.with(getActivity()).load(R.mipmap.up_image).into(holder.downImage);
                    }
                }
            });
            holder.seeNum.setText(String.format("%s次观看", item.getWatchTimes()));
            holder.like.setText(item.getTips());
            holder.comment.setText(item.getComment());
            holder.time.setText(item.getCreateTime());
            if (item.getProduct() != null) {
                holder.productLink.setVisibility(View.VISIBLE);
                holder.productLink.setText(item.getProduct().getName());
            } else {
                holder.productLink.setVisibility(View.GONE);
            }
            holder.productLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getProduct().getJumpAdress() != null) {
                        Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                        intent.putExtra("url", item.getProduct().getJumpAdress());
                        intent.putExtra("all", true);
                        startActivity(intent);
                    }
                }
            });
            if (item.getName() != null) {
                if (item.getName().startsWith("1") && item.getName().length() == 11) {//手机号隐藏中间四位
                    holder.name.setText(item.getName().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                } else {
                    holder.name.setText(item.getName());
                }
            }
            if (item.getPictures() != null && !item.getPictures().equals("")) {//显示图片
                holder.videoLayout.setVisibility(View.GONE);
                holder.picGridView.setVisibility(View.VISIBLE);
                /** 图片 */
                if (Arrays.asList(item.getPictures().split(",")).size() == 1) {
                    holder.picGridView.setSingleImageSize((int) (DisplayUtils.getScreenWidth(getActivity()) * 0.86), (int) (DisplayUtils.getScreenWidth(getActivity()) * 0.43));
                }
                holder.picGridView.setOnImageClickListener(new NineGridView.OnImageClickListener() {
                    @Override
                    public void onImageClick(int position, View view) {
                        videoNum(item.getId());
                        imageWatcher.show((ImageView) view, holder.picGridView.getImageViews(), Arrays.asList(item.getPictures().split(",")));
                    }
                });
                holder.picGridView.setAdapter(new NineImageAdapter(getActivity(), Arrays.asList(item.getPictures().split(","))));

            } else {//显示视频
                holder.videoLayout.setVisibility(View.VISIBLE);
                holder.picGridView.setVisibility(View.GONE);

                /** 视频 */
                holder.videoplayer.setUpEveryDay(
                        item.getVideoUrl(),
                        "", 0, "id" + item.getId());
                Glide.with(getActivity())
                        .load(item.getVideoImg())
                        .centerCrop()
                        .into(holder.videoplayer.thumbImageView);
                holder.videoplayer.positionInList = mposition;
                showPosOne = mposition;
                holder.videoplayer.productName.setText("");
            }


            if (item.getIsTip().equals("0")) {
                Glide.with(getActivity()).load(R.mipmap.like_no).asBitmap().into(holder.likeImg);
            } else {
                Glide.with(getActivity()).load(R.mipmap.like_yes).asBitmap().into(holder.likeImg);
            }

            holder.showComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentDialog(mposition, item.getId(), item.getComment());

                }
            });

            holder.commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentDialog(mposition, item.getId(), item.getComment());
                }
            });

            holder.likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StateMessage.IS_LOGIN) {    //判断是否登陆
                        if (item.getIsTip().equals("0")) {
                            likeOne(mposition, item.getId());
                        }
                    } else {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                }
            });
        }
    }


    /**
     * 买家秀
     *
     * @param view
     */
    private void initTwoView(View view) {
        twoList = view.findViewById(R.id.list);
        twoRefresh = view.findViewById(R.id.refresh);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        twoList.setLayoutManager(manager);
        twoList.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                Jzvd jzvd = view.findViewById(R.id.videoplayer);
                if (jzvd != null && Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl() != null &&
                        jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                    if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                        Jzvd.releaseAllVideos();
                        videoNum(twoData.get(showPosTwo).getId());
                    }
                }
            }
        });

        twoRefresh.setRefreshHeader(new SmartHeader(getActivity()));
        twoRefresh.setHeaderHeight(60);
        twoRefresh.setRefreshFooter(new SmartFooter(getActivity()));
        twoRefresh.setFooterHeight(60);
        twoRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo++;
                getTwoList();
//                refreshLayout.finishLoadMore(1000);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                getTwoList();
                refreshLayout.finishRefresh(1000);
            }
        });

        setTwoAdapter();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 轮播图
     *
     * @param banner
     */
    private void initBanner(XXBanner banner) {
        banner.setOnItemClickListener(new XXBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XXBanner banner, Object model, View view, int position) {
                if (bannerData.get(position).getUrl() != null && bannerData.get(position).getUrl().length() > 8) {
                    Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                    intent.putExtra("url", bannerData.get(position).getUrl());
                    intent.putExtra("all", true);
                    startActivity(intent);
                }
            }
        });
        //加载广告图片
        banner.loadImage((banner1, model, view, position) -> {
            NiceImageView imageView = view.findViewById(R.id.image);
            Glide.with(getActivity()).load(bannerData.get(position).getPic()).into(imageView);
        });

    }

    /**
     * 设置买家秀列表
     */
    private void setTwoAdapter() {
        if (twoAdapter != null) {
            twoAdapter.notifyDataSetChanged();
            return;
        }
        twoAdapter = new com.zhy.adapter.recyclerview.CommonAdapter<CommunBean>(getActivity(), R.layout.community_item_layout, twoData) {
            @Override
            protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, CommunBean item, int position) {
                final NineGridView picGridView = holder.getConvertView().findViewById(R.id.picGrid);
                JzvdStd videoplayer = holder.getConvertView().findViewById(R.id.videoplayer);
                LinearLayout videoLayout = holder.getConvertView().findViewById(R.id.videoLayout);
                RoundImageView header = holder.getConvertView().findViewById(R.id.header);
                Glide.with(getActivity()).load(item.getHeader()).asBitmap().placeholder(R.drawable.headpic)
                        .into(header);
                TextView content = holder.getConvertView().findViewById(R.id.content);
                content.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        //这个回调会调用多次，获取完行数记得注销监听
                        content.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (content.getLineCount() < 3) {
                            holder.getView(R.id.downImage).setVisibility(View.GONE);
                        } else {
                            holder.getView(R.id.downImage).setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                });
                content.setText(item.getText());
                content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (content.getMaxLines() == 50) {
                            content.setMaxLines(3);
                            Glide.with(getActivity()).load(R.mipmap.down_image).into((ImageView) holder.getView(R.id.downImage));
                        } else {
                            content.setMaxLines(50);
                            Glide.with(getActivity()).load(R.mipmap.up_image).into((ImageView) holder.getView(R.id.downImage));
                        }
                    }
                });
                holder.getView(R.id.downImage).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (content.getMaxLines() == 50) {
                            content.setMaxLines(3);
                            Glide.with(getActivity()).load(R.mipmap.down_image).into((ImageView) holder.getView(R.id.downImage));
                        } else {
                            content.setMaxLines(50);
                            Glide.with(getActivity()).load(R.mipmap.up_image).into((ImageView) holder.getView(R.id.downImage));
                        }
                    }
                });

                holder.setText(R.id.seeNum, String.format("%s次观看", item.getWatchTimes()));
                holder.setText(R.id.like, item.getTips());
                holder.setText(R.id.comment, item.getComment());
                holder.setText(R.id.time, item.getCreateTime());
                if (item.getProduct() != null) {
                    holder.getView(R.id.productLink).setVisibility(View.VISIBLE);
                    holder.setText(R.id.productLink, item.getProduct().getName());
                } else {
                    holder.getView(R.id.productLink).setVisibility(View.GONE);
                }
                holder.getView(R.id.productLink).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getProduct().getJumpAdress() != null) {
                            Intent intent = new Intent(getActivity(), HomeWebViewAct.class);
                            intent.putExtra("url", item.getProduct().getJumpAdress());
                            intent.putExtra("all", true);
                            startActivity(intent);
                        }
                    }
                });

                if (item.getName().startsWith("1") && item.getName().length() == 11) {//手机号隐藏中间四位
                    holder.setText(R.id.name, item.getName().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                } else {
                    holder.setText(R.id.name, item.getName());
                }

                if (item.getVideoUrl().equals("")) {//显示图片
                    videoLayout.setVisibility(View.GONE);
                    picGridView.setVisibility(View.VISIBLE);
                    /** 图片 */
                    if (Arrays.asList(item.getPictures().split(",")).size() == 1) {
                        picGridView.setSingleImageSize((int) (DisplayUtils.getScreenWidth(getActivity()) * 0.86), (int) (DisplayUtils.getScreenWidth(getActivity()) * 0.43));
                    }
                    picGridView.setOnImageClickListener(new NineGridView.OnImageClickListener() {
                        @Override
                        public void onImageClick(int position, View view) {
                            videoNum(item.getId());
                            imageWatcher.show((ImageView) view, picGridView.getImageViews(), Arrays.asList(item.getPictures().split(",")));
                        }
                    });
                    picGridView.setAdapter(new NineImageAdapter(getActivity(), Arrays.asList(item.getPictures().split(","))));

                } else {//显示视频
                    videoLayout.setVisibility(View.VISIBLE);
                    picGridView.setVisibility(View.GONE);

                    /** 视频 */
                    videoplayer.setUpEveryDay(
                            item.getVideoUrl(),
                            "", 0, "id" + item.getId());
                    Glide.with(getActivity())
                            .load(item.getVideoImg())
                            .centerCrop()
                            .into(videoplayer.thumbImageView);
                    videoplayer.positionInList = position;
                    showPosTwo = position;
                    videoplayer.productName.setText("");
                }


                if (item.getIsTip().equals("0")) {
                    Glide.with(getActivity()).load(R.mipmap.like_no).asBitmap().into((ImageView) holder.getView(R.id.likeImg));
                } else {
                    Glide.with(getActivity()).load(R.mipmap.like_yes).asBitmap().into((ImageView) holder.getView(R.id.likeImg));
                }

                holder.getView(R.id.showComment).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commentDialog(position, item.getId(), item.getComment());

                    }
                });

                holder.getView(R.id.commentBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commentDialog(position, item.getId(), item.getComment());
                    }
                });

                holder.getView(R.id.likeBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StateMessage.IS_LOGIN) {    //判断是否登陆
                            if (item.getIsTip().equals("0")) {
                                likeTwo(position, item.getId());
                            }
                        } else {
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                    }
                });
            }
        };
        twoList.setAdapter(twoAdapter);
    }

    @Override
    public void load(Context context, String url, ImageWatcher.LoadCallback lc) {
        Glide.with(context).load(url).asBitmap().into(new GlideSimpleTarget(lc));
    }

    @Override
    public void onPictureLongPress(ImageView v, String url, int pos) {

    }

    /**
     * 评论弹窗
     */
    private int mpageNo = 1;
    private List<CommentBean> commentData = new ArrayList<>();
    private com.zhy.adapter.recyclerview.CommonAdapter<CommentBean> commentAdapter;
    private SmartRefreshLayout refreshComment;
    private TextView commentNum;
    private EditText sendEdit;

    private void commentDialog(int position, String id, String comment) {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.comment_dialog, null);
        refreshComment = v.findViewById(R.id.refresh);
        commentNum = v.findViewById(R.id.commentNum);
        commentNum.setText(comment);
        TextView sendBtn = v.findViewById(R.id.sendBtn);
        RecyclerView commentList = v.findViewById(R.id.commentList);
        LinearLayout commentBtn = v.findViewById(R.id.commentBtn);
        sendEdit = v.findViewById(R.id.sendEdit);
        builder.setView(v);
        builder.setCancelable(true);
        final Dialog noticeDialog = builder.create();
        noticeDialog.getWindow().setGravity(Gravity.BOTTOM);
        noticeDialog.getWindow().setWindowAnimations(R.style.anim_menu_bottombar);
        noticeDialog.show();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        commentList.setLayoutManager(manager);
        commentData.clear();
        getCommentList(id, sendType, mpageNo);

        refreshComment.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mpageNo++;
                getCommentList(id, sendType, mpageNo);
                refreshLayout.finishLoadMore(1000);
            }
        });

        commentAdapter = new com.zhy.adapter.recyclerview.CommonAdapter<CommentBean>(getActivity(), R.layout.comment_item_layout, commentData) {
            @Override
            protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, CommentBean item, int position) {
                Glide.with(getActivity()).load(item.getHeader()).asBitmap().placeholder(R.drawable.headpic)
                        .into((ImageView) holder.getView(R.id.header));
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.content, item.getText());
                holder.setText(R.id.likeNum, item.getTips());
                if (item.getIsTip().equals("0")) {
                    Glide.with(getActivity()).load(R.mipmap.like_no).asBitmap().into((ImageView) holder.getView(R.id.likeImage));
                } else {
                    Glide.with(getActivity()).load(R.mipmap.like_yes).asBitmap().into((ImageView) holder.getView(R.id.likeImage));
                }
                holder.getView(R.id.likeBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getIsTip().equals("0")) {
                            likeComment(position, id, item.getId());
                        }
                    }
                });
            }
        };
        //评论
        sendBtn.setOnClickListener(v12 -> {
            if (StateMessage.IS_LOGIN) {    //判断是否登陆
                if (TextUtils.isEmpty(sendEdit.getText())) {
                    ToastUtils.showToast(getActivity(), "请先输入评论内容");
                } else {
                    startProgressDialog("", getActivity());
                    if (sendType.equals("1")) {
                        saveOneComment(position, id, sendEdit.getText().toString());
                    } else {
                        saveTwoComment(position, id, sendEdit.getText().toString());
                    }
                }
            } else {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        //评论
        sendEdit.setOnEditorActionListener((v1, actionId, event) -> {
            if (StateMessage.IS_LOGIN) {    //判断是否登陆
                if (TextUtils.isEmpty(sendEdit.getText())) {
                    ToastUtils.showToast(getActivity(), "请先输入评论内容");
                } else {
                    startProgressDialog("", getActivity());
                    if (sendType.equals("1")) {
                        saveOneComment(position, id, sendEdit.getText().toString());
                    } else {
                        saveTwoComment(position, id, sendEdit.getText().toString());
                    }
                }
            } else {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
            return false;
        });
        commentList.setAdapter(commentAdapter);
        WindowManager.LayoutParams layoutParams = noticeDialog.getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = DisplayUtils.getScreenWidth(getActivity());
        noticeDialog.getWindow().setAttributes(layoutParams);
    }

    @OnClick(R.id.sendBtn)
    public void onViewClicked() {
        if (StateMessage.IS_LOGIN) {    //判断是否登陆
            if (sendType.equals("2")) {
                if (canShow) {
                    Intent intent = new Intent(getActivity(), SendCircleActivity.class);
                    intent.putExtra("sendType", sendType);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(getActivity(), "您还没有关联商品,无法发布买家秀");
                }
            } else {
                Intent intent = new Intent(getActivity(), SendCircleActivity.class);
                intent.putExtra("sendType", sendType);
                startActivity(intent);
            }
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }


    /**
     * 推荐列表
     */
    private void getOneList() {
        Map<String, Object> params = new HashMap<>();
        params.put("pageSize", 15);
        params.put("pageNo", onePageNo);
        params.put("distributorId", SharedPreferencesUtils.getInstance().getString("distributorId", "1"));
        HttpClient.post(HttpClient.ONELIST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
                if (oneRefresh != null) {
                    oneRefresh.finishRefresh();
                }
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    if (onePageNo == 1) {
                        data.clear();
                        data.addAll(JSON.parseArray(object.getString("data"), CommunBean.class));
                    } else {
                        data.addAll(JSON.parseArray(object.getString("data"), CommunBean.class));
                    }
//                    setAdapter();
                    if (oneAdapter == null) {
                        oneAdapter = new OneAdapter(getActivity());
                        oneList.setAdapter(oneAdapter);
                        oneAdapter.notifyDataSetChanged();
                    } else {
                        oneAdapter.notifyDataSetChanged();
                    }
                }
            }
        }, getActivity());
    }

    /**
     * 推荐列表轮播图
     */
    private void getOneListBanner() {
        Map<String, Object> params = new HashMap<>();
        params.put("parentLocation", "124");
        HttpClient.post(HttpClient.ONEBANNER, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                if (oneRefresh != null) {
                    oneRefresh.finishRefresh();
                }
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    bannerData.clear();
                    bannerData.addAll(JSON.parseArray(object.getString("data"), CommuBannerBean.class));
                    getOneList();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                bannerData.clear();
                getOneList();
            }
        }, getActivity());
    }


    /**
     * 买家秀列表
     */
    private void getTwoList() {
        Map<String, Object> params = new HashMap<>();
        params.put("pageSize", 15);
        params.put("pageNo", pageNo);
        params.put("parentLocation", "125");
        params.put("distributorId", SharedPreferencesUtils.getInstance().getString("distributorId", "1"));
        HttpClient.post(HttpClient.TWOLIST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
                if (twoRefresh != null) {
                    twoRefresh.finishRefresh();
                }
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    if (pageNo == 1) {
                        twoData.clear();
                        twoData.addAll(JSON.parseArray(object.getString("data"), CommunBean.class));
                        setTwoAdapter();
                    } else {
                        if (JSON.parseArray(object.getString("data"), CommunBean.class).size() > 0) {
                            twoData.addAll(JSON.parseArray(object.getString("data"), CommunBean.class));
                            setTwoAdapter();
                            twoRefresh.finishLoadMore();
                        } else {
                            pageNo--;
                            twoRefresh.finishLoadMore();
                            ToastUtils.showToast(getActivity(), "没有更多啦...");
                        }
                    }

                    if (object.getString("canShow").equals("1")) {
                        canShow = true;
                    } else {
                        canShow = false;
                    }
                }
            }
        }, getActivity());
    }


    /**
     * 评论推荐内容
     */
    private void saveOneComment(int position, String id, String text) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("text", text);
        params.put("distributorId", SharedPreferencesUtils.getInstance().getString("distributorId", "1"));
        HttpClient.post(HttpClient.SAVEONECOMMENT, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
                if (oneRefresh != null) {
                    oneRefresh.finishRefresh();
                }
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    data.get(position).setComment(String.valueOf(Integer.valueOf(data.get(position).getComment()) + 1));
                    oneAdapter.notifyItemChanged(position + 1);
                    commentNum.setText(data.get(position).getComment());
                    getCommentList(id, sendType, 1);
                    sendEdit.setText("");
                }
            }
        }, getActivity());
    }

    /**
     * 评论买家秀内容
     */
    private void saveTwoComment(int position, String id, String text) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("text", text);
        params.put("distributorId", SharedPreferencesUtils.getInstance().getString("distributorId", "1"));
        HttpClient.post(HttpClient.SAVETWOCOMMENT, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
                if (twoRefresh != null) {
                    twoRefresh.finishRefresh();
                }
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    twoData.get(position).setComment(String.valueOf(Integer.valueOf(twoData.get(position).getComment()) + 1));
                    twoAdapter.notifyItemChanged(position);
                    commentNum.setText(twoData.get(position).getComment());
                    getCommentList(id, sendType, 1);
                    sendEdit.setText("");
                }
            }
        }, getActivity());
    }


    /**
     * 推荐点赞
     */
    private void likeOne(int position, String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("distributorId", SharedPreferencesUtils.getInstance().getString("distributorId", "1"));
        HttpClient.post(HttpClient.ONELIKE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
                if (oneRefresh != null) {
                    oneRefresh.finishRefresh();
                }
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    data.get(position).setIsTip("1");
                    data.get(position).setTips(String.valueOf(Integer.valueOf(data.get(position).getTips()) + 1));
                    oneAdapter.notifyItemChanged(position + 1);
                }
            }
        }, getActivity());
    }


    /**
     * 买家秀点赞
     */
    private void likeTwo(int position, String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("distributorId", SharedPreferencesUtils.getInstance().getString("distributorId", "1"));
        HttpClient.post(HttpClient.TWOLIKE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
                if (twoRefresh != null) {
                    twoRefresh.finishRefresh();
                }
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    twoData.get(position).setIsTip("1");
                    twoData.get(position).setTips(String.valueOf(Integer.valueOf(twoData.get(position).getTips()) + 1));
                    twoAdapter.notifyItemChanged(position);
                }
            }
        }, getActivity());
    }


    /**
     * 点赞评论
     */
    private void likeComment(int position, String topicId, String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("distributorId", SharedPreferencesUtils.getInstance().getString("distributorId", "1"));
        HttpClient.post(HttpClient.COMMENTLIKE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    commentData.get(position).setIsTip("1");
                    commentData.get(position).setTips(String.valueOf(Integer.valueOf(commentData.get(position).getTips()) + 1));
                    commentAdapter.notifyItemChanged(position);
                }
            }
        }, getActivity());
    }

    /**
     * 评论列表
     */
    private void getCommentList(String id, String type, int pageNo) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("type", type);
        params.put("pageSize", "20");
        params.put("pageNo", pageNo);
        params.put("distributorId", SharedPreferencesUtils.getInstance().getString("distributorId", ""));
        HttpClient.post(HttpClient.COMMENTLIST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
                if (refreshComment != null) {
                    refreshComment.finishRefresh();
                }
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {
                    if (mpageNo == 1) {
                        commentData.clear();
                        commentData.addAll(JSON.parseArray(object.getString("data"), CommentBean.class));
                        commentAdapter.notifyDataSetChanged();
                    } else {
                        if (JSON.parseArray(object.getString("data"), CommentBean.class).size() > 0) {
                            commentData.addAll(JSON.parseArray(object.getString("data"), CommentBean.class));
                            commentAdapter.notifyDataSetChanged();
                            refreshComment.finishLoadMore();
                        } else {
                            refreshComment.finishLoadMore();
                            mpageNo--;
                        }
                    }

                }
            }
        }, getActivity());
    }

    /**
     * 视频播放次数+1
     */
    private void videoNum(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("type", sendType);
        HttpClient.post(HttpClient.VIDEONUM, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                stopProgressDialog();
                JSONObject object = JSON.parseObject(content);
                if (HttpClient.RET_SUCCESS_CODE.equals(object.getString("status"))) {

                }
            }
        }, getActivity());
    }
}
