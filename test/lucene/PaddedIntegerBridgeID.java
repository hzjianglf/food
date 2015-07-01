package lucene;

import java.util.Map;

import org.hibernate.search.bridge.ParameterizedBridge;
import org.hibernate.search.bridge.TwoWayStringBridge;

/**
 * @ DocumentId 专用树形 用法和 StringBridge 是一致的
 * 
 * @FieldBridge(impl = PaddedIntegerBridge.class, params =
 *                   @Parameter(name="padding", value="10") )
 * @author sniper
 * 
 */
public class PaddedIntegerBridgeID implements TwoWayStringBridge,
		ParameterizedBridge {

	public static String PADDING_PROPERTY = "padding";
	private int padding = 5; // default

	@SuppressWarnings("rawtypes")
	public void setParameterValues(Map parameters) {
		Object padding = parameters.get(PADDING_PROPERTY);
		if (padding != null)
			this.padding = (Integer) padding;
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

	public Object stringToObject(String stringValue) {
		return new Integer(stringValue);
	}

}
