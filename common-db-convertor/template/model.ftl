package ${package};

<#list importPackage as importPackage>
import ${importPackage};
</#list>

public class ${table['className']} {
<#list table['columns'] as cList>
	private ${cList['fieldType']} ${cList['fieldName']};
</#list>
<#list table['columns'] as cList>

	<#if cList['fieldType'] == "boolean">
	public ${cList['fieldType']} is${cList['fieldName']?cap_first}() {
		return ${cList['fieldName']};
	}
	<#else>
	public ${cList['fieldType']} get${cList['fieldName']?cap_first}() {
		return ${cList['fieldName']};
	}
	</#if>
	
	public void set${cList['fieldName']?cap_first}(${cList['fieldType']} ${cList['fieldName']}) {
		this.${cList['fieldName']} = ${cList['fieldName']};
	}
</#list>
}