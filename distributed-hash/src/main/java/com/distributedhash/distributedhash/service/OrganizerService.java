package com.distributedhash.distributedhash.service;

import com.distributedhash.distributedhash.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizerService {
    @Autowired
    StudentRepository studentRepository;

    public int registerStudent(String roolNo){
        return studentRepository.addStudent(roolNo);
    }

    public void occupyBlock(String roolNo, String hash){
        studentRepository.occupyBlock(roolNo, hash);
    }

    public void freeBlock(int blockNumber){
        studentRepository.freeBlock(blockNumber);
    }
}
