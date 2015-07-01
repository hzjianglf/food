package lucene;

import java.util.Map;

import org.hibernate.search.bridge.ParameterizedBridge;
import org.hibernate.search.bridge.StringBridge;

/**
 * 可以传递一些自定义参数
 * 
 * @FieldBridge(impl = PaddedIntegerBridge.class, params =
 *                   @Parameter(name="padding", value="10") ) private Integer
 *                   length;
 * @author sniper
 * 
 */
public class PaddedIntegerBridgeParams implements StringBridge,
		ParameterizedBridge {

	public static String PADDING_PROPERTY = "padding";
	private int padding = 5; // default

	public void setParameterValues(Map<String, String> parameters) {
		String padding = parameters.get(PADDING_PROPERTY);
		if (padding != null)
			this.padding = Integer.parseInt(padding);
	}

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
