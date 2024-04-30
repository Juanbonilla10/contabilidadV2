/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.accounting.V2.service;

import com.accounting.V2.model.AccountsModel;
import com.accounting.V2.model.IncomesModel;
import com.accounting.V2.model.UsersModel;
import com.accounting.V2.repository.IncomesRepository;
import com.accounting.V2.utils.Utils;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author L E N O V O
 */
@Service
public class IncomesService {
    
    @Autowired
    private IncomesRepository incomesRepository;
    
    @Autowired
    private UsersService usersService; 
    
    @Autowired
    private AccountsService accountsService;
    
    public List<IncomesModel> getAllIncomes(){
        return incomesRepository.getAllIncomes();
    }
    
    public IncomesModel saveIncomes(IncomesModel incomesModel,String mail){
         try {
             Optional<UsersModel> um = usersService.getByEmail(mail);
            if (um.isPresent()) {
                System.out.println("Usuario encontrado ".concat(um.get().getFirstName()));
                if (!incomesModel.getMessage().isEmpty() & !incomesModel.getIncome_value().isEmpty()) {
                    System.out.println("Creando ingreso ");
                    Utils utils = new Utils();
                    incomesModel.setUsers_id(um.get().getIdusers());
                    incomesModel.setDate(utils.getSystemDate());
                    //Validación si la cuenta pertenece al usuario que peticiona
                    List<AccountsModel> accounts = accountsService.getAccountsByUser(mail);
                    if (accounts.stream().anyMatch(account -> account.getIdaccounts().equals(incomesModel.getAccounts_id()))) {
                        return incomesRepository.saveIncomes(incomesModel);
                    } else {
                        System.out.println("La cuenta no pertenece al usuario para poder ingresar el 'ingreso' ");
                        return new IncomesModel();
                    }
                } else {
                    System.out.println("No existe datos basicos para crear el gasto");
                    return new IncomesModel();
                }
            } else {
                System.out.println("El usuario no existe ".concat(mail));
                return new IncomesModel();
            }
        } catch (Exception e) {
            System.out.println("Error al guardar el ingreso ".concat(e.getMessage()));
            return new IncomesModel();
        }
    }
    
    public void deleteIncomes(Integer idIncomes,String mail){
         try {
            Optional<UsersModel> um = usersService.getByEmail(mail);
            if (um.isPresent()) {
                System.out.println("Usuario encontrado ".concat(um.get().getFirstName()));
                if (incomesRepository.getIncomesId(idIncomes).get().getUsers_id()
                        .equals(um.get().getIdusers())){
                    System.out.println("Se puede borrar el ingreso " .concat(idIncomes.toString()));
                    incomesRepository.deleteIncomesId(idIncomes);
                } else {
                    System.out.println("No se puede eliminar el ingreso porque no le pertenece");             
                }
            } else {
                System.out.println("El usuario no existe ".concat(mail));
            }
        } catch (Exception e) {
            System.out.println("Error al eliminar el ingreso ".concat(e.getMessage()));
        }
    
    }
    
}
