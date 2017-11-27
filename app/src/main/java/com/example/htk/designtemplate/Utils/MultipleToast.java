package com.example.htk.designtemplate.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by HTK on 11/27/2017.
 */

public class MultipleToast {
    public static Context context;
    public static Toast toast;
    public static void showToast(String string){
        if(toast == null){
            toast = Toast.makeText(context, string,Toast.LENGTH_SHORT);
        }
        toast.setText(string);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
