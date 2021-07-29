package me.sknz.api.paladins.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Clock;
import java.time.OffsetDateTime;

@Entity
@Table(name = "api_sessions")
public class PaladinsSession {

    @Id
    private String sessionId;
    @ManyToOne
    @JoinColumn(name = "developer_id")
    @JsonIgnore
    private PaladinsDeveloper developer;
    @CreatedDate
    private OffsetDateTime createdAt;
    @LastModifiedDate
    private OffsetDateTime lastUpdate;
    private int requests;

    public PaladinsSession() {
    }

    public PaladinsSession(String sessionId, PaladinsDeveloper developer) {
        this.sessionId = sessionId;
        this.developer = developer;
        this.createdAt = OffsetDateTime.now(Clock.systemUTC());
        this.lastUpdate = OffsetDateTime.now(Clock.systemUTC());
    }

    public String getSessionId() {
        return sessionId;
    }

    public PaladinsDeveloper getDeveloper() {
        return developer;
    }

    public int getRequests() {
        return requests;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getLastUpdate() {
        return lastUpdate;
    }

    public PaladinsSession update(){
        this.lastUpdate = OffsetDateTime.now(Clock.systemUTC());
        this.incrementRequest();
        return this;
    }

    public void setLastUpdate(OffsetDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setRequests(int requests) {
        this.requests = requests;
    }

    public void incrementRequest(){
        this.requests++;
    }
}
