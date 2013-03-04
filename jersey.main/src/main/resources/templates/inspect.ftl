class ${class} {
<#if fields?has_content >
	Fields:
<#list fields as field>
		${field.type.name} ${field.name} = ${field.get(obj)};
</#list>
</#if>
	Methods:
<#list methods as method>
		${method.returnType.name} ${method.name}(<#list method.parameterTypes as type><#if type_index != 0>, </#if>${type.name}</#list>);
</#list>
}