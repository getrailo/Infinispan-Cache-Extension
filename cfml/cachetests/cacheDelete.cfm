<cfif server.ColdFusion.ProductName EQ "railo">
<cflock scope="server" timeout="1">
	<cfset cacheClear()>
	
	<cfset cachePut('abc','123')>
	<cfcache action="get" name="s" id="abc"/>
    <cf_valueEquals left="#isDefined('s')#" right="true">
	
    <cfset cacheDelete('abc')>
	<cfcache action="get" name="s" id="abc"/>
    <cf_valueEquals left="#isDefined('s')#" right="false">

    <cfset cacheDelete('feg')>
    
    <cftry>
        <cfset cacheDelete('def',true)>
        <cf_mustThrow message="there is no entry in cache with key [DEF]">
        <cfcatch></cfcatch>
    </cftry>
    
 	<cfset cachePut('abc','123')>
	<cfcache action="get" name="s" id="abc"/>
    <cf_valueEquals left="#isDefined('s')#" right="true">
    
</cflock>

</cfif>