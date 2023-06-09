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
package org.apache.syncope.core.provisioning.api.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(value= Parameterized.class)
public class MyRealmUtilsTest {


    private boolean expected;
    private String newRealm;
    private Set<String> realms;


    static Set<String> valid_set = new HashSet<>(Arrays.asList("/a", "/a/b", "/c", "/c/d"));
    static Set<String> invalid_set = new HashSet<>(Arrays.asList("../", "../a", "../b", "../c"));
    static Set<String> empty_set = new HashSet<>();


    private static ArrayList<String> valid_paths = new ArrayList<>(Arrays.asList("/a", "/ab", "/e", "/c/d"));
    private static String invalid_path = "../";
    private static String empty_path = "";


    @Parameterized.Parameters
    public static Collection<Object[]> getParameters(){
        System.out.println(valid_paths.toString());
        System.out.println(valid_set);

        return Arrays.asList(new Object[][]{
                {false, valid_set, valid_paths.get(0)},
                {true, valid_set, valid_paths.get(1)},
                {true, valid_set, valid_paths.get(2)},
                {false, valid_set, valid_paths.get(3)},

                {false, valid_set, invalid_path},
                {false, valid_set, null},
                {false, valid_set, empty_path},

                {false, invalid_set, valid_paths.get(0)},
                {false, invalid_set, invalid_path},
                {false, invalid_set, null},
                {false, invalid_set, empty_path},

                {false, null, valid_paths.get(0)},
                {false, null, invalid_path},
                {false, null, null},
                {false, null, empty_path},

                {false, empty_set, valid_paths.get(0)},
                {false, empty_set, invalid_path},
                {false, empty_set, null},
                {false, empty_set, empty_path},
        });
    }

    public MyRealmUtilsTest(boolean expected, String newRealm, Set<String> realms) {
        this.expected = expected;
        this.newRealm = newRealm;
        this.realms = realms;


    }

    @Test
    public void testRealmUtils(){

        System.out.println(this.newRealm + " " + this.realms.toString());
        if(this.realms != null)
            assertEquals(this.expected, RealmUtils.normalizingAddTo(this.realms, this.newRealm));

    }
}
