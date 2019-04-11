#pragma version(1)
#pragma  rs  java_package_name(com.android.rssample)

uchar4 RS_KERNEL invert(uchar4 in, uint32_t x, uint32_t y){
const  float4  pixelf = rsUnpackColor8888(in);
pixelf.r = 1.0 - pixelf.r;
pixelf.g = 1.0 - pixelf.g;
pixelf.b = 1.0 - pixelf.b;
return  rsPackColorTo8888(pixelf.r , pixelf.g , pixelf.b , pixelf.a);
}