package com.footdablit2310.footlib.api.easy_register.builders;

import com.footdablit2310.footlib.api.easy_register.FootEasyRegisterSystem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class FERSoundEventBuilder {
	private final FootEasyRegisterSystem FER;

	public FERSoundEventBuilder(FootEasyRegisterSystem FER) {
		this.FER = FER;
	}

	/**
	 * This creates a variable range event
	 * @param soundname This param must be the snake_case name of the audio.
	 */
	public DeferredHolder<SoundEvent, SoundEvent> createVariableRangeEvent(@NotNull String soundname) {
		return FER.sounds.register(
			soundname,
			() -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(FER.getModId(), soundname))
		);
	}
	/**
	 * This creates a variable range event
	 * @param soundname This param must be the snake_case name of the audio.
	 * @param soundrange This param mu
	 */
	public DeferredHolder<SoundEvent, SoundEvent> createFixedRangeEvent(@NotNull String soundname, byte soundrange) {
		return FER.sounds.register(
			soundname,
			() -> SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(FER.getModId(), soundname), soundrange)
		);
	}
}
