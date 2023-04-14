package com.maaz.wallet.model;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table
public class IdempotencyTracker {

    @Id
    private String uniqueKey;

    private ZonedDateTime createdAt;

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
