package com.poe.preotrianav;

import com.poe.preotrianav.models.landmark;
import com.poe.preotrianav.models.location;

import java.util.ArrayList;

public class LandmarksSamples {

    public static ArrayList<landmark> Landmark(){
        ArrayList<landmark> l = new ArrayList<>();

        l.add(new landmark("Melrose House", -25.7555508, 28.1903113,"landmarks/Melrose House.png"));
        l.add(new landmark("Intiem Love Bridge", -25.7631729, 28.2185424,"landmarks/Intiem Love Bridge.png"));
        l.add(new landmark("Fort Klapperkop", -25.7802997, 28.2079808,"landmarks/Fort Klapperkop.png"));
        l.add(new landmark("Voortrekker Monument", -25.7762704, 28.1735722,"landmarks/Voortrekker Monument.png"));
        l.add(new landmark("Union Building", -25.7426025, 28.2091929,"landmarks/Union Building.png"));
        l.add(new landmark("Ditsong National Museum of Natural History", -25.7530585, 28.1871297,"landmarks/Ditsong National Museum of Natural History.png"));
        l.add(new landmark("Church Square", -25.7464199, 28.185857,"landmarks/Church Square.png"));
        l.add(new landmark("Freedom Park Heritage Site & Museum", -25.7661412, 28.1857633,"landmarks/Freedom Park Heritage Site & Museum.png"));
        l.add(new landmark("Jan Smuts House Museum", -25.889294, 28.2286952,"landmarks/Jan Smuts House Museum.png"));
        l.add(new landmark("Pretoria National Botanical Gardens", -25.7394658, 28.2710778,"landmarks/Pretoria National Botanical Gardens.png"));

        return l;
    }
}
