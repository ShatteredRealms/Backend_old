package me.wilsimpson.testmmo.models.game.repos;

import me.wilsimpson.testmmo.models.game.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long>
{

}
