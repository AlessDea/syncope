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

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class EncodeTest {

    static final int TESTCASES = 32;
    static final int PARAMS = 3;

    private static final String S_KEY = "secretkeykeykeyk";


    private String value;
    private CipherAlgorithm cipherAlgo; // true if valid
    private String expected;

    private static Encryptor ENCRYPTOR;

    public static MockedStatic<ApplicationContextProvider> applicationContextProvider;


    @BeforeClass
    public static void configure() {
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

        Object[][] params = new Object[TESTCASES][PARAMS];

        for (String value : values){
            for (CipherAlgorithm c : CipherAlgorithm.values()) {
                if(!c.equals(CipherAlgorithm.SSHA) && !c.equals(CipherAlgorithm.SHA)) {
                    if (value != null)
                        params[i] = new Object[]{MyEncryptor.encode(value, c, S_KEY), value, c};
                    else
                        params[i] = new Object[]{MyEncryptor.encode(null, c, S_KEY), null, c};

                    i++;
                }
            }
        }


        // cases where cipher algo is null
        for (String value : values){
            if(value != null){
                params[i] = new Object[]{MyEncryptor.encode(value, null, S_KEY), value, null};
                i++;
            }
        }


        return params;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
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
            if((this.cipherAlgo.isSalted() || this.cipherAlgo.equals(CipherAlgorithm.BCRYPT))) {
                System.out.println(this.value + " " + this.cipherAlgo.getAlgorithm());
                this.expected = ENCRYPTOR.encode(this.value, this.cipherAlgo);
                if(this.value != null)
                    assertTrue(ENCRYPTOR.verify(this.value, this.cipherAlgo, this.expected));
                else
                    assertFalse(ENCRYPTOR.verify(this.value, this.cipherAlgo, this.expected));
            }else {
                System.out.println(this.value + " " + this.cipherAlgo.getAlgorithm());
                assertEquals(this.expected, ENCRYPTOR.encode(this.value, this.cipherAlgo));
            }

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | java.lang.NullPointerException e) {
            /*e.printStackTrace();
            System.out.println(this.value + " " + this.cipherAlgo.getAlgorithm());*/
        }
    }
}
