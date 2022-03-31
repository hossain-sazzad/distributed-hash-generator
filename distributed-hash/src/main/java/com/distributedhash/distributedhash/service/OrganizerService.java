package com.distributedhash.distributedhash.service;

import com.distributedhash.distributedhash.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizerService {
    @Autowired
    StudentRepository studentRepository;

    public String registerStudent(String roolNo){
        return studentRepository.addStudent(roolNo);
    }

    public void occupyBlock(String roolNo, String hash){
        studentRepository.occupyBlock(roolNo, hash);
    }

    public void freeBlock(String roolNo, String blockNumber){
        studentRepository.freeBlock(roolNo, blockNumber);
    }

    public void initializeBlock(){
        studentRepository.initializeBlock();
    }
}
