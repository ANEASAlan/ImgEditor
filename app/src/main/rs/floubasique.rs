#pragma version(1)
#pragma rs java_package_name(com.android.rssample)

rs_allocation in;
float * matrix;
int matrixLength;
//int reds[];
//int greens[];
//int blues[];
int width;
int height;

static float4 Value(int x, int y) {
    float4 value;

        x -= (int) (matrixLength / 2);
        y -= (int) (matrixLength / 2);

        for (int y2 = 0; y2 < matrixLength; y2++) {
            for (int x2 = 0; x2 < matrixLength; x2++) {
                    int tmpx = x + x2;
                    int tmpy = y + y2;
                    int finalX = abs((int)(tmpx - (tmpx / (width - 1)) * fmod((float)tmpx, (float)(width - 1)) * 2));
                    int finalY = abs((int)(tmpy - (tmpy / (height - 1)) * fmod((float)tmpy, (float)(height - 1)) * 2));
                    float4 pixelf = rsUnpackColor8888(rsGetElementAt_uchar4(in,finalX,finalY));
                    float coeff = matrix[matrixLength*x+y];
                    value.r += pixelf.r * coeff;
                    value.g += pixelf.g * coeff;
                    value.b += pixelf.b * coeff;
            }

        }
        value.a =255;
        return value;

}
uchar4 RS_KERNEL floubasique(uint32_t x,uint32_t y){
    float4 pixel = Value(x,y);
    return rsPackColorTo8888(pixel.r , pixel.g , pixel.b , pixel.a);
}
