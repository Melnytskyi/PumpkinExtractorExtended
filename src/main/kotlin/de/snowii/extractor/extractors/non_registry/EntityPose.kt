package de.snowii.extractor.extractors.non_registry

import com.google.gson.JsonObject
import com.google.gson.JsonElement
import de.snowii.extractor.Extractor
import net.minecraft.entity.EntityPose
import net.minecraft.server.MinecraftServer

class EntityPose : Extractor.Extractor {
    override fun fileName(): String {
        return "entity_pose.json"
    }

    override fun extract(server: MinecraftServer): JsonElement {
        val poseesJson = JsonObject()
        for (pose in EntityPose.entries) 
            poseesJson.addProperty(pose.name,pose.index)
        return poseesJson
    }
}