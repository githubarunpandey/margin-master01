package com.margin.entity;

public class LotSize {

    public final double getLotSize(String lot)
    {
        double size = 0;

        switch (lot)
        {
            case "GOLDPETAL":
            case "SILVERMIC":
                size=1;
                break;
            case "SILVERM":
                size=5;
                break;
            case "GOLDGUINEA":
                size=8;
                break;
            case "GOLDM":
                size=10;
                break;
            case "COTTON":
            case "COTTONREF":
                size=25;
                break;
            case "SILVER":
                size=30;
                break;
            case "CRUDEOIL":
            case "GOLD":
                size=100;
                break;
            case "MENTHAOIL":
                size=360;
                break;
            case "RUBBER":
                size=1000;
                break;
            case "NATURALGAS":
                size=1250;
                break;
            case "NICKEL":
                size=1500;
                break;
            case "COPPER":
                size=2500;
                break;
            case "KAPAS":
                size=4000;
                break;
            case "ALUMINIUM":
            case "LEAD":
            case "ZINC":
                size=5000;
                break;
        }

        return size;
    }
}
