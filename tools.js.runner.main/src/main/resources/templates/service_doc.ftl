= object ${serviceName} =

Documentation:
	${doc}
Functions:
<#list functions as f>
	function ${f.functionName}(<#list f.parameters as parameter><#if parameter_index != 0>, </#if>${parameter.name}<#if parameter.multi?? >...</#if></#list>): ${f.doc}
</#list>