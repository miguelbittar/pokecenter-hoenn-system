package com.hoenn.pokecenter.mapper;

import com.hoenn.pokecenter.dto.request.NurseJoyProfileUpdateRequest;
import com.hoenn.pokecenter.dto.request.NurseJoyRequest;
import com.hoenn.pokecenter.dto.response.NurseJoyResponse;
import com.hoenn.pokecenter.entity.NurseJoy;

public final class NurseJoyMapper {

    private NurseJoyMapper(){}

    public static NurseJoy toEntity(NurseJoyRequest request){
        return new NurseJoy(
                request.name(),
                request.email(),
                request.city(),
                request.region(),
                request.role()
        );
    }
    public static NurseJoy toEntityForUpdate(NurseJoyProfileUpdateRequest request){
        return new NurseJoy(
                request.name(),
                request.email(),
                request.password(),
                request.city(),
                request.region(),
                request.role()
        );
    }

    public static NurseJoyResponse toResponse(NurseJoy nurseJoy){
        return new NurseJoyResponse(
                nurseJoy.getNurseJoyId(),
                nurseJoy.getName(),
                nurseJoy.getEmail(),
                nurseJoy.getCity(),
                nurseJoy.getRegion(),
                nurseJoy.getRole(),
                nurseJoy.getCreatedAt()
        );
    }
}
