package com.dazt.msscingredientsconversion.controller;


import com.dazt.msscingredientsconversion.dto.IngredientsConversionDTO;
import com.dazt.msscingredientsconversion.proxy.IngredientConversionProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
public class IngredientConversionController {

    @Autowired
    private IngredientConversionProxy proxy;

    @GetMapping("/ingredient-conversion/from/{from}/to/{to}/quantity/{quantity}/")
    public IngredientsConversionDTO calculateIngredientConversion(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity
            ) {

        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        ResponseEntity<IngredientsConversionDTO> responseEntity =
                new RestTemplate().getForEntity("http://localhost:8000/measure-conversion/from/{from}/to/{to}", IngredientsConversionDTO.class, uriVariables);

        IngredientsConversionDTO ingredientsConversionDTO = responseEntity.getBody();

        return new IngredientsConversionDTO(ingredientsConversionDTO.getFrom(), ingredientsConversionDTO.getTo(), quantity,
                ingredientsConversionDTO.getConversionFactor(), ingredientsConversionDTO.getConversionFactor().multiply(quantity),
                ingredientsConversionDTO.getLastUpdatedDate(), ingredientsConversionDTO.getCreationDate());
    }

    @GetMapping("/ingredient-conversion-feign/from/{from}/to/{to}/quantity/{quantity}/")
    public IngredientsConversionDTO calculateIngredientConversionFeign(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity
    ) {

        IngredientsConversionDTO ingredientsConversionDTO = proxy.retrieveMeasureValue(from, to);

        return new IngredientsConversionDTO(ingredientsConversionDTO.getFrom(), ingredientsConversionDTO.getTo(), quantity,
                ingredientsConversionDTO.getConversionFactor(), ingredientsConversionDTO.getConversionFactor().multiply(quantity),
                ingredientsConversionDTO.getLastUpdatedDate(), ingredientsConversionDTO.getCreationDate());
    }
}
