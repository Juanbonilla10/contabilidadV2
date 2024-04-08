/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.accounting.V2.service;

import com.accounting.V2.model.CardsModel;
import com.accounting.V2.model.UsersModel;
import com.accounting.V2.repository.CardsRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author L E N O V O
 */
@Service
public class CardsService {

    @Autowired
    private CardsRepository cardsRepository;

    @Autowired
    private UsersService usersService;

    public List<CardsModel> getAllCards() {
        return cardsRepository.getAllCards();
    }

    public CardsModel getByIdCardUser(String idCard, String mail) {
        try {
            System.out.println("Obteniedno los datos para ".concat(idCard.toString()));
            Optional<UsersModel> user = usersService.getByEmail(mail);
            if (user.isPresent()) {
                System.out.println("Se encontro usuario con id ".concat(mail));
                Integer userCard = cardsRepository.idStringCard(idCard);
                if (user.get().getIdusers().equals(userCard)) {
                    System.out.println("Se encontrarón datos homogeneos para card ");
                    return cardsRepository.getByIdCard(idCard);
                } else {
                    System.out.println("El id "
                            + user.get().getIdusers()
                            + " es disinto de " + userCard);
                    return new CardsModel(); 
                }
            }else{
                System.out.println("No se encontro usuario para este mail " .concat(mail));
                return new CardsModel(); 
            }
        } catch (Exception e) {
            System.out.println("Error al obtner los dato para ".concat(idCard.toString()));
            return new CardsModel();
        }
    }
    
    public List<CardsModel> getByIdCardsModel(String mail){
         try {
            System.out.println("Obteniedno los datos para ".concat(mail));
            Optional<UsersModel> user = usersService.getByEmail(mail);
            if (user.isPresent()) {
                System.out.println("Se encontro usuario con id ".concat(mail));
                return cardsRepository.getCardByUser(user.get().getIdusers());
            }else{
                System.out.println("No se encontro usuario para este mail " .concat(mail));
                return (List<CardsModel>) new CardsModel(); 
            }
        } catch (Exception e) {
            System.out.println("Error al obtner los dato para ".concat(mail));
            return (List<CardsModel>) new CardsModel();
        }
    }

}
