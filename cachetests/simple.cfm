<cfset cachePut('terra','Hello Terra')>
<cfset cachePut('mars','Hello Mars')>
<cfset cachePut('pluto','Hello Pluto')>
<cfset cachePut('2003UB313','Hello 2003UB313')>
<cfdump var="#cacheCount()#">
<cfdump var="#cacheGetAllIds()#">
<cfdump var="#cacheGetAllIds('*r*')#">
<cfdump var="#cacheGetAll('*r*')#">
<cfdump var="#cacheKeyExists("terra")#">
<cfdump var="#cacheCount()#">
<cfabort>

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
