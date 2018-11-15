package ec.net.httpserver.ecrtable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ec.net.httpserver.HttpClientRequest;
import ec.system.Basis;
import ec.system.DeveloperMode;
import ec.xpath.XPathManager;

public class EcRenderTable extends Basis implements Cloneable {

	public enum ColumnInfo {
		ID, TYPE, ALIAS
	}

	public enum ColumnQueryType {
		TEXT, RANGE
	}

	public enum QueryValueType {
		TEXT, RANGE_START, RANGE_END
	}

	private String definitionFileUri = null;
	private List<Map<Object, String>> columnInfos = null;
	private List<Map<Object, String>> queryCriterions = null;
	private String bindingClass = null;

	public EcRenderTable(String definitionFileUri) {
		this.definitionFileUri = definitionFileUri.trim();
	}

	public boolean ini() {
		try {
			File f = new File(definitionFileUri);
			if (!f.exists())
				throw new Exception("Definition File[" + definitionFileUri + "] not Found!!");
			XPathManager xpath = new XPathManager();
			xpath.ParsingXML(definitionFileUri);
			if (true) {
				NodeList nd1 = xpath.XSearch("//Column");
				for (int i = 0; i < nd1.getLength(); i++) {
					Map<Object, String> colMp = new HashMap<>();
					Node nd1item = nd1.item(i);
					String id = nd1item.getAttributes().getNamedItem("id") != null
							? nd1item.getAttributes().getNamedItem("id").getNodeValue() : null;
					String type = nd1item.getAttributes().getNamedItem("type") != null
							? nd1item.getAttributes().getNamedItem("type").getNodeValue() : null;
					String alias = nd1item.getTextContent();

					if (id == null)
						throw new Exception(
								"Definition File[" + definitionFileUri + "] Setting Fail,Column ID can't be null!!");
					colMp.put(ColumnInfo.ID, id);
					if (type != null) {
						colMp.put(ColumnInfo.TYPE, type.toUpperCase());
					}
					colMp.put(ColumnInfo.ALIAS, alias);
					if (columnInfos == null)
						columnInfos = new ArrayList<>();
					columnInfos.add(colMp);
				}
			}
			if (true) {
				NodeList nd1 = xpath.XSearch("//BindClass");
				for (int i = 0; i < nd1.getLength(); i++) {
					Node nd1item = nd1.item(i);
					bindingClass = nd1item.getTextContent();
					break;
				}
			}

			return true;
		} catch (Exception e) {
			this.exportExceptionText(e);
			this.except("Initial EcRenderTable For Web Fail,Error = " + e.getMessage());
			return false;
		}
	}

	public boolean parsingQueryCondition(HttpClientRequest request) {
		if (request.getParameters() != null) {
			try {
				if (columnInfos == null) ini();

				for (Map<Object, String> colMp : columnInfos) {
					if (colMp.get(ColumnInfo.TYPE) == null)
						continue;
					if (compareValue(colMp.get(ColumnInfo.TYPE), ColumnQueryType.TEXT.toString())) {
						String queryValue = request.getParameters().get(colMp.get(ColumnInfo.ID));
						if (queryValue != null) {
							if (queryCriterions == null)
								queryCriterions = new ArrayList<>();
							Map<Object, String> mp = new HashMap<>();
							mp.put(ColumnInfo.ID, colMp.get(ColumnInfo.ID));
							mp.put(ColumnInfo.TYPE, colMp.get(ColumnInfo.TYPE));
							mp.put(QueryValueType.TEXT, queryValue);
							queryCriterions.add(mp);
						}
					} else if (compareValue(colMp.get(ColumnInfo.TYPE), ColumnQueryType.RANGE.toString())) {
						String queryStartValue = request.getParameters().get(colMp.get(ColumnInfo.ID) + "@RANGE_START");
						String queryEndValue = request.getParameters().get(colMp.get(ColumnInfo.ID) + "@RANGE_END");
						if (queryStartValue != null && queryEndValue != null) {
							if (queryCriterions == null)
								queryCriterions = new ArrayList<>();
							Map<Object, String> mp = new HashMap<>();
							mp.put(ColumnInfo.ID, colMp.get(ColumnInfo.ID));
							mp.put(ColumnInfo.TYPE, colMp.get(ColumnInfo.TYPE));
							mp.put(QueryValueType.RANGE_START, queryStartValue);
							mp.put(QueryValueType.RANGE_END, queryEndValue);
							queryCriterions.add(mp);
						}
					}
				}
				log("EcRender Table Client Query Condition = " + (isListWithContent(queryCriterions) ? queryCriterions.toString() : null));
				return true;
			} catch (Exception e) {
				this.exportExceptionText(e);
				this.except("Parsing EcRenderTable Query Condition Fail,Error = " + e.getMessage());
				return false;
			}
		} else
			return true;
	}

	public List<Map<Object, String>> getColumnInfos() {
		return columnInfos;
	}

	public boolean isQueryConditionExist(String columnID) {
		if (isListWithContent(queryCriterions)) {
			for (Map<Object, String> mp : queryCriterions) {
				if (compareValue(columnID, mp.get(ColumnInfo.ID))) {
					if (compareValue(mp.get(ColumnInfo.TYPE), ColumnQueryType.TEXT.toString())) {
						return mp.get(QueryValueType.TEXT) != null;
					} else if (compareValue(mp.get(ColumnInfo.TYPE), ColumnQueryType.RANGE.toString())) {
						return mp.get(QueryValueType.RANGE_START) != null && mp.get(QueryValueType.RANGE_END) != null;
					}
				}
			}
			return false;
		} else
			return false;
	}

	public String getQueryTextValue(String columnID) {
		if (isListWithContent(queryCriterions)) {
			for (Map<Object, String> mp : queryCriterions) {
				if (compareValue(columnID, mp.get(ColumnInfo.ID))) {
					return mp.get(QueryValueType.TEXT);
				}
			}
			return null;
		} else
			return null;
	}

	public String getQueryRangeStartValue(String columnID) {
		if (isListWithContent(queryCriterions)) {
			for (Map<Object, String> mp : queryCriterions) {
				if (compareValue(columnID, mp.get(ColumnInfo.ID))) {
					return mp.get(QueryValueType.RANGE_START);
				}
			}
			return null;
		} else
			return null;
	}

	public String getQueryRangeEndValue(String columnID) {
		if (isListWithContent(queryCriterions)) {
			for (Map<Object, String> mp : queryCriterions) {
				if (compareValue(columnID, mp.get(ColumnInfo.ID))) {
					return mp.get(QueryValueType.RANGE_END);
				}
			}
			return null;
		} else
			return null;
	}

	public String getDefinitionFileUri() {
		return definitionFileUri;
	}

	public String getBindingClass() {
		return bindingClass;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
