package com.google.wash.entity;

import com.google.wash.R;

import android.content.Context;  
import android.text.Editable;  
import android.text.TextWatcher;  
import android.util.AttributeSet;  
import android.view.KeyEvent;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;  
import android.widget.ImageView;  
import android.widget.LinearLayout;  
  
  
/** 
 *  
 * 〈一句话功能简述〉<br> 
 * 〈功能详细描述〉 简密输入框 
 */  
public class Password extends LinearLayout { 
	private Context context;
    private EditText mEditText;  
    private ImageView oneTextView;  
    private ImageView twoTextView;  
    private ImageView threeTextView;  
    private ImageView fourTextView;  
    private ImageView fiveTextView;  
    private ImageView sixTextView;  
    LayoutInflater inflater;  
    ImageView[] imageViews;  
    View contentView;  
  
    public Password(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        this.context = context;
        inflater = LayoutInflater.from(context);  
        builder = new StringBuilder();  
        initWidget();  
    }  
  
    private void initWidget() {  
        contentView = inflater.inflate(R.layout.password, null);  
        mEditText = (EditText) contentView  
                .findViewById(R.id.sdk2_pwd_edit_simple);  
        oneTextView = (ImageView) contentView  
                .findViewById(R.id.sdk2_pwd_one_img);  
        twoTextView = (ImageView) contentView  
                .findViewById(R.id.sdk2_pwd_two_img);  
        fourTextView = (ImageView) contentView  
                .findViewById(R.id.sdk2_pwd_four_img);  
        fiveTextView = (ImageView) contentView  
                .findViewById(R.id.sdk2_pwd_five_img);  
        sixTextView = (ImageView) contentView  
                .findViewById(R.id.sdk2_pwd_six_img);  
        threeTextView = (ImageView) contentView  
                .findViewById(R.id.sdk2_pwd_three_img);  
        LinearLayout.LayoutParams lParams = new LayoutParams(  
                LinearLayout.LayoutParams.MATCH_PARENT,  
                LinearLayout.LayoutParams.WRAP_CONTENT);  
        mEditText.addTextChangedListener(mTextWatcher);  
        mEditText.setOnKeyListener(keyListener);  
        imageViews = new ImageView[] { oneTextView, twoTextView, threeTextView,  
                fourTextView, fiveTextView, sixTextView };  
        this.addView(contentView, lParams);  
    }  
  
    TextWatcher mTextWatcher = new TextWatcher() {  
  
        @Override  
        public void onTextChanged(CharSequence s, int start, int before,  
                int count) {  
  
        }  
  
        @Override  
        public void beforeTextChanged(CharSequence s, int start, int count,  
                int after) {  
  
        }  
  
        @Override  
        public void afterTextChanged(Editable s) {  
  
            if (s.toString().length() == 0) {  
                return;  
            }  
  
            if (builder.length() < 6) {  
                builder.append(s.toString());  
                getTextValue();  
            }  
            s.delete(0, s.length());  
        }  
  
    };  
  
    OnKeyListener keyListener = new OnKeyListener() {  
  
        @Override  
        public boolean onKey(View v, int keyCode, KeyEvent event) {  
  
            if (keyCode == KeyEvent.KEYCODE_DEL  
                    && event.getAction() == KeyEvent.ACTION_UP) {  
                delTextValue();  
                return true;  
            }  
            return false;  
        }  
    };  
  
    public String getTextValue() {  
  
        String str = builder.toString();  
        int len = str.length();  
  
        if (len <= 6&&len>0) {  
            imageViews[len - 1].setVisibility(View.VISIBLE);  
        }  
        if (len == 6) {  
            if (mListener != null) {  
                mListener.onNumCompleted(str);  
            }   
            InputMethodManager imm = (InputMethodManager) context
    				.getSystemService(Context.INPUT_METHOD_SERVICE); 
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
        }  
        return str;
    }  
  
    private void delTextValue() {  
        String str = builder.toString();  
        int len = str.length();  
        if (len == 0) {  
            return;  
        }  
        if (len > 0 && len <= 6) {  
            builder.delete(len - 1, len);  
        }  
        imageViews[len - 1].setVisibility(View.INVISIBLE);  
        ;  
    }  
  
    StringBuilder builder;  
  
    public interface SecurityEditCompleListener {  
        public void onNumCompleted(String num);  
    }  
  
    public SecurityEditCompleListener mListener;  
  
    public void setSecurityEditCompleListener(  
            SecurityEditCompleListener mListener) {  
        this.mListener = mListener;  
    }  
  
    public void clearSecurityEdit() {  
        if (builder != null) {  
            if (builder.length() == 6) {  
                builder.delete(0, 6);  
            }  
        }  
        for (ImageView tv : imageViews) {  
            tv.setVisibility(View.INVISIBLE);  
        }  
  
    }  
  
    public EditText getSecurityEdit() {  
        return this.mEditText;  
    }  
    
    
}  