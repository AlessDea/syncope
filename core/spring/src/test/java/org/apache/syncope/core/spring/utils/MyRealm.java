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
import org.apache.syncope.core.persistence.api.entity.policy.*;

import java.util.List;
import java.util.Optional;

public class MyRealm implements Realm {

    private String fullPath;

    public MyRealm(String path){
        this.fullPath = path;
    }


    @Override
    public String getKey() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public Realm getParent() {
        return null;
    }

    @Override
    public void setParent(Realm parent) {

    }

    @Override
    public String getFullPath() {
        return this.fullPath;
    }

    public void setFullPath(final String fullPath) {
        this.fullPath = fullPath;
    }


    @Override
    public AccountPolicy getAccountPolicy() {
        return null;
    }

    @Override
    public void setAccountPolicy(AccountPolicy accountPolicy) {

    }

    @Override
    public PasswordPolicy getPasswordPolicy() {
        return null;
    }

    @Override
    public void setPasswordPolicy(PasswordPolicy passwordPolicy) {

    }

    @Override
    public AuthPolicy getAuthPolicy() {
        return null;
    }

    @Override
    public void setAuthPolicy(AuthPolicy authPolicy) {

    }

    @Override
    public AccessPolicy getAccessPolicy() {
        return null;
    }

    @Override
    public void setAccessPolicy(AccessPolicy accessPolicy) {

    }

    @Override
    public AttrReleasePolicy getAttrReleasePolicy() {
        return null;
    }

    @Override
    public void setAttrReleasePolicy(AttrReleasePolicy policy) {

    }

    @Override
    public TicketExpirationPolicy getTicketExpirationPolicy() {
        return null;
    }

    @Override
    public void setTicketExpirationPolicy(TicketExpirationPolicy policy) {

    }

    @Override
    public boolean add(Implementation action) {
        return false;
    }

    @Override
    public List<? extends Implementation> getActions() {
        return null;
    }

    @Override
    public boolean add(AnyTemplateRealm template) {
        return false;
    }

    @Override
    public Optional<? extends AnyTemplateRealm> getTemplate(AnyType anyType) {
        return Optional.empty();
    }

    @Override
    public List<? extends AnyTemplateRealm> getTemplates() {
        return null;
    }

    @Override
    public boolean add(ExternalResource resource) {
        return false;
    }

    @Override
    public List<String> getResourceKeys() {
        return null;
    }

    @Override
    public List<? extends ExternalResource> getResources() {
        return null;
    }
}
