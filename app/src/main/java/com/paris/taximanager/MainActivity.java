
package com.paris.taximanager;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    int gold = Color.rgb(212,175,55);
    int black = Color.rgb(18,18,18);
    LinearLayout main;
    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("TaxiParis", MODE_PRIVATE);
        showHome();
    }

    TextView text(String value, int size) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(gold);
        t.setPadding(12,16,12,16);
        return t;
    }

    void setup(String title) {
        main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setPadding(18,18,18,18);
        main.setBackgroundColor(black);

        ScrollView scroll = new ScrollView(this);
        scroll.addView(main);
        setContentView(scroll);

        main.addView(text("TAXI MANAGER ΠΑΡΗΣ",24));
        main.addView(text(title,20));
    }

    void addButton(String name, Runnable action) {
        Button b = new Button(this);
        b.setText(name);
        b.setTextColor(gold);
        b.setBackgroundColor(Color.DKGRAY);
        main.addView(b);
        b.setOnClickListener(v -> action.run());
    }

    void showHome() {
        setup("ΚΕΝΤΡΙΚΟ ΜΕΝΟΥ");

        addButton("ΟΙΚΟΝΟΜΙΚΑ", () -> showFinance());
        addButton("ΠΕΛΑΤΕΣ", () -> showCustomers());
        addButton("ΔΡΟΜΟΛΟΓΙΑ", () -> showRoutes());
        addButton("ΕΤΗΣΙΑ ΣΤΑΤΙΣΤΙΚΑ", () -> showYear());
    }

    void showFinance() {
        setup("ΜΗΝΙΑΙΑ ΟΙΚΟΝΟΜΙΚΑ");

        String month = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());

        EditText income = new EditText(this);
        income.setHint("Έσοδα (€)");
        income.setInputType(8194);
        main.addView(income);

        EditText expense = new EditText(this);
        expense.setHint("Έξοδα (€)");
        expense.setInputType(8194);
        main.addView(expense);

        TextView result = text("",18);
        main.addView(result);

        addButton("ΑΠΟΘΗΚΕΥΣΗ", () -> {
            double in = number(income.getText().toString());
            double out = number(expense.getText().toString());

            double oldIn = Double.longBitsToDouble(
                prefs.getLong(month+"in", Double.doubleToLongBits(0)));
            double oldOut = Double.longBitsToDouble(
                prefs.getLong(month+"out", Double.doubleToLongBits(0)));

            prefs.edit()
                .putLong(month+"in", Double.doubleToLongBits(oldIn+in))
                .putLong(month+"out", Double.doubleToLongBits(oldOut+out))
                .apply();

            result.setText("ΣΥΝΟΛΟ ΕΣΟΔΩΝ: "+(oldIn+in+0)+" €\n"+
                "ΣΥΝΟΛΟ ΕΞΟΔΩΝ: "+(oldOut+out)+" €\n"+
                "ΚΑΘΑΡΟ: "+(oldIn+in-oldOut-out)+" €");
            Toast.makeText(this,"Αποθηκεύτηκε!",Toast.LENGTH_SHORT).show();
        });

        addButton("ΕΠΙΣΤΡΟΦΗ", () -> showHome());
    }

    double number(String s) {
        try {
            return Double.parseDouble(s.replace(",","."));
        } catch(Exception e) {
            return 0;
        }
    }

    void showCustomers() {
        setup("ΙΣΤΟΡΙΚΟ ΠΕΛΑΤΩΝ");

        EditText name = new EditText(this);
        name.setHint("Όνομα πελάτη");
        main.addView(name);

        EditText phone = new EditText(this);
        phone.setHint("Τηλέφωνο");
        main.addView(phone);

        TextView list = text(prefs.getString("customers",""),16);
        main.addView(list);

        addButton("ΠΡΟΣΘΗΚΗ ΠΕΛΑΤΗ", () -> {
            String old = prefs.getString("customers","");
            String entry = name.getText().toString()+" - "+phone.getText().toString();
            if(name.getText().length()==0) return;
            String updated = old+entry+"\n";
            prefs.edit().putString("customers",updated).apply();
            list.setText(updated);
            name.setText("");
            phone.setText("");
        });

        addButton("ΕΠΙΣΤΡΟΦΗ", () -> showHome());
    }

    void showRoutes() {
        setup("ΔΡΟΜΟΛΟΓΙΑ");

        EditText route = new EditText(this);
        route.setHint("Περιγραφή δρομολογίου");
        main.addView(route);

        EditText price = new EditText(this);
        price.setHint("Ποσό (€)");
        price.setInputType(8194);
        main.addView(price);

        TextView list = text(prefs.getString("routes",""),16);
        main.addView(list);

        addButton("ΠΡΟΣΘΗΚΗ ΔΡΟΜΟΛΟΓΙΟΥ", () -> {
            if(route.getText().length()==0) return;
            String entry = route.getText().toString()+" - "+
                price.getText().toString()+" €\n";
            String updated = prefs.getString("routes","")+entry;
            prefs.edit().putString("routes",updated).apply();
            list.setText(updated);
            route.setText("");
            price.setText("");
        });

        addButton("ΕΠΙΣΤΡΟΦΗ", () -> showHome());
    }

    void showYear() {
        setup("ΕΤΗΣΙΑ ΣΤΑΤΙΣΤΙΚΑ");

        int year = Integer.parseInt(
            new SimpleDateFormat("yyyy",Locale.getDefault()).format(new Date()));

        double totalIn = 0, totalOut = 0;

        for(int m=1;m<=12;m++) {
            String key = String.format(Locale.US,"%04d-%02d",year,m);
            totalIn += Double.longBitsToDouble(prefs.getLong(
                key+"in",Double.doubleToLongBits(0)));
            totalOut += Double.longBitsToDouble(prefs.getLong(
                key+"out",Double.doubleToLongBits(0)));
        }

        main.addView(text("ΕΤΟΣ: "+year,18));
        main.addView(text("ΕΣΟΔΑ: "+totalIn+" €",18));
        main.addView(text("ΕΞΟΔΑ: "+totalOut+" €",18));
        main.addView(text("ΚΑΘΑΡΟ ΚΕΡΔΟΣ: "+(totalIn-totalOut)+" €",20));

        addButton("ΕΠΙΣΤΡΟΦΗ", () -> showHome());
    }
}


