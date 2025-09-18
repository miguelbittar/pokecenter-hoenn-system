package com.hoenn.pokecenter.controller;

import com.hoenn.pokecenter.dto.request.NurseJoyProfileUpdateByAdminRequest;
import com.hoenn.pokecenter.dto.request.NurseJoyProfileUpdateRequest;
import com.hoenn.pokecenter.dto.request.NurseJoyRequest;
import com.hoenn.pokecenter.dto.response.NurseJoyResponse;
import com.hoenn.pokecenter.entity.NurseJoy;
import com.hoenn.pokecenter.mapper.NurseJoyMapper;
import com.hoenn.pokecenter.service.NurseJoyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pokecenter/staff/nurse-joys")
public class NurseJoyController {

    private final NurseJoyService nurseJoyService;

    public NurseJoyController(NurseJoyService nurseJoyService) {
        this.nurseJoyService = nurseJoyService;
    }

    @PostMapping
    public ResponseEntity<NurseJoyResponse> registerNurseJoy(@Valid @RequestBody NurseJoyRequest request){
        NurseJoy savedNurseJoy = nurseJoyService.registerNurseJoy(NurseJoyMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(NurseJoyMapper.toResponse(savedNurseJoy));
    }

    @GetMapping
    public ResponseEntity<List<NurseJoyResponse>> getAllNurseJoys(){
        return ResponseEntity.ok(nurseJoyService.getAllNurseJoys()
                .stream()
                .map(NurseJoyMapper::toResponse)
                .toList());
    }

    @GetMapping("/{nurseJoyId}")
    public ResponseEntity<NurseJoyResponse> getNurseJoyByNurseJoyId(@PathVariable String nurseJoyId) {
        NurseJoy nurseJoy = nurseJoyService.findByNurseJoyId(nurseJoyId);
        return ResponseEntity.ok(NurseJoyMapper.toResponse(nurseJoy));
    }

    @PutMapping("/{nurseJoyId}")
    public ResponseEntity<NurseJoyResponse> updateNurseJoyProfileByAdmin(
            @PathVariable String nurseJoyId, @Valid @RequestBody NurseJoyProfileUpdateByAdminRequest request){
        NurseJoy updateNurseJoy = nurseJoyService.updateNurseJoyProfileByAdmin(nurseJoyId, NurseJoyMapper.toEntityForAdminUpdate(request));
        return ResponseEntity.ok(NurseJoyMapper.toResponse(updateNurseJoy));
    }

    @PutMapping("/{nurseJoyId}/profile")
    public ResponseEntity<NurseJoyResponse> updateOwnProfile(
            @PathVariable String nurseJoyId, @Valid @RequestBody NurseJoyProfileUpdateRequest request){
        NurseJoy updateNurseJoy = nurseJoyService.updateOwnProfile(nurseJoyId, NurseJoyMapper.toEntityForUpdate(request));
        return ResponseEntity.ok(NurseJoyMapper.toResponse(updateNurseJoy));
    }
}
