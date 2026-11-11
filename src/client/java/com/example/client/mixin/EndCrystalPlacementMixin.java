package com.example.client.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystalItem.class)
public class EndCrystalPlacementMixin {

    private static float savedPitch = 0;
    private static float savedYaw = 0;
    private static boolean isLookingUp = false;

    @Inject(
        method = "useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onEndCrystalPlace(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        ClientWorld world = client.world;

        if (player == null || world == null) {
            return;
        }

        ItemStack stack = context.getStack();
        if (!(stack.getItem() instanceof EndCrystalItem)) {
            return;
        }

        BlockHitResult hitResult = context.getHitResult();
        if (hitResult.getType() != BlockHitResult.Type.BLOCK) {
            return;
        }

        BlockPos pos = hitResult.getBlockPos();
        BlockPos crystalPos = pos.offset(hitResult.getSide());

        // Check if the block being placed on is Obsidian
        if (world.getBlockState(pos).isOf(Blocks.OBSIDIAN)) {
            // Save original rotation
            savedPitch = player.getPitch();
            savedYaw = player.getYaw();

            // Set pitch to look straight up (-90)
            player.setPitch(-90.0f);
            isLookingUp = true;

            // Schedule restoration of original pitch after a very short delay
            // This runs on the next client tick
            client.execute(() -> {
                // Small delay to let the explosion happen
                client.execute(() -> {
                    if (isLookingUp && player != null) {
                        player.setPitch(savedPitch);
                        player.setYaw(savedYaw);
                        isLookingUp = false;
                    }
                });
            });

            // Also try to trigger the explosion immediately by spawning the crystal
            // The actual explosion happens server-side, but we can try to trigger it client-side too
            if (!world.isClient()) {
                EndCrystalEntity crystal = new EndCrystalEntity(world, crystalPos.getX() + 0.5, crystalPos.getY(), crystalPos.getZ() + 0.5);
                world.spawnEntity(crystal);
            }
        }
    }
}