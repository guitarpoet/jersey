function ${functionName}():
    ${doc}
<#if parameters?has_content >
    Parameters:
<#list parameters as parameter>
        ${parameter.name}<#if parameter.optional?? >(o)</#if><#if parameter.multi?? >...</#if>: ${parameter.doc}
</#list>
</#if>
<#if returns?has_content >
    Returns:
    	${returns}
</#if>