package com.example.aaneas.imgeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v8.renderscript.Allocation;

import android.support.v8.renderscript.RenderScript;
import android.util.Log;
import android.widget.ImageView;

import com.android.rssample.*;
import android.support.v8.renderscript.RenderScript;

public class Couleurs extends MainActivity {


    /// CHANGER LA TEINTE D'UNE IMAGE ///

    static protected Bitmap Coloriser(Bitmap bmp) {

        Bitmap n = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );
        double rand = 20;
        float HSV[] = {0, 0, 0};

        int [] pixel = new int[bmp.getWidth()*bmp.getHeight()];
        int [] color = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixel,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());

        for (int i =0; i < pixel.length ; i++){
            int a = pixel[i];

            /// Je convertis en HSV puis réinsert le pixel en "Color" ///

            Color.RGBToHSV(Color.green(a), Color.red(a), Color.blue(a), HSV);
            HSV[0] = (float) rand;
            color[i] = Color.HSVToColor(HSV);
            ///
        }
        n.setPixels(color,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        MainActivity.Img.setImageBitmap(n);
        return n;


    }

    /// RENDERSCRIPT COLORISER VERSION///

    static protected Bitmap ColoriserRS(Bitmap bmp, Context context) {

        Bitmap n = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );

        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_teinte ColorScript = new ScriptC_teinte(rs);

        // J'envoie un nombre aléatoire en paramètre ///


        int rand1 =(int) (Math.random() * 360) ;
        ColorScript.set_rand1(rand1);


        ///

        ColorScript.forEach_toColor(input, output);
        output.copyTo(n);

        input.destroy();
        output.destroy();
        ColorScript.destroy();
        rs.destroy();

        MainActivity.Img.setImageBitmap(n);
        return n;
    }

    /// CONSERVER UNE COULEUR ///

    static  protected Bitmap Conserve(Bitmap bmp) {

        Bitmap newimg = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );

        int [] pixel = new int[bmp.getWidth()*bmp.getHeight()];
        int [] colortab = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixel,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());


        double color = 0.2;
        for (int y = 0; y < pixel.length; y++) {
            int a = pixel[y];

            /// Conserve uniquement le rouge ///
            if ( color < 0.33){
                double Grey = 0.3 * Color.red(a) + 0.59 * Color.green(a) + 0.11 * Color.blue(a);
                if (Color.green(a)<= 100 && Color.blue(a) <= 100 && Color.red(a)> Color.green(a) && Color.red(a)>Color.blue(a)){
                    colortab[y] = Color.argb(Color.alpha(a),Color.red(a), Color.green(a) ,Color.blue(a) );
                }else{
                    colortab[y] = Color.argb(Color.alpha(a),(int) Grey, (int) Grey, (int) Grey);
                }

                /// Conserve uniquement le vert ///


            }else if( color < 0.66){
                double Grey = 0.3 * Color.red(a) + 0.59 * Color.green(a) + 0.11 * Color.blue(a);
                if (Color.blue(a)<= 200 && Color.red(a) <= 200 && Color.green(a)> Color.red(a) && Color.green(a)>Color.blue(a)){
                    colortab[y] = Color.argb(Color.alpha(a),Color.red(a), Color.green(a) ,Color.blue(a) );
                }else{
                    colortab[y] = Color.argb(Color.alpha(a),(int) Grey, (int) Grey, (int) Grey);
                }
                /// Conserve uniquement le bleu ///


            }else{
                double Grey = 0.3 * Color.red(a) + 0.59 * Color.green(a) + 0.11 * Color.blue(a);
                if (Color.green(a)<= 200 && Color.red(a) <= 200 && Color.blue(a)> Color.red(a) && Color.blue(a)>Color.green(a)){
                    colortab[y] = Color.argb(Color.alpha(a),Color.red(a), Color.green(a) ,Color.blue(a) );
                }else{
                    colortab[y] = Color.argb(Color.alpha(a),(int) Grey, (int) Grey, (int) Grey);
                }

            }

        }
        newimg.setPixels(colortab,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        MainActivity.Img.setImageBitmap(newimg);
        return newimg;
    }

    /// RENDERSCRIPT VERSION ///


    static  protected Bitmap ConserveRS(Bitmap bmp, Context context) {

        Bitmap n = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );

        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_conserve Conserve = new ScriptC_conserve(rs);
        Conserve.set_rand(Math.random());
        Conserve.forEach_toConserve(input, output);
        output.copyTo(n);

        input.destroy();
        output.destroy();
        Conserve.destroy();
        rs.destroy();

        MainActivity.Img.setImageBitmap(n);
        return n;
    }

    static protected Bitmap invert(Bitmap bmp) {
        Bitmap newBmp = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );
        int h = newBmp.getHeight();
        int w = newBmp.getWidth();
        int[] pixels = new int[w * h];
        int c = 0;
        newBmp.getPixels(pixels, 0, w, 0, 0, w, h);
        for (int i = 0; i < pixels.length; i++) {
            c = pixels[i];
            pixels[i] = Color.rgb(255-Color.red(c), 255-Color.green(c), 255-Color.blue(c));
        }
        newBmp.setPixels(pixels, 0, w, 0, 0, w, h);
        return newBmp;
    }
}
