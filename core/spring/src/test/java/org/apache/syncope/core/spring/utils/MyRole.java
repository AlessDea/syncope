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

import org.apache.syncope.core.persistence.api.entity.*;
import org.apache.syncope.core.persistence.api.entity.user.DynRoleMembership;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyRole implements Role {

    private String entitlements;

    private Set<String> entitlementsSet = new HashSet<>();

    private List<MyRealm> realms = new ArrayList<>();



    public MyRole(){

    }

    public MyRole(Set<String> entitlementsSet, List<MyRealm> realms){
        this.entitlementsSet = entitlementsSet;
        this.realms = realms;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public void setKey(String key) {

    }

    @Override
    public Set<String> getEntitlements() {
        return entitlementsSet;
    }

    @Override
    public boolean add(Realm realm) {
        return false;
    }

    @Override
    public List<? extends Realm> getRealms() {
        return realms;
    }

    @Override
    public boolean add(DynRealm dynRealm) {
        return false;
    }

    @Override
    public List<? extends DynRealm> getDynRealms() {
        return null;
    }

    @Override
    public DynRoleMembership getDynMembership() {
        return null;
    }

    @Override
    public void setDynMembership(DynRoleMembership dynMembership) {

    }

    @Override
    public String getAnyLayout() {
        return null;
    }

    @Override
    public void setAnyLayout(String anyLayout) {

    }

    @Override
    public boolean add(Privilege privilege) {
        return false;
    }

    @Override
    public Set<? extends Privilege> getPrivileges(Application application) {
        return null;
    }

    @Override
    public Set<? extends Privilege> getPrivileges() {
        return null;
    }
}
