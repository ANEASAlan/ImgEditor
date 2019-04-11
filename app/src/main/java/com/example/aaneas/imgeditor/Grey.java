package com.example.aaneas.imgeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.widget.ImageView;

import com.android.rssample.ScriptC_grey;

/*La classe Gris prend 2 fonctions:
    - toGrey
    - toGreyRS
  Ces fonctions ont pour but de rendre l'image grises.
 */

public class Grey extends MainActivity {


    /*toGrey() va appliquer un filtre qui grise les pixels du Bitmap*/

    static protected Bitmap toGrey(Bitmap bmp) {

        Bitmap newbitmap = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );
        int [] pixel = new int[bmp.getWidth()*bmp.getHeight()];
        int [] greytab = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixel,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        for (int i =0; i < pixel.length ; i++){
            int Grey = (int)( 0.3 * Color.red(pixel[i]) +0.59 *Color.green(pixel[i])+0.11* Color.blue(pixel[i]));
            greytab[i] = Color.argb(Color.alpha(pixel[i]),Grey,Grey,Grey);
        }
        newbitmap.setPixels(greytab,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        MainActivity.Img.setImageBitmap(newbitmap);
        return newbitmap;
    }

    /*toGreyRS() effectue la même opération mais en Renderscript*/

    static protected Bitmap toGreyRS(Bitmap bmp, Context context) {

        Bitmap resultBitmap = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );

        RenderScript rs = RenderScript.create(context);
        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptC_grey Grey = new ScriptC_grey(rs);
        Grey.forEach_toGrey(input, output);
        output.copyTo(resultBitmap);

        input.destroy();
        output.destroy();
        Grey.destroy();
        rs.destroy();

        MainActivity.Img.setImageBitmap(resultBitmap);
        return resultBitmap;

    }
}
