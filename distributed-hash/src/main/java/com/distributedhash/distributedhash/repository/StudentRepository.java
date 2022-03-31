package com.distributedhash.distributedhash.repository;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class StudentRepository {
    public static int totalBlock = 1024;
    List<Integer> freeBlocks;
    Map<String, Set<String>> studentToHash = new HashMap<>();
    Map<String, Set<String>> studentFailedBlock = new HashMap<>();

    public String addStudent(String roolNo){
        synchronized (freeBlocks){
            int size = freeBlocks.size();
            if(size == 0){
                System.out.println("ends free blocks");
                return "end";
            }
            int randomNumber = getRandomNumber(0,size);
            int occupiedBlock = freeBlocks.get(randomNumber);
            freeBlocks.remove(randomNumber);
            return Integer.toString(occupiedBlock);
        }
    }

    public void occupyBlock (String roolNo, String hash){
        System.out.println("free blocks size "+ freeBlocks.size());
        if (studentToHash.containsKey(roolNo)){
            studentToHash.get(roolNo).add(hash);
        } else {
            Set<String> hashes = new HashSet<>();
            hashes.add(hash);
            studentToHash.put(roolNo, hashes);
        }
    }
    public void freeBlock(String roolNo, String blockNumber){
        freeBlocks.add(Integer.parseInt(blockNumber));
        if (studentFailedBlock.containsKey(roolNo)){
            studentFailedBlock.get(roolNo).add(blockNumber);
        } else {
            Set<String> blocks = new HashSet<>();
            blocks.add(blockNumber);
            studentFailedBlock.put(roolNo, blocks);
        }
    }

    public void initializeBlock(){
        System.out.println("initializing..");
        freeBlocks = IntStream.range(1,totalBlock+1).boxed().collect(Collectors.toList());
    }
    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
