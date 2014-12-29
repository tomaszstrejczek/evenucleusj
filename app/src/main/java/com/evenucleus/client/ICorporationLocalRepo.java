package com.evenucleus.client;

import java.util.List;

/**
 * Created by tomeks on 2014-12-29.
 */
public interface ICorporationLocalRepo {
    void Update(UserData data);
    List<Corporation> GetAll();
}
