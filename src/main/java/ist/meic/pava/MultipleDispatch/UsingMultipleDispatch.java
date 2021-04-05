package ist.meic.pava.MultipleDispatch;

import java.util.List;

import jdk.javadoc.internal.doclets.formats.html.resources.standard;

import java.util.ArrayList;

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
				//System.out.println("argo: " + arg);
			}

			List<Method> availableMethods = getAvailableMethods(receiver, name, argsType);

			Method method = bestMethod(availableMethods);
			return method.invoke(receiver, args);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean compatibleParameters(List<Class> partypes, List<Class> argstypes) {
		int i;
		for(i = 0; i < argstypes.size(); i++) {
			Class type = argstypes.get(i);
			while(type != Object.class && type != partypes.get(i)) {
				type = type.getSuperclass();
			}
			if (partypes.get(i) != type) {
				break;
			}
		}
		return i == argstypes.size();
	}

	public static List<Method> getAvailableMethods(Object receiver, String name, List<Class> argsType) {
		List<Method> availableMethods = new ArrayList<Method>();

		for (Method m : receiver.getClass().getMethods()) { 
			if (name.equals(m.getName()) && m.getParameters().length == argsType.size()) {

				List<Class> parTypes = new ArrayList<Class>();

				for(Parameter p : m.getParameters()){
					parTypes.add(p.getType());
				}

				if (compatibleParameters(parTypes, argsType)) {
					availableMethods.add(m);
					//System.out.println("method: " + m);
				}
			}
		}

		return availableMethods;
	}

	public static Method chooseBest(Method m1, Method m2) {
		Class type;
		
		//System.out.println(m1 + " vs " + m2);
		//System.out.println(m1.getDeclaringClass() + " vst " + m2.getDeclaringClass());
		if (m1.getDeclaringClass() == m2.getDeclaringClass()) {
			int i;
			for(i = 0; i < m1.getParameters().length; i++) {

				Class m1Type = m1.getParameters()[i].getType();
				Class m2Type = m2.getParameters()[i].getType();

				if (m1Type == m2Type) {
					//System.out.println("entrou1");
					continue;
				}
				while(m1Type != Object.class && m1Type != m2Type) {
					m1Type = m1Type.getSuperclass();
				}
				if (m2Type != m1Type) {
					//System.out.println("entrou2");
					return m2;
				}
				//System.out.println("entrou3");
				return m1;
			}
			return m1;
		}
		type = m1.getDeclaringClass();
		while(type != Object.class && type != m2.getDeclaringClass()) {
			type = type.getSuperclass();
		}
		if (type == m2.getDeclaringClass()){
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
			int i;
			for (i = 1; i < availableMethods.size(); i++) {
				best = chooseBest(best, availableMethods.get(i));
			}
			return best;
		} catch (NoSuchMethodException e) {
			throw e;
		}
	}
}
