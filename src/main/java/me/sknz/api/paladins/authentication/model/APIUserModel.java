package me.sknz.api.paladins.authentication.model;

import me.sknz.api.paladins.model.APIUserRegister;
import me.sknz.api.paladins.model.PaladinsDeveloper;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Collection;

@Entity
@Table(name = "api_users")
public class APIUserModel implements UserDetails {

    @Id
    private int devId;
    private String password;
    @CreatedDate
    private OffsetDateTime createdAt;
    @LastModifiedDate
    private OffsetDateTime lastUpdate;
    @OneToOne(cascade = CascadeType.ALL)
    private PaladinsDeveloper developer;

    public APIUserModel() {
        this.createdAt = OffsetDateTime.now(Clock.systemUTC());
        this.lastUpdate = OffsetDateTime.now(Clock.systemUTC());
    }

    public APIUserModel(APIUserRegister userRegister) {
        this.devId = userRegister.getDevId();
        this.password = userRegister.getPassword();
        this.createdAt = OffsetDateTime.now(Clock.systemUTC());
        this.lastUpdate = OffsetDateTime.now(Clock.systemUTC());
    }

    public int getDevId() {
        return devId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setDevId(int devId) {
        this.devId = devId;
    }
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(OffsetDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return String.valueOf(devId);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "APIUserModel{" +
                "devId=" + devId +
                ", createdAt=" + createdAt +
                ", lastUpdate=" + lastUpdate +
                ", developer=" + developer +
                '}';
    }

    public PaladinsDeveloper getDeveloper() {
        return developer;
    }

    public APIUserModel setDeveloper(PaladinsDeveloper developer) {
        this.developer = developer;
        return this;
    }
}
