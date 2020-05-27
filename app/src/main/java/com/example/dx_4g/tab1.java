package com.example.dx_4g;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dx_4g.funclass.ActivityCollector;
import com.example.dx_4g.funclass.DXDeviceRegAdapter;
import com.example.dx_4g.funclass.DX_4G_Reg;
import com.example.dx_4g.funclass.DX_Device_Reg;
import com.example.dx_4g.funclass.HttpCallbackListener;
import com.example.dx_4g.funclass.HttpUtil;
import com.example.dx_4g.funclass.RegCallBackListener;
import com.example.dx_4g.funclass.httpopenException;
import com.example.dx_4g.funclass.myApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class tab1 extends Fragment {
   private SearchView searchView;
   private ListView listView;
   private  int regPosition;
   private  int regsign;
    private static final int SEND_REQUEST = 3;
    private static final int SEND_REQUEST_ERR=4;

    private LinkedList<DX_Device_Reg> mDataReg;
    private List<DX_4G_Reg.DataBean> dataBeansReg;
    private DXDeviceRegAdapter DXAdapterReg;
    private ListView list2;
    private Context mContext;
    private TextView regCount;
    private SwipeRefreshLayout mSwipe;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tab1,container,false);
        listView=(ListView)view.findViewById(R.id.reg_list);
        regCount=(TextView)view.findViewById(R.id.reg_count);

        //listview控件点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getRegValue_type(position,mDataReg);
                Intent intent=new Intent(view.getContext(),Regedit.class);
                intent.putExtra("regaddr",regPosition);
                intent.putExtra("regaddrtype",regsign);
                intent.putExtra("regname",mDataReg.get(position).getRegName());
                intent.putExtra("regvalue",mDataReg.get(position).getRegValue());
                startActivity(intent);
                //getActivity().onBackPressed();//关闭fragment tab1

            }
        });



        androidx.appcompat.widget.Toolbar toolbar =(Toolbar)view.findViewById(R.id.toolbar2);
        toolbar.setTitle("设备");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        //Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);//隐藏默认的Title

        // 以下动作让标题居中显地
        TextView textView = (TextView) toolbar.getChildAt(0);//主标题
        textView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;//填充父类
        textView.setGravity(Gravity.CENTER);
        toolbar.setNavigationIcon(R.drawable.reexit);//设置导航图标

        searchView = (SearchView) view.findViewById(R.id.search_view1);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);//增加提交按钮
        // TextView textView1=searchView.findViewById(android.support.
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text",null,null);
//获取到searchview TextView的控件
        TextView textView1 = (TextView) searchView.findViewById(id);
//设置字体大小为14sp
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);//14sp

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Main2Activity.class);
                startActivity(intent);
            }
        });


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_exit:
                        ActivityCollector.killAllActivity();
                        break;
                    case R.id.action_test:
                        //Intent intent=new Intent(Main4Activity.this,Main3Activity.class);
                        //startActivity(intent);
                        //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }
                return true;
            }
        });

        int devicdID= myApplication.getInstance().getRegID();
            readRegValue(devicdID);


        mSwipe=(SwipeRefreshLayout)view.findViewById(R.id.swipelayout);
        /*
         * 设置进度条的颜色
         * 参数是一个可变参数、可以填多个颜色
         */
        mSwipe.setColorSchemeColors(Color.parseColor("#d7a101"),Color.parseColor("#54c745"),Color.parseColor("#f16161"),Color.BLUE,Color.YELLOW);
        /*
         * 设置下拉刷新的监听
         */
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listView.setAdapter(null);
                int devicdID= myApplication.getInstance().getRegID();
                readRegValue(devicdID);
                mSwipe.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*
                         * 加载完毕之后就关闭进度条
                         */
                        mSwipe.setRefreshing(false);
                    }
                }, 5000);
            }
        });


        return view;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.what == SEND_REQUEST) {
                String response = (String) msg.obj;

                try {
                    parseJSONWITHGSON(response);
                    mSwipe.setRefreshing(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what==SEND_REQUEST_ERR){
                Toast.makeText(getContext(),"ErrorCode:"+msg.arg1+"  Message:"+(String)msg.obj,Toast.LENGTH_LONG).show();
            }
        }
    };

    private void readRegValue(int deviceID) {
        String webAddr="https://api.diacloudsolutions.com/devices/"+deviceID+"/regs";
        HttpUtil.sendHttpRequest(webAddr, myApplication.getInstance().getPasbas64(), new HttpCallbackListener() {
            @Override
            public void onFinish(String response,int httpcode) {
                Message msg = Message.obtain();
                msg.what = SEND_REQUEST;
                msg.obj = response;
                msg.arg1=httpcode;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(int httpcode,String httpmessage) {
                Message msg = Message.obtain();
                msg.what = SEND_REQUEST_ERR;
                msg.obj = httpmessage;
                msg.arg1=httpcode;
                handler.sendMessage(msg);
            }
        });
    }

    private void parseJSONWITHGSON(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);//将读回的字符串转换成JSON对象
        JSONArray jsonArray = jsonObject.getJSONArray("data");//获取名称data的JSON数组

        Gson gson = new Gson();
        dataBeansReg = gson.fromJson(jsonArray.toString(), new TypeToken<List<DX_4G_Reg.DataBean>>() {
        }.getType());
        RegValueHandle(dataBeansReg);
        regCount.setText(String.valueOf(dataBeansReg.size()));
        mContext = this.getContext();
        DXAdapterReg = new DXDeviceRegAdapter((LinkedList<DX_Device_Reg>) mDataReg, mContext);

        listView.setAdapter(DXAdapterReg);

    }

    private  void RegValueHandle(List<DX_4G_Reg.DataBean> RegValue){
        mDataReg = new LinkedList<DX_Device_Reg>();

        for(int i=0;i<RegValue.size();i++){
            if(i==RegValue.size()-1){
                mDataReg.add(new DX_Device_Reg(dataBeansReg.get(i).getName(),String.valueOf(dataBeansReg.get(i).getValue()),dataBeansReg.get(i).getAddr(),0,0));
                break;
            }
            if ((RegValue.get(i).getTemplate()!=null)&&(RegValue.get(i+1).getTemplate()==null)){
                float dx_regvalue=intToFloat(dataBeansReg.get(i+1).getValue(),dataBeansReg.get(i).getValue());
                mDataReg.add(new DX_Device_Reg(dataBeansReg.get(i).getName(),String.valueOf(dx_regvalue),dataBeansReg.get(i).getAddr(),0,1));
                i=i+1;
                continue;
            }

            if ((RegValue.get(i).getTemplate()!=null)&&(RegValue.get(i+1).getTemplate()!=null)) {
                mDataReg.add(new DX_Device_Reg(dataBeansReg.get(i).getName(), String.valueOf(dataBeansReg.get(i).getValue()),dataBeansReg.get(i).getAddr(), 0,0));

            }
        }
    }

    private static float intToFloat(int HValue,int LValue){
        String Hhex= Integer.toHexString(HValue);
        String Lhex=Integer.toHexString(LValue);
        String hex=Hhex+Lhex;
        return Float.intBitsToFloat(new BigInteger(hex, 16).intValue());
    }




   private void getRegValue_type(int position,LinkedList<DX_Device_Reg> mDataReg){
         regPosition=mDataReg.get(position).getRegAddr();
         regsign=mDataReg.get(position).getRegsign();

   }

    @Override
    public void onResume() {
        listView.setAdapter(null);
        int devicdID= myApplication.getInstance().getRegID();
        readRegValue(devicdID);
        super.onResume();
    }


}
