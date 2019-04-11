package com.example.aaneas.imgeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v8.renderscript.Allocation;
import com.android.rssample.*;
import android.support.v8.renderscript.RenderScript;
import android.widget.ImageView;
import android.widget.SeekBar;

/*La classe Lighting permet d'appliquer les filtres:
    -changeBrightness
    -changeBrightnessRS
  qui change la luminosité d'une image Bitmap
 */

public class Lighting extends MainActivity {

    /*changeBrightness() applique un filtre qui change la luminosité. Le paramètre BrightnessScale
    permet de faire varier progressivement l'intensité de la luminosité à l'aide d'une "Seekbar"*/

    static protected Bitmap changeBrightness(Bitmap bmp, int BrightnessScale) {
        Bitmap newbitmap = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(),bmp.getConfig() );
        int h = bmp.getHeight();
        int w = bmp.getWidth();
        int[] pixels = new int[w * h];
        int c;
        int r;
        int g;
        int b;
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);
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
        newbitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        MainActivity.Img.setImageBitmap(newbitmap);
        return newbitmap;
    }

    /*changeBrightnessRS() applique l'effet de luminosité à l'aide d'un fichier RenderScript. Le paramètre
    "scale" à la même utilisation que "BrightnessScale" de la fonction ci-dessus
     */

    static protected Bitmap changeBrightnessRS(Bitmap bmp, Context context, float scale) {
        Bitmap resultBitmap = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );
        RenderScript rs = RenderScript.create(context);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_brightness brightnessScript = new ScriptC_brightness(rs);
        brightnessScript.set_BrightnessScale(scale/255);
        brightnessScript.forEach_changeBrightness(input, output);
        output.copyTo(resultBitmap);

        input.destroy();
        output.destroy();
        brightnessScript.destroy();
        rs.destroy();

        MainActivity.Img.setImageBitmap(resultBitmap);
        return resultBitmap;
    }


}
