package com.example.dx_4g;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.dx_4g.funclass.ActivityCollector;
import com.example.dx_4g.funclass.BaseActivity;
import com.example.dx_4g.funclass.DXDeviceAdapter;
import com.example.dx_4g.funclass.DX_4G;
import com.example.dx_4g.funclass.DX_Device;
import com.example.dx_4g.funclass.HttpCallbackListener;
import com.example.dx_4g.funclass.HttpUtil;
import com.example.dx_4g.funclass.myApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Main2Activity extends BaseActivity implements View.OnClickListener {

    private static final int SEND_REQUEST = 1;
    private myApplication application;
    private SearchView searchView;
    private DXDeviceAdapter DXAdapter;
    private DXDeviceAdapter DXAdapter_query;
    private LinkedList<DX_Device> mData;
    private List<DX_4G.DataBean> dataBeans;
    private LinkedList<DX_Device> queryData;
    private  ListView list1;
    private  Context mContext;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.what == SEND_REQUEST) {
                String response = (String) msg.obj;

                try {
                    parseJSONWITHGSON(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };



    @Override
    protected int getLayoutId() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        return R.layout.activity_main2;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
         list1 = (ListView) findViewById(R.id.dxlist);
         mContext = this;

        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar1));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);//隐藏默认的Title
        ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar1)).setTitle("设备");
        // 以下动作让标题居中显地
        TextView textView = (TextView) ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar1)).getChildAt(0);//主标题
        textView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;//填充父类
        textView.setGravity(Gravity.CENTER);
        //((androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar1)).setSubtitle("副标题");
//        //((Toolbar)findViewById(R.id.toolbar)).setLogo(R.mipmap.ic_launcher);//设置标题LOGO
        // ((androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar1)).setSubtitleTextColor(Color.WHITE);//设置副标题文本颜色
        //      ((androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar1)).setNavigationIcon(R.mipmap.ic_launcher);//设置导航图标

        //搜索栏设置
        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);//增加提交按钮


        //注册SearchView监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String query_name="设备名称:"+query;
                queryData = new LinkedList<DX_Device>();

                if (query.equals(null)) {
                    Toast.makeText(getApplication(), "请输入查询条件！", Toast.LENGTH_SHORT).show();
                } else {
                   for (int i=0;i<DXAdapter.getCount();i++){
                       if (query_name.equals(mData.get(i).getDxName())){
                           queryData.add(new DX_Device(mData.get(i).getDxName(),mData.get(i).getDxIp(),R.mipmap.earth_foreground,mData.get(i).getDxStatus()));

                       }
                   }

                    DXAdapter_query = new DXDeviceAdapter((LinkedList<DX_Device>) queryData, mContext);
                    list1.setAdapter(null);
                    list1.setAdapter(DXAdapter_query);


                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.isEmpty()){
                    list1.setAdapter(null);
                    list1.setAdapter(DXAdapter);
                }
                return true;
            }
        });


        //标题栏中的menu中的按钮监听
        ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar1)).setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_exit:
                        ActivityCollector.killAllActivity();
                        break;
                }
                return true;
            }
        });


    }


    @Override
    protected void initData() {
        HttpUtil.sendHttpRequest("https://api.diacloudsolutions.com/devices", myApplication.getInstance().getPasbas64(), new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message msg = Message.obtain();
                msg.what = SEND_REQUEST;
                msg.obj = response;
                handler.sendMessage(msg);

            }

            @Override
            public void onError(Exception E) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }


    @Override
    public void onClick(View v) {

    }

    private void parseJSONWITHGSON(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);//将读回的字符串转换成JSON对象
        JSONArray jsonArray = jsonObject.getJSONArray("data");//获取名称data的JSON数组

        Gson gson = new Gson();
         dataBeans = gson.fromJson(jsonArray.toString(), new TypeToken<List<DX_4G.DataBean>>() {
        }.getType());

         mData = new LinkedList<DX_Device>();
        //ListView list1 = (ListView) findViewById(R.id.dxlist);
        //Context mContext = this;
        for (int i = 0; i < dataBeans.size(); i++) {
            mData.add(new DX_Device("设备名称:" + dataBeans.get(i).getName(), "IP地址:" + dataBeans.get(i).getIp(), R.mipmap.earth_foreground,dataBeans.get(i).getOnline()));

        }
         DXAdapter = new DXDeviceAdapter((LinkedList<DX_Device>) mData, mContext);
        list1.setAdapter(DXAdapter);




    }

}


