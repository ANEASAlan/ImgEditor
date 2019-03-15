package com.example.aaneas.imgeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v8.renderscript.Allocation;
import com.android.rssample.*;
import android.support.v8.renderscript.RenderScript;
import android.widget.ImageView;
import android.widget.SeekBar;

public class Luminosite extends MainActivity {
    /// CHANGER LA LUMINOSITE D'UNE IMAGE ///

    static protected void changeBrightness(Bitmap Bmp, int BrightnessScale) {

        Bitmap n = Bitmap.createBitmap(Bmp.getWidth(),Bmp.getHeight(),Bmp.getConfig() );

        int h = Bmp.getHeight();
        int w = Bmp.getWidth();
        int[] pixels = new int[w * h];
        int c = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        Bmp.getPixels(pixels, 0, w, 0, 0, w, h);
        for (int i = 0; i < pixels.length; i++) {
            c = pixels[i];
            r = BrightnessScale+Color.red(c);
            if(r > 255) r = 255;
            g = BrightnessScale+Color.green(c);
            if(g > 255) g = 255;
            b = BrightnessScale+Color.blue(c);
            if(b > 255) b = 255;
            pixels[i] = Color.rgb(r,g,b);
        }
        n.setPixels(pixels, 0, w, 0, 0, w, h);
        MainActivity.Img.setImageBitmap(n);
    }

    /// CHANGEBRIGHTNESS RENDERSCRIPT VERSION///

    static protected void changeBrightnessRS(Bitmap bmp, Context context, float scale) {

        Bitmap n = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );

//1)  Creer un  contexte  RenderScript
        RenderScript rs = RenderScript.create(context);
//2)  Creer  des  Allocations  pour  passer  les  donnees
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
//3)  Creer le  script
        ScriptC_brightness brightnessScript = new ScriptC_brightness(rs);
//4)  Copier  les  donnees  dans  les  Allocations
// ...
//5)  Initialiser  les  variables  globales  potentielles
        brightnessScript.set_BrightnessScale(scale/255);
//6)  Lancer  le noyau

        brightnessScript.forEach_changeBrightness(input, output);
//7)  Recuperer  les  donnees  des  Allocation(s)
        output.copyTo(n);
//8)  Detruire  le context , les  Allocation(s) et le  script
        input.destroy();
        output.destroy();
        brightnessScript.destroy();
        rs.destroy();

        MainActivity.Img.setImageBitmap(n);
    }
}
