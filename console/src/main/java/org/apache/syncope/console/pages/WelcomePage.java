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
package org.apache.syncope.console.pages;

import org.apache.syncope.console.SyncopeApplication;
import org.apache.syncope.console.commons.XMLRolesReader;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Welcome page to display after successful login.
 */
public class WelcomePage extends WebPage {

    private static final long serialVersionUID = 8851399358753120581L;

    @SpringBean
    private XMLRolesReader xmlRolesReader;

    public WelcomePage(final PageParameters parameters) {
        super(parameters);
        setupNavigationPanel();


    }

    //To prevent warning: "leaking this in constructor java" 
    private void setupNavigationPanel() {
        ((SyncopeApplication) getApplication()).setupNavigationPanel(this, xmlRolesReader, false);
    }
}
