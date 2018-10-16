#include "com_tuacy_nestedscrolldemo_MainActivity.h"


JNIEXPORT jstring JNICALL
//Java_包名_类名_方法名
Java_com_tuacy_nestedscrolldemo_Java2CJNI_Java2C
  (JNIEnv *env, jobject instance){
        return (*env) -> NewStringUTF(env,"I am From Native C");

  }
