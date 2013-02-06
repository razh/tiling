package com.razh.tiling.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.razh.tiling.Level;
import com.razh.tiling.MeshActor;
import com.razh.tiling.json.LevelDeserializer;
import com.razh.tiling.json.MeshActorDeserializer;

public class LevelLoader {
	private static String sLevelsFileName = "levels.json";

	private Gson mGson;
	private String[] mFileNames;
	private FileHandle mFile;

	public LevelLoader() {
		mGson = new GsonBuilder()
		.registerTypeAdapter(Level.class, new LevelDeserializer())
		.registerTypeAdapter(MeshActor.class, new MeshActorDeserializer())
		.create();

		FileHandle levelsFile = Gdx.files.internal(sLevelsFileName);
		mFileNames = mGson.fromJson(levelsFile.readString(), String[].class);
	}

	public Level getLevelByIndex(int levelIndex) {
		mFile = Gdx.files.internal(mFileNames[levelIndex]);

		return mGson.fromJson(mFile.readString(), Level.class);
	}
}
