package com.distributedhash.distributedhash.controller;

import com.distributedhash.distributedhash.repository.StudentRepository;
import com.distributedhash.distributedhash.service.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrganizerController {

    @Autowired
    OrganizerService organizerService;

    @GetMapping("/{roolNo}")
    public int registerStudent(@PathVariable String roolNo){
        return organizerService.registerStudent(roolNo);
    }

    @GetMapping("/success/{roolNo}/{hash}")
    public void addMatchingBlock(@PathVariable String roolNo, @PathVariable String hash){
        organizerService.occupyBlock(roolNo, hash);
    }

    public void freeBlock(int blockNumber){
        organizerService.freeBlock(blockNumber);
    }
}
