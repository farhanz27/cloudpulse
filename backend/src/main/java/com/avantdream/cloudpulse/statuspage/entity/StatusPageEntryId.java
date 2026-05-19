package com.avantdream.cloudpulse.statuspage.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class StatusPageEntryId implements Serializable {

    private UUID statusPageId;
    private UUID serviceId;

    public StatusPageEntryId() {}

    public StatusPageEntryId(UUID statusPageId, UUID serviceId) {
        this.statusPageId = statusPageId;
        this.serviceId = serviceId;
    }

    public UUID getStatusPageId() { return statusPageId; }
    public void setStatusPageId(UUID statusPageId) { this.statusPageId = statusPageId; }
    public UUID getServiceId() { return serviceId; }
    public void setServiceId(UUID serviceId) { this.serviceId = serviceId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatusPageEntryId)) return false;
        StatusPageEntryId that = (StatusPageEntryId) o;
        return Objects.equals(statusPageId, that.statusPageId) && Objects.equals(serviceId, that.serviceId);
    }

    @Override
    public int hashCode() { return Objects.hash(statusPageId, serviceId); }
}
