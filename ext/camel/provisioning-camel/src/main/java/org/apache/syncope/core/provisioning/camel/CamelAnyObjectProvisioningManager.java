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
package org.apache.syncope.core.provisioning.camel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.camel.Exchange;
import org.apache.camel.PollingConsumer;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.syncope.common.lib.mod.AnyObjectMod;
import org.apache.syncope.common.lib.to.PropagationStatus;
import org.apache.syncope.common.lib.to.AnyObjectTO;
import org.apache.syncope.core.provisioning.api.AnyObjectProvisioningManager;

public class CamelAnyObjectProvisioningManager
        extends AbstractCamelProvisioningManager implements AnyObjectProvisioningManager {

    @Override
    public Pair<Long, List<PropagationStatus>> create(final AnyObjectTO any) {
        return create(any, Collections.<String>emptySet());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Pair<Long, List<PropagationStatus>> create(
            final AnyObjectTO anyObjectTO, final Set<String> excludedResources) {

        PollingConsumer pollingConsumer = getConsumer("direct:createAnyObjectPort");

        Map<String, Object> props = new HashMap<>();
        props.put("excludedResources", excludedResources);

        sendMessage("direct:createAnyObject", anyObjectTO, props);

        Exchange exchange = pollingConsumer.receive();

        if (exchange.getProperty(Exchange.EXCEPTION_CAUGHT) != null) {
            throw (RuntimeException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
        }

        return exchange.getIn().getBody(Pair.class);
    }

    @Override
    public Pair<Long, List<PropagationStatus>> update(final AnyObjectMod anyMod) {
        return update(anyMod, Collections.<String>emptySet());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Pair<Long, List<PropagationStatus>> update(
            final AnyObjectMod anyMod, final Set<String> excludedResources) {

        PollingConsumer pollingConsumer = getConsumer("direct:updateAnyObjectPort");

        Map<String, Object> props = new HashMap<>();
        props.put("excludedResources", excludedResources);

        sendMessage("direct:updateAnyObject", anyMod, props);

        Exchange exchange = pollingConsumer.receive();

        if (exchange.getProperty(Exchange.EXCEPTION_CAUGHT) != null) {
            throw (RuntimeException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
        }

        return exchange.getIn().getBody(Pair.class);
    }

    @Override
    public List<PropagationStatus> delete(final Long anyObjectObjectKey) {
        return delete(anyObjectObjectKey, Collections.<String>emptySet());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PropagationStatus> delete(final Long anyObjectKey, final Set<String> excludedResources) {
        PollingConsumer pollingConsumer = getConsumer("direct:deleteAnyObjectPort");

        Map<String, Object> props = new HashMap<>();
        props.put("excludedResources", excludedResources);

        sendMessage("direct:deleteAnyObject", anyObjectKey, props);

        Exchange exchange = pollingConsumer.receive();

        if (exchange.getProperty(Exchange.EXCEPTION_CAUGHT) != null) {
            throw (RuntimeException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
        }

        return exchange.getIn().getBody(List.class);
    }

    @Override
    public Long unlink(final AnyObjectMod anyObjectMod) {
        PollingConsumer pollingConsumer = getConsumer("direct:unlinkAnyObjectPort");

        sendMessage("direct:unlinkAnyObject", anyObjectMod);

        Exchange exchange = pollingConsumer.receive();

        if (exchange.getProperty(Exchange.EXCEPTION_CAUGHT) != null) {
            throw (RuntimeException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
        }

        return exchange.getIn().getBody(Long.class);
    }

    @Override
    public Long link(final AnyObjectMod anyObjectMod) {
        PollingConsumer pollingConsumer = getConsumer("direct:linkAnyObjectPort");

        sendMessage("direct:linkAnyObject", anyObjectMod);

        Exchange exchange = pollingConsumer.receive();

        if (exchange.getProperty(Exchange.EXCEPTION_CAUGHT) != null) {
            throw (RuntimeException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
        }

        return exchange.getIn().getBody(Long.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PropagationStatus> provision(final Long key, final Collection<String> resources) {
        PollingConsumer pollingConsumer = getConsumer("direct:provisionAnyObjectPort");

        Map<String, Object> props = new HashMap<>();
        props.put("resources", resources);

        sendMessage("direct:provisionAnyObject", key, props);

        Exchange exchange = pollingConsumer.receive();

        if (exchange.getProperty(Exchange.EXCEPTION_CAUGHT) != null) {
            throw (RuntimeException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
        }

        return exchange.getIn().getBody(List.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PropagationStatus> deprovision(final Long anyObjectKey, final Collection<String> resources) {
        PollingConsumer pollingConsumer = getConsumer("direct:deprovisionAnyObjectPort");

        Map<String, Object> props = new HashMap<>();
        props.put("resources", resources);

        sendMessage("direct:deprovisionAnyObject", anyObjectKey, props);

        Exchange exchange = pollingConsumer.receive();

        if (exchange.getProperty(Exchange.EXCEPTION_CAUGHT) != null) {
            throw (RuntimeException) exchange.getProperty(Exchange.EXCEPTION_CAUGHT);
        }

        return exchange.getIn().getBody(List.class);
    }

}
