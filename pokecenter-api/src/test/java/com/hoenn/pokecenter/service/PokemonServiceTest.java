package com.hoenn.pokecenter.service;

import com.hoenn.pokecenter.components.BusinessIdGenerator;
import com.hoenn.pokecenter.dto.request.PokemonUpdateRequest;
import com.hoenn.pokecenter.dto.response.RVPValidationResponse;
import com.hoenn.pokecenter.entity.NurseJoy;
import com.hoenn.pokecenter.entity.Pokemon;
import com.hoenn.pokecenter.enums.NurseJoyRole;
import com.hoenn.pokecenter.enums.PokemonCondition;
import com.hoenn.pokecenter.enums.PokemonStatus;
import com.hoenn.pokecenter.exception.custom.*;
import com.hoenn.pokecenter.external.PokeApiClient;
import com.hoenn.pokecenter.external.PokemonLeagueClient;
import com.hoenn.pokecenter.repository.PokemonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokemonServiceTest {

    @Mock
    private PokemonRepository pokemonRepository;

    @Mock
    private PokeApiClient pokeApiClient;

    @Mock
    private PokemonLeagueClient pokemonLeagueClient;

    @Mock
    private BusinessIdGenerator businessIdGenerator;

    @Mock
    private NurseJoyService nurseJoyService;

    @InjectMocks
    private PokemonService pokemonService;


    @Test
    @DisplayName("Should register Pokemon successfully for Hoenn trainer")
    void registerPokemonCase1() {
        Pokemon mockPokemon = createTestPokemon("pikachu", "HN123456");

        when(pokemonLeagueClient.validateTrainerId("HN123456")).thenReturn(true);
        when(pokeApiClient.validateSpecies("pikachu")).thenReturn(true);
        when(businessIdGenerator.generateSequentialPokemonId()).thenReturn("PKM000001");
        when(pokemonRepository.save(any(Pokemon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pokemon result = pokemonService.registerPokemon(mockPokemon);

        assertThat(result.getPokemonId()).isEqualTo("PKM000001");
        assertThat(result.getStatus()).isEqualTo(PokemonStatus.ADMISSION);
        assertThat(result.getSpecies()).isEqualTo("pikachu");

        verify(pokemonLeagueClient).validateTrainerId("HN123456");
        verify(pokeApiClient).validateSpecies("pikachu");
        verify(pokemonRepository).save(any(Pokemon.class));
    }

    @Test
    @DisplayName("Should throw TrainerNotRegisteredException when trainer not registered")
    void registerPokemonCase2() {
        Pokemon mockPokemon = createTestPokemon("pikachu", "HN123456");

        when(pokemonLeagueClient.validateTrainerId("HN123456")).thenReturn(false);

        assertThrows(TrainerNotRegisteredException.class,
                () -> pokemonService.registerPokemon(mockPokemon));

        verify(pokemonLeagueClient).validateTrainerId("HN123456");
    }

    @Test
    @DisplayName("Should throw InvalidPokemonSpeciesException when species is invalid")
    void registerPokemonCase3() {
        Pokemon mockPokemon = createTestPokemon("pikachu", "HN123456");

        when(pokemonLeagueClient.validateTrainerId("HN123456")).thenReturn(true);
        when(pokeApiClient.validateSpecies("pikachu")).thenReturn(false);

        assertThrows(InvalidPokemonSpeciesException.class,
                () -> pokemonService.registerPokemon(mockPokemon));

        verify(pokeApiClient).validateSpecies("pikachu");
    }

    @Test
    @DisplayName("Should throw InvalidRVPException when foreign trainer has no valid RVP")
    void registerPokemonCase4() {
        Pokemon mockPokemon = createTestPokemon("pikachu", "KA123456");
        RVPValidationResponse invalidRvp = createInvalidRVPResponse("EXPIRED", "RVP expired");

        when(pokemonLeagueClient.validateTrainerId("KA123456")).thenReturn(true);
        when(pokemonLeagueClient.validateTrainerRVP("KA123456", "HOENN")).thenReturn(invalidRvp);

        assertThrows(InvalidRVPException.class,
                () -> pokemonService.registerPokemon(mockPokemon));

        verify(pokemonLeagueClient).validateTrainerId("KA123456");
        verify(pokemonLeagueClient).validateTrainerRVP("KA123456", "HOENN");
    }

    @Test
    @DisplayName("Should return Pokemon when Pokemon ID exists")
    void findByPokemonIdCase1() {
        Pokemon mockPokemon = createTestPokemon("pikachu", "KA123456");
        mockPokemon.setPokemonId("PKM000001");

        when(pokemonRepository.findByPokemonIdAndIsDeletedFalse(mockPokemon.getPokemonId())).thenReturn(Optional.of(mockPokemon));

        Pokemon result = pokemonService.findByPokemonId(mockPokemon.getPokemonId());

        assertThat(result).isEqualTo(mockPokemon);
        verify(pokemonRepository).findByPokemonIdAndIsDeletedFalse(mockPokemon.getPokemonId());
    }

    @Test
    @DisplayName("Should throw PokemonNotFoundException when Pokemon not found")
    void findByPokemonIdCase2(){
        String pokemonId = "PKM000001";

        when(pokemonRepository.findByPokemonIdAndIsDeletedFalse(pokemonId)).thenReturn(Optional.empty());

        assertThrows(PokemonNotFoundException.class,
                () -> pokemonService.findByPokemonId(pokemonId));

        verify(pokemonRepository).findByPokemonIdAndIsDeletedFalse(pokemonId);
    }

    @Test
    @DisplayName("Should update Pokemon profile successfully with valid data")
    void updatePokemonProfileCase1() {
        Pokemon mockPokemon = createTestPokemon("charmeleon", "HN123456");
        mockPokemon.setPokemonId("PKM000001");

        NurseJoy mockNurseJoy = createTestNurseJoy();
        mockNurseJoy.setNurseJoyId("JOY000001");

        PokemonUpdateRequest updateRequest = createUpdateRequest("charizard");

        when(pokemonRepository.findByPokemonIdAndIsDeletedFalse("PKM000001")).thenReturn(Optional.of(mockPokemon));
        when(pokeApiClient.validateSpecies("charizard")).thenReturn(true);
        when(nurseJoyService.findByNurseJoyId("JOY000001")).thenReturn(mockNurseJoy);
        when(pokemonRepository.save(any(Pokemon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pokemon result = pokemonService.updatePokemonProfile("PKM000001", updateRequest);

        assertThat(result.getSpecies()).isEqualTo("charizard");
        assertThat(result.getResponsibleJoy()).isEqualTo(mockNurseJoy);
        assertThat(result.getName()).isEqualTo("Pokemon Name");

        verify(pokeApiClient).validateSpecies("charizard");
        verify(nurseJoyService).findByNurseJoyId("JOY000001");
    }

    @Test
    @DisplayName("Should throw InvalidPokemonSpeciesException when updating to invalid species")
    void updatePokemonProfileCase2() {
        Pokemon mockPokemon = createTestPokemon("charmeleon", "HN123456");
        mockPokemon.setPokemonId("PKM000001");

        PokemonUpdateRequest updateRequest = createUpdateRequest("Invalid Specie");

        when(pokemonRepository.findByPokemonIdAndIsDeletedFalse("PKM000001")).thenReturn(Optional.of(mockPokemon));
        when(pokeApiClient.validateSpecies(updateRequest.species())).thenReturn(false);

        assertThrows(InvalidPokemonSpeciesException.class,
                () -> pokemonService.updatePokemonProfile("PKM000001", updateRequest));
    }

    @Test
    @DisplayName("Should throw NurseJoyNotFoundException when responsible Joy ID is invalid")
    void updatePokemonProfileCase3() {
        Pokemon mockPokemon = createTestPokemon("charmeleon", "HN123456");
        mockPokemon.setPokemonId("PKM000001");

        PokemonUpdateRequest updateRequest = createUpdateRequest("charizard");

        when(pokemonRepository.findByPokemonIdAndIsDeletedFalse("PKM000001")).thenReturn(Optional.of(mockPokemon));
        when(pokeApiClient.validateSpecies(updateRequest.species())).thenReturn(true);
        when(nurseJoyService.findByNurseJoyId("JOY000001")).thenThrow(new NurseJoyNotFoundException("Joy not found"));

        assertThrows(NurseJoyNotFoundException.class,
                () -> pokemonService.updatePokemonProfile("PKM000001", updateRequest));
    }

    @Test
    @DisplayName("Should soft delete Pokemon successfully")
    void deleteByPokemonIdCase1() {
        Pokemon mockPokemon = createTestPokemon("pikachu", "HN123456");
        mockPokemon.setPokemonId("PKM000001");

        when(pokemonRepository.findByPokemonIdAndIsDeletedFalse("PKM000001")).thenReturn(Optional.of(mockPokemon));
        when(pokemonRepository.save(any(Pokemon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        pokemonService.deleteByPokemonId("PKM000001");

        assertThat(mockPokemon.getDeleted()).isTrue();
        assertThat(mockPokemon.getDeletedAt()).isNotNull();
        verify(pokemonRepository).findByPokemonIdAndIsDeletedFalse("PKM000001");
        verify(pokemonRepository).save(mockPokemon);
    }

    private Pokemon createTestPokemon(String species, String trainerId) {
        Pokemon pokemon = new Pokemon();
        pokemon.setName("Test Pokemon");
        pokemon.setSpecies(species);
        pokemon.setTrainerId(trainerId);
        pokemon.setCondition(PokemonCondition.FAINTED);
        return pokemon;
    }

    private NurseJoy createTestNurseJoy(){
        NurseJoy nurseJoy = new NurseJoy();
        nurseJoy.setName("Amanda");
        nurseJoy.setEmail("amanda@test.com");
        nurseJoy.setCity("Rustboro City");
        nurseJoy.setRegion("HOENN");
        nurseJoy.setRole(NurseJoyRole.COMMON);
        return nurseJoy;
    }

    private PokemonUpdateRequest createUpdateRequest(String species) {
        return new PokemonUpdateRequest(
                "Pokemon Name",
                species,
                "KA123456",
                PokemonCondition.HEALTHY,
                PokemonStatus.TREATMENT,
                "JOY000001",
                null
        );
    }

    private RVPValidationResponse createInvalidRVPResponse(String status, String message) {
        return new RVPValidationResponse(false, status, null, message);
    }
    private RVPValidationResponse createValidRVPResponse() {
        return new RVPValidationResponse(true, "ACTIVE", LocalDate.now().plusDays(30), "Valid RVP found");
    }
}