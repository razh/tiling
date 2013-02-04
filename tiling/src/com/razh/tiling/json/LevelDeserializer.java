package com.razh.tiling.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.razh.tiling.Level;

public class LevelDeserializer implements JsonDeserializer<Level> {

	@Override
	public Level deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {

		JsonObject object = json.getAsJsonObject();

		Level level = new Level();
		return null;
	}
}
