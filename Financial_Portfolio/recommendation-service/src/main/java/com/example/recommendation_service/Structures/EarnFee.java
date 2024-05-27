package com.example.recommendation_service.Structures;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EarnFee {

    float earn;
    float fee;

    public EarnFee(float earn, float fee) {
        this.earn = earn;
        this.fee = fee;
    }
}
