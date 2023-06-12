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

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class VerifyTest {

    //per la mutation bisogna saltare tutti i test che utilizzano un un cipher salted perchè altrimenti viene lanciata un'eccezione causata dal fatto che non c'è un'ApplicationContext valida


    static final int TESTCASES = 99; // 11 (num of ciphers available) * 9 (number of base test where cipher is valid)
    static final int PARAMS = 4;
    static final int OTHER_CASES = 3; // number of cases with an invalid string and invalid (null) cipher



    private String value;
    private CipherAlgorithm cipherAlgo; // true if valid
    private boolean expected;
    private String encoded;


    private static Encryptor ENCRYPTOR = Encryptor.getInstance();
    static String valid_str = "thisIsAvalidString"; //valid value
    static String valid_str_digest = "42C149E60B1DCF9D391C99B729D82D0862EABA9DDE5D57DD1B9B4C98A2684A09"; // valid cipher



    public static Object[][] prepareParams(){
        int i = 0;
        String[] values = {"thisIsAvalidString", "", null};
        String[] encoded_values = {"inv", "", null};
                                            // 'thisIsAvalidString'                                                 ""
        String[] encoded_valid_values = {"42C149E60B1DCF9D391C99B729D82D0862EABA9DDE5D57DD1B9B4C98A2684A09", "R+huzUfAYbxoBZ3v4o82LriOpgYrxtHVOJwSn0mUBZA=", "FN0NSfsM70DxXlF9hhLitQ=="};

        String value;
        String enc;
        Object[][] params = new Object[TESTCASES + OTHER_CASES][PARAMS];

        for(int j = 0; j < 3; j++) {
            for(int z = 0; z < 3; z++){
                value = values[z];
                enc = encoded_values[j];
                for (CipherAlgorithm c : CipherAlgorithm.values()) {
                    params[i] = new Object[]{false, value, c, enc};
                    i++;
                }
            }
        }

        //for these three cases, the attribute encoded it's, actually, a valid string that will be encoded before the test
        for(String v : values){
            // when the cipher specified is null, AES will be used as default. So, if a valid string is passed the result is a valid encoded string
            if(v != null && v.equals(valid_str))
                params[i] = new Object[]{true, v, null, valid_str};
            else
                params[i] = new Object[]{false, v, null, valid_str};
            i++;
        }

        return params;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters(){
        return Arrays.asList(prepareParams());
    }


    public VerifyTest(boolean expected, String value, CipherAlgorithm cipherAlgo, String encoded) {
        this.expected = expected;
        this.value = value;
        this.cipherAlgo = cipherAlgo;
        this.encoded = encoded;

    }


    @Test
    public void testVerify(){
        try {

            if(this.cipherAlgo == null)
                assertEquals(this.expected, ENCRYPTOR.verify(this.value, null, ENCRYPTOR.encode(this.encoded, null)));
            else
                assertEquals(this.expected, ENCRYPTOR.verify(this.value, this.cipherAlgo, this.encoded));

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | java.lang.NullPointerException e) {
            //e.printStackTrace();
        }
    }
}
