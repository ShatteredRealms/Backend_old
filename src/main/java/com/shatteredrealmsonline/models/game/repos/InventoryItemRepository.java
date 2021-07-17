package com.shatteredrealmsonline.models.game.repos;

import com.shatteredrealmsonline.models.game.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long>
{

}
