package labs.rfa;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class EnumerationHelper {

	// Transforms an Enumeration into a Stream using Java's Stream support.
	public static <T> Stream<T> enumerationAsStream(Enumeration<T> e) {
		return StreamSupport.stream(
			Spliterators.spliteratorUnknownSize(
				new Iterator<T>() {
					public T next() {
						return e.nextElement();
					}
					public boolean hasNext() {
						return e.hasMoreElements();
					}
				},
				Spliterator.ORDERED), false);
	}
}
