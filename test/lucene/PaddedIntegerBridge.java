package lucene;

import org.hibernate.search.bridge.StringBridge;

/**
 * Padding Integer bridge. All numbers will be padded with 0 to match 5 digits
 * 
 * @author Emmanuel Bernard
 */
public class PaddedIntegerBridge implements StringBridge {
	private int padding = 5;

	public String objectToString(Object object) {
		String rawInteger = ((Integer) object).toString();
		if (rawInteger.length() > padding)
			throw new IllegalArgumentException("Number too big to be padded");
		StringBuilder paddedInteger = new StringBuilder();
		for (int padIndex = rawInteger.length(); padIndex < padding; padIndex++) {
			paddedInteger.append('0');
		}
		return paddedInteger.append(rawInteger).toString();
	}
}
