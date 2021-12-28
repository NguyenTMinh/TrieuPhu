package com.example.trieuphu.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.trieuphu.R;
import com.example.trieuphu.intf.IRuntime;

public class CallHelpDialog extends Dialog {

    public CallHelpDialog(@NonNull Context context) {
        super(context);
    }

    public void init(String trueCase, IRuntime runtime){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_call_help);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.layout_call_above).setVisibility(View.GONE);
                findViewById(R.id.layout_call_below).setVisibility(View.VISIBLE);
                Drawable drawable = ((TextView)v).getCompoundDrawables()[1];
                CharSequence person = ((TextView) v).getText();
                TextView textView = findViewById(R.id.image_person_help);
                textView.setText(person);
                textView.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
                String s = person + " " + getContext().getResources().getString(R.string.advise) + " " + trueCase;
                ((TextView) findViewById(R.id.advise_text)).setText(s);
            }
        };
        findViewById(R.id.call_grandpa).setOnClickListener(listener);
        findViewById(R.id.call_grandma).setOnClickListener(listener);
        findViewById(R.id.call_pa).setOnClickListener(listener);
        findViewById(R.id.call_ma).setOnClickListener(listener);
        findViewById(R.id.bt_call_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                runtime.doRuntime();
            }
        });
    }
}
