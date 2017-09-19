package com.ecarroadview;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/*
 *===============================================
 *
 * 文件名:${type_name}
 *
 * 描述:  银行卡号或手机号码分割
 *
 * 作者:
 *
 * 版权所有:深圳市亿车科技有限公司
 *
 * 创建日期: ${date} ${time}
 *
 * 修改人:   金征
 *
 * 修改时间:  ${date} ${time} 
 *
 * 修改备注: 
 *
 * 版本:      v1.0 
 *
 *===============================================
 */
public class BankAndPhoneTextWatch implements TextWatcher {
    /**
     * 是否开启6222 2316 1256和138 8888 8888 银行卡号和电话号码显示方式 默认显示银行卡号显示方式
     */
    public boolean showType;
    /**
     * 开启电话号码显示方式
     */
    public boolean showMobileType;

    EditText editText;//宿主Editext

    public BankAndPhoneTextWatch(EditText editText, boolean showType, boolean showMobileType) {
        this.showType = showType;
        this.showMobileType = showMobileType;
        this.editText=editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
              show(s,before,start,count);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (clearListener != null) {
            clearListener.clearEditListen(editText.getId());
        }
    }

    /**
     * 开启银行卡号和电话号码显示方式
     */
    private void show(CharSequence s, int before, int start, int after) {
        if (showType) {// 开启了银行卡号、电话号码显示方式
            // if (isRun) {
            // isRun = false;
            // return;
            // }
            // isRun = true;
            if (!showMobileType) {// 银行卡号显示方式
//                if (isRun) {
//                    isRun = false;
//                    return;
//                }
//                isRun = true;
//                inputStr = "";
//                String newStr = s.toString();
//                newStr = newStr.replace(" ", "");
//                int index = 0;
//
//                // if ((index + 3) < newStr.length()) {
//                // inputStr += (newStr.substring(index, index + 3) + " ");
//                // index += 3;
//                // }
//
//                while ((index + 4) < newStr.length()) {
//                    inputStr += (newStr.substring(index, index + 4) + " ");
//                    index += 4;
//                }
//                inputStr += (newStr.substring(index, newStr.length()));
//                isRun=true;
//                this.setBtnText(inputStr);
////				this.setSelection(inputStr.length());
//                ClearMeteriaEditText.this.setSelection(inputStr.length());
                if (s == null) {
                    return;
                }
                //判断是否是在中间输入，需要重新计算
                boolean isMiddle = (start + after) < (s.length());
                //在末尾输入时，是否需要加入空格
                boolean isNeedSpace = false;
                if (!isMiddle && s.length() > 0 && s.length() % 5 == 0) {
                    isNeedSpace = true;
                }
                if (isMiddle || isNeedSpace) {
                    String newStr = s.toString();
                    newStr = newStr.replace(" ", "");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < newStr.length(); i += 4) {
                        if (i > 0) {
                            sb.append(" ");
                        }
                        if (i + 4 <= newStr.length()) {
                            sb.append(newStr.substring(i, i + 4));
                        } else {
                            sb.append(newStr.substring(i, newStr.length()));
                        }
                    }
                    editText.removeTextChangedListener(this);
                    editText.setText(sb);
                    //如果是在末尾的话,或者加入的字符个数大于零的话（输入或者粘贴）
                    if (!isMiddle || after > 1) {
                        editText.setSelection(editText.length());
                    } else if (isMiddle) {
                        //如果是删除
                        if (after == 0) {
                            //如果删除时，光标停留在空格的前面，光标则要往前移一位
                            if ((start - before + 1) % 5 == 0) {
                                editText.setSelection((start - before) > 0 ? start - before : 0);
                            } else {
                                editText.setSelection((start - before + 1) > sb.length() ? sb.length() : (start - before + 1));
                            }
                        }
                        //如果是增加
                        else {
                            if ((start - before + after) % 5 == 0) {
                                editText.setSelection((start + after - before + 1) < sb.length() ? (start + after - before + 1) : sb.length());
                            } else {
                                editText.setSelection(start + after - before);
                            }
                        }
                    }
                    editText.addTextChangedListener(this);
                }
            } else {// 电话号码显示方式
                if (s == null || s.length() == 0)
                    return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == 4 || sb.length() == 9)
                                && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }
                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    editText.setText(sb.toString());
                    editText.setSelection(index);
                }
            }
        }
    }

    private ClearEditListener clearListener;

    public void setClearListener(ClearEditListener listener) {
        clearListener = listener;
    }

    public interface ClearEditListener {
        void clearEditListen(int id);
    }
}
