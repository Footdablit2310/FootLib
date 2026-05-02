package com.footdablit2310.footlib.client;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import com.footdablit2310.footlib.FootLib;

@Mod(FootLib.MOD_ID)
public final class ClientEvents {

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS)
            return;

        float pt = event.getPartialTick().getGameTimeDeltaPartialTick(false);

        StructurePreviewRenderer.render(
                event.getLevelRenderer(),
                event.getPoseStack(),
                pt
        );
    }
}
