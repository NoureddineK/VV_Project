package Software_Testing.VV_Project;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;


/**
 * @author Fahim MERZOUK & Noureddine KADRI 
 *
 */
public class Analyzer {
	/**
	 * @param testMethod: method to analyze
	 * @return list of local variables in method passed in parameter
	 */
	public List<CtLocalVariable> analyze(CtMethod testMethod){
		TypeFilter filterLocalVar = 
				new TypeFilter(CtLocalVariable.class) {
			public boolean matches(CtLocalVariable localVariable) {
				return !localVariable.getType().isPrimitive();
			}
		};
		return testMethod.getElements(filterLocalVar);
	
	}
	/**
	 * @param ctClass : class to analyze 
	 * @return map of methods in keys and its list of local variables in values
	 */
	public Map<CtMethod, List<CtLocalVariable>> analyze(CtType<?> ctClass) {
		CtTypeReference<Test> reference =
				ctClass.getFactory().Type().createReference(Test.class);
		return ctClass.getMethods().stream()
				.filter(ctMethod -> ctMethod.getAnnotation(reference) != null)
				.collect(Collectors.toMap(
						ctMethod -> ctMethod,
						this::analyze)
				);
	}
}