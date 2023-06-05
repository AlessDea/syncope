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

import org.junit.Before;
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


    private static Set<String> valid_set;
    private static Set<String> invalid_set;
    private static Set<String> empty_set;

    private static ArrayList<String> valid_paths;
    private static String invalid_path;
    private static String empty_path;


    @Before
    public static void configureTest(){
        valid_set = new HashSet<>();
        // populating with some random realms
        valid_set.add("/a");
        valid_set.add("/a/b");
        valid_set.add("/c");
        valid_set.add("/c/d");

        invalid_set = new HashSet<>();
        // '/' is the root realm so '../' is not valid
        invalid_set.add("../");
        invalid_set.add("../a");
        invalid_set.add("../b");
        invalid_set.add("../c");

        empty_set = new HashSet<>();


        valid_paths = new ArrayList<>();
        valid_paths.add("/a"); // already exists
        valid_paths.add("/ab"); // new
        valid_paths.add("/e"); // new
        valid_paths.add("/c/d"); // already exists

        invalid_path = "../";


    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters(){
        return Arrays.asList(new Object[][]{
                {false, valid_set, valid_paths.get(0)},
                {true, valid_set, valid_paths.get(1)},
                {true, valid_set, valid_paths.get(2)},
                {false, valid_set, valid_paths.get(3)},

                {false, valid_set, invalid_path},
                {false, valid_set, null},
                {false, valid_set, empty_set},

                {false, invalid_set, valid_paths},
                {false, invalid_set, invalid_path},
                {false, invalid_set, null},
                {false, invalid_set, empty_set},

                {false, null, valid_paths},
                {false, null, invalid_path},
                {false, null, null},
                {false, null, empty_set},

                {false, empty_set, valid_paths},
                {false, empty_set, invalid_path},
                {false, empty_set, null},
                {false, empty_set, empty_set},
        });
    }

    public MyRealmUtilsTest(boolean expected, String newRealm, Set<String> realms) {
        this.expected = expected;
        this.newRealm = newRealm;
        this.realms = realms;
    }

    @Test
    public void testRealmUtils(){
        assertEquals(this.expected, RealmUtils.normalizingAddTo(this.realms, this.newRealm));
    }
}
