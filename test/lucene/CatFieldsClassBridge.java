package lucene;

import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.ParameterizedBridge;

import com.sniper.springmvc.model.Post;

/**
 * 作用在类上面可以做一些特殊处理并添加到索引上面
 * 
 * @author sniper
 * 
 */
public class CatFieldsClassBridge implements FieldBridge, ParameterizedBridge {

	private String sepChar;

	public void setParameterValues(Map parameters) {
		this.sepChar = (String) parameters.get("sepChar");
	}

	public void set(String name, Object value, Document document,
			LuceneOptions luceneOptions) {
		// In this particular class the name of the new field was passed
		// from the name field of the ClassBridge Annotation. This is not
		// a requirement. It just works that way in this instance. The
		// actual name could be supplied by hard coding it below.
		Post dep = (Post) value;
		String fieldValue1 = dep.getName();
		if (fieldValue1 == null) {
			fieldValue1 = "";
		}

		String fieldValue = fieldValue1 + sepChar;

		StringField field1 = new StringField(name, fieldValue,
				luceneOptions.getStore());
		field1.setBoost(luceneOptions.getBoost());

		Field field = new Field(name, fieldValue, luceneOptions.getStore(),
				luceneOptions.getIndex(), luceneOptions.getTermVector());
		field.setBoost(luceneOptions.getBoost());
		document.add(field);
	}

}
