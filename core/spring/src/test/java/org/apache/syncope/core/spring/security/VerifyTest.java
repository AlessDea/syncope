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
import org.apache.syncope.core.spring.utils.MyEncryptor;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.context.ConfigurableApplicationContext;

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


    static final int VALUE = 81;
    static final int NULL_CIPHER = 6;
    static final int PARAMS = 4;

    private static final String S_KEY = "secretkeykeykeyk";

    private String value;
    private CipherAlgorithm cipherAlgo; // true if valid
    private boolean expected;
    private String encoded;


    private static Encryptor ENCRYPTOR;

    public static MockedStatic<ApplicationContextProvider> applicationContextProvider;


    @BeforeClass
    public static void setUp() {
        SecurityProperties securityProperties = new SecurityProperties();
        ConfigurableApplicationContext context = Mockito.mock(ConfigurableApplicationContext.class);
        Mockito.when(context.getBean(SecurityProperties.class)).thenReturn(securityProperties);
        applicationContextProvider = Mockito.mockStatic(ApplicationContextProvider.class);
        applicationContextProvider.when(ApplicationContextProvider::getApplicationContext).thenReturn(context);
    }

    @AfterClass
    public static void tearDown() {
        applicationContextProvider.close();
    }

    @Before
    public void initEncr(){
         ENCRYPTOR = Encryptor.getInstance(S_KEY);
    }

    public static Object[][] prepareParams() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        int i = 0;
        String[] values = {"password123", "", null};

        Object[][] params = new Object[VALUE+NULL_CIPHER][PARAMS];

        for (String value : values){
            for (CipherAlgorithm c : CipherAlgorithm.values()) {
                if(!c.equals(CipherAlgorithm.SSHA) && !c.equals(CipherAlgorithm.SHA)) {
                    if (value != null)
                        params[i] = new Object[]{true, value, c, MyEncryptor.encode(value, c, S_KEY)};
                    else
                        params[i] = new Object[]{false, null, c, MyEncryptor.encode(null, c, S_KEY)};

                    i++;
                }
            }
        }

        for (String value : values){
            for (CipherAlgorithm c : CipherAlgorithm.values()) {
                if(!c.equals(CipherAlgorithm.SSHA) && !c.equals(CipherAlgorithm.SHA)) {
                    if (value != null)
                        params[i] = new Object[]{false, value, c, ""};
                    else
                        params[i] = new Object[]{false, null, c, ""};

                    i++;
                }
            }
        }

        for (String value : values){
            for (CipherAlgorithm c : CipherAlgorithm.values()) {
                if(!c.equals(CipherAlgorithm.SSHA) && !c.equals(CipherAlgorithm.SHA)) {
                    if (value != null)
                        params[i] = new Object[]{false, value, c, null};
                    else
                        params[i] = new Object[]{false, null, c, null};

                    i++;
                }
            }
        }

        // cases where cipher algo is null (2)
        for (String value : values){
            if(value != null){
                params[i] = new Object[]{true, value, null, MyEncryptor.encode(value, null, S_KEY)};
                i++;
            }
        }

        // cases where cipher algo is null and encoded is empty (2)
        for (String value : values){
            if(value != null){
                params[i] = new Object[]{false, value, null, ""};
                i++;
            }
        }

        // cases where cipher algo is null and ecnoded is null (2)
        for (String value : values){
            if(value != null){
                params[i] = new Object[]{false, value, null, null};
                i++;
            }
        }

        return params;
    }


    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() throws UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
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
        boolean verified = false;

        try {
            if(this.cipherAlgo == null) {
                //System.out.println(null + " " + this.value + " " + this.encoded + " " + this.expected);
                verified = ENCRYPTOR.verify(this.value, null, this.encoded);
            }
            else {
                //System.out.println(this.cipherAlgo.getAlgorithm() + " " + this.value + " " + this.encoded + " " + this.expected);
                verified = ENCRYPTOR.verify(this.value, this.cipherAlgo, this.encoded);
            }

        } catch (Exception e) {
            //e.printStackTrace();
        }
        assertEquals(this.expected, verified);

    }
}
