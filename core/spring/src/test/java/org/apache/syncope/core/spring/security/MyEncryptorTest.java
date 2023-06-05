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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(value= Parameterized.class)
public class MyEncryptorTest {

    private static Encryptor ENCRYPTOR;


    public enum CipherAlgo {
        VALID, INVALID, NULL,
    }


    private String value;
    private String encoded;
    private CipherAlgorithm cipherAlgo; // true if valid, fa

    private static String valid_str; //valid value
    private static String invalid_str; //invalid value
    private static String valid; // valid cipher
    private static String invalid; // invalid cipher

    @Before
    public static void configureTest(){
        valid_str = "thisIsAvalidString";

        valid = "42c149e60b1dcf9d391c99b729d82d0862eaba9dde5d57dd1b9b4c98a2684a09";
        invalid = "a0c299b71a9e59d5ebb07917e70601a3570aa103e99a7bb65a58e780ec9077b1902d1dedb31b1457beda595fe4d71d779b6ca9cad476266cc07590e31d84b206";
    }


    @Parameterized.Parameters
    public static Collection<Object[]> getParameters(){
        return Arrays.asList(new Object[][]{
            {valid_str, CipherAlgo.VALID, invalid},
            {valid_str, CipherAlgo.VALID, null},
            {valid_str, CipherAlgo.VALID, ""},
            {valid_str, CipherAlgo.INVALID, valid},
            {valid_str, CipherAlgo.NULL, valid},

            {invalid_str, CipherAlgo.VALID, invalid},
            {invalid_str, CipherAlgo.VALID, null},
            {invalid_str, CipherAlgo.VALID, ""},
            {invalid_str, CipherAlgo.INVALID, valid},
            {invalid_str, CipherAlgo.NULL, valid},

            {"", CipherAlgo.VALID, invalid},
            {"", CipherAlgo.VALID, null},
            {"", CipherAlgo.VALID, ""},
            {"", CipherAlgo.INVALID, valid},
            {"", CipherAlgo.NULL, valid},

            {null, CipherAlgo.VALID, invalid},
            {null, CipherAlgo.VALID, null},
            {null, CipherAlgo.VALID, ""},
            {null, CipherAlgo.INVALID, valid},
            {null, CipherAlgo.NULL, valid},
        });
    }


    public MyEncryptorTest(String value, CipherAlgo cipherAlgo, String encoded) {
        this.value = value;
        this.encoded = encoded;

        if (cipherAlgo.equals(CipherAlgo.VALID))
            this.cipherAlgo = CipherAlgorithm.SHA256;
        else
            this.cipherAlgo = null;

    }



    @Test
    public void testVerify(){
        assertTrue(ENCRYPTOR.verify(this.value, this.cipherAlgo, this.encoded));
    }

}
