package br.inatel.dm111.api.supermaketlist.controller;

import br.inatel.dm111.api.core.ApiException;
import br.inatel.dm111.api.product.ProductRequest;
import br.inatel.dm111.api.supermaketlist.SuperMarketListRequest;
import br.inatel.dm111.api.supermaketlist.service.SuperMarketListService;
import br.inatel.dm111.persistence.product.Product;
import br.inatel.dm111.persistence.supermarketlist.SuperMarketList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SuperMarketListController {

    private final SuperMarketListService service;

    public SuperMarketListController(SuperMarketListService service) {
        this.service = service;
    }

    @GetMapping("/supermarketlist")
    public ResponseEntity<List<SuperMarketList>> getAllSuperMarketList() throws ApiException {
        var list = service.searchAllLists();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/supermarketlist/{id}")
    public ResponseEntity<SuperMarketList> getSuperMarketList(@PathVariable("id") String id)
            throws ApiException {
        var list = service.searchById(id);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/supermarketlist")
    public ResponseEntity<SuperMarketList> postSuperMarketList(@RequestBody SuperMarketListRequest request)
            throws ApiException {
        var list = service.createList(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(list);
    }

    @PutMapping("/supermarketlist/{id}")
    public ResponseEntity<SuperMarketList> putSuperMarketList(@PathVariable("id") String id,
                                                      @RequestBody SuperMarketListRequest request)
            throws ApiException {
        var list = service.updateList(id, request);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/supermarketlist/{id}")
    public ResponseEntity<?> deleteSuperMarketList(@PathVariable("id") String id) throws ApiException {
        service.removeList(id);
        return ResponseEntity.noContent().build();
    }
}
