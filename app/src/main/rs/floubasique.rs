#pragma version(1)
#pragma rs java_package_name(com.android.rssample)


double rmin = 0.0;
double rmax = 1.0;
double gmin = 0.0;
double gmax = 1.0;
double bmin = 0.0;
double bmax = 1.0;

uchar4 RS_KERNEL floubasique(uchar4 in){

float4  pixelf = rsUnpackColor8888(in);
pixelf.r = (pixelf.r - rmin) / (rmax - rmin);
if(pixelf.r > 1.0) pixelf.r = 1.0;
else if(pixelf.r < 0.0) pixelf.r = 0.0;
pixelf.g = (pixelf.g - gmin) / (gmax - gmin);
if(pixelf.g > 1.0) pixelf.g = 1.0;
else if(pixelf.g < 0.0) pixelf.g = 0.0;
pixelf.b = (pixelf.b - bmin) / (bmax - bmin);
if(pixelf.b > 1.0) pixelf.b = 1.0;
else if(pixelf.b < 0.0) pixelf.b = 0.0;
    return  rsPackColorTo8888(pixelf.r , pixelf.g , pixelf.b , pixelf.a);
}