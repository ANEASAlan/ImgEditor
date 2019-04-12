package com.example.aaneas.imgeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v8.renderscript.Allocation;

import android.support.v8.renderscript.RenderScript;
import android.util.Log;
import android.view.ViewDebug;
import android.widget.ImageView;

import com.android.rssample.*;
import android.support.v8.renderscript.RenderScript;

/*Cette classe 6 fonctions:
    -Hue
    -hueRS
    -Colorkeep
    -ColorkeepRS
    -invert
    -invertRS
  Toutes ces fonctions intéreagissent avec la couleur des pixels du bitmap
 */
public class Colors extends MainActivity {

    /* Change la teinte d'une Bitmap passée en paramêtre et le renvoie dans MainActivity.Img*/
    static protected Bitmap Hue(Bitmap Bmp, double ColorScale) { //colorScale est la valeur de la seekBar au moment où elle est laché

        Bitmap newbitmap = Bitmap.createBitmap(Bmp.getWidth(),Bmp.getHeight(), Bmp.getConfig() );
        float HSV[] = {0, 0, 0};
        int [] pixel = new int[Bmp.getWidth()*Bmp.getHeight()];
        int [] color = new int[Bmp.getWidth()*Bmp.getHeight()];
        Bmp.getPixels(pixel,0,Bmp.getWidth(),0,0,Bmp.getWidth(),Bmp.getHeight());

        for (int i =0; i < pixel.length ; i++){
            int a = pixel[i];
            /// Je convertis en HSV puis réinsert le pixel en "Color"
            Color.RGBToHSV( Color.red(a),Color.green(a),Color.blue(a), HSV);
            HSV[0] = (float) (ColorScale*360/100);
            color[i] = Color.HSVToColor(HSV);
        }
        newbitmap.setPixels(color,0,Bmp.getWidth(),0,0,Bmp.getWidth(),Bmp.getHeight());
        MainActivity.Img.setImageBitmap(newbitmap);
        return newbitmap;
    }

    /*Réalise la même chose que la fonction précédante mais en RenderScript*/

    static protected Bitmap hueRS(Bitmap bmp, Context context) {

        Bitmap newbitmap = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );
        RenderScript rs = RenderScript.create(context);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_hue ColorScript = new ScriptC_hue(rs);

        // J'envoie un nombre aléatoire en paramètre

        int rand1 =(int) (Math.random() * 360) ;
        ColorScript.set_rand1(rand1);

        ColorScript.forEach_toColor(input, output);
        output.copyTo(newbitmap);
        input.destroy();
        output.destroy();
        ColorScript.destroy();
        rs.destroy();
        MainActivity.Img.setImageBitmap(newbitmap);
        return newbitmap;
    }

   /*Colorkeep une couleur de la Bitmap passée en paramêtre et la renvoie dans MainActivity.Img*/

    static  protected Bitmap Colorkeep(Bitmap bmp, double colorScale) { //colorScale est la valeur de la seekBar au moment où elle est laché
        double sensibility=40.0;
        Bitmap newimg = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );
        double color=colorScale * (360-2*sensibility)/100 + sensibility;
        float HSV[] = {0, 0, 0};
        int [] pixel = new int[bmp.getWidth()*bmp.getHeight()];
        int [] colortab = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixel,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());

        for (int y = 0; y < pixel.length; y++) {
            int a = pixel[y];
            Color.RGBToHSV( Color.red(a), Color.green(a),Color.blue(a), HSV);
            double Grey = 0.3 * Color.red(a) + 0.59 * Color.green(a) + 0.11 * Color.blue(a);
            if (HSV[0]>=color-sensibility && HSV[0]<=color+sensibility){
                colortab[y] = Color.HSVToColor(HSV);
            }else{
                colortab[y] = Color.argb(Color.alpha(a),(int) Grey, (int) Grey, (int) Grey);
            }

        }
        newimg.setPixels(colortab,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        MainActivity.Img.setImageBitmap(newimg);
        return newimg;
    }

    /*Réalise la même chose que la fonction précédante mais en RenderScript*/

    static  protected Bitmap ColorkeepRS(Bitmap bmp, Context context) {

        Bitmap resultBitmap = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );
        RenderScript rs = RenderScript.create(context);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_colorkeep Colorkeep = new ScriptC_colorkeep(rs);
        Colorkeep.set_rand(Math.random());
        Colorkeep.forEach_colorKeep(input, output);
        output.copyTo(resultBitmap);

        input.destroy();
        output.destroy();
        Colorkeep.destroy();
        rs.destroy();
        MainActivity.Img.setImageBitmap(resultBitmap);
        return resultBitmap;
    }

    /*Inverse les couleurs de la Bitmap en paramêtre pour donner un aspect négatif*/

    static protected Bitmap invert(Bitmap bmp) {
        Bitmap newBmp = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );
        int h = newBmp.getHeight();
        int w = newBmp.getWidth();
        int[] pixels = new int[w * h];
        int c;
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);
        for (int i = 0; i < pixels.length; i++) {
            c = pixels[i];
            pixels[i] = Color.rgb(255-Color.red(c), 255-Color.green(c), 255-Color.blue(c));
        }
        newBmp.setPixels(pixels, 0, w, 0, 0, w, h);
        MainActivity.Img.setImageBitmap(newBmp);
        return newBmp;
    }

    /*Réalise la même chose que la fonction précédante mais en RenderScript*/

    static  protected Bitmap invertRS(Bitmap bmp, Context context) {

        Bitmap resultBitmap = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );
        RenderScript rs = RenderScript.create(context);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_invert invert = new ScriptC_invert(rs);
        invert.forEach_invert(input, output);
        output.copyTo(resultBitmap);

        input.destroy();
        output.destroy();
        invert.destroy();
        rs.destroy();
        MainActivity.Img.setImageBitmap(resultBitmap);
        return resultBitmap;
      }
}
