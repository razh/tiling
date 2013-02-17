package com.razh.tiling.files;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.razh.tiling.Level;
import com.razh.tiling.MeshActor;
import com.razh.tiling.PointLight;
import com.razh.tiling.json.ColorDeserializer;
import com.razh.tiling.json.LevelDeserializer;
import com.razh.tiling.json.MeshActorDeserializer;
import com.razh.tiling.json.PointLightDeserializer;

public class LevelLoader {
	private static final String LEVELS_FILE = "levels.json";

	private Gson mGson;
	private LinkedHashMap<String, String> mFileNames;
	private ArrayList<String> mFileNamesArray;

	public LevelLoader() {
		mGson = new GsonBuilder()
		.registerTypeAdapter(Level.class, new LevelDeserializer())
		.registerTypeAdapter(MeshActor.class, new MeshActorDeserializer())
		.registerTypeAdapter(PointLight.class, new PointLightDeserializer())
		.registerTypeAdapter(Color.class, new ColorDeserializer())
		.create();

		FileHandle levelsFile = Gdx.files.internal(LEVELS_FILE);

		Type mapType = new TypeToken<LinkedHashMap<String, String>>() {}.getType();
		mFileNames = mGson.fromJson(levelsFile.readString(), mapType);

		// Create array.
		mFileNamesArray = new ArrayList<String>(mFileNames.values());
	}

	public Level getLevelByIndex(int levelIndex) {
		if (0 <= levelIndex && levelIndex < mFileNamesArray.size()) {
			FileHandle file = Gdx.files.internal(mFileNamesArray.get(levelIndex));
			return mGson.fromJson(file.readString(), Level.class);
		}

		return null;
	}

	public Level getLevelByName(String name) {
		FileHandle file = Gdx.files.internal(mFileNames.get(name));
		return mGson.fromJson(file.readString(), Level.class);
	}
}
