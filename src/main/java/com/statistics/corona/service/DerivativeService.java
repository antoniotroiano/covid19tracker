package com.statistics.corona.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
@Service
@Slf4j
@RequiredArgsConstructor
public class DerivativeService {

    public Map<String, List<Double>> calculation(double susStart, double infStart, double reStart, double transRate, double reRate, int maxT) {

        double susceptible;
        double infected;
        double recovered;
        double derivativeS;
        double derivativeI;
        double derivativeR;
        List<Double> susList = new ArrayList<>();
        List<Double> infList = new ArrayList<>();
        List<Double> reList = new ArrayList<>();

        susList.add(susStart);
        infList.add(infStart);
        reList.add(reStart);

        for (int i = 0; i <= maxT; i++) {
            derivativeS = -transRate * susList.get(i) * infList.get(i);
            derivativeI = transRate * susList.get(i) * infList.get(i) - reRate * infList.get(i);
            derivativeR = reRate * infList.get(i);

            susceptible = susList.get(i) + derivativeS * 1;
            infected = infList.get(i) + derivativeI * 1;
            recovered = reList.get(i) + derivativeR * 1;

            susList.add(susceptible);
            infList.add(infected);
            reList.add(recovered);
        }
        Map<String, List<Double>> map = new HashMap<>();
        map.put("Susceptible", susList);
        map.put("Infected", infList);
        map.put("Recovered", reList);
        return map;
    }
}