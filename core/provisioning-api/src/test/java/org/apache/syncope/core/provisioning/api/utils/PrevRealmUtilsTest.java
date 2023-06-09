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

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(value= Parameterized.class)
public class PrevRealmUtilsTest {

    private enum Validity{
        VALID, INVALID, EMPTY, NULL
    }


    private boolean expected;
    private Validity newRealm;
    private Validity realms;


    static Set<String> valid_set;
    static Set<String> invalid_set;
    static Set<String> empty_set;

    private static ArrayList<String> valid_paths = new ArrayList<>(Arrays.asList("/a", "/ab", "/e", "/c/d"));
    private static String invalid_path = "../";
    private static String empty_path = "";


    @BeforeClass
    public static void configure(){
        valid_set = new HashSet<>(Arrays.asList("/a", "/a/b", "/c", "/c/d"));
        invalid_set = new HashSet<>(Arrays.asList("../", "../a", "../b", "../c"));
        empty_set = new HashSet<>();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters(){

        return Arrays.asList(new Object[][]{
                //exp       realms          newRealm
                {false, Validity.VALID, Validity.VALID},
                {false, Validity.VALID, Validity.VALID},

                {true, Validity.VALID, Validity.INVALID},
                {false, Validity.VALID, Validity.NULL},
                {false, Validity.VALID, Validity.EMPTY},

                {true, Validity.INVALID, Validity.VALID},
                {false, Validity.INVALID, Validity.INVALID},
                {false, Validity.INVALID, Validity.NULL},
                {false, Validity.INVALID, Validity.EMPTY},

                {false, Validity.NULL, Validity.VALID},
                {false, Validity.NULL, Validity.INVALID},
                {false, Validity.NULL, Validity.NULL},
                {false, Validity.NULL, Validity.EMPTY},

                {true, Validity.EMPTY, Validity.VALID},
                {false, Validity.EMPTY, Validity.INVALID},
                {false, Validity.EMPTY, Validity.NULL},
                {false, Validity.EMPTY, Validity.EMPTY},
        });
    }

    public PrevRealmUtilsTest(boolean expected, Validity newRealm, Validity realms) {
        this.expected = expected;
        this.newRealm = newRealm;
        this.realms = realms;
    }


    @Ignore
    @Test
    public void testRealmUtils(){

        boolean res;
        Set<String> realmss;
        String newR;

        if(this.newRealm.equals(Validity.VALID))
            newR = valid_paths.get(0);
        else if(this.newRealm.equals(Validity.INVALID))
            newR = invalid_path;
        else if (this.newRealm.equals(Validity.NULL))
            newR = null;
        else
            newR = "";

        if(this.realms.equals(Validity.VALID))
            realmss = valid_set;
        else if(this.realms.equals(Validity.INVALID))
            realmss = invalid_set;
        else if(this.realms.equals(Validity.NULL))
            realmss = null;
        else
            realmss = empty_set;

        System.out.println(newR + " " + realmss + " --> expected: " + this.expected);
        try {
            res = RealmUtils.normalizingAddTo(realmss, newR);
        } catch (Exception e) {
            e.printStackTrace();
            res = false;
        }

        assertEquals(this.expected, res);

    }
}
