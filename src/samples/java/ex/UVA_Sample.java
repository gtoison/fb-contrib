package ex;

import java.util.ArrayList;
import java.util.Date;
import java.util.function.Function;
import java.util.function.Supplier;

public class UVA_Sample<T> extends ArrayList<T> {

	private static final long serialVersionUID = 6633661343653099357L;

	public void testNormalUVA(String[] foo) {
	}

	public void testLowUVA1(int boo, String[] hoo) {
	}

	public static void testStaticUVA(Date[] d) {
	}

	public void fpNoParms() {
	}

	public void fpHasOtherArrayUVA1(String[] one, int[] two) {
	}

	public void fpTooManyArgs(int i, char j, long k, String[] moo) {
	}

	public void fpNotAtEnd(String[] foo, int bar) {
	}

	public void fpAlreadyVarArg(String... darnit) {
	}

	public void fpSimilarVarArg(String info, String... data) {
	}

	public void fpBytes(byte[] data) {

	}

	public void fpPass(char[] data) {

	}

	@Override
	public <E> E[] toArray(E[] a) {
		return null;
	}

	public void testCallbacks() {

		final String[] callbacks = new String[] { "No", "No" };
		Function<Supplier, Void> cb1 = new Function<Supplier, Void>() {
			@Override
			public Void apply(final Supplier loginContextSupplier) {
				callbacks[0] = "Yes";
				return null;
			}
		};
	}

	public void testNullVarArgs() {
		fpAlreadyVarArg((String[]) null);
		fpAlreadyVarArg();
	}
}
