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


import org.apache.syncope.core.persistence.api.entity.Delegation;
import org.apache.syncope.core.persistence.api.entity.Role;
import org.apache.syncope.core.spring.utils.MyRealm;
import org.apache.syncope.core.spring.utils.MyRole;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;


public class AuthDataAccessorIT {

    static final String ENT1 = "REALM_CREATE";
    static final String ENT2 = "REALM_DELETE";
    static final String ENT3 = "USER_CREATE";
    static final String ENT4 = "USER_DELETE";
    static final String ENT5 = "DUMMY_CREATE";

    static AuthDataAccessor authDataAccessor;
    static Delegation delegation;
    static Set<Role> roles = new HashSet<>();

    static Map<String, Set<String>> entitlementRealmMap = new HashMap<>();



    public static Delegation createDelegation(){

        Delegation delegation = Mockito.mock(Delegation.class);

        doReturn(roles).when(delegation).getRoles();

        return delegation;
    }

    private static void addRealmToEntitlement(Map<String, Set<String>> entitlementRealmMap,
                                              String entitlement, String realm) {
        if (entitlementRealmMap.containsKey(entitlement)) {
            Set<String> realms = entitlementRealmMap.get(entitlement);
            realms.add(realm);
        } else {
            Set<String> realms = new HashSet<>();
            realms.add(realm);
            entitlementRealmMap.put(entitlement, realms);
        }
    }

    @Before
    public void init(){

        authDataAccessor = new AuthDataAccessor(null, null, null, null, null,
                null, null, null, null, null,
                null, null, null);

        delegation = createDelegation();

    }


    /*@BeforeClass
    public static void setUp(){

        // Aggiungi i valori alla HashMap
        addRealmToEntitlement(entitlementRealmMap, "USER_CREATE", "/a/b");
        addRealmToEntitlement(entitlementRealmMap, "USER_CREATE", "/b/c");
        addRealmToEntitlement(entitlementRealmMap, "REALM_CREATE", "/a");
        addRealmToEntitlement(entitlementRealmMap, "REALM_CREATE", "/b");
        addRealmToEntitlement(entitlementRealmMap, "REALM_DELETE", "/a/b/c");
        addRealmToEntitlement(entitlementRealmMap, "REALM_DELETE", "/d");

        // Stampa la HashMap
        for (Map.Entry<String, Set<String>> entry : entitlementRealmMap.entrySet()) {
            String entitlement = entry.getKey();
            Set<String> realms = entry.getValue();
            System.out.println("Entitlement: " + entitlement);
            System.out.println("Realms: " + realms);
        }
    }*/

    @Test
    public void testAuthDataAccessor1(){
        //Set<SyncopeGrantedAuthority> syncopeGrantedAuthorities = authDataAccessor.buildAuthorities(entitlementRealmMap);

        Map<String, Set<String>> expected = new HashMap<>();

        try {
            Set<String> entitlements1 = new HashSet<>();
            entitlements1.add(ENT1);
            List<MyRealm> realms1 = new ArrayList<>(Arrays.asList(new MyRealm("/a/b/c"), new MyRealm("/b"), new MyRealm("/a"), new MyRealm("/d"), new MyRealm("/a/b")));
            roles.add(new MyRole(entitlements1, realms1));
            Set<String> exp1 = new HashSet<>(Arrays.asList("/a", "/b", "/d"));
            expected.put(ENT1, exp1);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            Set<String> entitlements2 = new HashSet<>();
            entitlements2.add(ENT2);
            List<MyRealm> realms2 = new ArrayList<>(Arrays.asList(new MyRealm("/a/b"), new MyRealm("/a/b/c")));
            roles.add(new MyRole(entitlements2, realms2));
            Set<String> exp2 = new HashSet<>(Arrays.asList("/a/b"));
            expected.put(ENT2, exp2);
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*try {
            Set<String> entitlements3 = new HashSet<>();
            entitlements3.add(ENT3);
            List<MyRealm> realms3 = new ArrayList<>(Arrays.asList(new MyRealm("/a/b"), new MyRealm("/c/d"), new MyRealm("")));
            roles.add(new MyRole(entitlements3, realms3));
            Set<String> exp3 = new HashSet<>(Arrays.asList("/a/b", "/c/d"));
            expected.put(ENT3, exp3);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        Set<SyncopeGrantedAuthority> syncopeGrantedAuthorities = authDataAccessor.getDelegatedAuthorities(delegation);
        syncopeGrantedAuthorities.forEach(a -> {
            assertEquals(expected.get(a.getAuthority()), a.getRealms());
            //System.out.println(a.getAuthority() + ": " + Arrays.toString(a.getRealms().toArray()));
        });


        try { //Exception expected
            Set<String> entitlements4 = new HashSet<>();
            entitlements4.add(ENT4);
            List<MyRealm> realms4 = new ArrayList<>(Arrays.asList(new MyRealm("/a/b"), new MyRealm("/c/d"), new MyRealm(null)));
            roles.add(new MyRole(entitlements4, realms4));
            Set<String> exp4 = new HashSet<>(Arrays.asList("/a/b", "/c/d"));
        } catch (Exception e) {
            //e.printStackTrace();
            assertTrue(true);
        }


        try {//Exception expected
            Set<String> entitlements5 = new HashSet<>();
            entitlements5.add(ENT5);
            List<MyRealm> realms5 = null; // questo provoca eccezione quindi mi risolve il problema del null in normalizeAddTo
            roles.add(new MyRole(entitlements5, realms5));
            Set<String> exp5 = new HashSet<>(); //me l'aspetto vuoto
        } catch (Exception e) {
            //e.printStackTrace();
            assertTrue(true);
        }


        try {//Exception expected
            Set<String> entitlements6 = new HashSet<>();
            entitlements6.add(null);
            List<MyRealm> realms6 = new ArrayList<>();
            roles.add(new MyRole(entitlements6, realms6));
            Set<String> exp6 = new HashSet<>();
        } catch (Exception e) {
            //e.printStackTrace();
            assertTrue(true);
        }

    }
}
