/*
 * Copyright (c) 2015 EMC Corporation
 * All Rights Reserved
 */
package com.emc.object.s3.bean;

import com.emc.object.util.RestUtil;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.*;

@XmlRootElement(name = "AccessControlPolicy")
@XmlType(propOrder = {"owner", "grants"})
public class AccessControlList {
    private CanonicalUser owner;
    private Set<Grant> grants = new LinkedHashSet<Grant>();

    @XmlElement(name = "Owner")
    public CanonicalUser getOwner() {
        return owner;
    }

    public void setOwner(CanonicalUser owner) {
        this.owner = owner;
    }

    @XmlElementWrapper(name = "AccessControlList")
    @XmlElement(name = "Grant")
    public Set<Grant> getGrants() {
        return grants;
    }

    public void setGrants(Set<Grant> grants) {
        this.grants = grants;
    }

    public Map<String, List<Object>> toHeaders() {
        Map<String, List<Object>> headers = new HashMap<String, List<Object>>();
        for (Grant grant : grants) {
            RestUtil.add(headers, grant.getPermission().getHeaderName(), grant.getGrantee().getHeaderValue());
        }
        return headers;
    }
}
