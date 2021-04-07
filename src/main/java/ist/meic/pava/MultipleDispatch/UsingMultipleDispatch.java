package ist.meic.pava.MultipleDispatch;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.InvocationTargetException;

public class UsingMultipleDispatch {

	public static Object invoke(Object receiver, String name, Object... args) {
		try {

			List<Class> argsType = new ArrayList<Class>();
			for (Object arg : args) {
				argsType.add(arg.getClass());
			}

			List<Method> availableMethods = getAvailableMethods(receiver.getClass(), name, argsType);
			Method method = bestMethod(availableMethods);
			return method.invoke(receiver, args);
			
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean compatibleParameters(List<Class> parTypes, List<Class> argsType) {
		for (int i = 0; i < argsType.size(); i++) {
			if (!isSubClassOf(argsType.get(i), parTypes.get(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isSubClassOf(Class c1, Class c2) {
		Class type = c1;
		while (type != Object.class && type != c2) {
			type = type.getSuperclass();
		}
		return type == c2;
	}

	public static List<Method> getAvailableMethods(Class receiverType, String name, List<Class> argsTypes) {
		List<Method> availableMethods = new ArrayList<Method>();

		for (Method method : receiverType.getMethods()) { 
			if (name.equals(method.getName()) && (method.getParameters().length == argsTypes.size())) {

				List<Class> parametersTypes = new ArrayList<Class>();
				for (Parameter parameter : method.getParameters()){
					parametersTypes.add(parameter.getType());
				}

				if (compatibleParameters(parametersTypes, argsTypes)) {
					availableMethods.add(method);
				}
			}
		}

		return availableMethods;
	}

	public static Method chooseBest(Method m1, Method m2) {
		
		// When the declaring classes are identical, then m1 is more specific
		// than m2 if the type of the first parameter of m1 is a subtype of
		// the type of the first parameter of m2.
		if (m1.getDeclaringClass() == m2.getDeclaringClass()) {
			// Repeat the process with following parameters, from left to right,
			// until we find one that is a subtype of the other.
			for(int i = 0; i < m1.getParameters().length; i++) {

				Class m1Type = m1.getParameters()[i].getType();
				Class m2Type = m2.getParameters()[i].getType();

				if (m1Type == m2Type) {
					continue;
				}

				if (isSubClassOf(m1Type, m2Type)) {
					return m1;
				}
				return m2;
			}
			return m1;
		}

		// An applicable method m1 is more specific than an applicable method m2 when the
		// declaring class of m1 is a subclass of the declaring class of m2.
		if (isSubClassOf(m1.getDeclaringClass(), m2.getDeclaringClass())) {
			return m1;
		}
		return m2;
	}


	public static Method bestMethod(List<Method> availableMethods)
	  throws NoSuchMethodException {
		try {
			if (availableMethods.isEmpty()) {
				throw new NoSuchMethodException();
			}

			Method best = availableMethods.get(0);
			
			for (int i = 1; i < availableMethods.size(); i++) {
				best = chooseBest(best, availableMethods.get(i));
			}

			return best;
		} catch (NoSuchMethodException e) {
			throw e;
		}
	}
}
