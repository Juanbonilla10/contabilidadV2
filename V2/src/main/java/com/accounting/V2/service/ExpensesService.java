/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.accounting.V2.service;

import com.accounting.V2.model.AccountsModel;
import com.accounting.V2.model.AccounttypesModel;
import com.accounting.V2.model.BanksModel;
import com.accounting.V2.model.ExpensesModel;
import com.accounting.V2.model.FixedCostsModel;
import com.accounting.V2.model.FranchisesModel;
import com.accounting.V2.model.UsersModel;
import com.accounting.V2.model.response.ExpensesModelResponse;
import com.accounting.V2.repository.ExpensesRepository;
import com.accounting.V2.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author L E N O V O
 */
@Service
public class ExpensesService {

    @Autowired
    private ExpensesRepository expensesRepository;

    @Autowired
    private AccountTypesService accountTypeService;

    @Autowired
    private UsersService usersService;
    
    @Autowired
    private BanksService banksService;

    @Autowired
    private AccountsService accountsService;

    @Autowired
    private FranchisesService franchisesService;

    @Autowired
    private FixedCostsService fixedCostsService;

    public List<ExpensesModel> getAllExpenses() {
        return expensesRepository.getAllExpenses();
    }

    private Optional<ExpensesModel> getExopensesId(Integer idExpenses) {
        return expensesRepository.getExpeneseId(idExpenses);
    }

    public ExpensesModel saveExpenses(ExpensesModel expensesModel, String mail) {
        try {
            Optional<UsersModel> um = usersService.getByEmail(mail);
            if (um.isPresent()) {
                System.out.println("Usuario encontrado ".concat(um.get().getFirstName()));
                if (!expensesModel.getDescription_expense().isEmpty()) {
                    System.out.println("Creando gasto ");
                    Utils utils = new Utils();
                    expensesModel.setUsers_id(um.get().getIdusers());
                    expensesModel.setDate(utils.getSystemDate());
                    //Validación si la cuenta pertenece al usuario que peticiona
                    List<AccountsModel> accounts = accountsService.getAccountsByUser(mail);
                    if (accounts.stream().anyMatch(account -> account.getIdaccounts().equals(expensesModel.getAccounts_id()))) {
                        return expensesRepository.saveExpenses(expensesModel);
                    } else {
                        System.out.println("La cuenta no pertenece al usuario");
                        return new ExpensesModel();
                    }
                } else {
                    System.out.println("No existe datos basicos para crear el gasto");
                    return new ExpensesModel();
                }
            } else {
                System.out.println("El usuario no existe ".concat(mail));
                return new ExpensesModel();
            }
        } catch (Exception e) {
            System.out.println("Error al guardar el gasto ".concat(e.getMessage()));
            return new ExpensesModel();
        }

    }

    public List<ExpensesModelResponse> expensesAllUser(String mail) {
        try {
            Optional<UsersModel> um = usersService.getByEmail(mail);
            if (um.isPresent()) {
                System.out.println("Consultando los gastos del usuario  " .concat(mail));
                List<ExpensesModel> expenses = expensesRepository.getAllExpensesUser(um.get().getIdusers().toString());
                System.out.println("Result expenses " .concat(expenses.toString()));
                List<ExpensesModelResponse> expMdlRsp = new ArrayList<>();

                //Recorriendo los gastos consultados para setear información
                for (ExpensesModel exp : expenses) {
                    System.out.println("Consultando primer gasto " .concat(exp.toString()));
                    ExpensesModelResponse expMdRsp = new ExpensesModelResponse();
                    Optional<AccountsModel> accountUser = accountsService.getAccountByAccount(exp.getAccounts_id(), mail);
                    System.out.println("Obteniendo la cuenta del user " .concat(accountUser.toString()));
                    Optional<AccounttypesModel> accTyp = accountTypeService.getAccountType(accountUser.get().getAccountTypes_id());
                    System.out.println("Obteniendo los tipos de cuenta " .concat(accTyp.toString()));
                    List<FranchisesModel> franchisesList = franchisesService.getAllFranchises();
                    System.out.println("Obteniendo las franquicias " .concat(franchisesList.toString()));
                    List<BanksModel> banks = banksService.getAllBanks();

                    expMdRsp.setIdegresos(exp.getIdegresos());
                    expMdRsp.setDate(exp.getDate());
                    expMdRsp.setEgress_value(exp.getEgress_value());
                    
                    for (BanksModel bank : banks) {
                        if(bank.getIdbanks().equals(accountUser.get().getBanks_id())){
                            expMdRsp.setBanksAccount(bank.getDescription());
                        }
                    }

                    if (exp.getDescription_expense().isBlank()) {
                        for (FixedCostsModel fix : fixedCostsService.getAllFixedCosts()) {
                            if (fix.getIdfixedCosts().equals(exp.getFixedcosts_id())) {
                                expMdRsp.setDescription_expense(fix.getDescription());
                            }
                        }
                    } else {
                        expMdRsp.setDescription_expense(exp.getDescription_expense());
                    }
                    expMdRsp.setAccountsDescription(accTyp.get().getDescription());
                    for (FranchisesModel fran : franchisesList) {
                        if (fran.getIdfranchises().equals(accountUser.get().getFranchises_id())) {
                            expMdRsp.setFranchisesDescription(fran.getDescription());
                        }
                    }
                    
                    expMdlRsp.add(expMdRsp);

                }
                
                return expMdlRsp;

            } else {
                System.out.println("No existe el usuario");
                return (List<ExpensesModelResponse>) new ExpensesModelResponse();
            }
        } catch (Exception e) {
            System.out.println("Error al obtener los gastos del usuario");
            return (List<ExpensesModelResponse>) new ExpensesModelResponse();
        }
    }

    public void deleteExpenses(Integer idExpenses, String mail) {
        try {
            Optional<UsersModel> um = usersService.getByEmail(mail);
            if (um.isPresent()) {
                System.out.println("Usuario encontrado ".concat(um.get().getFirstName()));
                if (expensesRepository.getExpeneseId(idExpenses).get().getUsers_id()
                        .equals(um.get().getIdusers())) {
                    System.out.println("Se puede borrar el gasto ".concat(idExpenses.toString()));
                    expensesRepository.deleteExpenses(idExpenses);
                } else {
                    System.out.println("No se puede eliminar el gasto porque no le pertenece");
                }
            } else {
                System.out.println("El usuario no existe ".concat(mail));
            }
        } catch (Exception e) {
            System.out.println("Error al eliminar el gasto ".concat(e.getMessage()));
        }

    }

}
