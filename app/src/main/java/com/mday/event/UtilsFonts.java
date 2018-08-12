package com.mday.event;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by admin on 3/20/18.
 */

public class UtilsFonts {
    public static Typeface typeMontserratLight(Context context){
        return Typeface.createFromAsset(context.getAssets(), "fonts/montserrat_light.otf");
    }
    public static Typeface typeMontserratRegular(Context context){
        return Typeface.createFromAsset(context.getAssets(), "fonts/montserrat_regular.otf");
    }
    public static Typeface helveticaRegular(Context context){
        return Typeface.createFromAsset(context.getAssets(), "fonts/helvetica_regular.ttf");
    }
    public static Typeface typeMontserratMedium(Context context){
        return Typeface.createFromAsset(context.getAssets(), "fonts/montserrat_medium.otf");
    }
    public static Typeface typeMontserratBold(Context context){
        return Typeface.createFromAsset(context.getAssets(), "fonts/montserrat_bold.otf");
    }
}
