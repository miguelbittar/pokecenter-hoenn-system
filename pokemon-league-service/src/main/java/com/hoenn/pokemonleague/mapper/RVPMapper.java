package com.hoenn.pokemonleague.mapper;

import com.hoenn.pokemonleague.dto.response.RVPResponse;
import com.hoenn.pokemonleague.dto.response.RVPStatusResponse;
import com.hoenn.pokemonleague.entity.RVP;

public final class RVPMapper {

    private RVPMapper() {}

    public static RVPResponse toResponse(RVP rvp) {
        return new RVPResponse(
                rvp.getRvpId(),
                rvp.getTrainer().getTrainerId(),
                rvp.getTrainer().getName(),
                rvp.getTargetRegion(),
                rvp.getIssuingCity().getAuthorityName(),
                rvp.getStatus(),
                rvp.getIssueDate(),
                rvp.getExpiryDate(),
                rvp.getCreatedAt()
        );
    }

    public static RVPStatusResponse toStatusResponse(RVP rvp) {
        return new RVPStatusResponse(
                true,
                rvp.getStatus().toString(),
                rvp.getExpiryDate().toString(),
                "Valid RVP found"
        );
    }

    public static RVPStatusResponse toNoRVPResponse() {
        return new RVPStatusResponse(
                false,
                null,
                null,
                "No valid RVP found for target region"
        );
    }
}
