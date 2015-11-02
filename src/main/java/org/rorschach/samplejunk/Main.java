package org.rorschach.samplejunk;

import org.rorschach.complex.ValidationDriver;

import java.lang.reflect.InvocationTargetException;

public class Main {

    /*
    returnで始まるメソッド
    荒ぶるStream API
    不毛なコードの先に少女が見た物とは？
     */

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        // init object
        Bom bom = new Bom("1000000000", "2", "all work and no play makes jack a dull boy.");
        BomVerifier verifier = new BomVerifier();
        ValidationDriver<BomVerifier, Bom> driver = new ValidationDriver<>();

        // run validator
        System.out.println(driver.RunValidation(verifier, bom));

        // get validation context and display console
        verifier.errorMsg.forEach(System.out::println);
    }

}
