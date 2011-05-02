<cfset cachePut('terra','Hello Terra')>
<cfset cachePut('mars','Hello Mars')>
<cfset cachePut('pluto','Hello Pluto')>
<cfset cachePut('2003UB313','Hello 2003UB313')>
<cftry>
<cfdump var="#cacheCount()#">
<cfcatch></cfcatch>
</cftry>
<cftry>
<cfdump var="#cacheGetAllIds()#">
<cfcatch></cfcatch>
</cftry>
<cftry>
<cfdump var="#cacheGetAllIds('*r*')#">
<cfcatch></cfcatch>
</cftry>
<cftry>
<cfdump var="#cacheGetAll('*r*')#">
<cfcatch></cfcatch>
</cftry>
<cfdump var="#cacheKeyExists("terra")#">
<cftry>
<cfdump var="#cacheCount()#">
<cfcatch></cfcatch>
</cftry>

<cfset cacheDelete("terra")>
<cfdump var="#cacheCount()#">
<cfset cacheRemove("mars,pluto")>
<cfdump var="#cacheCount()#">
<cfif structKeyExists(url,"clearcache")>
	<cfset cacheClear()>
<cfelse>
	<a href="?clearcache=true">Clear Cache</a>
</cfif>
<cfdump var="#cacheCount()#">
