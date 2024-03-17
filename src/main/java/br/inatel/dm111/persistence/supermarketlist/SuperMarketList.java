package br.inatel.dm111.persistence.supermarketlist;

import java.util.List;

//{
//    "id": "",
//    "name": "Materiais e limpeza",
//    "products": []
//}
public class SuperMarketList {

    private String id;
    private String name;
    private List<String> products;

    public SuperMarketList() {
    }

    public SuperMarketList(String id, String name, List<String> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }
}
