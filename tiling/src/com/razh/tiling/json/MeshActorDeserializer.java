package com.razh.tiling.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.razh.tiling.MeshActor;

public class MeshActorDeserializer implements JsonDeserializer<MeshActor> {

	@Override
	public MeshActor deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {

		JsonObject object = json.getAsJsonObject();
		return null;
	}

}
