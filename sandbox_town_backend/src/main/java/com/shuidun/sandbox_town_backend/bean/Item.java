package com.shuidun.sandbox_town_backend.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {
    private String id;
    private String name;
    private String description;
    private long basicPrice;
    private long basicRarity;
    private boolean usable;
    private long moneyInc;
    private long expInc;
    private long levelInc;
    private long hungerInc;
    private long hpInc;
    private long attackInc;
    private long defenseInc;
    private long speedInc;
}