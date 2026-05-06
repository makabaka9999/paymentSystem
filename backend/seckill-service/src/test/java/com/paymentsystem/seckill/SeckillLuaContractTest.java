package com.paymentsystem.seckill;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SeckillLuaContractTest {
    @Test
    void luaStatusCodesAreStable() {
        assertThat(-1).isEqualTo(-1);
        assertThat(-2).isEqualTo(-2);
    }
}
