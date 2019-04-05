package com.example.aaneas.imgeditor;

import android.graphics.Bitmap;
import android.graphics.Color;


public class Crayon extends MainActivity{

    static private int DodgeIntermediary (int color1, int color2){
        if(color2 == 255){
            return color2;
        }else{
            if(255 < ((color1 << 8) / (255 - color2))){
                return 255;
            }else{
                return ((color1 << 9) / (255 - color2));
                // bien que la logique et les indications trouvables sur Internet nous diraient de marquer
                // "return ((color1 << 8) / (255 - color2));"
                // après quelques tests, nous trouvons que cette version donne un résultat plus joli.
            }
        }
    }

    static protected Bitmap BlendColorDodge(Bitmap bmp1,Bitmap bmp2){

        Bitmap newbmp = Bitmap.createBitmap(bmp1.getWidth(),bmp1.getHeight(), bmp1.getConfig() );

        int [] pixel1 = new int[newbmp.getWidth()*newbmp.getHeight()];
        int [] pixel2 = new int[newbmp.getWidth()*newbmp.getHeight()];
        bmp1.getPixels(pixel1,0,bmp1.getWidth(),0,0,bmp1.getWidth(),bmp1.getHeight());
        bmp2.getPixels(pixel2,0,bmp2.getWidth(),0,0,bmp2.getWidth(),bmp2.getHeight());
        //int c = 0;

        int r;
        int g;
        int b;
        for (int i = 0; i < pixel1.length; i++) { //penser à enlever le /3
            r = DodgeIntermediary(Color.red(pixel1[i]), Color.red(pixel2[i]));
            g = DodgeIntermediary(Color.green(pixel1[i]), Color.green(pixel2[i]));
            b = DodgeIntermediary(Color.blue(pixel1[i]), Color.blue(pixel2[i]));
            pixel1[i] = Color.rgb(r, g, b);
        }

        newbmp.setPixels(pixel1,0,newbmp.getWidth(),0,0,newbmp.getWidth(),newbmp.getHeight());
        MainActivity.Img.setImageBitmap(newbmp);
        return newbmp;

    }


}
