//引入头文件
#include "com_example_downrefresh_TestNdkUtils.h"

JNIEXPORT jstring JNICALL Java_com_example_downrefresh_TestNdkUtils_helloWorld
  (JNIEnv *env, jobject instance){

  return (*env) -> NewStringUTF(env,"I am a ndk demo");

  }