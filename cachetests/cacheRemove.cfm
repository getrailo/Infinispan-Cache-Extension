<cfset server.enableCache=true>

<cflock scope="server" timeout="10">
	<cfset cacheName="membaseCache">
	<cfset cacheClear()>
    
	<cfset cachePut('abc','123')>
	<cfset cachePut('def','123')>
	<cfset cachePut('ghi','123')>
    <cfset cacheRemove('abc,def')>
	
	<cfcache action="get" name="c" id="abc"/>
    <cf_valueEquals left="#isdefined('c')#" right="false">

	<cfcache action="get" name="c" id="def"/>
    <cf_valueEquals left="#isdefined('c')#" right="false">

	<cfcache action="get" name="c" id="ghi"/>
    <cf_valueEquals left="#isdefined('c')#" right="true">
        
	<cfset cachePut('abc','123')>
	<cfset cachePut('def','123')>
	<cfset cachePut('ghi','123')>
    <cfset cacheRemove(' abc , def ,fff')>

	<cfcache action="get" name="c" id="abc"/>
    <cf_valueEquals left="#isdefined('c')#" right="false">

	<cfcache action="get" name="c" id="def"/>
    <cf_valueEquals left="#isdefined('c')#" right="false">

	<cfcache action="get" name="c" id="ghi"/>
    <cf_valueEquals left="#isdefined('c')#" right="true">
 
    
    <cftry>
        <cfset cacheRemove(' abc , def ,fff',true)>
        <cf_mustThrow message="can not remove the elements with the following id(s) [ABC,DEF,FFF]">
        <cfcatch></cfcatch>
    </cftry>

    
    
<cfif server.ColdFusion.ProductName EQ "railo">    
	<cfset cachePut('def','123',CreateTimeSpan(0,0,0,2),CreateTimeSpan(0,0,0,1),cacheName)>
</cfif>
</cflock>

