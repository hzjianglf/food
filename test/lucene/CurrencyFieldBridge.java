package lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

import com.sniper.springmvc.model.PostValue;

public class CurrencyFieldBridge implements FieldBridge {

	public static FieldBridge INSTANCE;

	@Override
	public void set(String name, Object value, Document document,
			LuceneOptions luceneOptions) {

		PostValue postValue = (PostValue) value;
		Field field = new Field(name, postValue.getValue(),
				luceneOptions.getStore(), luceneOptions.getIndex(),
				luceneOptions.getTermVector());
		document.add(field);
	}

}
