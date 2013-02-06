package com.razh.tiling.json;

import java.lang.reflect.Type;

import com.badlogic.gdx.graphics.Color;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.razh.tiling.PointLight;

public class PointLightDeserializer implements JsonDeserializer<PointLight> {

	@Override
	public PointLight deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = json.getAsJsonObject();

		float x = object.get("x").getAsFloat();
		float y = object.get("y").getAsFloat();
		float distance = object.get("distance").getAsFloat();

		Color color = (Color) context.deserialize(object.get("color"), Color.class);

		PointLight light = new PointLight();
		light.setPosition(x, y, 100);
		light.setWidth(3);
		light.setHeight(3);
		light.setColor(color);
		light.setDistance(distance);

		return light;
	}

}
