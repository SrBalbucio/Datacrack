/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.client.data.custom;

import java.util.Map;

public interface CustomData {

    CustomData setCustomData(Map<String, Object> values);
    Map<String, Object> getCustomData();

}
