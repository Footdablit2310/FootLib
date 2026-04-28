package com.footdablit2310.footlib.client;

import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

public final class ClientEvents {

    private ClientEvents() {}

    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {
            StructurePreviewState.render(event.getPoseStack());
        }
    }
}
