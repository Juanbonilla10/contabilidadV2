/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.accounting.V2.service;

import com.accounting.V2.model.BanksModel;
import com.accounting.V2.repository.BanksRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author L E N O V O
 */
@Service
public class BanksService {
    
    @Autowired
    private BanksRepository banksRepository;
    
    public List<BanksModel> getAllBanks(){
        return banksRepository.getAllBanks();
    }
    
}
