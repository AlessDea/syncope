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
import org.apache.syncope.core.spring.SpringTestConfiguration;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
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

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(Parameterized.class)
public class VerifyTest{

    enum CipherAlgo {
        VALID, INVALID, NULL,
    }


    private String value;
    private String encoded;
    private CipherAlgorithm cipherAlgo; // true if valid
    private boolean expected;

    static Encryptor ENCRYPTOR = Encryptor.getInstance();
    static String valid_str = "thisIsAvalidString"; //valid value
    static String invalid_str = "a0c299b71a9e59d5ebb07917e70601a3570aa103e99a7bb65a58e780ec9077b1902d1dedb31b1457beda595fe4d71d779b6ca9cad476266cc07590e31d84b206"; //invalid value
    static String valid = "42C149E60B1DCF9D391C99B729D82D0862EABA9DDE5D57DD1B9B4C98A2684A09"; // valid cipher



    @Parameterized.Parameters
    public static Collection<Object[]> getParameters(){
        return Arrays.asList(new Object[][]{
            {false, valid_str, CipherAlgo.VALID, invalid_str},
            {false, valid_str, CipherAlgo.VALID, null},
            {false, valid_str, CipherAlgo.VALID, ""},
            {false, valid_str, CipherAlgo.INVALID, valid},
            {false, valid_str, CipherAlgo.NULL, valid},

                //questi possono essere skippati, perchè quando una stringa non è valida?
            {false, invalid_str, CipherAlgo.VALID, invalid_str},
            {false, invalid_str, CipherAlgo.VALID, null},
            {false, invalid_str, CipherAlgo.VALID, ""},
            {false, invalid_str, CipherAlgo.INVALID, valid},
            {false, invalid_str, CipherAlgo.NULL, valid},
                // fino qui si può saltare

            {false, "", CipherAlgo.VALID, invalid_str},
            {false, "", CipherAlgo.VALID, null},
            {false, "", CipherAlgo.VALID, ""},
            {false, "", CipherAlgo.INVALID, valid},
            {false, "", CipherAlgo.NULL, valid},

            {false, null, CipherAlgo.VALID, invalid_str},
            {false, null, CipherAlgo.VALID, null},
            {false, null, CipherAlgo.VALID, ""},
            {false, null, CipherAlgo.INVALID, valid},
            {false, null, CipherAlgo.NULL, valid},

            {true, valid_str, CipherAlgo.VALID, valid}

        });
    }


    public VerifyTest(boolean expected, String value, CipherAlgo cipherAlgo, String encoded) {
        this.expected = expected;
        this.value = value;
        this.encoded = encoded;

        if (cipherAlgo.equals(CipherAlgo.VALID))
            this.cipherAlgo = CipherAlgorithm.SHA256;
        else
            this.cipherAlgo = null;

    }


    @Ignore
    @Test
    public void testVerify(){
        System.out.println(this.encoded + " " + this.value);
        assertEquals(this.expected, ENCRYPTOR.verify(this.value, this.cipherAlgo, this.encoded));
    }

}
