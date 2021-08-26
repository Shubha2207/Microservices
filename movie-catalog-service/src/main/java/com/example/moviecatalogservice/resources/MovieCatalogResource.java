package com.example.moviecatalogservice.resources;

import com.example.moviecatalogservice.models.CatalogItem;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalogItems(@PathVariable("userId") String userId) {
//        return "List of movie ratings of "+userId;
        return Collections.singletonList(
          new CatalogItem("transformer", "sci-fi movie", 4)
        );
    }

}
