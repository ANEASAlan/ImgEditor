package com.example.aaneas.imgeditor;

import android.support.v7.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import com.android.rssample.*;


public class Gris extends AppCompatActivity{

    public Gris( Bitmap map, String type){
        if (type == "Gris"){
            toGrey(map);
        }else if (type == "RS"){ /// Ne marche pas
            toGreyRS(map);
        }
    }

    protected void toGrey(Bitmap bmp) {
        int [] pixel = new int[bmp.getWidth()*bmp.getHeight()];
        int [] greytab = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixel,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());

        for (int i =0; i < pixel.length ; i++){
            int Grey = (int)( 0.3 * Color.red(pixel[i]) +0.59 *Color.green(pixel[i])+0.11* Color.blue(pixel[i]));
            greytab[i] = Color.argb(1,Grey,Grey,Grey);
        }
        bmp.setPixels(greytab,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());

    }

    ///RENDERSCRIPT VERSION///

    private void toGreyRS(Bitmap bmp) {
        RenderScript rs = RenderScript.create(this);

        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_grey Grey = new ScriptC_grey(rs);

        Grey.forEach_toGrey(input, output);
        output.copyTo(bmp);

        input.destroy();
        output.destroy();
        Grey.destroy();
        rs.destroy();

    }

}
