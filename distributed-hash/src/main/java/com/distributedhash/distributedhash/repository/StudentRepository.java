package com.distributedhash.distributedhash.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class StudentRepository {
    public static int totalBlock = 1024;
    List<Integer> freeBlocks = IntStream.range(1,totalBlock+1).boxed().collect(Collectors.toList());
    Map<String, Set<String>> studentToHash = new HashMap<>();

    public int addStudent(String roolNo){
        synchronized (this){
            int size = freeBlocks.size();
            int randomNumber = ((int)Math.random()) % size;
            int occupiedBlock = freeBlocks.get(randomNumber);
            freeBlocks.remove(occupiedBlock);
            return occupiedBlock;
        }
    }

    public void occupyBlock (String roolNo, String hash){
        if (studentToHash.containsKey(roolNo)){
            studentToHash.get(roolNo).add(hash);
        } else {
            studentToHash.put(roolNo, Set.of(hash));
        }
    }
    public void freeBlock(int blockNumber){
        freeBlocks.add(blockNumber);
    }
}
