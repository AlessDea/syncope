/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.syncope.core.spring.security;

import org.apache.syncope.common.lib.types.CipherAlgorithm;
import org.apache.syncope.core.spring.ApplicationContextProvider;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class EncodeTest {

    //per la mutation bisogna saltare tutti i test che utilizzano un un cipher salted perchè altrimenti viene lanciata un'eccezione causata dal fatto che non c'è un'ApplicationContext valida

    static final int TESTCASES = 22; // 11 (num of ciphers available) * 2 (number of test where cipher is valid)
    static final int PARAMS = 3;
    static final int OTHER_CASES = 3; // number of cases with an invalid string and invalid (null) cipher



    private String value;
    private CipherAlgorithm cipherAlgo; // true if valid
    private String expected;


    private static Encryptor ENCRYPTOR = Encryptor.getInstance();
    static String valid_str = "thisIsAvalidString"; //valid value


    @BeforeClass
    public static void configure() {

        try {
            ApplicationContextProvider.getBeanFactory().registerSingleton("securityProperties", new SecurityProperties());
            //ApplicationContextProvider.getBeanFactory().getSingleton("securityProperties");

        } catch (Exception e) {
            e.printStackTrace();


        }

    }

    public static Object[][] prepareParams(){
        int i = 0;
        Object[][] params = new Object[TESTCASES + OTHER_CASES][PARAMS];
        for(CipherAlgorithm c : CipherAlgorithm.values()){
            //System.out.println("ALGO: " + c.getAlgorithm());
            params[i] = new Object[]{valid_str , valid_str, c};
            i++;
        }

        for(CipherAlgorithm c : CipherAlgorithm.values()){
            //System.out.println("ALGO: " + c.getAlgorithm());
            params[i] = new Object[]{"", "", c};
            i++;
        }


        params[i] = new Object[]{"R+huzUfAYbxoBZ3v4o82LriOpgYrxtHVOJwSn0mUBZA=", valid_str, null};
        params[++i] = new Object[]{"FN0NSfsM70DxXlF9hhLitQ==", "", null};
        params[++i] = new Object[]{null, null, null};

        //System.out.println("params:" + Arrays.deepToString(params));
        return params;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters(){
        return Arrays.asList(prepareParams());
    }


    public EncodeTest(String expected, String value, CipherAlgorithm cipherAlgo) {
        this.expected = expected;
        this.value = value;
        this.cipherAlgo = cipherAlgo;

    }


    @Test
    public void testEncode(){

        try {
            System.out.println("{" + this.expected + ", " + this.value + ", " + this.cipherAlgo + "}");

            if(this.cipherAlgo != null && (this.cipherAlgo.isSalted() || this.cipherAlgo.equals(CipherAlgorithm.BCRYPT))) {
                //with salted ciphers or BRCRYPT every call to encode generates a different result
                assertTrue(ENCRYPTOR.verify(this.value, this.cipherAlgo, ENCRYPTOR.encode(this.value, this.cipherAlgo)));
            }
            else {
                //assertEquals(ENCRYPTOR.encode(this.value, this.cipherAlgo), ENCRYPTOR.encode(this.value, this.cipherAlgo));
                if(this.value == null)
                    assertFalse(ENCRYPTOR.verify(this.value, this.cipherAlgo, ENCRYPTOR.encode(this.value, this.cipherAlgo)));
                else
                    assertTrue(ENCRYPTOR.verify(this.value, this.cipherAlgo, ENCRYPTOR.encode(this.value, this.cipherAlgo)));
            }

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | java.lang.NullPointerException e) {
            //e.printStackTrace();
        }
    }
}
