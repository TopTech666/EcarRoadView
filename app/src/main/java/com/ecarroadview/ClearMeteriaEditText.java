package com.ecarroadview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;

import materialedittext.MaterialEditText;


public class ClearMeteriaEditText extends MaterialEditText {
    /**
     * 是否开启6222 2316 1256和138 8888 8888 银行卡号和电话号码显示方式 默认显示银行卡号显示方式
     */
    public boolean showType;
    /**
     * 开启电话号码显示方式
     */
    public boolean showMobileType;

    public ClearMeteriaEditText(Context context) {
        this(context, null);
    }

    public ClearMeteriaEditText(Context context, AttributeSet attrs) {
        // 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearMeteriaEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setIsShowPhone(context, attrs, defStyle);
        init();
        initHint(context, attrs, defStyle);
    }

    private void initHint(Context context, AttributeSet attrs, int defStyle) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ClearTextPhoneOrBank, defStyle, 0);
        boolean hintNormal = typedArray.getBoolean(R.styleable.ClearTextPhoneOrBank_hint_normal, false);
        // 设置hint
//        if (!hintNormal) {
        DataFormatUtil.setHintSize(context, this, 13);
//        }
    }

    //设置是否是电话类型 ，是否显示
    private void setIsShowPhone(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ClearTextPhoneOrBank, defStyle, 0);
        showMobileType = a.getBoolean(R.styleable.ClearTextPhoneOrBank_is_phone, false);
        showType = a.getBoolean(R.styleable.ClearTextPhoneOrBank_is_show, false);
        a.recycle();
    }


    private void init() {
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(new BankAndPhoneTextWatch(this, showType, showMobileType));
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
     * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    this.setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }


}
