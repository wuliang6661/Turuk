package com.haoyigou.hyg.wxapi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.haoyigou.hyg.R;
import com.haoyigou.hyg.entity.Constants;
import com.haoyigou.hyg.view.onekeyshare.OnekeyShare;

import cn.sharesdk.wechat.friends.Wechat;


public class ImagePagerView extends PopupWindow implements ViewPager.OnPageChangeListener {
	Context context;
	View view;
	
	/** 
     * ViewPager 
     */  
    private ViewPager viewPager;  
      
    /** 
     *
     */  
    private ImageView[] tips;  
      
    /** 
     *
     */  
    private ImageView[] mImageViews;  
    private String[] imgIdArray;
    private Bitmap[] bitmaps;
    private String images;
    TextView number;
    LinearLayout saveBox;
    TextView save;
    TextView cancel;
    TextView share;
    int currentPage=0;
    
    public void init(int current,String images) {
    	 this.images = images;
    	 this.currentPage = current;
    	 view = ((Activity)context).getLayoutInflater().inflate(R.layout.image_pager, null);
	     this.setWidth(LayoutParams.MATCH_PARENT);
	     this.setHeight(LayoutParams.MATCH_PARENT);
	     this.setBackgroundDrawable(new BitmapDrawable());
	     this.setFocusable(true);
	     this.setOutsideTouchable(true);
	     this.setContentView(view);
	     
	     ViewGroup group = (ViewGroup)view.findViewById(R.id.viewGroup);
	        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
	        number = (TextView)view.findViewById(R.id.number);
	        //
	       imgIdArray = images.split(",");
	       number.setText((current+1)+"/"+imgIdArray.length);

	        tips = new ImageView[imgIdArray.length];
	        for(int i=0; i<tips.length; i++){  
	            ImageView imageView = new ImageView(context);  
	            imageView.setLayoutParams(new LayoutParams(10,10));  
	            tips[i] = imageView;  
	            if(i == 0){  
//	                tips[i].setBackgroundResource(R.drawable.);  
	            }else{  
//	                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);  
	            }  
	              
	            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
	                    LayoutParams.WRAP_CONTENT));  
	            layoutParams.leftMargin = 5;  
	            layoutParams.rightMargin = 5;  
	            group.addView(imageView, layoutParams);  
	        }
	        
	      //
	        mImageViews = new ImageView[imgIdArray.length];
	        for(int i=0; i<mImageViews.length; i++){  
	            ImageView imageView = new ImageView(context);  
	            mImageViews[i] = imageView;  
	            imageView.setOnLongClickListener(new OnLongClickListener() {	
					@Override
					public boolean onLongClick(View arg0) {
						saveBox.setVisibility(View.VISIBLE);
						return false;
					}
				});
	        }  
	         new  PicThread().start();

	        viewPager.setAdapter(new ImageAdapter());  

	        viewPager.setOnPageChangeListener(this);  

	        viewPager.setCurrentItem(current); 
	        saveBox = (LinearLayout)view.findViewById(R.id.saveBox);
	        save = (TextView)view.findViewById(R.id.save);
	        save.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View arg0) {
					new Thread(getImages).start();
					Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
				}
			});
	        cancel = (TextView)view.findViewById(R.id.cancel);
	        cancel.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					saveBox.setVisibility(View.GONE);
				}
			});
	        share = (TextView)view.findViewById(R.id.share);
	        share.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					final OnekeyShare oks = new OnekeyShare();
			        oks.setImageUrl(Constants.domain+imgIdArray[currentPage]);
//			        oks.setUrl(Constants.domain+shareUrl);
//			        oks.setText(shareContent);
			        oks.setSilent(true);
//			        oks.setDialogMode();
			        oks.disableSSOWhenAuthorize();
			        
			            oks.setPlatform(Wechat.NAME);
			        
			        //
			        // http://wiki.mob.com/Android_%E5%BF%AB%E6%8D%B7%E5%88%86%E4%BA%AB#.E4.B8.BA.E4.B8.8D.E5.90.8C.E5.B9.B3.E5.8F.B0.E5.AE.9A.E4.B9.89.E5.B7.AE.E5.88.AB.E5.8C.96.E5.88.86.E4.BA.AB.E5.86.85.E5.AE.B9
//			        oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());
			        oks.show(context);
					
				}
			});
	}
    
    class PicThread extends Thread{	
		@Override
		public void run() {
			try {	
				String[] imgIdArray = images.split(",");
				bitmaps = new Bitmap[imgIdArray.length];
				for(int i=0; i<mImageViews.length; i++){
					URL url = new URL(Constants.domain+imgIdArray[i]);
					bitmaps[i] = BitmapFactory.decodeStream(url.openStream());
				}
				handler.sendEmptyMessage(2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.run();
		}
    }
    
    Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
								
			}else if(msg.what==2){
				for(int i=0; i<mImageViews.length; i++){  
					mImageViews[i].setImageBitmap(bitmaps[i]);
				}
				
			}
		}
    };
    

    Runnable getImages = new Runnable(){
        public void run() {
            try {
            	
            	String URI_PREFIX = Environment.getExternalStorageDirectory()+"/meipingmi/";  
                	URL u = new URL(Constants.domain+imgIdArray[currentPage]);
                    HttpURLConnection c = (HttpURLConnection) u.openConnection();
                    c.setRequestMethod("GET");
                    c.setDoOutput(true);
                    c.connect();
                    String filename = System.currentTimeMillis()+".jpg";
                    String filepath = URI_PREFIX+filename;
                    File path = new File(URI_PREFIX);
                    if(!path.exists()){
                    	path.mkdir();
                    }
                    File file = new File(filepath);
                    file.createNewFile();
					FileOutputStream f = new FileOutputStream(file);
					
                    InputStream in = c.getInputStream();
                    
                    byte[] buffer = new byte[1024];
                    int len1 = 0;
    			    while ( (len1 = in.read(buffer)) > 0 ) {
    			        f.write(buffer,0, len1);
    			    }
    			    f.close();
                	
                
            }catch (Exception e) {
//            	Log.e(TAG, "could not download and save IMAGE file", e);
            	e.printStackTrace();
            }
        }
    };
	
	public ImagePagerView(Context context){
		super(context);
		this.context = context;
		
	}
    

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setImageBackground(arg0);  
	}
	
	/** 
     *
     * @param selectItems 
     */  
    private void setImageBackground(int selectItems){  
    	currentPage = selectItems;
    	number.setText((selectItems+1)+"/"+mImageViews.length);
//        for(int i=0; i<tips.length; i++){  
//            if(i == selectItems){  
//                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);  
//            }else{  
//                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);  
//            }  
//        }  
    }  
	
	public class ImageAdapter extends PagerAdapter {
        @Override  
        public int getCount() {  
            return mImageViews.length;  
        }  
  
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            return arg0 == arg1;  
        }  
  
        @Override  
        public void destroyItem(View container, int position, Object object) {  
            ((ViewPager)container).removeView(mImageViews[position]);  
              
        }  
  
        /** 
         *
         */  
        @Override  
        public Object instantiateItem(View container, int position) {  
        	currentPage = position;
            ((ViewPager)container).addView(mImageViews[position], 0);
            return mImageViews[position];  
        }      
          
    }  
	
}
