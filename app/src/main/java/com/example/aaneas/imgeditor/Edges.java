package com.example.aaneas.imgeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;

import com.android.rssample.*;

/*La class Edges contient trois fonctions :
    -SobelEdges
    -LaplaceEdges
    -EdgesRS
  Ces deux fonctions permettront de détecter les countours des objets dans une image en trouvant un contraste fort entre des pixels avoisinants
  Elles supposent les images en niveaux de gris, ce n'est pas bien grave sinon.
 */

public class Edges extends MainActivity{

    /*La fonction SobelEdges() utilise la matrice de Sobel avec des valeurs fortes dans les coins (-2 et 2) moyennes au milieu (-1 et 1) mais faible au centre (0).*/
    static protected Bitmap SobelEdges(Bitmap bmp){

        Bitmap newimg = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );

        //int div = (2*n +1)*(2*n+1);

        int [] pixel = new int[bmp.getWidth()*bmp.getHeight()];
        int [] newpixel = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixel,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());

        for (int x = 1; x < newimg.getWidth()-1; x++){
            for (int y = 1; y < newimg.getHeight()-1; y++ ){

                /// va chercher les valeurs des pixels autours //

                int horiz = 0;
                int verti = 0;
                int c = pixel[(x-1) + ((y-1)* (bmp.getWidth()))];
                horiz -= Color.green(c);
                verti -= Color.green(c);
                c = pixel[(x) + ((y-1)* (bmp.getWidth()))];
                verti -= 2*Color.green(c);
                c = pixel[(x+1) + ((y-1)* (bmp.getWidth()))];
                horiz += Color.green(c);
                verti -= Color.green(c);
                c = pixel[(x-1) + ((y)* (bmp.getWidth()))];
                horiz -= 2*Color.green(c);
                c = pixel[(x+1) + ((y)* (bmp.getWidth()))];
                horiz += 2*Color.green(c);
                c = pixel[(x-1) + ((y+1)* (bmp.getWidth()))];
                horiz -= Color.green(c);
                verti += Color.green(c);
                c = pixel[(x) + ((y+1)* (bmp.getWidth()))];
                verti += 2*Color.green(c);
                c = pixel[(x+1) + ((y+1)* (bmp.getWidth()))];
                horiz += Color.green(c);
                verti += Color.green(c);
                double dnewcolor = java.lang.Math.sqrt((double) (verti*verti + horiz*horiz));
                int newcolor = (int) (255.0 * dnewcolor / (java.lang.Math.sqrt((double) ((4*255*4*255*2)))));
                newpixel[x + (y*newimg.getWidth())] = Color.argb(Color.alpha(pixel[x + (y* (bmp.getWidth()))]),newcolor,newcolor,newcolor);

            }
        }
        newimg.setPixels(newpixel,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        MainActivity.Img.setImageBitmap(newimg);
        return newimg;

    }


/*La fonction LaplaceEdges() utilise la matrice de Laplace avec des valeurs fortes au centre (-8) mais faible autour (0).*/

    static protected Bitmap LaplaceEdges(Bitmap bmp){

        Bitmap newimg = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );

        int [] pixel = new int[bmp.getWidth()*bmp.getHeight()];
        int [] newpixel = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixel,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());

        for (int x = 1; x < newimg.getWidth()-1; x++){
            for (int y = 1; y < newimg.getHeight()-1; y++ ){

                /// va chercher les valeurs des pixels autours //
                int newcolor = 0;
                for (int x2 = x-1; x2 < x+2; x2++) {
                    for (int y2 = y - 1; y2 < y + 2; y2++) {
                        int c = pixel[x2 + (y2 * (bmp.getWidth()))];
                        if (x2 != x || y2 != y) newcolor += Color.green(c);
                        else newcolor -= 8 * Color.green(c);
                    }
                }
                if(newcolor > 255) newcolor = 255;
                else if (newcolor < 0) newcolor = 0;
                newpixel[x + (y*newimg.getWidth())] = Color.argb(Color.alpha(pixel[x + (y* (bmp.getWidth()))]),newcolor,newcolor,newcolor);



            }
        }
        newimg.setPixels(newpixel,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        MainActivity.Img.setImageBitmap(newimg);
        return newimg;
    }


    /*La fonction en renderscript utilise la matrice de Sobel*/

    static protected Bitmap edgesRS(Bitmap image, Context context) {
        Bitmap newbitmap = Bitmap.createBitmap(image.getWidth(),image.getHeight(), image.getConfig() );

        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs, image);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_edges BlurScript = new ScriptC_edges(rs);
        int matrixLength = 3;

        float[] matrix = new float[]{
                -1/9.0f,0/9.0f,1/9.0f,
                -2/9.0f,0/9.0f,2/9.0f,
                -1/9.0f,0/9.0f,1/9.0f,
        };

        BlurScript.set_in(input);
        Allocation matrix2 = Allocation.createSized(rs, Element.F32(rs),matrix.length);
        matrix2.copyFrom(matrix);
        BlurScript.bind_matrix(matrix2);
        BlurScript.set_matrixLength(matrixLength);
        BlurScript.set_width(image.getWidth());
        BlurScript.set_height(image.getHeight());

        BlurScript.forEach_contours(output);
        output.copyTo(newbitmap);

        input.destroy();
        output.destroy();
        BlurScript.destroy();
        rs.destroy();

        MainActivity.Img.setImageBitmap(newbitmap);
        return newbitmap;
    }



}
