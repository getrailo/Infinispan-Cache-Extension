<cfset server.enableCache=true>

<cflock scope="server" timeout="10">
	<cfset cacheName="membaseCache">
	<cfset cacheClear()>
	<cfset start = getTickCount()>
	<cfset cachePut('abc','_cachePut',CreateTimeSpan(0,0,0,1))>
    
    <cfset a=cacheGet('abc')>

    <cf_valueEquals left="#structKeyExists(variables,'a')#" right="true">
	
	
    <cfset sleep(2000)>
    <cfset d=cacheGet('abc')>
    <cf_valueEquals left="#structKeyExists(variables,'d')#" right="false">
    
    
    <cfset a = []>
	<cfset ArrayAppend(a,"one")>
	<cfset cachePut("test_array",a,CreateTimeSpan(0,0,10,0))>
	<cfset one = cacheGet("test_array")>
	<cf_valueEquals left="#arraylen(one)#" right="1">
	
	<cfset ArrayAppend(a,"two")>
	<cfset cachePut("test_array",a,CreateTimeSpan(0,0,10,0))>
	<cfset two = cacheGet("test_array")>
	<cf_valueEquals left="#arraylen(two)#" right="2">

</cflock>

