package com.razh.tiling.json;

import java.lang.reflect.Type;

import com.badlogic.gdx.graphics.Color;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.razh.tiling.Level;
import com.razh.tiling.MeshActor;
import com.razh.tiling.PointLight;
import com.razh.tiling.logic.GraphEntity;
import com.razh.tiling.logic.TilingEntity;

public class LevelDeserializer implements JsonDeserializer<Level> {

	@Override
	public Level deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {

		JsonObject object = json.getAsJsonObject();

		String name = object.get("name").getAsString();

		float stageX = object.get("x").getAsFloat();
		float stageY = object.get("y").getAsFloat();
		float scale = object.get("scale").getAsFloat();
		float stroke = object.get("stroke").getAsFloat();

		Color backgroundColor = (Color) context.deserialize(object.get("backgroundColor"), Color.class);
		Color ambientColor = (Color) context.deserialize(object.get("ambientColor"), Color.class);

		Level level = new Level();
		level.setName(name);
		level.setScale(scale);
		level.setStroke(stroke);
		level.setBackgroundColor(backgroundColor);
		level.setAmbientColor(ambientColor);

		JsonArray jsonShapes = object.get("shapes").getAsJsonArray();
		MeshActor actor = null;
		TilingEntity entity;
		for (int i = 0, n = jsonShapes.size(); i < n; i++) {
			actor = (MeshActor) context.deserialize(jsonShapes.get(i), MeshActor.class);
			actor.translate(-stageX, -stageY);
			if (actor != null) {
				entity = new TilingEntity();
				actor.setEntity(entity);

				level.addActor(actor);
			}
		}

		JsonArray jsonLights = object.get("lights").getAsJsonArray();
		PointLight light = null;
		for (int i = 0, n = jsonLights.size(); i < n; i++) {
			light = (PointLight) context.deserialize(jsonLights.get(i), PointLight.class);
			light.translate(-stageX, -stageY);
			if (light != null) {
				level.addLight(light);
			}
		}

		JsonArray jsonGraph = object.get("graph").getAsJsonArray();
		JsonArray jsonEdgeList;
		int index;
		for (int i = 0, n = jsonGraph.size(); i < n; i++) {
			if (jsonGraph.get(i) != null) {
				jsonEdgeList = jsonGraph.get(i).getAsJsonArray();
				entity = (TilingEntity) level.getActorAt(i).getEntity();

				for (int j = 0, m = jsonEdgeList.size(); j < m; j++) {
					index = jsonEdgeList.get(j).getAsInt();
					entity.addNeighbor((GraphEntity) level.getActorAt(index).getEntity());
				}
			}
		}

		return level;
	}
}
