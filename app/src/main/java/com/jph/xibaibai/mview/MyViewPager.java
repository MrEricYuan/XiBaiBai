package com.jph.xibaibai.mview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
public class MyViewPager extends ViewPager {  
	boolean flag;
	  
    public MyViewPager(Context context) {  
        super(context);  
    }   
      
    public MyViewPager(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
    
    @Override
    protected void onPageScrolled(int arg0, float arg1, int arg2) {
    	if(0==arg2){
    		flag = false;
    	}else{
    		flag = true;
    	}
    	super.onPageScrolled(arg0, arg1, arg2);
    }
    @Override  
    public boolean dispatchTouchEvent(MotionEvent ev) {  
        getParent().requestDisallowInterceptTouchEvent(flag);
        return super.dispatchTouchEvent(ev);  
    }  
}

