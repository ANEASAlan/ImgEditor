package com.example.aaneas.imgeditor;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;

import com.android.rssample.ScriptC_blur;


public class Blur extends MainActivity {

    /// Flou gaussien ////

    static protected Bitmap GaussianBlur(Bitmap bmp, boolean gaussian) {
        int[][] matrix;
        Bitmap n = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        if (gaussian) {
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

        int halfLength = matrix.length / 2;
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


        for (int x = halfLength; x < n.getWidth() - halfLength; x++) {
            for (int y = halfLength; y < n.getHeight() - halfLength; y++) {

                /// Va chercher les valeurs r g b des pixels autours //


                int r = 0;
                int g = 0;
                int b = 0;
                int alpha = 0;
                for (int x2 = 0; x2 < matrix.length; x2++) {
                    for (int y2 = 0; y2 < matrix.length; y2++) {
                        int e = pixel[(x - matrix.length / 2) + x2 + ((y - matrix.length / 2) + y2) * bmp.getWidth()];
                        r = r + Color.red(e) * matrix[x2][y2];
                        g = g + Color.green(e) * matrix[x2][y2];
                        b = b + Color.blue(e) * matrix[x2][y2];
                        alpha = Color.alpha(e);
                    }
                }

                newpixel[x + (y * n.getWidth())] = Color.argb(alpha, r / div, g / div, b / div);


            }
        }
        n.setPixels(newpixel, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        MainActivity.Img.setImageBitmap(n);
	return n;

    }

    static protected Bitmap BlurRS(Bitmap image, Context context) {
        Bitmap resultBitmap = Bitmap.createBitmap(image.getWidth(),image.getHeight(), image.getConfig() );

        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs, image);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_blur BlurScript = new ScriptC_blur(rs);
        int matrixLength = 10;
        float size = 100.0f;
        float[] matrix = new float[]{
                    1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,
                    1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,
                    1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,
                    1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,
                    1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,
                    1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,
                    1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,
                    1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,
                    1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,
                    1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,1/size,
            };

        BlurScript.set_in(input);
        Allocation matrix2 = Allocation.createSized(rs, Element.F32(rs),matrix.length);
        matrix2.copyFrom(matrix);
        BlurScript.bind_matrix(matrix2);
        BlurScript.set_matrixLength(matrixLength);
        BlurScript.set_width(image.getWidth());
        BlurScript.set_height(image.getHeight());

        BlurScript.forEach_blur(output);
        output.copyTo(resultBitmap);

        input.destroy();
        output.destroy();
        BlurScript.destroy();
        rs.destroy();

        MainActivity.Img.setImageBitmap(resultBitmap);
	    return resultBitmap;
    }

/* le code suivant est inspiré de https://medium.com/@ssaurel/create-a-blur-effect-on-android-with-renderscript-aa05dae0bd7d

    private static final float BITMAP_SCALE = 0.01f;
    private static final float BLUR_RADIUS = 0.01f;

    static protected Bitmap BlurRS(Bitmap image, Context context) {
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