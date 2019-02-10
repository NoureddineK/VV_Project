package Software_Testing.VV_Project;

import org.junit.Assert;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtTypeAccess;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtExecutableReference;

import java.util.List;
import  Software_Testing.VV_Project.Observer;
import static Software_Testing.VV_Project.Util.getGetters;
import static Software_Testing.VV_Project.Util.getKey;
import static Software_Testing.VV_Project.Util.invok;

/**
 * @author Fahim MERZOUK & Noureddine KADRI
 *
 */
public class AssertionAdder {

	private Factory factory;

	public AssertionAdder(Factory factory) {
		this.factory = factory;
	}

	@SuppressWarnings("unchecked")
	public CtMethod addAssertion(CtMethod<?> testMethod, List<CtLocalVariable> ctLocalVariables) {
		ctLocalVariables.forEach(ctLocalVariable -> this.addAssertion(testMethod, ctLocalVariable));
		return testMethod;
	}

	/**
	 * Add assertion in test method to test local variables in that method
	 * @param testMethod test method
	 * @param localVariable local variable
	 */
	@SuppressWarnings("unchecked")
	public void addAssertion(CtMethod testMethod, CtLocalVariable localVariable) {
		List<CtMethod> getters =
				getGetters(localVariable);
		getters.forEach(getter -> {
			String key = getKey(getter);
			CtInvocation invocationToGetter =
					invok(getter, localVariable);
			CtInvocation invocationToAssert = createAssert("assertEquals",
					factory.createLiteral(Observer.observations.get(key)), // expected
					invocationToGetter); // actual
			testMethod.getBody().insertEnd(invocationToAssert);
		});
		
	}

	/**
	 * create assertion of type name with using parameters
	 * @param name type of assertion
	 * @param parameters parameter of the assertion
	 * @return
	 */
	public static CtInvocation createAssert(String name, CtExpression... parameters) {
		final Factory factory = parameters[0].getFactory();
		CtTypeAccess accessToAssert =
				factory.createTypeAccess(factory.createCtTypeReference(Assert.class));
		CtExecutableReference assertEquals = factory.Type().get(Assert.class)
				.getMethodsByName(name).get(0).getReference();
		return factory.createInvocation(
				accessToAssert,
				assertEquals,
				parameters[0],
				parameters[1]
		);
	}

}
