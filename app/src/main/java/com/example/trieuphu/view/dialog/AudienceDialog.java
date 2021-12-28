package com.example.trieuphu.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example.trieuphu.R;
import com.example.trieuphu.intf.IRuntime;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

public class AudienceDialog extends Dialog {

    public AudienceDialog(@NonNull Context context) {
        super(context);
    }

    public void init(int[] percent, IRuntime runtime){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_audience_answer);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        AppCompatButton button = findViewById(R.id.bt_audience_ok);
        button.setOnClickListener(v -> {
            cancel();
            runtime.doRuntime();
        });

        BarChart barChart = findViewById(R.id.barchart);

        ArrayList<BarEntry> arr = new ArrayList<>();
        arr.add(new BarEntry(1,percent[0]));
        arr.add(new BarEntry(2,percent[1]));
        arr.add(new BarEntry(3,percent[2]));
        arr.add(new BarEntry(4,percent[3]));

        BarDataSet dataSet = new BarDataSet(arr,"data");
        dataSet.setColor(getContext().getResources().getColor(R.color.hcb));
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(getContext().getResources().getColor(R.color.white));
        dataSet.setValueFormatter(new PercentFormatter());

        BarData barData = new BarData(dataSet);

        barChart.setData(barData);
        barChart.getDescription().setText("");
        barChart.animateY(2000);
        barChart.getXAxis().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);
    }
}
