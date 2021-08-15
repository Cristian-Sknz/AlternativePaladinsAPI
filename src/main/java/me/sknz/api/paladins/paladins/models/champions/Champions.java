package me.sknz.api.paladins.paladins.models.champions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import me.sknz.api.paladins.paladins.models.APILanguage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class Champions implements Iterable<Champion> {

    public APILanguage language;
    public List<Champion> champions;

    public Champions(APILanguage language, ArrayNode array) {
        this.language = language;
        this.champions = new ArrayList<>();
        for (JsonNode node : array){
            this.champions.add(new Champion(node));
        }
    }

    public Champions(APILanguage language, List<Champion> champions) {
        this.language = language;
        this.champions = champions;
    }

    @NotNull
    @Override
    public Iterator<Champion> iterator() {
        return champions.iterator();
    }

    public Stream<Champion> stream() {
        return champions.stream();
    }
}
