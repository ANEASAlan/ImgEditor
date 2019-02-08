package com.example.aaneas.imgeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v8.renderscript.Allocation;
import com.android.rssample.*;
import android.support.v8.renderscript.RenderScript;

public class Luminosite extends MainActivity {

    public Luminosite(Bitmap map, String type, Context context){
        if(type == "Luminosite"){
            changeBrightness(map);
        }else if(type == "LuminositeRS"){
            changeBrightnessRS(map,context);
        }
    }




    /// CHANGER LA LUMINOSITE D'UNE IMAGE ///

    void changeBrightness(Bitmap Bmp) {
        int h = Bmp.getHeight();
        int w = Bmp.getWidth();
        int[] pixels = new int[w * h];
        int c = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        int BrightnessScale = 75;
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
        Bmp.setPixels(pixels, 0, w, 0, 0, w, h);
    }

    /// CHANGEBRIGHTNESS RENDERSCRIPT VERSION///

    private void changeBrightnessRS(Bitmap bmp,Context context) {
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
// ...
//6)  Lancer  le noyau

        brightnessScript.forEach_changeBrightness(input, output);
//7)  Recuperer  les  donnees  des  Allocation(s)
        output.copyTo(bmp);
//8)  Detruire  le context , les  Allocation(s) et le  script
        input.destroy();
        output.destroy();
        brightnessScript.destroy();
        rs.destroy();
    }
}
