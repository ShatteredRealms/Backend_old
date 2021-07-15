package me.wilsimpson.testmmo.models.game.repos;

import me.wilsimpson.testmmo.models.game.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>
{
    Optional<Item> findByName(String name);
}
