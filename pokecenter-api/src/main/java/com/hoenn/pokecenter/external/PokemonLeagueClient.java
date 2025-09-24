package com.hoenn.pokecenter.external;

import com.hoenn.pokecenter.dto.response.RVPValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class PokemonLeagueClient {

    private static final Logger log = LoggerFactory.getLogger(PokemonLeagueClient.class);

    @Value("${pokecenter-hoenn.integrations.pokemon-league.base-url}")
    private String pokemonLeagueBaseUrl;

    private final RestTemplate restTemplate;

    public PokemonLeagueClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validateTrainerId (String trainerId){
        try {
            String url = pokemonLeagueBaseUrl + "/pokemon-league/trainers/" + trainerId;
            restTemplate.getForObject(url, Object.class);
            log.debug("Trainer ID '{}' validated successfully via Pokemon League", trainerId);
            return true;

        } catch (HttpClientErrorException.NotFound e){
            log.debug("Trainer ID '{}' not found in Pokemon League database", trainerId);
            return false;
        }
        catch (Exception e) {
        log.warn("Error validating trainer ID '{}': {}", trainerId, e.getMessage());
        return false;
        }
    }

    public RVPValidationResponse validateTrainerRVP(String trainerId, String targetRegion) {
        try {
            String url = pokemonLeagueBaseUrl + "/pokemon-league/rvp/" + trainerId + "/status?targetRegion=" + targetRegion;
            RVPValidationResponse response = restTemplate.getForObject(url, RVPValidationResponse.class);
            log.debug("RVP validation for trainer '{}' targeting '{}': {}", trainerId, targetRegion, response);
            return response;
        } catch (Exception e) {
            log.warn("Error validating RVP for trainer '{}': {}", trainerId, e.getMessage());
            return createNoRVPResponse(trainerId, targetRegion);
        }
    }

    private RVPValidationResponse createNoRVPResponse(String trainerId, String targetRegion) {
        return new RVPValidationResponse(
                false,
                null,
                null,
                "Unable to validate RVP - Pokemon League service unavailable"
        );
    }
}