package de.snowii.extractor.extractors

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonArray
import com.mojang.serialization.JsonOps
import de.snowii.extractor.Extractor
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryOps
import net.minecraft.server.MinecraftServer
import net.minecraft.potion.Potion
import net.minecraft.resource.featuretoggle.FeatureFlags

class Potion : Extractor.Extractor {
    override fun fileName(): String {
        return "potion.json"
    }

    override fun extract(server: MinecraftServer): JsonElement {
        val potionData = JsonObject()
        val potionRegistry =
            server.registryManager.getOrThrow(RegistryKeys.POTION)
        val statusEffectRegistry =
            server.registryManager.getOrThrow(RegistryKeys.STATUS_EFFECT)
        for (potion in potionRegistry) {
            val json = JsonObject()
            json.addProperty("id", potionRegistry.getRawId(potion))
            val effects = JsonArray()
            for(effect in potion.effects)
                effects.add(statusEffectRegistry.getRawId(effect.effectType.value()))
            json.add("effects", effects)
            json.add(
                "required_features",
                FeatureFlags.CODEC
                    .encodeStart(
                        RegistryOps.of(JsonOps.INSTANCE, server.registryManager),
                        potion.requiredFeatures
                    )
                    .getOrThrow()
            )
            potionData.add(
                potionRegistry.getId(potion)!!.path, json
            )
        }

        return potionData
    }
}
