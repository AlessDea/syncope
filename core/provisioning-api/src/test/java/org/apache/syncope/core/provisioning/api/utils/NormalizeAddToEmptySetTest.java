package org.apache.syncope.core.provisioning.api.utils;
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

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NormalizeAddToEmptySetTest {

    @Test
    public void testRealUtilsEmptySet(){
        Set<String> empty_set = new HashSet<>();
        boolean res;

        assertTrue(RealmUtils.normalizingAddTo(empty_set, "/a/c"));

        try {
            res = RealmUtils.normalizingAddTo(empty_set, null);
        } catch (Exception e) {
            //e.printStackTrace();
            res = false;
        }
        assertFalse(res);
    }
}
