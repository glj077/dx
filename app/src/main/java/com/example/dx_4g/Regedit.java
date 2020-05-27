package com.example.dx_4g;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dx_4g.funclass.BaseActivity;
import com.example.dx_4g.funclass.HttpCallbackListener;
import com.example.dx_4g.funclass.HttpUtil;
import com.example.dx_4g.funclass.HttpUtilPut;
import com.example.dx_4g.funclass.JsonUtil;
import com.example.dx_4g.funclass.RegValue;
import com.example.dx_4g.funclass.myApplication;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Regedit extends BaseActivity implements View.OnClickListener {

    private static final int SEND_QUEST=1;
    private static final int SEND_QUEST_ERR=2;

    private  int regAddr;
    private  int regType;
    private  String regName;
    private  String regValue;
    private  String regValuejsonString;
    private  EditText userValueEdit;

    /********************************************/
    //消息处理
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            final ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            if (msg.what == SEND_QUEST) {
                String response = (String) msg.obj;
                int showcode=msg.arg1;
                try {
                    if (showcode==202) {
                        int deviceID=myApplication.getInstance().getRegID();
                        final Intent intent=new Intent(Regedit.this,Main3Activity.class);
                        intent.putExtra("deviceID",deviceID);
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                startActivity(intent);

                            }
                        }, 2000);

                        //Toast.makeText(Regedit.this, "code:" + showcode, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (msg.what==SEND_QUEST_ERR){
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Regedit.this,"code:"+msg.arg1+" "+(String)msg.obj,Toast.LENGTH_SHORT).show();
            }

        }
    };
/**********************************************************/

/*********************************************************/
//加载界面

    @Override
    protected int getLayoutId() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        return R.layout.activity_regedit;
    }
    /*********************************************************/

    /*********************************************************/
    //初始化
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initView() {
        userValueEdit=(EditText)findViewById(R.id.editvalueshow);

        /************************************/
        //标题栏设置
        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar3));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);//隐藏默认的Title
        ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar3)).setTitle("设备");
        ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar3)).setNavigationIcon(R.drawable.reexit);//设置导航图标
        // 以下动作让标题居中显地
        TextView textView = (TextView) ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar3)).getChildAt(0);//主标题
        textView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;//填充父类
        textView.setGravity(Gravity.CENTER);
        /************************************/

        /************************************/
        //ToolBarr 控件的溢出菜单按钮监中事件
        ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar3)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /****************************************/

        /************************************/
        //注册按钮事件
        Button exitok=(Button)findViewById(R.id.exitok);
        Button exitcancel=(Button)findViewById(R.id.exitcancel);
        exitok.setOnClickListener(this);
        exitcancel.setOnClickListener(this);
        /****************************************/

        /****************************************/
        //添加EDITTEXT输入监听
        userValueEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                String editinput=s.toString();

                    switch (regType) {
                        case 0:
                            if (isInteger(editinput)) {
                                if (Integer.parseInt(editinput) < -32768) {
                                    userValueEdit.setText("-32768");
                                }
                                if (Integer.parseInt(editinput) > 32767) {
                                    userValueEdit.setText("32767");
                                }
                            }

                            break;

                    }
            }
        });
    }
    /****************************************/

    /*********************************************************/
    //数据初始化
    @Override
    protected void initData() {
        regAddr=getIntent().getIntExtra("regaddr",0);
        regType=getIntent().getIntExtra("regaddrtype",0);
        regName=getIntent().getStringExtra("regname");
        regValue=getIntent().getStringExtra("regvalue");
        TextView textView=(TextView)findViewById(R.id.editaddrshow);
        textView.setText(regName);
        userValueEdit.setHint(regValue);
    }
    /*****************************************/
    //按钮事件响应
    @Override
    public void onClick(View v) {
         String svvalue=userValueEdit.getText().toString();
         int DeviceID=myApplication.getInstance().getRegID();
        switch (v.getId()){
            case R.id.exitok:
                if (!svvalue.isEmpty()){
                        if(((regType==0)&&(isInteger(svvalue)))||((regType==1)&&(isDouble(svvalue)))){
                            regValuejsonString=JsonString(regAddr, regType, svvalue);
                             String webAddr="https://api.diacloudsolutions.com/devices/"+DeviceID+"/regs";
                             HttpUtilPut.sendHttpRequest(webAddr, myApplication.getInstance().getPasbas64(), regValuejsonString,new HttpCallbackListener() {
                                        @Override
                                        public void onFinish(String response, int httpcode) {
                                            Message msg=Message.obtain();
                                            msg.what=SEND_QUEST;
                                            msg.arg1=httpcode;
                                            msg.obj=(Object)response;
                                           handler.sendMessage(msg);
                                        }

                                        @Override
                                        public void onError(int httpcode, String httpmessage) {
                                            Message msg=Message.obtain();
                                            msg.what=SEND_QUEST;
                                            msg.arg1=httpcode;
                                            msg.obj=(Object)httpmessage;
                                            handler.sendMessage(msg);
                                        }
                                    });

                    }
                        else{
                            Toast.makeText(Regedit.this,"数值类型错误!",Toast.LENGTH_SHORT).show();
                        }
                }else
                {
                    Toast.makeText(Regedit.this,"数值不能为空!",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.exitcancel:
                finish();
                break;

        }

    }
    /*****************************************/

    /****************************************/
    //加载Toolbar的溢出菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }
    /*****************************************/

    /*****************************************/
    //生成JSON格式的字符串
    private String JsonString(int regAddr_F,int regType_F,String regValue_SV_F){
        String jsonString;
        List<RegValue> list = new ArrayList<>() ;
       switch (regType_F){
           case 0:
               RegValue rV=new RegValue();
               rV.setReg_addr(regAddr_F);
               rV.setReg_value(Integer.parseInt(regValue_SV_F));
               list.add(rV);
               jsonString=JsonUtil.objectToString(list);
               break;
           case 1:
               String strRegValue=folatToHexString(Float.valueOf(regValue_SV_F));
               String H_Hex=strRegValue.substring(0,4);
               String L_Hex=strRegValue.substring(4,8);
               int L_Dec = Integer.parseInt(L_Hex, 16);//16进转十进制
               int H_Dec=Integer.parseInt(H_Hex, 16);//16进转十进制
               RegValue rV1=new RegValue();
               rV1.setReg_addr(regAddr_F);
               rV1.setReg_value(L_Dec);
               list.add(rV1);
               RegValue rV2=new RegValue();
               rV2.setReg_addr(regAddr_F+1);
               rV2.setReg_value(H_Dec);
               list.add(rV2);
               jsonString=JsonUtil.objectToString(list);
               break;
           default:
               throw new IllegalStateException("Unexpected value: " + regType_F);
       }
       return jsonString;
    }
    /*****************************************/

    /*****************************************/
    //浮点数转16进制，结果是字符串
    private   static  String folatToHexString(Float value){
        return  Integer.toHexString(Float.floatToIntBits(value));
    }
    /******************************************/

    /*****************************************/
    //判断是否为整型数
    private boolean isInteger(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        //Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        Pattern pattern = Pattern.compile("^-?[1-9]\\d*$");
        return pattern.matcher(str).matches();
    }
   /******************************************/

   /******************************************/
   //判断是否为浮点数
   // 判断浮点数（double和float）
    private boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
        return pattern.matcher(str).matches();
    }


}
