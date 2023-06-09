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
import org.apache.syncope.core.spring.SpringTestConfiguration;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

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

    static final int TESTCASES = 22; // 11 (num of ciphers available) * 2 (number of test where cipher is valid)
    static final int PARAMS = 3;
    static final int OTHER_CASES = 3; // number of cases with an invalid string and invalid (null) cipher

    public enum CipherAlgo {
        VALID, INVALID, NULL,
    }


    private String value;
    private CipherAlgorithm cipherAlgo; // true if valid
    private String expected;


    private static Encryptor ENCRYPTOR = Encryptor.getInstance();
    static String valid_str = "thisIsAvalidString"; //valid value

    @BeforeAll
    public static void configure() {
        ApplicationContextProvider.getBeanFactory().getSingleton("securityProperties"); //.registerSingleton("securityProperties", new SecurityProperties());

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

        System.out.println("params:" + Arrays.deepToString(params));
        return params;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters(){
        return Arrays.asList(prepareParams()
                /*new Object[][]{
            {"42C149E60B1DCF9D391C99B729D82D0862EABA9DDE5D57DD1B9B4C98A2684A09", valid_str, CipherAlgo.VALID},
            {"R+huzUfAYbxoBZ3v4o82LriOpgYrxtHVOJwSn0mUBZA=", valid_str, CipherAlgo.INVALID},
            {"R+huzUfAYbxoBZ3v4o82LriOpgYrxtHVOJwSn0mUBZA=", valid_str, CipherAlgo.NULL},

            {"E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855", "", CipherAlgo.VALID},
            {"FN0NSfsM70DxXlF9hhLitQ==", "", CipherAlgo.INVALID},
            {"FN0NSfsM70DxXlF9hhLitQ==", "", CipherAlgo.NULL},

            {null, null, CipherAlgo.VALID},
            {null, null, CipherAlgo.INVALID},
            {null, null, CipherAlgo.NULL}

        }*/
        );
    }


    public EncodeTest(String expected, String value, CipherAlgorithm cipherAlgo) {
        this.expected = expected;
        this.value = value;
        this.cipherAlgo = cipherAlgo;

    }



    @Test
    public void testEncode(){
        try {
            /*if(this.cipherAlgo.equals(CipherAlgo.NULL) || this.cipherAlgo.equals(CipherAlgo.INVALID)) {
                assertEquals(this.expected, ENCRYPTOR.encode(this.value, null));
                System.out.println("{" + this.expected + ", " + this.value + ", null}");
            }
            else {
                for(CipherAlgorithm c : CipherAlgorithm.values()){
                    assertEquals(this.expected, ENCRYPTOR.encode(this.value, c));
                    System.out.println("{" + this.expected + ", " + this.value + ", " + c.getAlgorithm() + "}");
                }
            }*/
            if(this.cipherAlgo != null && (this.cipherAlgo.isSalted() || this.cipherAlgo.equals(CipherAlgorithm.BCRYPT))) //with salted ciphers or BRCRYPT every call to encode generates a different result
                assertTrue(ENCRYPTOR.verify(this.value, this.cipherAlgo, ENCRYPTOR.encode(this.value, this.cipherAlgo)));
            else
                assertEquals(ENCRYPTOR.encode(this.value, this.cipherAlgo), ENCRYPTOR.encode(this.value, this.cipherAlgo));

            //System.out.println("{" + this.expected + ", " + this.value + ", " + this.cipherAlgo + "}");

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
    }
}
