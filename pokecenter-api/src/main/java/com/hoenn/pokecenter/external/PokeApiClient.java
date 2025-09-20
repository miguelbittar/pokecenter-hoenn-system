package com.hoenn.pokecenter.external;

import com.hoenn.pokecenter.entity.Pokemon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Component
public class PokeApiClient {

    private static final Logger log = LoggerFactory.getLogger(PokeApiClient.class);
    private static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2";
    private final RestTemplate restTemplate;

    public PokeApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validateSpecies (String species){
        try {
            String url = POKEAPI_BASE_URL + "/pokemon-species/" + species.toLowerCase();
            restTemplate.getForObject(url, Object.class);
            log.debug("Species '{}' validated successfully via API", species);
            return true;

        } catch (HttpClientErrorException.NotFound e) {
            log.debug("Species '{}' not found in PokéAPI", species);
            return false;

        } catch (Exception e) {
            log.warn("Error validating species '{}': {}", species, e.getMessage());
            return false;
        }
    }
}
