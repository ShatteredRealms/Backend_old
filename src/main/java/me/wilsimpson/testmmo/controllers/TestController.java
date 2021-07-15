package me.wilsimpson.testmmo.controllers;

import me.wilsimpson.testmmo.models.game.Item;
import me.wilsimpson.testmmo.models.game.repos.ItemRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(path = "/api/v1/testing")
public class TestController
{
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping(path="/items")
    public @ResponseBody
    ResponseEntity<List<Item>> getItems(@RequestParam(name = "id", required = false) Long id)
    {
        if(id == null)
        {
            LoggerFactory.getLogger(this.getClass()).info(itemRepository.findAll().toString());
            return ResponseEntity.ok(itemRepository.findAll());
        }
        return itemRepository.findById(id).map(value -> ResponseEntity.ok(List.of(value))).orElseGet(() -> ResponseEntity.ok(List.of()));
    }
}
