package com.example.aaneas.imgeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;

import com.android.rssample.*;

/*La class Pencil contient deux fonctions :
    -BlendColorDodge
    -BlendRS
  Ces deux fonctions permettront de terminer le filtre crayon (l'effet crayon est un
  enchaînement de différents effets)
 */

public class Pencil extends MainActivity{

    /*DodgeIntermediary() est une fonction intérmdiaire pour la fonction BlendColorDodge, elle permet de faire la fusion des deux couleurs de pixels envoyés si la fusion ne donne pas une valeur dépassant 255*/

    static private int DodgeIntermediary (int color1, int color2){
        if(color2 == 255){
            return color2;
        }else{
            if(255 < ((color1 << 8) / (255 - color2))){
                return 255;
            }else{
                return ((color1 << 9) / (255 - color2));

                // bien que la logique et les indications trouvables sur Internet nous diraient de marquer
                // "return ((color1 << 8) / (255 - color2));
            }
        }
    }

    /*BlendColorDodge() prend les deux images et pour chaque pixel, demande à la fonction intermédiaire des les fusionner*/

    static protected Bitmap BlendColorDodge(Bitmap bmp1,Bitmap bmp2){

        Bitmap newbmp = Bitmap.createBitmap(bmp1.getWidth(),bmp1.getHeight(), bmp1.getConfig() );

        int [] pixel1 = new int[newbmp.getWidth()*newbmp.getHeight()];
        int [] pixel2 = new int[newbmp.getWidth()*newbmp.getHeight()];
        bmp1.getPixels(pixel1,0,bmp1.getWidth(),0,0,bmp1.getWidth(),bmp1.getHeight());
        bmp2.getPixels(pixel2,0,bmp2.getWidth(),0,0,bmp2.getWidth(),bmp2.getHeight());

        int g;
        for (int i = 0; i < pixel1.length; i++) {
            g = DodgeIntermediary(Color.green(pixel1[i]), Color.green(pixel2[i]));
            pixel1[i] = Color.rgb(g, g, g);
        }

        newbmp.setPixels(pixel1,0,newbmp.getWidth(),0,0,newbmp.getWidth(),newbmp.getHeight());
        MainActivity.Img.setImageBitmap(newbmp);
        return newbmp;

    }

    /*BlendRS() appelle la version renderscript blend.rs*/

    static  protected Bitmap BlendRS(Bitmap bmp1, Bitmap bmp2, Context context) {

        Bitmap resultBitmap = Bitmap.createBitmap(bmp1.getWidth(),bmp1.getHeight(), bmp1.getConfig() );
        RenderScript rs = RenderScript.create(context);
        Allocation input2 = Allocation.createFromBitmap(rs, bmp2);
        Allocation input = Allocation.createFromBitmap(rs, bmp1);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_blend blend = new ScriptC_blend(rs);

        blend.set_inBitmap2(input2);
        blend.forEach_blend(input, output);

        output.copyTo(resultBitmap);
        input.destroy();
        output.destroy();
        blend.destroy();
        rs.destroy();

        MainActivity.Img.setImageBitmap(resultBitmap);
        return resultBitmap;
    }

}
