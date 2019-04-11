#pragma version(1)
#pragma rs java_package_name(com.android.rssample)

rs_allocation in;
float * matrix;
int matrixLength;
int width;
int height;

static float4 blurCalculus(int indexX, int indexY){
    float4 value = {0, 0, 0, 255};

    indexX -= (int)(matrixLength / 2);
    indexY -= (int)(matrixLength / 2);

    for(int y = 0; y < matrixLength; y++){
        for(int x = 0; x < matrixLength; x++){
            int localX = indexX + x;
            int localY = indexY + y;

            int finalX = abs((int)(localX - (localX / (width - 1)) * fmod((float)localX, (float)(width - 1)) * 2));
            int finalY = abs((int)(localY - (localY / (height - 1)) * fmod((float)localY, (float)(height - 1)) * 2));

            float4 color = rsUnpackColor8888(rsGetElementAt_uchar4(in, finalX, finalY));
            float mult = matrix[y + x * matrixLength];

            value.r += color.r * mult;
            value.g += color.g * mult;
            value.b += color.b * mult;

        }
    }
    value.a = 255;
    return value;
}


uchar4 RS_KERNEL blur(uint32_t x,uint32_t y){
    float4 pixel = blurCalculus(x,y);
    return rsPackColorTo8888(pixel.r , pixel.g , pixel.b , pixel.a);
}
