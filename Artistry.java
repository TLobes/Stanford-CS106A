/*
 * File: Artistry.java
 * Name: Tim Lobes
 */

import acm.program.*;
import acm.graphics.*;
import java.awt.*;

public class Artistry extends GraphicsProgram {
  
	public void run() {		
		GRect rect = new GRect(90, 55, 200, 100 / PHI);
		rect.setFillColor(Color.GREEN);
		rect.setColor(Color.blue);
		add(rect);
		
		GArc arc = new GArc(200, 200, 30, 210);
		arc.setFilled(true);
		arc.setFillColor(Color.orange);
		add(arc);
		
		GOval oval = new GOval(0, 0, 600, 600 / PHI);
		oval.setFilled(true);
		oval.setColor(Color.gray);
		oval.setFillColor(Color.white);
		add(oval);
		
		GOval oval2 = new GOval(0, 0, 600, 600);
		oval2.setColor(Color.yellow);
		oval2.setFillColor(Color.blue);
		add(oval2);
		
		GLine line = new GLine(0, 0, 200, 200);
		line.setColor(Color.green);
		add(line);
		
		GLine line2 = new GLine(0, 0, 700, 100);
		line2.setColor(Color.red);
		add(line2);
		
		G3DRect rect3d = new G3DRect(600, 300, 50, 50, true);
		rect3d.setFilled(true);
		rect3d.setFillColor(Color.blue);
		rect3d.setColor(Color.cyan);
		rect3d.setRaised(true);
		add(rect3d);
		
		GRoundRect roundrect = new GRoundRect(525, 400, 200, 50, 25);
		roundrect.setFilled(true);
		roundrect.setFillColor(Color.lightGray);
		add(roundrect);
		
		GLabel label = new GLabel("derpy FX", 100, 100);
		label.setFont("SansSerif-36");
		label.setColor(Color.RED);
		add(label);
		
		GLabel name = new GLabel("Tim Lobes", 575, 435);
		name.setFont("Helvetica-24");
		name.setColor(Color.black);
		add(name);
	}
	
	// Golden ratio
	public static final double PHI = 1.618;
}
