package com.hoenn.pokecenter.controller;

import com.hoenn.pokecenter.dto.request.NurseJoyRequest;
import com.hoenn.pokecenter.dto.response.NurseJoyResponse;
import com.hoenn.pokecenter.entity.NurseJoy;
import com.hoenn.pokecenter.mapper.NurseJoyMapper;
import com.hoenn.pokecenter.service.NurseJoyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
