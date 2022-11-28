package top.mikecao.wager.common;

import java.util.UUID;

public final class KeyGen {

    public long next(){
        return Math.abs(UUID.randomUUID().getMostSignificantBits());
    }

}
