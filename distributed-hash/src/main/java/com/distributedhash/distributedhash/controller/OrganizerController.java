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
    public String registerStudent(@PathVariable String roolNo){
        System.out.println("rool no -------------- " + roolNo);
        return organizerService.registerStudent(roolNo);
    }

    @GetMapping("/success/{roolNo}/{hash}")
    public void addMatchingBlock(@PathVariable String roolNo, @PathVariable String hash){
        organizerService.occupyBlock(roolNo, hash);
    }
    @GetMapping("/fail/{roolNo}/{blockNumber}")
    public void freeBlock(@PathVariable String roolNo, @PathVariable String blockNumber){
        organizerService.freeBlock(roolNo, blockNumber);
    }
}
