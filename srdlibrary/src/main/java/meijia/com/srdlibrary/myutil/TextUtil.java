package meijia.com.srdlibrary.myutil;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;

/**
 * ----------------------------------------------------------
 * Copyright ©
 * ----------------------------------------------------------
 *
 * @author 师瑞东
 *         Create：2018/3/2
 */
public class TextUtil {
    public static SpannableStringBuilder setNumberMi(String number,String mi){
        SpannableString m2 = new SpannableString(mi);
        m2.setSpan(new RelativeSizeSpan(0.7f), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//一半大小
        m2.setSpan(new SuperscriptSpan(), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);   //上标
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(number);
        spannableStringBuilder.append(m2);
        return spannableStringBuilder;
    }
}
