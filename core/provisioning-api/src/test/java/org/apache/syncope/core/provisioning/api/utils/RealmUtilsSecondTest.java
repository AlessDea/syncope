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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class RealmUtilsSecondTest {



    static Set<String> valid_set;
    static Set<String> empty_set;



    @Test
    public void testRealmUtils(){

        Set<String> set1 = new HashSet<>(Arrays.asList("/a/b", "/c", "/d/e"));
        String ns1 = "/a/c";
        Set<String> exp1 = new HashSet<>(Arrays.asList("/a/b", "/c", "/d/e", "/a/c"));

        Set<String> set2 = new HashSet<>(Arrays.asList("/a/b", "/c", "/d/e"));
        String ns2 = "/a";
        Set<String> exp2 = new HashSet<>(Arrays.asList("/a", "/c", "/d/e"));

        Set<String> set3 = new HashSet<>(Arrays.asList("/a/b", "/c", "/d/e"));
        String ns3 = "/d/e";
        Set<String> exp3 = new HashSet<>(Arrays.asList("/a/b", "/c", "/d/e"));

        boolean res;

        RealmUtils.normalizingAddTo(set1, ns1);
        assertEquals(set1, exp1);

        RealmUtils.normalizingAddTo(set2, ns2);
        assertEquals(set2, exp2);

        RealmUtils.normalizingAddTo(set3, ns3);
        assertEquals(set3, exp3);


    }
}


