<!---<cfset cachePut('abc','123')>

<cflock scope="server" timeout="1">
	<cfset cacheClear()>	
	<cfset cachePut('abc','123')>
	<cfset cacheGetMetadata('abc')>		
	<cfif server.ColdFusion.ProductName EQ "railo"> 
		<cfset cachePut('abc','123')>
		<cfset cacheGetMetadata('abc')>
	</cfif>
</cflock>--->