package com.raphael.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.raphael.framework.FileIO;

import android.os.Environment;

public class WeightsReader {

	public WeightsReader() {
	}

	public void readFileToWeights(String path, float[][] weights) {
		// read file and append all text into data
		StringBuilder data = new StringBuilder();
		try {
			File file = new File(Environment.getExternalStorageDirectory(),
					path);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				data.append(line);
				data.append('\n');
			}
			br.close();
			// convert data into lines of text
			String[] lines = data.toString().split("\n");
			// i is line number
			for (int i = 0; i < lines.length; i++) {
				// split each number into a string
				String[] rows = lines[i].split("\t");
				// j is the position in the line
				for (int j = 0; j < rows.length; j++) {
					// convert string to float and insert in weights
					weights[i + 1][j] = Float.parseFloat(rows[j]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readAssetWeights(FileIO fileIO, String name, float[][] weights) {
		// read file and append all text into data
		StringBuilder data = new StringBuilder();
		try {
			InputStream inputStream = fileIO.readAsset(name);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = br.readLine()) != null) {
				data.append(line);
				data.append('\n');
			}
			br.close();
			// convert data into lines of text
			String[] lines = data.toString().split("\n");
			// i is line number
			for (int i = 0; i < lines.length; i++) {
				// split each number into a string
				String[] rows = lines[i].split("\t");
				// j is the position in the line
				for (int j = 0; j < rows.length; j++) {
					// convert string to float and insert in weights
					weights[i + 1][j] = Float.parseFloat(rows[j]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
