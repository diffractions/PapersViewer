package inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.lang.reflect.Field;

public class FieldReflector {

	public static List<Field> collectUpTo(Class<?> clazz, Class<?> upperBound) {
		ArrayList<Field> result = new ArrayList<Field>();
		Class<?> current = clazz;
		while(current != upperBound){
			result.addAll(Arrays.asList(current.getDeclaredFields()));
			current = current.getSuperclass();
		}
		return result;
	}

	public static Collection<Field> filterInject(Collection<Field> allFields) {
		ArrayList<Field> result = new ArrayList<Field>();
		for(Field curentField : allFields){
			Inject annotation = curentField.getAnnotation(Inject.class);
			if(annotation!= null){
				result.add(curentField);
			}
		}
		return result;
	}

}
