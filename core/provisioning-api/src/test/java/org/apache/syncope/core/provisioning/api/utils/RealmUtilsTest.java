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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(value= Parameterized.class)
public class RealmUtilsTest {

    private enum Validity{
        VALID, INVALID, EMPTY, NULL
    }


    private boolean expected;
    private String newRealm;
    private Validity realms;


    static Set<String> valid_set;
    static Set<String> empty_set;


    private static final String newR1 = "/a/c";
    private static final String newR2 = "/b";
    private static final String newR3 = "/b/c";

    @BeforeClass
    public static void configure(){
        valid_set = new HashSet<>(Arrays.asList("/a/b", "/c", "/d/e"));
        empty_set = new HashSet<>();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters(){


        return Arrays.asList(new Object[][]{
                //exp      realms     newRealm
                {true, Validity.VALID, "/a/c"},
                {true, Validity.VALID, "/b"},
                {false, Validity.VALID, "/d/e"},
                {true, Validity.VALID, "/a"},
                {false, Validity.VALID, null},


                /*P.Bug2: these two tests below produce expected output only in case they are run in this order. (read more on the relation)
                    A dedicated test is done in RealmUtilsEmptySetTest which guarantees the order.
                {true, Validity.EMPTY, "/a/c"},
                {false, Validity.EMPTY, null},*/


/*              P.Bug1: (read on relation)
                {false, Validity.EMPTY, ""},
                {false, Validity.VALID, ""},
*/
        });
    }


    public RealmUtilsTest(boolean expected, Validity realms, String newRealm) {
        this.expected = expected;
        this.realms = realms;
        this.newRealm = newRealm;

    }


    @Test
    public void testRealmUtils(){

        boolean res;
        Set<String> realmss;


        if(this.realms.equals(Validity.VALID))
            realmss = valid_set;
        else
            realmss = empty_set;

        System.out.println(this.newRealm + " " + realmss + " --> expected: " + this.expected);
        try {
            res = RealmUtils.normalizingAddTo(realmss, this.newRealm);
        } catch (java.lang.NullPointerException e) {
            //e.printStackTrace();
            res = false;
        }

        assertEquals(this.expected, res);

    }
}


