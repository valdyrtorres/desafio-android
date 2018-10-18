package br.com.valdir.desafiolistafrutas.jni;

public class CalculeNative {

    static {
        System.loadLibrary("cambio-lib");
    }

    public native double asyncConvertToReal(double valueToConvert);
}