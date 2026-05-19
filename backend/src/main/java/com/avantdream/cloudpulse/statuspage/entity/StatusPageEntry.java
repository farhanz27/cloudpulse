package com.avantdream.cloudpulse.statuspage.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "status_page_services")
public class StatusPageEntry {

    @EmbeddedId
    private StatusPageEntryId id;

    @Column(name = "display_order", nullable = false)
    private int displayOrder = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("statusPageId")
    @JoinColumn(name = "status_page_id")
    private StatusPage statusPage;

    public StatusPageEntry() {}

    public StatusPageEntry(StatusPage statusPage, java.util.UUID serviceId, int displayOrder) {
        this.id = new StatusPageEntryId(statusPage.getId(), serviceId);
        this.statusPage = statusPage;
        this.displayOrder = displayOrder;
    }

    public StatusPageEntryId getId() { return id; }
    public void setId(StatusPageEntryId id) { this.id = id; }
    public java.util.UUID getServiceId() { return id != null ? id.getServiceId() : null; }
    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
    public StatusPage getStatusPage() { return statusPage; }
    public void setStatusPage(StatusPage statusPage) { this.statusPage = statusPage; }
}
