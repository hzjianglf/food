package com.sniper.springmvc.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public class TestSVGGen {
	public void paint(Graphics2D g2d) {
		g2d.setPaint(Color.red);
		g2d.fill(new Rectangle(10, 10, 100, 100));
	}

	public static void main(String[] args) throws IOException {

		// Get a DOMImplementation
		DOMImplementation domImpl = GenericDOMImplementation
				.getDOMImplementation();

		// Create an instance of org.w3c.dom.Document
		Document document = domImpl.createDocument(null, "svg", null);

		// Create an instance of the SVG Generator
		SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

		// Ask the test to render into the SVG Graphics2D implementation
		TestSVGGen test = new TestSVGGen();
		test.paint(svgGenerator);

		// Finally, stream out SVG to the standard output using UTF-8
		// character to byte encoding
		boolean useCSS = true; // we want to use CSS style attribute
		Writer out = new OutputStreamWriter(System.out, "UTF-8");
		svgGenerator.stream(out, useCSS);
	}
}
