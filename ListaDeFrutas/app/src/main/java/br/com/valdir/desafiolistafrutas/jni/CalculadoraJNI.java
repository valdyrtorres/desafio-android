package br.com.valdir.desafiolistafrutas.jni;

public class CalculadoraJNI {

    static {
        System.loadLibrary("cambio-lib");
    }

    public native int somarJNI(int a, int b);

    public native int subtrairJNI(int a, int b);
}