package com.onethefull.attendmobile.adapter.popup;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.graphics.Color;

import com.onethefull.attendmobile.R;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnDismissedListener;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

public class PowerMenuUtils {


    public static PowerMenu getContactParentMenu(Context context, LifecycleOwner lifecycleOwner, OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener){

        return   new PowerMenu.Builder(context)
                .addItem(new PowerMenuItem("전화 걸기", false))
                .addItem(new PowerMenuItem("문자 보내기", false))
                .setLifecycleOwner(lifecycleOwner)
                .setAnimation(MenuAnimation.DROP_DOWN)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setTextColor(context.getResources().getColor(R.color.cell_text_color))
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(context.getResources().getColor(R.color.main_blue))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .build();


    }


}
