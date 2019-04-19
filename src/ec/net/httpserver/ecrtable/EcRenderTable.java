package ec.net.httpserver.ecrtable;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import ec.net.httpserver.EcHttpServer;
import ec.net.httpserver.HttpClientRequest;
import ec.system.Basis;
import ec.system.DeveloperMode;
import ec.xpath.XPathManager;

public class EcRenderTable extends Basis implements Cloneable {

	public enum ColumnInfo {
		ID, TYPE, ALIAS, UPDATE , OPTION , NOSHOW
	}
	
	public enum FormInputDefintion {
		READ_ONLY,FORM_ONLY,VIEW_ONLY
	}

	public enum ColumnQueryType {
		TEXT, RANGE , SELECT
	}

	public enum QueryValueType {
		TEXT, RANGE_START, RANGE_END
	}
	
	public enum UpdateValueType {
		VALUE
	}
	
	public enum OrderByType {
		ASC,DESC
	}
	

	private String definitionFileUri = null;
	private List<Map<Object, String>> columnInfos = null;
	private List<Map<Object, String>> queryCriterions = null;
	private List<Map<Object, String>> commitDatas = null;
	private String bindingClass = null;
	private boolean isAllowUpdate = false;
	private boolean isAllowDelete = false;
	
	private String orderByColumeID = null;
	private Object orderByType = OrderByType.ASC;
	
	private List<Map<Object,String>> dymanicSelectCols = null;
	private Map<String,String> dymanicOptionGeDefMp = null;

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
					boolean allowUpdate = nd1item.getAttributes().getNamedItem("update") != null
							? compareValueIn(nd1item.getAttributes().getNamedItem("update").getNodeValue(), new String[]{"1","true"}) : false;
					boolean isNoShow = nd1item.getAttributes().getNamedItem("show") != null
							? compareValueIn(nd1item.getAttributes().getNamedItem("show").getNodeValue(), new String[]{"0","false"}) : false;
					if (id == null)
						throw new Exception(
								"Definition File[" + definitionFileUri + "] Setting Fail,Column ID can't be null!!");
					
					String sort = nd1item.getAttributes().getNamedItem("sort") != null
							? nd1item.getAttributes().getNamedItem("sort").getNodeValue() : null;
					if(sort != null) {
						orderByColumeID = id;
						if(compareValue(sort, "desc")) {
							orderByType = OrderByType.DESC;
						} else {
							orderByType = OrderByType.ASC;
						}
					}
							
					//set column info to memory
					colMp.put(ColumnInfo.ID, id);
					if (type != null) {
						colMp.put(ColumnInfo.TYPE, type.toUpperCase());
						if(compareValue(type, "SELECT")) {
							String option = nd1item.getAttributes().getNamedItem("option") != null
									? nd1item.getAttributes().getNamedItem("option").getNodeValue() : null;
							if(option != null) {
								if(option.indexOf(".") >= 0) {
									String selectOptionStr = loadOptionFromXMLOptionText(option);
									if(selectOptionStr != null) {
										//Check select option genereate method works or not
										if(dymanicSelectCols == null){
											dymanicSelectCols = new ArrayList<>();
											dymanicOptionGeDefMp = new HashMap<>();
										}
										dymanicSelectCols.add(colMp);
										dymanicOptionGeDefMp.put(colMp.get(ColumnInfo.ID), option);
									} else {
										colMp.put(ColumnInfo.OPTION, "OptionPrepareFail");
									}
								} else {
									colMp.put(ColumnInfo.OPTION, option);
								}
							} else {
								colMp.put(ColumnInfo.TYPE, "TEXT");
							}
						}
					}
					if(isNoShow) colMp.put(ColumnInfo.NOSHOW, "1");
					colMp.put(ColumnInfo.ALIAS, alias);
					colMp.put(ColumnInfo.UPDATE, allowUpdate ? "1" : "0");
					
					iniFormExtraDefinition(xpath,colMp);
					
					if (columnInfos == null)
						columnInfos = new ArrayList<>();
					columnInfos.add(colMp);
					if(allowUpdate) 
						isAllowUpdate = true;
					
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
			iniSetting(xpath);
			return true;
		} catch (Exception e) {
			this.exportExceptionText(e);
			this.except("Initial EcRenderTable For Web Fail,Error = " + e.getMessage());
			return false;
		}
	}
	
	private void iniSetting(XPathManager xpath) throws Exception{
		NodeList nd1 = xpath.XSearch("//Setting");
		for (int i = 0; i < nd1.getLength(); i++) {
			Node nd1item = nd1.item(i);
			Object key = nd1item.getAttributes().getNamedItem("id");
			if(key != null) {
				String value = nd1item.getAttributes().getNamedItem("id").getNodeValue();
				if(compareValue(value, "delete")) {
					String v = nd1item.getTextContent();
					if(compareValue(v, "1") || compareValue(v, "true")) this.isAllowDelete = true;
				}
			} 
		}
	}
	
	
	private void iniFormExtraDefinition(XPathManager xpath,Map<Object, String> colMp) throws Exception{
		NodeList nd1 = xpath.XSearch("//Input[@refer='" + colMp.get(ColumnInfo.ID) + "']");
		if(nd1.getLength() > 0) {
			Node nd1item = nd1.item(0);
			boolean isReadOnly = nd1item.getAttributes().getNamedItem("readonly") != null
					? compareValueIn(nd1item.getAttributes().getNamedItem("readonly").getNodeValue(), new String[]{"1","true"}) : false;
			if(isReadOnly)  colMp.put(FormInputDefintion.READ_ONLY, "1");
			boolean isFormOnly = nd1item.getAttributes().getNamedItem("formonly") != null
					? compareValueIn(nd1item.getAttributes().getNamedItem("formonly").getNodeValue(), new String[]{"1","true"}) : false;
			if(isFormOnly)  colMp.put(FormInputDefintion.FORM_ONLY, "1");
			boolean isViewOnly = nd1item.getAttributes().getNamedItem("viewonly") != null
					? compareValueIn(nd1item.getAttributes().getNamedItem("viewonly").getNodeValue(), new String[]{"1","true"}) : false;
			if(isViewOnly)  colMp.put(FormInputDefintion.VIEW_ONLY, "1");
		}
	}

	public boolean parsingQueryCondition(HttpClientRequest request) {
		if (request.getParameters() != null) {
			try {
				if (columnInfos == null) ini();

				for (Map<Object, String> colMp : columnInfos) {
					if (colMp.get(ColumnInfo.TYPE) == null)
						continue;
					if (compareValue(colMp.get(ColumnInfo.TYPE), ColumnQueryType.TEXT.toString()) || 
							compareValue(colMp.get(ColumnInfo.TYPE), ColumnQueryType.SELECT.toString())) {
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
						if (queryStartValue != null || queryEndValue != null) {
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
	
	public boolean parsingCommitUpdateParameters(HttpClientRequest request) {
		if (request.getParameters() != null) {
			try {
				if (columnInfos == null) ini();

				for (Map<Object, String> colMp : columnInfos) {
					String commitValue = request.getParameters().get(colMp.get(ColumnInfo.ID));
					if (commitValue != null) {
						if (commitDatas == null)
							commitDatas = new ArrayList<>();
						
						Map<Object, String> mp = new HashMap<>();
						mp.put(ColumnInfo.ID, colMp.get(ColumnInfo.ID));
						mp.put(UpdateValueType.VALUE, commitValue);
						commitDatas.add(mp);
					}
				}
				log("EcRender Table Client Commit Updates = " + (isListWithContent(commitDatas) ? commitDatas.toString() : null));
				return true;
			} catch (Exception e) {
				this.exportExceptionText(e);
				this.except("Parsing EcRenderTable Commit Updates Fail,Error = " + e.getMessage());
				return false;
			}
		} else
			return false;
	}
	

	public List<Map<Object, String>> getColumnInfos() {
		if(dymanicSelectCols != null){
			for(Map<Object,String> dymanicSelectCols : dymanicSelectCols){
				String optionDef = dymanicOptionGeDefMp.get(dymanicSelectCols.get(ColumnInfo.ID));
				if(optionDef != null) {
					String selectOptionStr = loadOptionFromXMLOptionText(optionDef);
					if(selectOptionStr != null) {
						dymanicSelectCols.put(ColumnInfo.OPTION, selectOptionStr);
					} else {
						dymanicSelectCols.put(ColumnInfo.OPTION, "OptionPrepareFail");
					}
				} else {
					dymanicSelectCols.put(ColumnInfo.OPTION, "OptionPrepareFail");
				}
			}
		}
		return columnInfos;
	}

	public boolean isQueryConditionExist(String columnID) {
		if (isListWithContent(queryCriterions)) {
			for (Map<Object, String> mp : queryCriterions) {
				if (compareValue(columnID, mp.get(ColumnInfo.ID))) {
					if (compareValue(mp.get(ColumnInfo.TYPE), ColumnQueryType.TEXT.toString()) || 
							compareValue(mp.get(ColumnInfo.TYPE), ColumnQueryType.SELECT.toString())) {
						return mp.get(QueryValueType.TEXT) != null;
					} else if (compareValue(mp.get(ColumnInfo.TYPE), ColumnQueryType.RANGE.toString())) {
						return mp.get(QueryValueType.RANGE_START) != null || mp.get(QueryValueType.RANGE_END) != null;
					}
				}
			}
			return false;
		} else
			return false;
	}
	
	public boolean isQueryConditionExist(String[] columnIDs) {
		if(columnIDs != null && columnIDs.length > 0) {
			for(String columnID : columnIDs){
				if(isQueryConditionExist(columnID)) return true;
			}
		}
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
	
	public String getCommiValue(String columnID) {
		if (isListWithContent(commitDatas)) {
			for (Map<Object, String> mp : commitDatas) {
				if (compareValue(columnID, mp.get(ColumnInfo.ID))) {
					return mp.get(UpdateValueType.VALUE);
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
	
	
	public String getOrderByColumeID() {
		return orderByColumeID;
	}

	public Object getOrderByType() {
		return orderByType;
	}

	public String getDefinitionFileUri() {
		return definitionFileUri;
	}

	public String getBindingClass() {
		return bindingClass;
	}

	public boolean isAllowDelete() {
		return isAllowDelete;
	}

	public boolean isAllowUpdate() {
		return isAllowUpdate;
	}
	
	private String loadOptionFromXMLOptionText(String option){
		String className = option.substring(0, option.lastIndexOf("."));
		String methodName = option.substring(option.lastIndexOf(".") + 1);
		try {
			Class c = getClass().getClassLoader().loadClass(className);
			Constructor cc = c.getDeclaredConstructor();
			Object o = cc.newInstance();
			Method m = c.getDeclaredMethod(methodName);
			Object k = m.invoke(o);
			return k != null ? k.toString() : null;
		} catch(Exception e){
			this.exportExceptionText(e);
			return null;
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
