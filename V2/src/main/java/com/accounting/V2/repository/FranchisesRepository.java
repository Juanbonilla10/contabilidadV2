/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.accounting.V2.repository;

import com.accounting.V2.model.FranchisesModel;
import com.accounting.V2.repository.crud.FranchisesCrudRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author L E N O V O
 */

@Repository
public class FranchisesRepository {
    
    @Autowired
    private FranchisesCrudRepository franchisesCrudRepository;
    
    public List<FranchisesModel> getAll(){
        return franchisesCrudRepository.findAll();
    }
    
}
