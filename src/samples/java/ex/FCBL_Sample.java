package ex;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;

import javax.swing.SwingUtilities;

@SuppressWarnings("all")
public final class FCBL_Sample {
	public int foo; // no warning (scope)
	private int moo; // warning
	private int boo; // warning
	int hoo; // no warning (scope)
	private int fp; // no warning (read in method test2)
	private int multiMethodFP; // no warning (used in a couple methods)
	private String test; // warning
	private String testNestedIfs;// no warning (in one branch of the if, this is
									// read first)
	private int x = 1; // warning
	private int y = 2; // no warning (read first in method1)
	private boolean ret; // warning (shielded in foo)
	@FooAnnotation
	private String notUsed; // warning
	@FooAnnotation
	private String used; // no warning (annotation+stored)

	private String fooS = "Foo"; // warning
	private String[] fooSS = { fooS }; // no warning (read in testInArray)

	private boolean checkMethodCall;

	public FCBL_Sample() {
		foo = 0;
		moo = 1;
		boo = 2;
		hoo = 3;
		fp = 4;
		used = "Hello";
		ret = false;

		// to mask standard URF_UNREAD_FIELD
		System.out.println(foo + hoo + used + moo + boo + ret);
	}

	public void method1() {
		x = 50;
		System.out.println(x);
		System.out.println(y);
	}

	public void test1() {
		foo = 2;
		moo = 3;
		boo = 4;
		fp = 5;
	}

	public void testInArray() {
		for (String s : fooSS) {
		}
	}

	public void test2() {
		boo = fp;
	}

	public void test3(String in) {
		boolean found = false;
		if ("boo".equals(in)) {
			test = "boo";
		} else if ("hoo".equals(in)) {
			test = "hoo";
		} else if ("moo".equals(in)) {
			if ("loo".equals(in) && !found) {
				found = true;
			}
		}

		test = "WooWoo".toLowerCase(Locale.ENGLISH); // hides the PME warning
														// and the standard
														// DM_CONVERT_CASE
		if (in.length() > 1) {
			test = "woowoo";
			System.out.println(test);
		}
	}

	public void testNestedIfs(String in) {
		boolean found = false;
		if ("boo".equals(in)) {
			testNestedIfs = "boo";
		} else if ("hoo".equals(in)) {
			testNestedIfs = "hoo";
		} else if ("moo".equals(in)) {
			if ("loo".equals(in) && !found) {
				System.out.println(in + testNestedIfs);
			}
		}

		testNestedIfs = "WooWoo".toLowerCase(Locale.ENGLISH); // hides the PME
																// warning and the
																// standard
																// DM_CONVERT_CASE
		if (in.length() > 1) {
			testNestedIfs = "woowoo";
		}
	}

	public void mm1FP(int i) {
		multiMethodFP = i;
		mm2FP(3);

		if (multiMethodFP == i) {
			System.out.println("FP");
		}
	}

	public void mm2FP(int i) {
		multiMethodFP = i;
	}

	public void baseMethod() {
		checkMethodCall = false;
		setCheckMethod();

		if (checkMethodCall) {
			System.out.println("It changed");
		}
	}

	public void setCheckMethod() {
		if (Math.random() < 0.05) {
			checkMethodCall = true;
		}
	}

	static class Foo {
		boolean ret;

		public boolean testFPAnon() {
			ret = false;

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					ret = false;
				}
			});

			return ret;
		}
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@interface FooAnnotation {

	}

	static class FieldNameCollisionFP {
		OutputStream out;

		public FieldNameCollisionFP(OutputStream out) {
			this.out = out;
		}

		public void collide() throws IOException {
			System.out.println("See out!");
			out.close();
		}
	}
}

interface I320 {
	void doStuff();
}

class FP320 implements I320 {
	private final String s;

	public FP320(String s) {
		this.s = s;
	}

	@Override
	public void doStuff() {
		System.out.println(s);
	}
}

final class FP460 {
	public final Type type;
	public final String id;

	public FP460(Type type, String id) {
		this.type = (type);
		this.id = (id);
	}

	@Override
	public String toString() {
		return type.displayName + " <" + id + ">";
	}

	public enum Type {
		FOO("Foolicious"), BAR("Barocious");

		private final String displayName;

		private Type(String displayName) {
			this.displayName = displayName;
		}
	};
}
