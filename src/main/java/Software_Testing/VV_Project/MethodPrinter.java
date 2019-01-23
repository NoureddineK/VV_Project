package Software_Testing.VV_Project;

import java.util.Collection;
import java.util.List;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

public class MethodPrinter {

	public static void main(String[] args) {

		Launcher launcher = new Launcher();
		launcher.addInputResource("/home/noureddine/Projets/VV_Project/src/main/java/classToTest");
		launcher.buildModel();
		CtModel model = launcher.getModel();
		
		Launcher launcherTest = new Launcher();
		launcherTest.addInputResource("/home/noureddine/Projets/VV_Project/src/test/java/Software_Testing/VV_Project/");
		launcherTest.buildModel();
		CtModel modelTest = launcherTest.getModel();
		

		for (CtType type : model.getAllTypes()) {
			System.out.println(type.getSimpleName());
			for (CtMethod method :(Collection <CtMethod>) type.getMethods()) {
				System.out.println("Refer : "+method);
			}

		}
		System.out.println("TEST : ");
		
		for (CtType type : modelTest.getAllTypes()) {
			System.out.println(type.getSimpleName());
			// Collection<CtMethod> methods = (Collection<CtMethod>) type.getMethods();
			for (CtMethod method : (Collection<CtMethod>) type.getMethods()) {
			
				System.out.println(method);
			
			}
		}
	}
}