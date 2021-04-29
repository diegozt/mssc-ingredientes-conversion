package com.dazt.msscingredientsconversion.controller;


import com.dazt.msscingredientsconversion.dto.IngredientsConversionDTO;
import com.dazt.msscingredientsconversion.proxy.IngredientConversionProxy;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
@Slf4j
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
                ingredientsConversionDTO.getHostPort(), ingredientsConversionDTO.getLastUpdatedDate(), ingredientsConversionDTO.getCreationDate());
    }


    @GetMapping("/ingredient-conversion-feign/from/{from}/to/{to}/quantity/{quantity}/")
    //@Retry(name = "retry-times", fallbackMethod = "unavailableServiceError")
    //@CircuitBreaker(name = "default", fallbackMethod = "unavailableServiceError")
    //@RateLimiter(name = "default")
    @Bulkhead(name = "default")
    public IngredientsConversionDTO calculateIngredientConversionFeign(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity
    ) {

        log.info("Invoking method: {} with the parameters: {} - {}", "calculateIngredientConversionFeign", from, to);

        IngredientsConversionDTO ingredientsConversionDTO = proxy.retrieveMeasureValue(from, to);

        return new IngredientsConversionDTO(ingredientsConversionDTO.getFrom(), ingredientsConversionDTO.getTo(), quantity,
                ingredientsConversionDTO.getConversionFactor(), ingredientsConversionDTO.getConversionFactor().multiply(quantity),
                ingredientsConversionDTO.getHostPort(), ingredientsConversionDTO.getLastUpdatedDate(), ingredientsConversionDTO.getCreationDate());
    }

    public IngredientsConversionDTO unavailableServiceError(Exception ex) {
        IngredientsConversionDTO dto = new IngredientsConversionDTO();
        dto.setFrom("error");
        return dto;
    }
}
