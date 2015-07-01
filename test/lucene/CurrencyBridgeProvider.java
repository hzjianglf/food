package lucene;

import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.spi.BridgeProvider;

import com.sniper.springmvc.model.PostValue;

/**
 * 根据返回类型,返回对应的字段数据
 * 由于官网例子不全
 * 
 * @author sniper
 * 
 */
public class CurrencyBridgeProvider implements BridgeProvider {

	public CurrencyBridgeProvider() {

	}

	@Override
	public FieldBridge provideFieldBridge(
			BridgeProviderContext bridgeProviderContext) {
		if (bridgeProviderContext.getReturnType().equals(PostValue.class)) {
			return CurrencyFieldBridge.INSTANCE;
		}
		return null;
	}

}
