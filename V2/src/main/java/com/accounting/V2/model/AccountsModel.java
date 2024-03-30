/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.accounting.V2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author L E N O V O
 */

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Accounts")
@Table(name = "accounts")
public class AccountsModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idaccounts")
    private Integer idaccounts;
    @Column(name = "account_number",length = 45)
    private String account_number;
    @Column(name = "card_number",length = 45)
    private String card_number;
    @Column(name = "balance",length = 45)
    private String balance;
    @Column(name = "banks_id")
    private Integer banks_id;
    @Column(name = "accounttypes_id")
    private Integer accountTypes_id;
    @Column(name = "franchises_id")
    private Integer franchises_id;
    @Column(name = "users_id")
    private Integer users_id;
}
