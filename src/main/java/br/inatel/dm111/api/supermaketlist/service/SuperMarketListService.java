package br.inatel.dm111.api.supermaketlist.service;

import br.inatel.dm111.api.core.ApiException;
import br.inatel.dm111.api.core.AppErrorCode;
import br.inatel.dm111.api.supermaketlist.SuperMarketListRequest;
import br.inatel.dm111.persistence.product.Product;
import br.inatel.dm111.persistence.product.ProductRepository;
import br.inatel.dm111.persistence.supermarketlist.SuperMarketList;
import br.inatel.dm111.persistence.supermarketlist.SuperMarketListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class SuperMarketListService {

    private static final Logger log = LoggerFactory.getLogger(SuperMarketListService.class);

    private final SuperMarketListRepository splRepository;
    private final ProductRepository productRepository;

    public SuperMarketListService(SuperMarketListRepository splRepository, ProductRepository productRepository) {
        this.splRepository = splRepository;
        this.productRepository = productRepository;
    }

    public List<SuperMarketList> searchAllLists() throws ApiException {
        try {
            return splRepository.findAll();
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new ApiException(AppErrorCode.SUPERMARKET_LIST_QUERY_ERROR);
        }
    }

    public SuperMarketList searchById(String id) throws ApiException {
        return retrieveSuperMarketList(id);
    }

    public SuperMarketList createList(SuperMarketListRequest request) throws ApiException {
        var list = buildSuperMarketList(request);

        var allProductsAvailable = true;
        for (String id: list.getProducts()) {
            try {
                if (productRepository.findById(id).isEmpty()) {
                    allProductsAvailable = false;
                    break;
                }
            } catch (ExecutionException | InterruptedException e) {
                throw new ApiException(AppErrorCode.PRODUCTS_QUERY_ERROR);
            }
        }

        if (allProductsAvailable) {
            splRepository.save(list);
            return list;
        } else {
            throw new ApiException(AppErrorCode.PRODUCTS_NOT_FOUND);
        }
    }

    public SuperMarketList updateList(String id, SuperMarketListRequest request) throws ApiException {
        var list = retrieveSuperMarketList(id);
        list.setName(request.name());
        list.setProducts(request.products());

        var allProductsAvailable = true;
        for (String productId: list.getProducts()) {
            try {
                if (productRepository.findById(productId).isEmpty()) {
                    allProductsAvailable = false;
                    break;
                }
            } catch (ExecutionException | InterruptedException e) {
                throw new ApiException(AppErrorCode.PRODUCTS_QUERY_ERROR);
            }
        }

        if (allProductsAvailable) {
            splRepository.update(list);
            return list;
        } else {
            throw new ApiException(AppErrorCode.PRODUCTS_NOT_FOUND);
        }

    }

    public void removeList(String id) throws ApiException {
        try {
            var splOpt = splRepository.findById(id);
            if (splOpt.isPresent()) {
                var spl = splOpt.get();
                splRepository.delete(spl.getId());
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.SUPERMARKET_LIST_QUERY_ERROR);
        }
    }

    private SuperMarketList buildSuperMarketList(SuperMarketListRequest request) {
        var id = UUID.randomUUID().toString();
        return new SuperMarketList(id, request.name(), request.products());
    }

    private SuperMarketList retrieveSuperMarketList(String id) throws ApiException {
        try {
            return splRepository.findById(id)
                    .orElseThrow(() -> new ApiException(AppErrorCode.SUPERMARKET_LIST_NOT_FOUND));
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.SUPERMARKET_LIST_QUERY_ERROR);
        }
    }
}
