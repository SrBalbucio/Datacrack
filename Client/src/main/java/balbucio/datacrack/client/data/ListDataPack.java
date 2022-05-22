/*
 * Copyright (c) 2022.
 * Datacrack é desenvolvido e mantido por SrBalbucio.
 * Não revenda e/ou distribua como seu.
 */

package balbucio.datacrack.client.data;

import org.json.JSONObject;

import java.util.List;

public class ListDataPack {

    private String name;
    private List<SimplePack> list;

    public ListDataPack(String name, List<String> s){
        this.name = name;
        int p = 0;
        for(String json : s){
            list.set(p, new SimplePack(json, p, this));
            p++;
        }
    }

    public class SimplePack{

        private JSONObject json;
        private int position;
        private ListDataPack rootPack;

        public SimplePack(String json, int position, ListDataPack pack){
            this.json = new JSONObject(json);
            this.position = position;
            this.rootPack = pack;
        }

    }
}
