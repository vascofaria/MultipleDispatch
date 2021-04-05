package ist.meic.pava.MultipleDispatch;

import java.util.List;
import java.util.ArrayList;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.InvocationTargetException;

public class UsingMultipleDispatch {

	public static Object invoke(Object receiver, String name, Object... args) {
		try {

			List<Class> argsClasses = new ArrayList<Class>();

			for (Object arg : args) {
				argsClasses.add(arg.getClass());
			}

			List<Method> availableMethods = new ArrayList<Method>();

			for (Method m : receiver.getClass().getMethods()) { // getMethods
				if (name.equals(m.getName()) && m.getParameters().length == args.length) {
					availableMethods.add(m);
					/*System.out.println("<Method>");
					System.out.println("  " + m);
					System.out.println("    <Parameter>");
					for (Parameter p : m.getParameters()) {
						System.out.println("      " + p.getType());
					}
					System.out.println("    </Parameter>");
					System.out.println("</Method>");*/
				}
			}

			Class[] argsType = argsClasses.toArray(new Class[argsClasses.size()]);
			for (Class a : argsType) {
				System.out.println(a);
			}

			Method method = bestMethod(receiver.getClass(),	name, argsClasses.toArray(new Class[argsClasses.size()]));
			return method.invoke(receiver, args);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static Method bestMethod(Class type, String name, Class... argsType)
	  throws NoSuchMethodException {
		try {
			return type.getMethod(name, argsType);
		} catch (NoSuchMethodException e) { // fazer todas as permutaçoes possiveis e escolher a que tem super class
			List<Class> argsTypesSuperClasses = new ArrayList<>();

			// System.out.println(argsType.getDeclaredFields());

			for (Class argType : argsType) {
				if (argType == Object.class) {
					throw e;
				} else {
					argsTypesSuperClasses.add(argType.getSuperclass());
				}
			}

			return bestMethod(type, name, argsTypesSuperClasses.toArray(new Class[argsTypesSuperClasses.size()]));
		}
	}
}
