package ist.meic.pava.MultipleDispatch;

import java.util.List;
import java.util.ArrayList;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class UsingMultipleDispatch {

	public static Object invoke(Object receiver, String name, Object... args) {
		try {

			List<Class> argsClasses = new ArrayList<Class>();

			for (Object arg : args) {
				argsClasses.add(arg.getClass());
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
		} catch (NoSuchMethodException e) {
			List<Class> argsTypesSuperClasses = new ArrayList<>();

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
