package me.sknz.api.paladins.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.sknz.api.paladins.authentication.model.APIUserModel;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "api_developer")
public class PaladinsDeveloper {
    @Id
    private Integer id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "dev_id")
    @JsonIgnore
    private APIUserModel devId;
    private String authKey;

    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<PaladinsSession> sessions;
    private OffsetDateTime createdIn;

    public PaladinsDeveloper() {
        this.sessions = new HashSet<>();
        this.createdIn = OffsetDateTime.now();
    }

    public PaladinsDeveloper(APIUserModel user, String authKey) {
        this.devId = user;
        this.authKey = authKey;
        this.sessions = new HashSet<>();
        this.createdIn = OffsetDateTime.now(Clock.systemUTC());
    }

    public APIUserModel getUser(){
        return devId;
    }

    public int getDevId() {
        return devId.getDevId();
    }

    public String getAuthKey() {
        return authKey;
    }

    public Set<PaladinsSession> getSessions() {
        return sessions;
    }

    public Optional<PaladinsSession> getSession(String sessionId){
        return sessions.stream().filter(session -> session.getSessionId().equalsIgnoreCase(sessionId)).findFirst();
    }

    public void addSession(PaladinsSession session){
        this.sessions.add(session);
    }

    public void setSessions(Set<PaladinsSession> sessions) {
        this.sessions = sessions;
    }

    public OffsetDateTime getCreatedIn() {
        return createdIn;
    }
}
