//
// Created by valdyr on 13/10/2018.
//

#include <jni.h>
#include "cambio-lib.h"

JNIEXPORT jint JNICALL
Java_br_com_valdir_desafiolistafrutas_jni_CalculadoraJNI_somarJNI(JNIEnv *env, jobject instance,
                                                                  jint a, jint b) {
    int resp = a + b;
    return resp;

}

extern "C" JNIEXPORT jint JNICALL
Java_br_com_valdir_desafiolistafrutas_jni_CalculadoraJNI_subtrairJNI(JNIEnv *env, jobject instance, jint a,
                                                            jint b) {
    return a - b;
}