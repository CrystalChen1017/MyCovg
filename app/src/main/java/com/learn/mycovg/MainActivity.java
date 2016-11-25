package com.learn.mycovg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
   public  EditText edtNumA, edtNumB;
    int a, b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtNumA = (EditText) findViewById(R.id.edt_numA);
        edtNumB = (EditText) findViewById(R.id.edt_numB);

    }

    public void add(View v) {
        if (getMyNum()) {
            String info = String.valueOf(MyUtils.add(a, b));
            Toast.makeText(this, "Result is：" + info, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Wrong numbers!", Toast.LENGTH_SHORT).show();
        }
    }

    public void plus(View v) {
        if (getMyNum()) {
            String info = String.valueOf(MyUtils.plus(a, b));
            Toast.makeText(this, "Result is：" + info, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Wrong numbers!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean getMyNum() {
        String aStr = edtNumA.getText().toString();
        String bStr = edtNumB.getText().toString();
        if (!TextUtils.isEmpty(aStr) && !TextUtils.isEmpty(bStr)) {
            a = Integer.parseInt(aStr);
            b = Integer.parseInt(bStr);
            return true;
        }
        return false;
    }
}
