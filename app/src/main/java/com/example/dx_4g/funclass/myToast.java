package com.example.dx_4g.funclass;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dx_4g.MainActivity;
import com.example.dx_4g.R;

import org.xmlpull.v1.XmlPullParser;

public class myToast extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    private View toastView;
    private int toastlayout;
    private Context context;
    private  TextView toasttext;
    private int toasttextID;
    private int toasttextvalue;
    public myToast(Context context,int toastlayout,int toasttextID,int toasttextvalue) {
        super(context);
        this.context=context;
          this.toastlayout=toastlayout;
          this.toasttextID=toasttextID;
          this.toasttextvalue=toasttextvalue;
    }

    public void show(int local,int xOffset,int yOffset){
        toastView=LayoutInflater.from(context).inflate(toastlayout,null);
        toasttext=(TextView)toastView.findViewById(toasttextID);
        toasttext.setText(toasttextvalue);
        Toast toast=new Toast(context);
        toast.setGravity(local,xOffset,yOffset);
        toast.setView(toastView);
        toast.show();
   }


}
