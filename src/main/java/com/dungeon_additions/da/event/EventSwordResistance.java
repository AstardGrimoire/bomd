package com.dungeon_additions.da.event;

import com.dungeon_additions.da.config.ModConfig;
import com.dungeon_additions.da.entity.EntityFireResistantItems;
import com.dungeon_additions.da.init.ModItems;
import com.dungeon_additions.da.util.ModReference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

@Mod.EventBusSubscriber(modid = ModReference.MOD_ID)
public class EventSwordResistance {

    @SubscribeEvent
    public void onPlayerHoldItem(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase base = event.getEntityLiving();

            if (base.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == ModItems.SWORN_OF_AMBITION) {
                if (!base.world.isRemote && base.ticksExisted % 40 == 0) {
                    base.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 60, 0));
                }
            }


    }


    @SubscribeEvent
    public static void turnItemsFireproof(EntityJoinWorldEvent event) {
        World worldIn = event.getWorld();

        if(event.getEntity().getClass() == EntityItem.class) {
            EntityItem entityItem = (EntityItem)event.getEntity();
            ItemStack stack = entityItem.getItem();

            if(stack.getItem() == ModItems.SWORN_OF_AMBITION || stack.getItem() == ModItems.ENFLAMED_MEAT || stack.getItem() == ModItems.FLAME_OF_AMBITION || stack.getItem() == ModItems.FLAME_BOOTS ||
            stack.getItem() == ModItems.FLAME_METAL_SCRAP || stack.getItem() == ModItems.FLAME_LEGGINGS || stack.getItem() == ModItems.FLAME_HELMET || stack.getItem() == ModItems.FLAME_CHESTPLATE) {
                if(!stack.isEmpty()) {
                    EntityFireResistantItems fireproofItem = new EntityFireResistantItems(worldIn, entityItem, stack);
                    worldIn.spawnEntity(fireproofItem);
                    entityItem.setDead();
                }
            }
        }
    }
}
