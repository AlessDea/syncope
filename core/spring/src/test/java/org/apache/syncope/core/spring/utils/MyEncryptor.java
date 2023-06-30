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

package org.apache.syncope.core.spring.utils;

import org.apache.syncope.common.lib.types.CipherAlgorithm;
import org.apache.syncope.core.spring.security.SecurityProperties;
import org.jasypt.commons.CommonUtils;
import org.jasypt.digest.StandardStringDigester;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MyEncryptor {

    private MyEncryptor() {
    }

    public static String encode(String value, CipherAlgorithm cipherAlgorithm, String secretKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String digest;
        if(value == null)
            return null;

        if(cipherAlgorithm == null)
            cipherAlgorithm = CipherAlgorithm.AES;

        switch (cipherAlgorithm) {
            case SHA1, SHA256, SHA512 -> {
                StandardStringDigester digester = new StandardStringDigester();
                digester.setAlgorithm(cipherAlgorithm.getAlgorithm());
                digester.setIterations(1);
                digester.setSaltSizeBytes(0);
                digester.setStringOutputType(CommonUtils.STRING_OUTPUT_TYPE_HEXADECIMAL);
                digest = digester.digest(value);
            }
            case SMD5, SSHA1, SSHA512, SSHA256 -> {
                SecurityProperties securityProperties = new SecurityProperties();
                StandardStringDigester digester = new StandardStringDigester();
                digester.setAlgorithm(cipherAlgorithm.getAlgorithm().replaceFirst("S-", ""));
                digester.setIterations(securityProperties.getDigester().getSaltIterations());
                digester.setSaltSizeBytes(securityProperties.getDigester().getSaltSizeBytes());
                digester.setInvertPositionOfPlainSaltInEncryptionResults(
                        securityProperties.getDigester().isInvertPositionOfPlainSaltInEncryptionResults());
                digester.setInvertPositionOfSaltInMessageBeforeDigesting(
                        securityProperties.getDigester().isInvertPositionOfSaltInMessageBeforeDigesting());
                digester.setUseLenientSaltSizeCheck(
                        securityProperties.getDigester().isUseLenientSaltSizeCheck());
                digester.setStringOutputType(CommonUtils.STRING_OUTPUT_TYPE_HEXADECIMAL);
                digest = digester.digest(value);
            }
            case BCRYPT -> digest = BCrypt.hashpw(value, BCrypt.gensalt());
            default -> {
                // Case AES
                SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), CipherAlgorithm.AES.getAlgorithm());
                Cipher cipher = Cipher.getInstance(CipherAlgorithm.AES.getAlgorithm());
                cipher.init(Cipher.ENCRYPT_MODE, keySpec);
                digest = Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes(StandardCharsets.UTF_8)));
            }
        }
        return digest;
    }
}
