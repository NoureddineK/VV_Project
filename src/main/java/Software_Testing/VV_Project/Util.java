package Software_Testing.VV_Project;

import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtTypeAccess;
import spoon.reflect.code.CtVariableAccess;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtExecutableReference;
import spoon.testing.Assert;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Util {

	//private static Factory factory;
	
	public static String getKey(CtMethod method) {
		return method.getParent(CtClass.class).getSimpleName() + "#" + method.getSimpleName();
	}

	public static CtInvocation invok(CtMethod method, CtLocalVariable localVariable) {
		final CtExecutableReference reference = method.getReference();
		final CtVariableAccess variableRead = method.getFactory().createVariableRead(localVariable.getReference(), false);
		return method.getFactory().createInvocation(variableRead, reference);
	}

	public static List<CtMethod> getGetters(CtLocalVariable localVariable) {
		return ((Set<CtMethod<?>>) localVariable.getType().getDeclaration().getMethods()).stream()
				.filter(method -> method.getParameters().isEmpty() &&
						method.getType() != localVariable.getFactory().Type().VOID_PRIMITIVE &&
						(method.getSimpleName().startsWith("get") ||
								method.getSimpleName().startsWith("is"))
				).collect(Collectors.toList());
	}
	
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
