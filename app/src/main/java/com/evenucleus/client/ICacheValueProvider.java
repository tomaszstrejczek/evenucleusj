package com.evenucleus.client;

import java.util.Date;
import java.util.Map;

/**
 * Created by tomeks on 2014-12-31.
 */
public interface ICacheValueProvider {
    Map.Entry<Date,Object> Get();
}
