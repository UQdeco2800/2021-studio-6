package com.badlogic.box2dlights.shaders;

import com.badlogic.box2dlights.box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public final class LightShader {
	static final public ShaderProgram createLightShader() {
		String gamma = ""; 
		if (RayHandler.getGammaCorrection())
			gamma = "sqrt";
		
		final String vertexShader = 
				"attribute vec4 vertex_positions;\n" //
				+ "attribute vec4 quad_colors;\n" //
				+ "attribute float s;\n"
				+ "uniform mat4 u_projTrans;\n" //
				+ "varying vec4 v_color;\n" //				
				+ "void main()\n" //
				+ "{\n" //
				+ "   v_color = s * quad_colors;\n" //
				+ "   gl_Position =  u_projTrans * vertex_positions;\n" //
				+ "}\n";
		final String fragmentShader = "#ifdef GL_ES\n" //
			+ "precision lowp float;\n" //
			+ "#define MED mediump\n"
			+ "#else\n"
			+ "#define MED \n"
			+ "#endif\n" //
				+ "varying vec4 v_color;\n" //
				+ "void main()\n"//
				+ "{\n" //
				+ "  gl_FragColor.rgb = "+gamma+"(v_color.rgb) + 0.2;\n" //
				+ "  gl_FragColor.a = "+gamma+" (v_color.a) + 0.7 ;\n" //
				+ "}";

		ShaderProgram.pedantic = false;
		ShaderProgram lightShader = new ShaderProgram(vertexShader,
				fragmentShader);
		if (lightShader.isCompiled() == false) {
			Gdx.app.log("ERROR", lightShader.getLog());
		}

		return lightShader;
	}
}
