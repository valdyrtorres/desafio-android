//
// Created by valdyr on 13/10/2018.
//

#include <jni.h>
#include "cambio-lib.h"

extern "C"
JNIEXPORT jdouble JNICALL
Java_br_com_valdir_desafiolistafrutas_jni_CalculeNative_asyncConvertToReal(JNIEnv *env,
                                                                           jobject instance,
                                                                           jdouble valueToConvert) {
    return valueToConvert*3.5;

}