/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.accounting.V2.service;

import com.accounting.V2.model.AccountsModel;
import com.accounting.V2.model.UsersModel;
import com.accounting.V2.repository.AccountsRepository;
import com.accounting.V2.utils.EnumVarsState;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author L E N O V O
 */
@Service
public class AccountsService {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private UsersService usersService;

    public List<AccountsModel> getAllAccounts() {
        return accountsRepository.getAllAccounts();
    }
    
    public AccountsModel saveAccountByUser(AccountsModel accountsModel,String mail){
        try {
            System.out.println("Entrando a guardar la cuenta");
            Optional<UsersModel> user = usersService.getByEmail(mail);
             if (user.isPresent()) {
                 System.out.println("El usuario si existe " .concat(mail));
                 //Validamos que no exista ya la cuenta con el numero de cuenta que se pasa
                 AccountsModel accountByUser = getAccountByUser(accountsModel.getAccount_number(),mail);
                 System.out.println("Datos para valdiar si la cuenta existe o no " .concat(accountByUser.toString()));
                 if(accountByUser.getCard_number() == null){
                     System.out.println("Se puede crear la cuenta ");
                     accountsModel.setIdaccounts(0);
                     accountsModel.setUsers_id(user.get().getIdusers());
                     return accountsRepository.saveAccountsModel(accountsModel);
                 }else{
                     System.out.println("La cuenta ya existe");
                     return new AccountsModel();
                 }
             }else{
                 System.out.println("El usuario no existe " .concat(mail));
                 return new AccountsModel();
             }
        } catch (Exception e) {
            System.out.println("Error no se puede crear la cuenta");
            return new AccountsModel();
        }
    }

    public List<AccountsModel> getAccountsByUser(String mail) {
        try {
            System.out.println("Entrando a obtener las cuentas del usuario ".concat(mail));
            Optional<UsersModel> user = usersService.getByEmail(mail);
            if (user.isPresent()) {
                System.out.println("Obteniendo los datos para el usuario ".concat(user.get().getFirstName()));
                return accountsRepository.getAllAccountsByUser(user.get().getIdusers());
            } else {
                return (List<AccountsModel>) new AccountsModel();
            }
        } catch (Exception e) {
            System.out.println("Error al obtener la cuenta asociada al usuario ");
            return (List<AccountsModel>) new AccountsModel();
        }
    }

    public AccountsModel getAccountByUser(String idAccount, String mail) {
        try {
            System.out.println("Entrando a obtener la cuenta del usuario ".concat(mail));
            Optional<UsersModel> user = usersService.getByEmail(mail);
            if (user.isPresent()) {
                System.out.println("Obteniendo los datos para el usuario ".concat(user.get().getFirstName()));
                AccountsModel account = accountsRepository.getAccountByUser(idAccount);
                System.out.println("Dta : " .concat(account.toString()));
                if (account.getUsers_id().equals(user.get().getIdusers())) {
                    System.out.println("Devolviendo dato de account " .concat(account.toString()));
                    return account;
                } else {
                    System.out.println("Devolviendo dato de account pero vacio");
                    return new AccountsModel();
                }
            } else {
                return new AccountsModel();
            }
        } catch (Exception e) {
            System.out.println("Error al obtener la cuenta asociada al usuario ");
            return new AccountsModel();
        }
    }

    public Integer deleteAccount(String accountNumber, String email) {
        try {
            System.out.println("Entrando a eliminar la cuenta ".concat(accountNumber));
            if (!accountNumber.isEmpty()) {
                AccountsModel accountValidate = this.getAccountByUser(accountNumber, email);
                System.out.println("Cuenta informada " .concat(accountValidate.toString()));
                if (accountValidate.getAccount_number().equalsIgnoreCase(accountNumber)) {
                    System.out.println("Borrando la cuenta ".concat(accountValidate.getAccount_number()));
                    accountsRepository.deleteAccount(accountValidate.getIdaccounts());
                    return EnumVarsState.CREATE_200.getCodigo();
                } else {
                    System.out.println("No se puede borrar la cuenta porque no pertenece al usuario");
                    return EnumVarsState.ERROR_403.getCodigo();
                }
            } else {
                return EnumVarsState.ERROR_400.getCodigo();
            }
        } catch (Exception e) {
            System.out.println("Error al borrar la cuenta".concat(e.getMessage()));
            return EnumVarsState.ERROR_500.getCodigo();
        }
    }
    
    public Optional<AccountsModel> getAccountByAccount(Integer idAccount,String mail){
        try {
            System.out.println("Entrando a obtener la cuenta del usuario ".concat(mail));
            Optional<UsersModel> user = usersService.getByEmail(mail);
            if(user.isPresent()){
                return accountsRepository.getByAccountUser(idAccount);
            }else{
                return  Optional.empty() ;
            }
        } catch (Exception e) {         
              System.out.println("Errro al obtener la cuenta " .concat(e.getMessage()));
              return  Optional.empty() ;
        }
    }

}
