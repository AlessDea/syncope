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
import org.junit.Before;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class TestUtils {


    static final int TESTCASES = 22; // 11 (num of ciphers available) * 2 (number of test where cipher is valid)
    static final int PARAMS = 3;
    static final int OTHER_CASES = 3; // number of cases with an invalid string and invalid (null) cipher


    static Encryptor ENCRYPTOR = Encryptor.getInstance();
    static String valid_str = "thisIsAvalidString"; //valid value

    public static void main(String[] argv){

        ApplicationContextProvider.setBeanFactory(new DefaultListableBeanFactory());
        ApplicationContextProvider.getBeanFactory().registerSingleton("securityProperties", new SecurityProperties());

        int i;
        Object[][] params = new Object[TESTCASES + OTHER_CASES][PARAMS];
        for(i = 0; i < TESTCASES/2; i++){
            for(CipherAlgorithm c : CipherAlgorithm.values()){
                /*if(c.getAlgorithm().startsWith("S-"))
                    continue;*/
                try {
                    params[i] = new Object[]{ENCRYPTOR.encode(valid_str, c), valid_str, c.getAlgorithm()};
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException e) {
                    e.printStackTrace();
                }
            }
        }

        for(i = TESTCASES/2; i < TESTCASES; i++){
            for(CipherAlgorithm c : CipherAlgorithm.values()){
                if(c.getAlgorithm().startsWith("S-"))
                    continue;
                try {
                    params[i] = new Object[]{ENCRYPTOR.encode("", c), "", c};
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException e) {
                    e.printStackTrace();
                }
            }
        }

        params[i] = new Object[]{"R+huzUfAYbxoBZ3v4o82LriOpgYrxtHVOJwSn0mUBZA=", valid_str, null};
        params[++i] = new Object[]{"FN0NSfsM70DxXlF9hhLitQ==", "", null};
        params[++i] = new Object[]{null, null, null};
    }

}
