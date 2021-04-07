package ist.meic.pava.app;

public class Screen extends Device {
	public void draw(Line l, Brush b) {
		System.err.println("draw a line where and with what?");
	}
	public void draw(Line l, Pencil p) {
		System.err.println("drawing a line on screen with pencil!");
	}
	public void draw(Line l, Crayon c) {
		System.err.println("drawing a line on screen with crayon!");
	}
	public void draw(Circle c, Brush b) {
		System.err.println("drawing a circle on screen with what?");
	}
	public void draw(Circle c, Pencil p) {
		System.err.println("drawing a circle on screen with pencil!");
	}

	/*
	public void draw(Circle c, Crayon r, Shape c, Brush r) { // 2 SubClasses - 2 SuperClasses
		System.err.println("drawing a circle on screen with crayon!");
	}
	public void draw(Circle c, Brush r, Circle c, Crayon r) { // 3 SubClasses - 1 SuperClass
		System.err.println("drawing a circle on screen with crayon!");
	}
	*/

	public void draw(Shape s) {
		System.err.println("draw what on screen?");
	}
	public void draw(Line l) {
		System.err.println("drawing a line on screen!");
	}
	public void draw(Circle c) {
		System.err.println("drawing a circle on screen!");
	}
}
