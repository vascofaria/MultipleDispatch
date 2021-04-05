package ist.meic.pava.app;

import ist.meic.pava.MultipleDispatch.UsingMultipleDispatch;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.lang.ClassNotFoundException;

public class App {
	public static void main(String[] args) {
		printer();
		reflection();
	}

	public static void printer() {
		Device[] devices = new Device[] { new Screen(), new Printer() };
		Shape[] shapes = new Shape[] { new Line(), new Circle() };
		Brush[] brushes = new Brush[] { new Pencil(), new Crayon() };
		for (Device device : devices) {
			for (Shape shape : shapes) {
				for (Brush brush : brushes) {
					// device.draw(shape, brush);
					UsingMultipleDispatch.invoke(device, "draw", shape, brush);
				}
			}
		}

		System.out.println("------------------------------------------------");

		Device[] devices2 = new Device[] { new Screen(), new Printer() };
		Shape[] shapes2 = new Shape[] { new Line(), new Circle() };
		for (Device device : devices2) {
			for (Shape shape : shapes2) {
				// device.draw(shape);
				UsingMultipleDispatch.invoke(device, "draw", shape);
			}
		}

		System.out.println("------------------------------------------------");
	}

	public static void reflection() {
		try {
			dumpClass(Class.forName("ist.meic.pava.app.Device"));
		} catch(ClassNotFoundException cnf) {
			System.err.println(cnf.toString());
		}
	}

	public static void dumpClass(Class c) {
		System.out.println(c + " {");

		for (Constructor con : c.getConstructors()) {
			System.out.println("	" + con);
		}

		for (Method m : c.getDeclaredMethods()) {
			if (! Modifier.isPrivate(m.getModifiers())) {
				System.out.println("	" + m);
			}
		}

		System.out.println("}");
	}
}
