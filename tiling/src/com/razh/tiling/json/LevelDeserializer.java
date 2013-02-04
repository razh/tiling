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

public class LevelDeserializer implements JsonDeserializer<Level> {

	@Override
	public Level deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {

		JsonObject object = json.getAsJsonObject();

		String name = object.get("name").getAsString();
		Color color = (Color) context.deserialize(object.get("color"), Color.class);

		Level level = new Level();
		level.setName(name);
		level.setBackgroundColor(color);

		JsonArray jsonShapes = object.get("shapes").getAsJsonArray();
		MeshActor actor = null;
		for (int i = 0, n = jsonShapes.size(); i < n; i++) {
			actor = (MeshActor) context.deserialize(jsonShapes.get(i), MeshActor.class);
			if (actor != null) {
				level.addActor(actor);
			}
		}

		return level;
	}
}
