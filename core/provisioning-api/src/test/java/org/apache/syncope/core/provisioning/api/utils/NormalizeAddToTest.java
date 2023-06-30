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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(value= Parameterized.class)
public class NormalizeAddToTest {

    private enum Validity{
        FULL, EMPTY, NULL
    }


    private boolean expected;
    private String newRealm;
    private Validity realms;


    static Set<String> full_set;
    static Set<String> empty_set;



    @Before
    public void configure(){
        full_set = new HashSet<>(Arrays.asList("/a/b", "/c", "/d/e"));
        empty_set = new HashSet<>();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters(){


        return Arrays.asList(new Object[][]{
                //exp      realms     newRealm
                {true, Validity.FULL, "/a/c"},
                {true, Validity.FULL, "/b"},
                {false, Validity.FULL, "/d/e"},
                {true, Validity.FULL, "/a"},
                //{false, Validity.FULL, ""},
                {false, Validity.FULL, null},
                {true, Validity.EMPTY, "/a/c"},
                //{false, Validity.EMPTY, ""},
                //{false, Validity.EMPTY, null},
                {false, null, "/a/c"},
                {false, null, ""},
                {false, null, null},


        });
    }


    public NormalizeAddToTest(boolean expected, Validity realms, String newRealm) {
        this.expected = expected;
        this.realms = realms;
        this.newRealm = newRealm;

    }


    @Test
    public void testRealmUtils(){

        boolean res;
        Set<String> realmss;

        if(this.realms == null){
            realmss = null;
        }else if(this.realms.equals(Validity.FULL))
            realmss = full_set;
        else
            realmss = empty_set;

        System.out.println("'" + this.newRealm + "' " + realmss + " " + " --> expected: " + this.expected);
        try {
            res = RealmUtils.normalizingAddTo(realmss, this.newRealm);
        } catch (NullPointerException e) {
            //e.printStackTrace();
            res = false;
        }

        System.out.println(realmss);
        assertEquals(this.expected, res);


    }
}


