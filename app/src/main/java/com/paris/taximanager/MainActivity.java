package com.paris.taximanager;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {

    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(25, 35, 25, 25);
        mainLayout.setBackgroundColor(Color.rgb(20,20,20));

        ScrollView scroll = new ScrollView(this);
        scroll.addView(mainLayout);

        TextView title = new TextView(this);
        title.setText("TAXI MANAGER\nΠΑΡΗΣ");
        title.setTextSize(28);
        title.setTextColor(Color.rgb(218,180,90));
        title.setGravity(Gravity.CENTER);
        title.setPadding(10,20,10,30);
        mainLayout.addView(title);

        addButton("ΟΙΚΟΝΟΜΙΚΑ");
        addButton("ΠΕΛΑΤΟΛΟΓΙΟ");
        addButton("ΔΡΟΜΟΛΟΓΙΑ");
        addButton("ΙΣΤΟΡΙΚΟ");
        addButton("ΡΥΘΜΙΣΕΙΣ");

        setContentView(scroll);
    }

    private void addButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(18);
        button.setTextColor(Color.WHITE);
        button.setBackgroundColor(Color.rgb(90,70,35));

        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(
                -1, 65
            );
        params.setMargins(0,10,0,10);

        mainLayout.addView(button, params);
    }
}
