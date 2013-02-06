package com.razh.tiling.json;

import java.lang.reflect.Type;

import com.badlogic.gdx.graphics.Color;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class ColorDeserializer implements JsonDeserializer<Color> {

	@Override
	public Color deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {

		JsonObject object = json.getAsJsonObject();

		float r = object.get("r").getAsFloat() / 255.0f;
		float g = object.get("g").getAsFloat() / 255.0f;
		float b = object.get("b").getAsFloat() / 255.0f;
		float a = object.get("a").getAsFloat();

		Color color = new Color(r, g, b, a);

		return color;
	}

}
