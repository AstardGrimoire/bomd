package com.dungeon_additions.da.entity.sky_dungeon.high_king.drake;

import com.dungeon_additions.da.entity.ai.IPitch;
import com.dungeon_additions.da.entity.ai.flying.AIPassiveCircle;
import com.dungeon_additions.da.entity.ai.flying.IAttackInitiator;
import com.dungeon_additions.da.entity.night_lich.EntityNightLich;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.EntityHighKingBoss;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.EntityHighKingDrake;
import com.dungeon_additions.da.entity.sky_dungeon.high_king.drake.pathing.AIDrakePassiveCircle;
import com.dungeon_additions.da.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.Vec3d;

public class EntityHighKingDrakeAI extends EntityAIBase {

    private final EntityHighKingDrake entity;
    private final float maxAttackDistSq;
    private final float lookSpeed;
    private final IAttackInitiator attackInitiator;
    private int unseeTime;
    private final AIDrakePassiveCircle<EntityHighKingDrake> circleAI;

    private static final int MEMORY = 100;

    public EntityHighKingDrakeAI(EntityHighKingDrake entity, float maxAttackDistance, float idealAttackDistance, float lookSpeed, IAttackInitiator attackInitiator) {
        this.entity = entity;
        this.maxAttackDistSq = maxAttackDistance * maxAttackDistance;
        this.lookSpeed = lookSpeed;
        this.attackInitiator = attackInitiator;
        circleAI = new AIDrakePassiveCircle<>(entity, idealAttackDistance);
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        return this.entity.getAttackTarget() != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return (this.shouldExecute() || !this.entity.getNavigator().noPath());
    }

    @Override
    public void resetTask() {
        super.resetTask();
        attackInitiator.resetTask();
    }

    @Override
    public void updateTask() {
        EntityLivingBase target = this.entity.getAttackTarget();

        if (target == null) {
            return;
        }

        double distSq = this.entity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        boolean canSee = this.entity.getEntitySenses().canSee(target);

        // Implements some sort of memory mechanism (can still attack a short while after the enemy isn't seen)
        if (canSee) {
            unseeTime = 0;
        } else {
            unseeTime += 1;
        }

        canSee = canSee || unseeTime < MEMORY;

        move(target, distSq, canSee);

        if (distSq <= this.maxAttackDistSq && canSee) {
            attackInitiator.update(target);
        }
    }

    public void move(EntityLivingBase target, double distSq, boolean canSee) {
        if(!this.entity.lockLook) {



            //Ground Navigator
            if(this.entity.isGrounded()) {
                if(!this.entity.isDoingBreathAttack) {
                    this.entity.getLookHelper().setLookPositionWithEntity(target, (float) (this.lookSpeed * 0.5), (float) (this.lookSpeed * 0.5));
                    this.entity.faceEntity(target, this.lookSpeed, this.lookSpeed);
                }

                if(distSq >= 64) {
                    this.entity.getNavigator().tryMoveToEntityLiving(target, 0.5D);
                }

                //Fly Navigator
            } else {
                circleAI.updateTask();
            }
           // if (this.entity instanceof IPitch) {
        //        Vec3d targetPos = target.getPositionEyes(1);
         //       Vec3d entityPos = this.entity.getPositionEyes(1);
         //       Vec3d forwardVec = ModUtils.direction(entityPos, targetPos);
          //      ((IPitch) this.entity).setPitch(forwardVec);
         //   }
        }

    }

}
