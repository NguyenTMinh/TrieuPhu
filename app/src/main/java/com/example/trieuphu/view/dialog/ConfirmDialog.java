package com.example.trieuphu.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.trieuphu.R;

public class ConfirmDialog extends Dialog {

    public ConfirmDialog(@NonNull Context context) {
        super(context);
    }

    public void init(String message, View.OnClickListener positiveListener){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm_give_up);
        setCanceledOnTouchOutside(false);
        TextView tvMessage = findViewById(R.id.tv_confirm_dialog);
        Button btNeg = findViewById(R.id.bt_cancel);
        Button btPos = findViewById(R.id.bt_ok);
        tvMessage.setText(message);
        btNeg.setOnClickListener(v -> cancel());
        btPos.setOnClickListener(v -> {
            positiveListener.onClick(v);
            cancel();
        });
    }
}
