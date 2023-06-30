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

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(value = Parameterized.class)
public class NormalizeTest {

    private enum Tests {
        ONE, TWO, THREE, FOUR, FIVE
    }


    private boolean expected;
    private Tests realms;


    static java.util.Collection<String> full_set;
    static java.util.Collection<String> empty_set;
    static java.util.Collection<String> other_set1;
    static java.util.Collection<String> other_set2;

    static Set<String> outNormal;
    static Set<String> outGroup;




    @Before
    public void configure(){
        full_set = new HashSet<>(Arrays.asList("/a/b", "/c", "/d/e", "g1@/a/b", "g2@/c", "g3@/d/e"));
        empty_set = new HashSet<>();

        other_set1 = new HashSet<>(Arrays.asList("g1@/a/b", "g2@/c", "g3@/d/e", "/a/b", "/c", "/d/e",""));
        other_set2 = new HashSet<>(Arrays.asList("g1@/a/b", "g2@/c", "g3@/d/e", ""));

        outNormal = new HashSet<>(Arrays.asList("/a/b", "/c", "/d/e"));
        outGroup = new HashSet<>(Arrays.asList("g1@/a/b", "g2@/c", "g3@/d/e"));

    }


    @Parameterized.Parameters
    public static java.util.Collection<Object[]> getParameters(){


        return Arrays.asList(new Object[][]{
                //exp      realms
                {true, Tests.ONE},

                {true, Tests.TWO},

                {true, Tests.THREE},

           /*     {true, Tests.FOUR},

                {true, Tests.FIVE},*/

        });
    }


    public NormalizeTest(boolean expected, Tests realms) {
        this.expected = expected;
        this.realms = realms;
    }


    @Test
    public void testRealmUtils(){

        boolean out = false;
        java.util.Collection<String> realmss = null;
        Set<String> exp1 = new HashSet<>();
        Set<String> exp2 = new HashSet<>();


        switch (this.realms){
            case ONE -> {
                realmss = full_set;
                exp1 = new HashSet<>(Arrays.asList("/a/b", "/c", "/d/e"));
                exp2 = new HashSet<>(Arrays.asList("g1@/a/b", "g2@/c", "g3@/d/e"));
            }
            case TWO -> {
                realmss = empty_set;
                exp1 = new HashSet<>();
                exp2 = new HashSet<>();
            }
            /*case THREE -> {
                realmss = other_set1;
                exp1 = new HashSet<>(Arrays.asList("/a/b", "/c", "/d/e"));
                exp2 = new HashSet<>(Arrays.asList("g1@/a/b", "g2@/c", "g3@/d/e"));
            }
            case FOUR -> {
                realmss = other_set2;
                exp1 = new HashSet<>();
                exp2 = new HashSet<>(Arrays.asList("g1@/a/b", "g2@/c", "g3@/d/e"));
            }*/
            case FIVE -> {
                realmss = null;
                exp1 = new HashSet<>();
                exp2 = new HashSet<>();
            }
        }

        //System.out.println("case: " + this.realms.toString());

        Pair<Set<String>, Set<String>> ret = RealmUtils.normalize(realmss);
        Set<String> s1 = ret.getLeft();
        Set<String> s2 = ret.getRight();

       /* System.out.println("out: " + Arrays.toString(s1.toArray()) + " - " + Arrays.toString(s2.toArray()));
        System.out.println("exp: " + Arrays.toString(exp1.toArray()) + " - " + Arrays.toString(exp2.toArray()));

        System.out.println(s1.size() + " " + s2.size());*/

        out = s1.equals(exp1) && s2.equals(exp2);

        assertEquals(this.expected, out);

    }

}
