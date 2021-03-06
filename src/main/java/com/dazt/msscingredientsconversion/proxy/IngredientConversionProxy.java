package com.dazt.msscingredientsconversion.proxy;

import com.dazt.msscingredientsconversion.dto.IngredientsConversionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "mssc-conversion")
public interface IngredientConversionProxy {

    @GetMapping("/measure-conversion/from/{from}/to/{to}")
    public IngredientsConversionDTO retrieveMeasureValue(
            @PathVariable String from,
            @PathVariable String to);

}
