package com.example.dx_4g;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dx_4g.funclass.BaseActivity;

import java.util.Objects;

public class Regedit extends BaseActivity implements View.OnClickListener {

    private  int regAddr;
    private  int regType;
    private  String regName;
    private  String regValue;

    @Override
    protected int getLayoutId() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        return R.layout.activity_regedit;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initView() {

        //        //标题栏设置
        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar3));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);//隐藏默认的Title
        ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar3)).setTitle("设备");
        ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar3)).setNavigationIcon(R.drawable.reexit);//设置导航图标
        // 以下动作让标题居中显地
        TextView textView = (TextView) ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar3)).getChildAt(0);//主标题
        textView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;//填充父类
        textView.setGravity(Gravity.CENTER);

        ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar3)).setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        finish();
                        break;
                }
                return false;
            }

        });

        ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar3)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        regAddr=getIntent().getIntExtra("regaddr",0);
        regType=getIntent().getIntExtra("regaddrtype",0);
        regName=getIntent().getStringExtra("regname");
        regValue=getIntent().getStringExtra("regvalue");
        TextView textView=(TextView)findViewById(R.id.exitaddrshow);
        textView.setText(regName);
        EditText editText=(EditText)findViewById(R.id.exitvalueshow);
        editText.setHint(regValue);
        Toast.makeText(Regedit.this,regAddr+"   "+regType,Toast.LENGTH_LONG).show();


    }

    @Override
    public void onClick(View v) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }
}
