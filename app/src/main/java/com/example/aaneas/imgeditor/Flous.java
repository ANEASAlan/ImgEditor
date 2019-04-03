package com.example.aaneas.imgeditor;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.Script;
import android.support.v8.renderscript.ScriptIntrinsic;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;

import com.android.rssample.ScriptC_floubasique;


public class Flous extends MainActivity {

    /// Flou gaussien ////

    static protected Bitmap Flougaussien(Bitmap bmp, boolean gaussien) {
        int[][] matrix;
        Bitmap n = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        if (gaussien) {
            matrix = new int[][]{
                    {1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1},
                    {2, 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3, 2},
                    {3, 4, 5, 6, 7, 8, 9, 10, 11, 10, 9, 8, 7, 6, 5, 4, 3},
                    {4, 5, 6, 7, 8, 9, 10, 11, 12, 11, 10, 9, 8, 7, 6, 5, 4},
                    {5, 6, 7, 8, 9, 10, 11, 12, 13, 12, 11, 10, 9, 8, 7, 6, 5},
                    {6, 7, 8, 9, 10, 11, 12, 13, 14, 13, 12, 11, 10, 9, 8, 7, 6},
                    {7, 8, 9, 10, 11, 12, 13, 14, 15, 14, 13, 12, 11, 10, 9, 8, 7},
                    {8, 9, 10, 11, 12, 13, 14, 15, 16, 15, 14, 13, 12, 11, 10, 9, 8},
                    {9, 10, 11, 12, 13, 14, 15, 16, 17, 16, 15, 14, 13, 12, 11, 10, 9},
                    {8, 9, 10, 11, 12, 13, 14, 15, 16, 15, 14, 13, 12, 11, 10, 9, 8},
                    {7, 8, 9, 10, 11, 12, 13, 14, 15, 14, 13, 12, 11, 10, 9, 8, 7},
                    {6, 7, 8, 9, 10, 11, 12, 13, 14, 13, 12, 11, 10, 9, 8, 7, 6},
                    {5, 6, 7, 8, 9, 10, 11, 12, 13, 12, 11, 10, 9, 8, 7, 6, 5},
                    {4, 5, 6, 7, 8, 9, 10, 11, 12, 11, 10, 9, 8, 7, 6, 5, 4},
                    {3, 4, 5, 6, 7, 8, 9, 10, 11, 10, 9, 8, 7, 6, 5, 4, 3},
                    {2, 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3, 2},
                    {1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1},
            };
        } else {


            matrix = new int[][]{
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
            };
        }

        int na = matrix.length / 2;
        int div = 0;

        // Parcours la matrice ///


        for (int e = 0; e < matrix.length; e++) {
            for (int f = 0; f < matrix.length; f++) {
                div += matrix[e][f];
            }
        }

        int[] pixel = new int[bmp.getWidth() * bmp.getHeight()];
        int[] newpixel = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixel, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());


        for (int x = na; x < n.getWidth() - na; x++) {
            for (int y = na; y < n.getHeight() - na; y++) {

                /// Va chercher les valeurs r g b des pixels autours //


                int a = 0;
                int b = 0;
                int c = 0;
                int alpha = 0;
                for (int x2 = 0; x2 < matrix.length; x2++) {
                    for (int y2 = 0; y2 < matrix.length; y2++) {
                        int e = pixel[(x - matrix.length / 2) + x2 + ((y - matrix.length / 2) + y2) * bmp.getWidth()];
                        a = a + Color.red(e) * matrix[x2][y2];
                        b = b + Color.green(e) * matrix[x2][y2];
                        c = c + Color.blue(e) * matrix[x2][y2];
                        alpha = Color.alpha(e);
                    }
                }

                newpixel[x + (y * n.getWidth())] = Color.argb(alpha, a / div, b / div, c / div);


            }
        }
        n.setPixels(newpixel, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        MainActivity.Img.setImageBitmap(n);
	return n;

    }

    static protected Bitmap FlouRS(Bitmap image, Context context, boolean gaussian) {
        Bitmap n = Bitmap.createBitmap(image.getWidth(),image.getHeight(), image.getConfig() );

        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs, image);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_floubasique BlurScript = new ScriptC_floubasique(rs);
        int matrixLength;
        int[] matrix;
        if (gaussian) {
            matrix = new int[]
                    {1, 2, 3, 2, 1,
                    2, 6, 8, 6, 2,
                    3, 8, 10, 8,3,
                    2, 6, 8, 6, 2,
                    1, 2, 3, 2, 1};
            matrixLength = 5;
        } else {
            matrix = new int[]{
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1
            };
            matrixLength = 10;
        }
        int div = 0;

        for (int e = 0; e < matrixLength; e++) {
            for (int f = 0; f < matrixLength; f++) {
                div += matrix[e+matrixLength*f];
            }
        }

        int[] pixel = new int[image.getWidth() * image.getHeight()];
        image.getPixels(pixel, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

        int[] reds = new int[image.getWidth() * image.getHeight()];
        int[] blues = new int[image.getWidth() * image.getHeight()];
        int[] greens = new int[image.getWidth() * image.getHeight()];

        for(int i = 0; i < image.getWidth() * image.getHeight(); i++){
            reds[i] = Color.red(pixel[i]);
            greens[i] = Color.green(pixel[i]);
            blues[i] = Color.blue(pixel[i]);
        }

        BlurScript.set_gaussian(gaussian);
        BlurScript.set_div((double) div);
        Allocation matrix2 = Allocation.createSized(rs, Element.I32(rs),matrix.length);
        matrix2.copyFrom(matrix);
        BlurScript.bind_matrix(matrix2);
        BlurScript.set_matrixLength(matrixLength);
       // BlurScript.set_reds(reds);
        //BlurScript.set_greens(greens);
        //BlurScript.set_blues(blues);
        BlurScript.set_width(image.getWidth());
        BlurScript.set_height(image.getHeight());

        BlurScript.forEach_floubasique(input, output);
        output.copyTo(n);

        input.destroy();
        output.destroy();
        rs.destroy();

        MainActivity.Img.setImageBitmap(n);
	    return n;
    }

/* le code suivant est inspiré de https://medium.com/@ssaurel/create-a-blur-effect-on-android-with-renderscript-aa05dae0bd7d

    private static final float BITMAP_SCALE = 0.01f;
    private static final float BLUR_RADIUS = 0.01f;

    static protected Bitmap FlouRS(Bitmap image, Context context) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        //Bitmap n = Bitmap.createBitmap(image.getWidth(), image.getHeight(), image.getConfig() );

        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation output = Allocation.createFromBitmap(rs, outputBitmap);

        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.A_8(rs));

        intrinsicBlur.setRadius(BLUR_RADIUS);
        intrinsicBlur.setInput(input);
        intrinsicBlur.forEach(output);
        output.copyTo(outputBitmap);
        input.destroy();
        output.destroy();
        rs.destroy();

        MainActivity.Img.setImageBitmap(outputBitmap);
        return outputBitmap;
    }*/
}
