package me.sknz.api.paladins.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import me.sknz.api.paladins.paladins.models.APILanguage;
import me.sknz.api.paladins.paladins.models.champions.Champion;
import me.sknz.api.paladins.paladins.models.champions.Champions;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;

@Entity
@Table(name = "api_champions")
public class DatabaseChampions {

    @Id private Integer language;
    @Lob private String champions;
    private OffsetDateTime lastUpdate;

    public DatabaseChampions(APILanguage language, String champions) {
        this.language = language.getValue();
        this.champions = champions;
        this.lastUpdate = OffsetDateTime.now(Clock.systemUTC());
    }

    public DatabaseChampions() {
        this.lastUpdate = OffsetDateTime.now(Clock.systemUTC());
    }

    public APILanguage getLanguage() {
        return APILanguage.getByValue(language);
    }

    public OffsetDateTime getLastUpdate() {
        return lastUpdate;
    }

    public String getJsonString() {
        return champions;
    }

    public void setJson(String champions) {
        this.champions = champions;
    }

    public Optional<Champion> getChampion(int id) {
        for (JsonNode node : getChampionsNode()) {
            if (node.get("id").asInt() == id)
                return Optional.of(new Champion(node));
        }
        return Optional.empty();
    }

    public Optional<Champion> getChampion(String name) {
        if (name.matches("\\d+"))
            return getChampion(Integer.parseInt(name));

        for (JsonNode node : getChampionsNode()) {
            String cname = node.get("Name_English").asText();
            if (cname.trim().replace("'", "").equalsIgnoreCase(name.trim().replace("'", "")))
                return Optional.of(new Champion(node));
        }
        return Optional.empty();
    }

    public Champions getChampions() {
        return new Champions(getLanguage(), getChampionsNode());
    }

    public ArrayNode getChampionsNode() {
        try {
            return (ArrayNode) new ObjectMapper().readTree(champions);
        } catch (JsonProcessingException ignored) {}
        return null;
    }
}
